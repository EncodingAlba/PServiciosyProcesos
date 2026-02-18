UT6 – Cifrado en Java: KeySpec y CipherAutores: Alba Muñoz y Gilberto GilMódulo: Programación de Servicios y Procesos / Seguridad y CriptografíaNivel: Avanzado (Rigurosidad Técnica)1. Contexto Tecnológico: JCA y JCEPara entender el cifrado en Java, es vital conocer que estas clases forman parte de la Java Cryptography Architecture (JCA) y su extensión, la Java Cryptography Extension (JCE).Java utiliza un modelo basado en Proveedores (Providers). Cuando solicitas un algoritmo (como AES), Java lo busca en los proveedores instalados (por defecto, el proveedor "SunJCE"). Esto permite que el código sea independiente de la implementación específica.2. Representación de Claves: La Jerarquía KeySpecEn Java, existe una distinción crítica entre la transparencia de una clave y su opacidad.2.1. KeySpec (Transparente)Es una interfaz que actúa como un "plano" de la clave. Permite acceder a los componentes individuales (por ejemplo, el array de bytes).SecretKeySpec: Se usa cuando ya tenemos los bytes de la clave (criptografía simétrica).PBEKeySpec: Se usa para derivar claves a partir de una contraseña (Password-Based Encryption).2.2. Key (Opaca)Representada por la interfaz java.security.Key. Es el objeto que el motor de cifrado puede usar. No siempre permite ver los bytes internos (por seguridad, especialmente en módulos de hardware).Regla de oro: Convertimos un KeySpec (plano) en un SecretKey (objeto ejecutable) mediante una factoría o directamente mediante el constructor en el caso de AES.3. La Clase Cipher: El Motor CriptográficoLa clase javax.crypto.Cipher es una máquina de estados. Su comportamiento cambia drásticamente según cómo se inicialice.3.1. La TransformaciónEl método Cipher.getInstance("AES/ECB/PKCS5Padding") requiere una cadena de transformación con tres partes:Algoritmo: (Ej: AES, DES, RSA). Define la estructura matemática.Modo de Operación: (Ej: ECB, CBC, GCM). Define cómo se procesan bloques de datos grandes.Padding (Relleno): (Ej: PKCS5Padding). AES trabaja con bloques de 16 bytes. Si el mensaje no es múltiplo de 16, el padding rellena el espacio sobrante.3.2. Gestión de ExcepcionesEl trabajo con Cipher requiere capturar excepciones específicas (rigurosidad profesional):NoSuchAlgorithmException: El algoritmo no existe en el proveedor.InvalidKeyException: La clave no tiene la longitud o formato correcto.BadPaddingException: Ocurre durante el descifrado si la clave es incorrecta o los datos están corruptos.4. Rigurosidad en Seguridad: ECB vs. GCMEs fundamental entender por qué el ejemplo educativo (ECB) no debe llegar a producción.Comparativa TécnicaCaracterísticaECB (Electronic Codebook)GCM (Galois/Counter Mode)DeterminismoSí: El mismo bloque siempre produce el mismo cifrado.No: El IV asegura que el mismo mensaje cambie siempre.SeguridadBaja: Expone patrones de datos (fugas de información).Alta: Estándar industrial moderno.IntegridadNo: No detecta si alguien modificó el mensaje cifrado.Sí: Incluye una firma (Tag) de autenticación.IV RequeridoNo.Sí (Vector de Inicialización).5. Implementación Profesional: Ejemplo con AES-GCMA continuación, se presenta un código robusto que incluye la generación de un IV aleatorio y el uso de Base64 para la representación.Javaimport javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityManagerAES {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_BIT_LENGTH = 128; // Longitud del tag de autenticación
    private static final int IV_BYTE_LENGTH = 12;  // Recomendado para GCM

    public static String encrypt(String plainText, byte[] keyBytes) throws Exception {
        // 1. Preparar la clave
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        // 2. Generar Vector de Inicialización (IV) aleatorio
        byte[] iv = new byte[IV_BYTE_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);

        // 3. Configurar Cipher
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        // 4. Cifrar
        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        // 5. Concatenar IV + Mensaje Cifrado para poder descifrar luego
        byte[] combined = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String combinedBase64, byte[] keyBytes) throws Exception {
        byte[] combined = Base64.getDecoder().decode(combinedBase64);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        // 1. Extraer el IV (primeros 12 bytes)
        byte[] iv = new byte[IV_BYTE_LENGTH];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);

        // 2. Extraer el cuerpo del mensaje cifrado
        int cipherTextLen = combined.length - IV_BYTE_LENGTH;
        byte[] cipherText = new byte[cipherTextLen];
        System.arraycopy(combined, IV_BYTE_LENGTH, cipherText, 0, cipherTextLen);

        // 3. Configurar Cipher para descifrar
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        return new String(cipher.doFinal(cipherText));
    }
}
6. Buenas Prácticas y Seguridad RealKey Derivation (PBKDF2): Nunca uses .getBytes() de un String "hardcodeado" como clave. Usa SecretKeyFactory con un Salt para generar claves robustas a partir de contraseñas.Gestión de Claves: En entornos reales, las claves se almacenan en un KeyStore de Java o en servicios externos (AWS KMS, HashiCorp Vault).Longitud de Clave:128 bits ($16$ bytes): Estándar.256 bits ($32$ bytes): Nivel gubernamental (requiere que Java esté configurado para "Unlimited Strength", estándar en versiones modernas).No reutilizar el IV: En modos como GCM, reutilizar el mismo IV con la misma clave para dos mensajes distintos destruye la seguridad del cifrado.