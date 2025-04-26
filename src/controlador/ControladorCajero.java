package controlador;

import excepciones.PinInvalidoException;
import excepciones.SaldoInsuficienteException;
import modelo.Cliente;
import modelo.Cuenta;
import modelo.ServicioBCCR;
import modelo.ServicioSMS;
import modelo.Transaccion;
import modelo.TipoTransaccion;
import modelo.EstadoCuenta;
import persistencia.Persistencia;
import modelo.Validacion;

import java.util.List;

/**
 * Controlador principal del Cajero Automático.
 * Implementa todas las operaciones requeridas para gestionar clientes, cuentas y realizar transacciones.
 */
public class ControladorCajero {
    private final Persistencia persistencia;
    private final List<Cliente> clientes;
    private final List<Cuenta> cuentas;

    /**
     * Constructor del controlador del cajero.
     * Inicializa el sistema cargando datos de clientes, cuentas y transacciones.
     *
     * @param persistencia Objeto de persistencia para acceder a los datos almacenados
     * @throws Exception Si ocurre algún error durante la carga de datos
     */
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

    /**
     * Busca un cliente por su identificación.
     *
     * @param idCliente La identificación del cliente a buscar
     * @return El cliente encontrado
     * @throws IllegalArgumentException Si el cliente no existe
     */

