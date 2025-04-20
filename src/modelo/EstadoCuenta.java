package modelo;

/**
 * Representa los posibles estados de una cuenta bancaria:
 * ACTIVA (operativa) o INACTIVA (bloqueada).
 */
public enum EstadoCuenta {
    /**
     * Cuenta operativa, permite todas las transacciones.
     */
    ACTIVA,

    /**
     * Cuenta bloqueada, solo permite consulta de estatus.
     */
    INACTIVA
}
