package modelo;

/**
 * Define los tipos de transacción que puede registrar una cuenta bancaria.
 */
public enum TipoTransaccion {
    /**
     * Depósito de fondos en colones.
     */
    DEPOSITO,

    /**
     * Retiro de fondos en colones.
     */
    RETIRO,

    /**
     * Transferencia de fondos entre cuentas del mismo dueño.
     */
    TRANSFERENCIA
}
