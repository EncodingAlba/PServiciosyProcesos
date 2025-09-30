import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Descripción: Ejemplo de clase para lanzar un proceso
 * @author Alba
 * @version 1.0
 * @since 30.09.2025
 */

public class Main {
    public static void main(String[] args) {

        try {
            //1. Creamos el proceso
            ProcessBuilder pb = new ProcessBuilder("ping", "8.8.8.8");
            //ping google.com no funciona por el firewall

            //2. Inicializamos el proceso
            Process p = pb.start();

            //3. Creamos BufferedReader
            BufferedReader lector = new BufferedReader(new InputStreamReader(p.getInputStream()));

            //4. Leemos linea a linea
            String line;
            while ((line = lector.readLine()) != null)  {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}