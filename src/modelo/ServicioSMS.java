package modelo;

import java.util.Random;

/**
 * Servicio simulado para envío de códigos SMS.
 * Genera palabras aleatorias de 3 a 7 letras mayúsculas.
 * En producción, reemplazar este stub por integración real
 * con una API de envío de SMS.
 */
public final class ServicioSMS {
    private static final Random RAND = new Random();
    private static final String ABECEDARIO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Constructor privado para evitar instanciación
    private ServicioSMS() { }

    /**
     * Simula el envío de un código secreto por SMS.
     *
     * @param telefono número de teléfono destino (para registro o logging)
     * @return la palabra generada (3 a 7 letras mayúsculas)
     */
    public static String enviarCodigo(String telefono) {
        int longitud = 3 + RAND.nextInt(5);  // genera entre 3 y 7
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            sb.append(ABECEDARIO.charAt(RAND.nextInt(ABECEDARIO.length())));
        }
        // Simulación de envío; en real se llamaría a la API externa:
        System.out.printf("Enviando SMS a %s: código=%s%n", telefono, sb);
        return sb.toString();
    }
}