    private Cliente buscarCliente(String idCliente) {
        return clientes.stream()
            .filter(c -> c.getIdentificacion().equals(idCliente))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("Debe crear el cliente antes de crear una cuenta."));
    }

    /**
     * Busca una cuenta por su número.
     *
     * @param numeroCuenta El número de cuenta a buscar
     * @return La cuenta encontrada
     * @throws IllegalArgumentException Si la cuenta no existe
     */
    private Cuenta buscarCuenta(String numeroCuenta) {
        return cuentas.stream()
            .filter(c -> c.getNumeroCuenta().equals(numeroCuenta))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));
    }

    /**
     * Guarda la lista de clientes en el sistema de persistencia.
     *
     * @throws Exception Si ocurre un error durante el guardado
     */
    private void guardarClientes() throws Exception {
        persistencia.guardarClientes(clientes);
    }

    /**
     * Guarda la lista de clientes en el sistema de persistencia.
     *
     * @throws Exception Si ocurre un error durante el guardado
     */
    private void guardarCuentas() throws Exception {
        persistencia.guardarCuentas(cuentas);
        for (Cuenta c : cuentas) {
            persistencia.guardarTransacciones(
                c.getNumeroCuenta(),
                c.getTransacciones()
            );
        }
    }
    
    // — Operaciones públicas —
    
    /**
     * Genera un reporte detallado del estado de una cuenta.
     *
     * @param numeroCuenta El número de la cuenta a consultar
     * @param pin El PIN de acceso a la cuenta
     * @return Un string con el estado de la cuenta, incluyendo depósitos, retiros y comisiones
     * @throws Exception Si ocurre un error durante la consulta o el PIN es inválido
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     */
    public String obtenerEstadoCuenta(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        if (!c.verificarPin(pin)) {
            throw new PinInvalidoException("PIN incorrecto o cuenta bloqueada");
        }

        long totalDeposito = 0, totalRetiro = 0, comisionDeposito = 0, comisionRetiro = 0;
        for (Transaccion t : c.getTransacciones()) {
            if (t.getTipo() == TipoTransaccion.DEPOSITO) {
                totalDeposito += t.getMonto();
                if (t.isCobroComision()) comisionDeposito += t.getMontoComision();
            } else {
                totalRetiro += t.getMonto();
                if (t.isCobroComision()) comisionRetiro += t.getMontoComision();
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

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param nombre El nombre completo del cliente
     * @param identificacion La identificación (cédula, pasaporte, etc.) del cliente
     * @param telefono El número de teléfono del cliente para notificaciones SMS
     * @param email El correo electrónico del cliente
     * @return El objeto Cliente creado
     * @throws Exception Si ocurre un error durante la creación o guardado del cliente
     */
    public Cliente crearCliente(String nombre,
                                String identificacion,
                                String telefono,
                                String email) throws Exception {
        Cliente c = new Cliente(nombre, identificacion, telefono, email);
        clientes.add(c);
        guardarClientes();
        return c;
    }

    /**
     * Crea una nueva cuenta bancaria asociada a un cliente existente.
     *
     * @param id La identificación del cliente dueño de la cuenta
     * @param pin El PIN inicial para la cuenta (debe cumplir con el formato requerido)
     * @param monto El monto inicial de depósito en colones
     * @throws Exception Si ocurre un error durante la creación o guardado de la cuenta
     * @throws IllegalArgumentException Si el formato del PIN es inválido o el cliente no existe
     */   
    public void crearCuenta(String id, String pin, long monto) throws Exception {
        Cliente cliente = buscarCliente(id);  

        if (!Validacion.validarPin(pin)) {   
            throw new IllegalArgumentException("Formato de PIN inválido");
        }

        Cuenta nueva = new Cuenta(cliente, pin, monto);  
        cuentas.add(nueva);
        guardarCuentas();
    }

    /**
     * Cambia el PIN de acceso de una cuenta.
     *
     * @param numeroCuenta El número de la cuenta a modificar
     * @param pinActual El PIN actual para verificación
     * @param pinNuevo El nuevo PIN a establecer
     * @throws Exception Si ocurre un error durante el cambio o guardado del PIN
     */
    public void cambiarPin(String numeroCuenta,
                          String pinActual,
                          String pinNuevo) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        c.cambiarPin(pinNuevo);
        guardarCuentas();
    }

    /**
     * Realiza un depósito en colones a una cuenta específica.
     *
     * @param numeroCuenta El número de la cuenta destinataria
     * @param monto El monto en colones a depositar
     * @throws Exception Si ocurre un error durante el depósito o guardado
     */
    public void depositarColones(String numeroCuenta, long monto) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        c.depositar(monto);
        guardarCuentas();
    }

    /**
     * Realiza un depósito en colones a una cuenta específica.
     *
     * @param numeroCuenta El número de la cuenta destinataria
     * @param montoUsd El monto en colones a depositar
     * @return  La transacción generada por el deposito
     * @throws Exception Si ocurre un error durante el depósito o guardado
     */    
    public Transaccion depositarDolares(String numeroCuenta, double montoUsd) throws Exception {
        long colones = Math.round(montoUsd * ServicioBCCR.getTipoCompra());
        Cuenta c = buscarCuenta(numeroCuenta);
        Transaccion t = c.depositar(colones);
        guardarCuentas();
        return t;
    }

    /**
     * Realiza un retiro en colones con verificación de PIN y código SMS.
     *
     * @param numeroCuenta El número de la cuenta origen
     * @param pin El PIN de acceso a la cuenta
     * @param codigoSms El código de verificación recibido por SMS
     * @param monto El monto en colones a retirar
     * @return La transacción generada por el retiro
     * @throws Exception Si ocurre un error durante el retiro o guardado
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     * @throws IllegalArgumentException Si el código SMS es inválido
     * @throws SaldoInsuficienteException Si la cuenta no tiene saldo suficiente para el retiro
     */
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

    /**
     * Realiza un retiro en dólares, convirtiendo el monto al tipo de cambio de venta.
     *
     * @param numeroCuenta El número de la cuenta origen
     * @param pin El PIN de acceso a la cuenta
     * @param codigoSms El código de verificación recibido por SMS
     * @param montoUsd El monto en dólares a retirar
     * @return La transacción generada por el retiro
     * @throws Exception Si ocurre un error durante el retiro o guardado
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     * @throws IllegalArgumentException Si el código SMS es inválido
     * @throws SaldoInsuficienteException Si la cuenta no tiene saldo suficiente para el retiro
     */
    public Transaccion retirarDolares(String numeroCuenta,
                                      String pin,
                                      String codigoSms,
                                      double montoUsd) throws Exception {
        long colonesReq = Math.round(montoUsd * ServicioBCCR.getTipoVenta());
        return retirarConSms(numeroCuenta, pin, codigoSms, colonesReq);
    }

    /**
     * Consulta el saldo disponible en colones de una cuenta.
     *
     * @param numeroCuenta El número de la cuenta a consultar
     * @param pin El PIN de acceso a la cuenta
     * @return El saldo disponible en colones
     * @throws Exception Si ocurre un error durante la consulta
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     */
    public long consultarSaldo(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        return c.consultarSaldo(pin);
    }

    /**
     * Consulta el saldo disponible en dólares de una cuenta, convirtiendo desde colones al tipo de cambio de compra.
     *
     * @param numeroCuenta El número de la cuenta a consultar
     * @param pin El PIN de acceso a la cuenta
     * @return El saldo disponible en dólares
     * @throws Exception Si ocurre un error durante la consulta
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     */
    public double consultarSaldoDolares(String numeroCuenta, String pin) throws Exception {
        long col = consultarSaldo(numeroCuenta, pin);
        return col / ServicioBCCR.getTipoCompra();
    }

    /**
     * Obtiene el historial de transacciones de una cuenta.
     *
     * @param numeroCuenta El número de la cuenta a consultar
     * @param pin El PIN de acceso a la cuenta
     * @return Una lista con todas las transacciones de la cuenta
     * @throws Exception Si ocurre un error durante la consulta
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     */
    public List<Transaccion> consultarTransacciones(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        return c.consultarTransacciones(pin);
    }


    /**
     * Obtiene el tipo de cambio de compra actual.
     *
     * @return El tipo de cambio de compra (colones por dólar)
     */
    public double getTipoCambioCompra() {
        return ServicioBCCR.getTipoCompra();
    }

    /**
     * Obtiene el tipo de cambio de venta actual.
     *
     * @return El tipo de cambio de venta (colones por dólar)
     */
    public double getTipoCambioVenta() {
        return ServicioBCCR.getTipoVenta();
    }

    /**
     * Actualiza el número de teléfono de un cliente.
     *
     * @param idCliente La identificación del cliente a modificar
     * @param nuevoTel El nuevo número de teléfono
     * @throws Exception Si ocurre un error durante la actualización o guardado
     */
    public void cambiarTelefono(String idCliente, String nuevoTel) throws Exception {
        Cliente c = buscarCliente(idCliente);
        c.setTelefono(nuevoTel);
        guardarClientes();
    }

    /**
     * Actualiza el correo electrónico de un cliente.
     *
     * @param idCliente La identificación del cliente a modificar
     * @param nuevoEmail El nuevo correo electrónico
     * @throws Exception Si ocurre un error durante la actualización o guardado
     */
    public void cambiarEmail(String idCliente, String nuevoEmail) throws Exception {
        Cliente c = buscarCliente(idCliente);
        c.setEmail(nuevoEmail);
        guardarClientes();
    }

    /**
     * Realiza una transferencia entre cuentas del mismo titular.
     *
     * @param ctaOrigen El número de la cuenta origen
     * @param pin El PIN de acceso a la cuenta origen
     * @param codigoSms El código de verificación recibido por SMS
     * @param ctaDestino El número de la cuenta destino
     * @param monto El monto en colones a transferir
     * @throws Exception Si ocurre un error durante la transferencia o guardado
     * @throws IllegalArgumentException Si las cuentas pertenecen a distintos titulares o el código SMS es inválido
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     * @throws SaldoInsuficienteException Si la cuenta origen no tiene saldo suficiente para la transferencia
     */
    public void transferir(String ctaOrigen,
                          String pin,
                          String codigoSms,
                          String ctaDestino,
                          long monto) throws Exception {
        Cuenta origen  = buscarCuenta(ctaOrigen);
        Cuenta destino = buscarCuenta(ctaDestino);
        if (!origen.getDueno().getIdentificacion().equals(destino.getDueno().getIdentificacion())) {
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

    /**
     * Elimina una cuenta y todas sus transacciones asociadas.
     *
     * @param numeroCuenta El número de la cuenta a eliminar
     * @param pin El PIN de acceso a la cuenta
     * @throws Exception Si ocurre un error durante la eliminación
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     */
    public void eliminarCuenta(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        if (!c.verificarPin(pin)) {
            throw new PinInvalidoException("PIN incorrecto o cuenta bloqueada");
        }
        cuentas.remove(c);
        persistencia.eliminarArchivoTransacciones(numeroCuenta);
        guardarCuentas();
    } 

    /**
     * Consulta el estado actual de una cuenta (activa, bloqueada, etc.).
     *
     * @param numeroCuenta El número de la cuenta a consultar
     * @param pin El PIN de acceso a la cuenta
     * @return El objeto EstadoCuenta con la información del estado
     * @throws Exception Si ocurre un error durante la consulta
     * @throws PinInvalidoException Si el PIN proporcionado es incorrecto o la cuenta está bloqueada
     */    public EstadoCuenta consultarEstadoCuenta(String numeroCuenta, String pin) throws Exception {
        Cuenta c = buscarCuenta(numeroCuenta);
        c.verificarPin(pin);
        return c.getEstatus();
    }
   
}