import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FTPLocalHost {
    public static void main(String[] args) {

        //para conectarme con pc profe:
        /*String servidor = "192.168.88.254";
        String usuario = "alumno";
        String password = "1234";
        String archivo = "archivo1.txt";*/

        //para conectarme al mio (en docker)
        String servidor = "localhost";
        String usuario = "alumno";
        String password = "1234";
        String archivo = "ProbandoFTP.txt";

        String urlString = String.format("ftp://%s:%s@%s/%s", usuario, password, servidor, archivo);

        try {
            URL url = new URL(urlString);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                System.out.println("---CONTENIDO DEL ARCHIVO---\n");
                String line;
                while ((line = reader.readLine()) != null){
                    System.out.println(line);
                }
                System.out.println("\n---FIN DEL ARCHIVO---");

            } catch (MalformedURLException e) {
                System.err.println("ERROR: La URL no tiene un formato válido.");
                System.err.println("Detalles: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("ERROR: Error al conectar o leer desde el servidor.");
                System.err.println("Detalles: " + e.getMessage());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}