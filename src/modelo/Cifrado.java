package modelo;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utilidad para cifrar y descifrar texto (p. ej. PINs) usando AES/ECB/PKCS5Padding.
 * La clave es de 16 bytes y debe mantenerse en secreto.
 */
public final class Cifrado {
    // Nombre del algoritmo y modo
    private static final String ALGORITMO = "AES/ECB/PKCS5Padding";
    // Clave fija de ejemplo: 16 caracteres (16 bytes). En producción, gestionarla de forma segura.
    private static final byte[] CLAVE = "MiClaveSecreta123".getBytes(StandardCharsets.UTF_8);

    // Constructor privado para evitar instanciación
    private Cifrado() { }

    /**
     * Cifra el texto plano y devuelve una cadena en Base64.
     *
     * @param textoPlano texto a cifrar
     * @return texto cifrado codificado en Base64
     */
    public static String encrypt(String textoPlano) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(CLAVE, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(textoPlano.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error cifrando texto", e);
        }
    }

    /**
     * Descifra un texto cifrado (en Base64) y devuelve el texto plano original.
     *
     * @param textoCifrado texto cifrado en Base64
     * @return texto plano descifrado
     */
    public static String decrypt(String textoCifrado) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(CLAVE, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(textoCifrado);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error descifrando texto", e);
        }
    }
}
