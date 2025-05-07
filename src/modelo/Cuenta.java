package modelo;

import excepciones.PinInvalidoException;
import excepciones.SaldoInsuficienteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Representa una cuenta bancaria en colones. Gestiona PIN cifrado,
 * bloqueos tras intentos fallidos, depósito, retiro con comisión y
 * registro de transacciones.
 */
public class Cuenta {
    private static final AtomicInteger SECUENCIA = new AtomicInteger(0);

    private final String numeroCuenta;
    private final LocalDateTime fechaCreacion;
    private EstadoCuenta estatus;
    private long saldo;  // en colones, sin decimales
    private final Cliente dueno;
    private String pinCifrado;
    private int intentosRestantes;
    private final List<Transaccion> transacciones = new ArrayList<>();

    /**
     * Crea una nueva cuenta con PIN válido y depósito inicial.
     *
     * @param dueno            cliente propietario
     * @param pinPlano         PIN en texto plano (6 chars, 1 mayúscula, 1 dígito)
     * @param depositoInicial  monto inicial en colones (>0)
     * @throws PinInvalidoException       si el PIN no cumple formato
     * @throws IllegalArgumentException   si el depósito inicial ≤ 0
     */
    public Cuenta(Cliente dueno, String pinPlano, long depositoInicial) {
        if (!Validacion.validarPin(pinPlano)) {
            throw new PinInvalidoException("Formato de PIN inválido");
        }
        if (depositoInicial <= 0) {
            throw new IllegalArgumentException("Depósito inicial debe ser mayor que cero");
        }

        this.dueno = dueno;
        this.numeroCuenta = "CTA" + SECUENCIA.incrementAndGet();
        this.fechaCreacion = LocalDateTime.now();
        this.estatus = EstadoCuenta.ACTIVA;
        this.pinCifrado = Cifrado.encrypt(pinPlano);
        this.intentosRestantes = 3;
        this.saldo = 0;
        // registra depósito inicial como transacción sin comisión
        depositar(depositoInicial);
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoCuenta getEstatus() {
        return estatus;
    }

    public Cliente getDueno() {
        return dueno;
    }

    /**
     * Deposita colones en la cuenta (sin comisión).
     *
     * @param monto cantidad en colones (>0)
     * @throws IllegalStateException      si la cuenta está inactiva
     * @throws IllegalArgumentException  si monto ≤ 0
     */
    public void depositar(long monto) {
        if (estatus != EstadoCuenta.ACTIVA) {
            throw new IllegalStateException("Cuenta inactiva");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto de depósito debe ser positivo");
        }
        saldo += monto;
        transacciones.add(new Transaccion(TipoTransaccion.DEPOSITO, monto, false, 0));
    }

    /**
     * Retira colones de la cuenta, aplicando 2% de comisión a partir de la 6ª transacción.
     *
     * @param monto cantidad en colones (>0)
     * @return objeto Transaccion con detalles del retiro
     * @throws IllegalStateException         si la cuenta está inactiva
     * @throws IllegalArgumentException      si monto ≤ 0
     * @throws SaldoInsuficienteException    si no hay fondos suficientes
     */
    public Transaccion retirar(long monto) {
        if (estatus != EstadoCuenta.ACTIVA) {
            throw new IllegalStateException("Cuenta inactiva");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto de retiro debe ser positivo");
        }

        boolean cobraComision = transacciones.size() >= 5;
        long comision = cobraComision ? Math.round(monto * 0.02) : 0L;
        long total = monto + comision;

        if (saldo < total) {
            throw new SaldoInsuficienteException(
                "Fondos insuficientes: saldo=" + saldo + ", requerido=" + total);
        }

        saldo -= total;
        Transaccion t = new Transaccion(TipoTransaccion.RETIRO, monto, cobraComision, comision);
        transacciones.add(t);
        return t;
    }

    /**
     * Cambia el PIN de la cuenta tras validar su formato.
     *
     * @param nuevoPin PIN en texto plano
     * @throws PinInvalidoException si el formato es inválido
     */
    public void cambiarPin(String nuevoPin) {
        if (!Validacion.validarPin(nuevoPin)) {
            throw new PinInvalidoException("Formato de PIN inválido");
        }
        this.pinCifrado = Cifrado.encrypt(nuevoPin);
        this.intentosRestantes = 3;
    }

    /**
     * Consulta el saldo tras validar el PIN.
     *
     * @param pinPlano PIN en texto plano
     * @return saldo en colones
     * @throws PinInvalidoException si el PIN es incorrecto o la cuenta se bloquea
     */
    public long consultarSaldo(String pinPlano) {
        if (!verificarPin(pinPlano)) {
            throw new PinInvalidoException("PIN incorrecto o cuenta bloqueada");
        }
        return saldo;
    }

    /**
     * Devuelve la lista de transacciones tras validar el PIN.
     *
     * @param pinPlano PIN en texto plano
     * @return lista inmutable de Transaccion
     * @throws PinInvalidoException si el PIN es incorrecto o la cuenta se bloquea
     */
    public List<Transaccion> consultarTransacciones(String pinPlano) {
        if (!verificarPin(pinPlano)) {
            throw new PinInvalidoException("PIN incorrecto o cuenta bloqueada");
        }
        return List.copyOf(transacciones);
    }

    /**
     * Para persistencia: devuelve todas las transacciones sin validar PIN.
     * Este método está destinado a ser utilizado por el subsistema de persistencia
     * para guardar el historial de transacciones.
     *
     * @return Una copia inmutable de la lista de transacciones de la cuenta
     */
    public List<Transaccion> getTransacciones() {
        return List.copyOf(transacciones);
    }

    /**
     * Verifica el PIN descifrado y bloquea la cuenta tras 3 intentos fallidos.
     *
     * @param pinPlano PIN en texto plano
     * @return true si el PIN coincide
     */
    public boolean verificarPin(String pinPlano) {
        if (estatus == EstadoCuenta.INACTIVA) {
            return false;
        }
        String actual = Cifrado.decrypt(pinCifrado);
        if (actual.equals(pinPlano)) {
            intentosRestantes = 3;
            return true;
        }
        if (--intentosRestantes <= 0) {
            estatus = EstadoCuenta.INACTIVA;
        }
        return false;
    }

    public String obtenerEstado() {
        long totalDeposito = 0, totalRetiro = 0;
        long comisionDeposito = 0, comisionRetiro = 0;

        for (Transaccion t : transacciones) {
            if (t.getTipo() == TipoTransaccion.DEPOSITO) {
                totalDeposito += t.getMonto();
                if (t.isCobroComision()) comisionDeposito += t.getMontoComision();
            } else {
                totalRetiro += t.getMonto();
                if (t.isCobroComision()) comisionRetiro += t.getMontoComision();
            }
        }

        return "Estatus: " + estatus + "\n" +
               "PIN (cifrado): " + pinCifrado + "\n\n" +
               "Total depositado: " + totalDeposito + " ₡\n" +
               "Total retiros: " + totalRetiro + " ₡\n\n" +
               "Comisión depósitos: " + comisionDeposito + " ₡\n" +
               "Comisión retiros: " + comisionRetiro + " ₡\n" +
               "Total comisiones: " + (comisionDeposito + comisionRetiro) + " ₡\n";
    }

    public String getPinCifrado() {
        return pinCifrado;
    }
}
