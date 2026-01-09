import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class URLExample {
    public static void main(String[] args) {
        //opcion 1: api de chistes de chuck norris (json, divertido)
        //String urlString = "https://api.chucknorris.io/jokes/random";

        //opcion 2: api de datos espaciales de la nasa
        //String urlString = "https://api.nasa.gov/planteary/apod?api_key=DEMO_KEY";

        //Opcion 3- Informacion sobre la estacion espacial internacional
        //String urlString = "http://api.open-notify.org/

        //Opcion 4: Datos del clima
        String urlString = "https://wttr.in/Murcia?format=j1";

        try {
            // Crear un objeto URL que apunte a un recurso HTTP
            URL url = new URL(urlString);

            //Paso 2: Abrir una conexión y obtener un flujo de entrada (inputstream)
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                System.out.println("-----------RESPUESTA DEL SERVIDOR-----------");
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println("------------FIN RESPUESTA DEL SERVIDOR-----------");


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