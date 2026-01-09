import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FTPDemo {
    public static void main(String[] args) {
        //Esta URL intenta acceder a un servidor FTP publico de prueba
        //Formato: ftp://usuario:contraseña@servidor/ruta/archivo
        //String urlString = "ftp://anonymous:password@ftp.example.com/archivo.txt";

        //SERVIDOR DE PRUEBA: test.rebex.net - pruebas para desarrollo
        String servidor = "test.rebex.net";
        String usuario = "demo";
        String password = "password";
        String archivo = "readme.txt";

        String urlString = String.format("ftp://%s:%s@%S/%s", usuario, password, servidor, archivo);

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