import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FTPWithURL {
    public static void main(String[] args) {
        //Esta URL intenta acceder a un servidor FTP publico de prueba
        //Formato: ftp://usuario:contraseña@servidor/ruta/archivo
        String urlString = "ftp://anonymous:password@ftp.example.com/archivo.txt";

        try {
            URL url = new URL(urlString);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null){
                    System.out.println(line);
                }

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