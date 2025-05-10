package modelo;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad para cifrar y descifrar texto usando AES/CBC/PKCS5Padding.
 * CBC con IV aleatorio es más seguro que ECB.
 */
public final class Cifrado {
    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";
    private static final String TIPO_CLAVE = "AES";
    private static final int TAMANO_CLAVE = 16; // 16 bytes = 128 bits
    private static final int TAMANO_IV = 16;
    
    // Clave fija (en producción debería ser configurable y protegida)
    private static final byte[] CLAVE = "MiClaveSecreta13".getBytes(StandardCharsets.UTF_8);

    private Cifrado() { }

    /**
     * Cifra el texto plano y devuelve una cadena en Base64 que incluye el IV.
     */
    public static String encrypt(String textoPlano) {
        try {
            // Validar que la clave tenga el tamaño correcto
            if (CLAVE.length != 16 && CLAVE.length != 24 && CLAVE.length != 32) {
                throw new IllegalArgumentException("Tamaño de clave inválido. Debe ser de 16, 24 o 32 bytes");
            }
            
            SecretKeySpec keySpec = new SecretKeySpec(CLAVE, TIPO_CLAVE);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            
            // Generar IV aleatorio
            byte[] iv = new byte[TAMANO_IV];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(textoPlano.getBytes(StandardCharsets.UTF_8));
            
            // Concatenar IV + texto cifrado y codificar en Base64
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Error cifrando texto", e);
        }
    }

    /**
     * Descifra un texto cifrado (en Base64 con IV incluido).
     */
    public static String decrypt(String textoCifrado) {
        try {
            // Validar que la clave tenga el tamaño correcto
            if (CLAVE.length != 16 && CLAVE.length != 24 && CLAVE.length != 32) {
                throw new IllegalArgumentException("Tamaño de clave inválido. Debe ser de 16, 24 o 32 bytes");
            }
            
            SecretKeySpec keySpec = new SecretKeySpec(CLAVE, TIPO_CLAVE);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            
            byte[] combined = Base64.getDecoder().decode(textoCifrado);
            
            // Verificar longitud mínima (IV + al menos 1 bloque cifrado)
            if (combined.length < TAMANO_IV + 16) {
                throw new IllegalArgumentException("Texto cifrado demasiado corto");
            }
            
            // Extraer IV (primeros 16 bytes)
            byte[] iv = new byte[TAMANO_IV];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            // Extraer texto cifrado (resto de bytes)
            byte[] encrypted = new byte[combined.length - TAMANO_IV];
            System.arraycopy(combined, TAMANO_IV, encrypted, 0, encrypted.length);
            
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error descifrando texto. Verifique que la clave y el texto cifrado sean correctos", e);
        }
    }
}