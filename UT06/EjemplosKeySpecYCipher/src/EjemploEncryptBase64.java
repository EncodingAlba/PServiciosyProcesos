import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EjemploEncryptBase64 {

    public static void main(String[] args) {

        try {
            // 1. Crear la clave
            byte[] keyBytes = "1234567890123456".getBytes(StandardCharsets.UTF_8);
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            // 2. Crear el Cipher
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // 3. Cifrar
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal("Hola clase".getBytes(StandardCharsets.UTF_8));

            // 4. Mostrar en Base64
            String base64 = Base64.getEncoder().encodeToString(encrypted);
            System.out.println("Cifrado: " + base64);

            //5. DESCIFRAR
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(base64));
            String descifrado = new String(decrypted, StandardCharsets.UTF_8);

            System.out.println("Mensaje descifrado: " + descifrado);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algoritmo no disponible: " + e.getMessage());

        } catch (NoSuchPaddingException e) {
            System.err.println("Padding no soportado: " + e.getMessage());

        } catch (InvalidKeyException e) {
            System.err.println("Clave inválida: " + e.getMessage());

        } catch (IllegalBlockSizeException e) {
            System.err.println("Tamaño de bloque incorrecto: " + e.getMessage());

        } catch (BadPaddingException e) {
            System.err.println("Error de padding (clave incorrecta o datos corruptos): " + e.getMessage());
        }
    }
}
