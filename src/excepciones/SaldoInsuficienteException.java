package excepciones;

import java.io.Serial;

/**
 * Se lanza cuando una cuenta no dispone de fondos suficientes
 * para completar una operación de retiro o transferencia.
 */
public class SaldoInsuficienteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepción con un mensaje descriptivo.
     *
     * @param message detalle de la insuficiencia de fondos
     */
    public SaldoInsuficienteException(String message) {
        super(message);
    }

    /**
     * Construye la excepción con mensaje y causa original.
     *
     * @param message detalle de la insuficiencia de fondos
     * @param cause   excepción que provocó este error
     */
    public SaldoInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
}
