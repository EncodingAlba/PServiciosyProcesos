import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EjemploCortoSecretKeySpec {
    public static void main(String[] args) {
        byte[] keyBytes = "1234567890123456".getBytes();
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

    }
}