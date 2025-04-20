package excepciones;

import java.io.Serial;

/**
 * Se lanza cuando el formato del PIN no cumple con los criterios establecidos
 * (6 caracteres alfanuméricos, al menos una letra mayúscula y un dígito).
 */
public class PinInvalidoException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepción con un mensaje descriptivo.
     *
     * @param message detalle de la razón de la invalidación del PIN
     */
    public PinInvalidoException(String message) {
        super(message);
    }

    /**
     * Construye la excepción con mensaje y causa original.
     *
     * @param message detalle de la razón de la invalidación del PIN
     * @param cause   excepción que provocó este error
     */
    public PinInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
