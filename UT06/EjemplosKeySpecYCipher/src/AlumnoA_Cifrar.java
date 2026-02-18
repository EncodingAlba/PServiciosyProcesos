import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AlumnoA_Cifrar {

    // ===== DATOS DE LA ACTIVIDAD =====
    private static final String MENSAJE = "Te ha salido muy bien la presentación <3";
    private static final String CLAVE_TEXTO = "1234567890123456"; // 16 bytes (AES-128)
    private static final String TRANSFORMACION = "AES/ECB/PKCS5Padding";
    // =================================

    public static void main(String[] args) {

        try {
            // 1. Crear la clave AES
            byte[] keyBytes = CLAVE_TEXTO.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            // 2. Crear el Cipher
            Cipher cipher = Cipher.getInstance(TRANSFORMACION);

            // 3. Cifrar el mensaje
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(MENSAJE.getBytes(StandardCharsets.UTF_8));

            // 4. Convertir a Base64 (para compartir)
            String base64 = Base64.getEncoder().encodeToString(encrypted);

            // 5. Mostrar los datos a pasar al Alumno B
            System.out.println("=== DATOS PARA EL ALUMNO B ===");
            System.out.println("Transformación: " + TRANSFORMACION);
            System.out.println("Clave: " + CLAVE_TEXTO);
            System.out.println("Mensaje cifrado (Base64): " + base64);

        } catch (Exception e) {
            System.err.println("Error al cifrar: " + e.getMessage());
        }
    }
}
