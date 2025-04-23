package controlador;

import excepciones.PinInvalidoException;
import excepciones.SaldoInsuficienteException;
import modelo.Cliente;
import modelo.Cuenta;
import modelo.ServicioBCCR;
import modelo.ServicioSMS;
import modelo.Transaccion;
import modelo.EstadoCuenta;
import persistencia.Persistencia;
import modelo.Validacion;

import java.util.List;

/**
 * Controlador principal del Cajero Automático.
 * Implementa todas las operaciones requeridas.
 */
public class ControladorCajero {
    private final Persistencia persistencia;
    private final List<Cliente> clientes;
    private final List<Cuenta> cuentas;
    private String pinCifrado;
    private boolean bloqueada;
    private int intentosFallidos;

    public ControladorCajero(Persistencia persistencia) throws Exception {
        this.persistencia = persistencia;
        this.clientes    = persistencia.cargarClientes();
        this.cuentas     = persistencia.cargarCuentas();
        // Cargar transacciones históricas
        for (Cuenta c : cuentas) {
            c.getTransacciones().clear();
            c.getTransacciones().addAll(
                persistencia.cargarTransacciones(c.getNumeroCuenta())
            );
        }
    }

    // — Helpers privados —

private Cliente buscarCliente(String idCliente) {
    return clientes.stream()
        .filter(c -> c.getIdentificacion().equals(idCliente))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException("Debe crear el cliente antes de crear una cuenta."));
}


    private Cuenta buscarCuenta(String numeroCuenta) {
        return cuentas.stream()
            .filter(c -> c.getNumeroCuenta().equals(numeroCuenta))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));
    }

    private void guardarClientes() throws Exception {
        persistencia.guardarClientes(clientes);
    }

    private void guardarCuentas() throws Exception {
        persistencia.guardarCuentas(cuentas);
        for (Cuenta c : cuentas) {
            persistencia.guardarTransacciones(
                c.getNumeroCuenta(),
                c.getTransacciones()
            );
        }
    }

    
    // Método auxiliar para cifrar el PIN 
    private String cifrarPin(String pin) {
        // Aquí deberías usar un algoritmo real de cifrado o hash seguro como SHA-256
        return Integer.toString(pin.hashCode()); 
    }
    
    // — Operaciones públicas —
    public String obtenerEstadoCuenta(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        if (!c.verificarPin(pin)) {
            throw new PinInvalidoException("PIN incorrecto o cuenta bloqueada");
        }

        long totalDeposito = 0, totalRetiro = 0, comisionDeposito = 0, comisionRetiro = 0;
        for (Transaccion t : c.getTransacciones()) {
            if (t.getTipo() == Transaccion.TipoTransaccion.DEPOSITO) {
                totalDeposito += t.getMonto();
                if (t.isCobraComision()) comisionDeposito += t.getComision();
            } else {
                totalRetiro += t.getMonto();
                if (t.isCobraComision()) comisionRetiro += t.getComision();
            }
        }
        long totalComision = comisionDeposito + comisionRetiro;

        return "Estatus: " + c.getEstatus() + "\n" +
               "PIN (cifrado): " + c.getPinCifrado() + "\n\n" +
               "Total depositado: " + totalDeposito + " ₡\n" +
               "Total retiros: " + totalRetiro + " ₡\n\n" +
               "Comisión depósitos: " + comisionDeposito + " ₡\n" +
               "Comisión retiros: " + comisionRetiro + " ₡\n" +
               "Total comisiones: " + totalComision + " ₡\n";
    }


    /** Alta de nuevo cliente */
    public Cliente crearCliente(String nombre,
                                String identificacion,
                                String telefono,
                                String email) throws Exception {
        Cliente c = new Cliente(nombre, identificacion, telefono, email);
        clientes.add(c);
        guardarClientes();
        return c;
    }

    /** Alta de nueva cuenta */
    public void crearCuenta(String id, String pin, long monto) throws Exception {
        Cliente cliente = buscarCliente(id);  

        if (!Validacion.validarPin(pin)) {   
            throw new IllegalArgumentException("Formato de PIN inválido");
        }

        Cuenta nueva = new Cuenta(cliente, pin, monto);  
        cuentas.add(nueva);
        guardarCuentas();
    }



    /** Cambiar PIN */
    public void cambiarPin(String numeroCuenta,
                          String pinActual,
                          String pinNuevo) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        c.cambiarPin(pinNuevo);
        guardarCuentas();
    }

    /** Depósito en colones */
    public void depositarColones(String numeroCuenta, long monto) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        c.depositar(monto);
        guardarCuentas();
    }

    /** Depósito en USD (convierte al tipo de compra) */
    public Transaccion depositarDolares(String numeroCuenta, double montoUsd) throws Exception {
        long colones = Math.round(montoUsd * ServicioBCCR.getTipoCompra());
        Cuenta c = buscarCuenta(numeroCuenta);
        Transaccion t = c.depositar(colones);
        guardarCuentas();
        return t;
    }

    /** Retiro en colones con validación de PIN + SMS */
    public Transaccion retirarConSms(String numeroCuenta,
                                     String pin,
                                     String codigoSms,
                                     long monto) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        if (!c.verificarPin(pin)) {
            throw new PinInvalidoException("PIN incorrecto o cuenta bloqueada");
        }
        String enviado = ServicioSMS.enviarCodigo(c.getDueno().getTelefono());
        if (!codigoSms.equals(enviado)) {
            throw new IllegalArgumentException("Código SMS inválido");
        }
        Transaccion t = c.retirar(monto);
        guardarCuentas();
        return t;
    }

    /** Retiro en USD (convierte al tipo de venta) */
    public Transaccion retirarDolares(String numeroCuenta,
                                      String pin,
                                      String codigoSms,
                                      double montoUsd) throws Exception {
        long colonesReq = Math.round(montoUsd * ServicioBCCR.getTipoVenta());
        return retirarConSms(numeroCuenta, pin, codigoSms, colonesReq);
    }

    /** Consulta de saldo en colones */
    public long consultarSaldo(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        return c.consultarSaldo(pin);
    }

    /** Consulta de saldo en USD (convierte al tipo de compra) */
    public double consultarSaldoDolares(String numeroCuenta, String pin) throws Exception {
        long col = consultarSaldo(numeroCuenta, pin);
        return col / ServicioBCCR.getTipoCompra();
    }

    /** Historial de transacciones */
    public List<Transaccion> consultarTransacciones(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        return c.consultarTransacciones(pin);
    }

    /** Tipo de cambio de compra */
    public double getTipoCambioCompra() {
        return ServicioBCCR.getTipoCompra();
    }

    /** Tipo de cambio de venta */
    public double getTipoCambioVenta() {
        return ServicioBCCR.getTipoVenta();
    }

    /** Cambio de teléfono del cliente */
    public void cambiarTelefono(String idCliente, String nuevoTel) throws Exception {
        Cliente c = buscarCliente(idCliente);
        c.setTelefono(nuevoTel);
        guardarClientes();
    }

    /** Cambio de email del cliente */
    public void cambiarEmail(String idCliente, String nuevoEmail) throws Exception {
        Cliente c = buscarCliente(idCliente);
        c.setEmail(nuevoEmail);
        guardarClientes();
    }

    /** Transferencia entre cuentas del mismo dueño */
    public void transferir(String ctaOrigen,
                          String pin,
                          String codigoSms,
                          String ctaDestino,
                          long monto) throws Exception {
        Cuenta origen  = buscarCuenta(ctaOrigen);
        Cuenta destino = buscarCuenta(ctaDestino);
        if (!origen.getDueno().getId().equals(destino.getDueno().getId())) {
            throw new IllegalArgumentException("Cuentas de distinto titular");
        }
        if (!origen.verificarPin(pin)) {
            throw new PinInvalidoException("PIN incorrecto o cuenta bloqueada");
        }
        String enviado = ServicioSMS.enviarCodigo(origen.getDueno().getTelefono());
        if (!codigoSms.equals(enviado)) {
            throw new IllegalArgumentException("Código SMS inválido");
        }
        origen.retirar(monto);
        destino.depositar(monto);
        guardarCuentas();
    }

    /** Eliminar cuenta y transacciones asociadas */
    public void eliminarCuenta(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        if (!c.verificarPin(pin)) {
            throw new PinInvalidoException("PIN incorrecto o cuenta bloqueada");
        }
        cuentas.remove(c);
        persistencia.eliminarArchivoTransacciones(numeroCuenta);
        guardarCuentas();
    } 


    public String getEstatus() {
        return bloqueada ? "Bloqueada" : "Activa";
    }

    /** Consultar estado de la cuenta (estatus y bloqueo) */
    public EstadoCuenta consultarEstadoCuenta(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        c.verificarPin(pin);
        return c.getEstatus();
    }
   
}