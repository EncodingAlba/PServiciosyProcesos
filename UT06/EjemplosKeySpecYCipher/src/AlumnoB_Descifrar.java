import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AlumnoB_Descifrar {

    // ===== DATOS RECIBIDOS DEL ALUMNO A =====
    private static final String TRANSFORMACION = "AES/ECB/PKCS5Padding";
    private static final String CLAVE_TEXTO = "1234567890123456";
    private static final String CIFRADO_BASE64 = "lk/+i912m59xKnhJPnPLTClLYwuRGurpTQbTZp43+b19BkBn92WB5RvFVTTMZUow";
    // =======================================

    public static void main(String[] args) {

        try {
            // 1. Crear la clave AES
            byte[] keyBytes = CLAVE_TEXTO.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            // 2. Crear el Cipher
            Cipher cipher = Cipher.getInstance(TRANSFORMACION);

            // 3. Descifrar el mensaje
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(CIFRADO_BASE64));

            // 4. Convertir a texto
            String mensaje = new String(decrypted, StandardCharsets.UTF_8);

            System.out.println("Mensaje descifrado: " + mensaje);

        } catch (Exception e) {
            System.err.println("Error al descifrar: " + e.getMessage());
        }
    }
}
