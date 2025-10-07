import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Clase que genera líneas de texto aleatorio y las escribe en un archivo compartido
 */

public class Lenguaje implements Runnable {

    private final String fichero;
    private final int numLineas;
    private final int numLetrasPorLinea;

    public Lenguaje(String fichero, int numLineas, int numLetrasPorLinea) {
        this.fichero = fichero;
        this.numLineas = numLineas;
        this.numLetrasPorLinea = numLetrasPorLinea;
    }

    /**
     * Creamos el método que genera las lineas de texto aleatorio
     * y las escribe en el archivo
     */
    @Override
    public void run() {
        //Generador aleatorio
        Random random = new Random();
        //Creamos archivo
        for (int i = 1; i <= numLineas; i++) {
            StringBuilder sb = new StringBuilder(); //Almacenamos la línea
            for (int j = 1; j <= numLetrasPorLinea; j++) {
                sb.append(" ");
                char randomChar = (char) ('a' + random.nextInt(26)); //Hacemos un casting para generar letras en vez de números
                sb.append(randomChar);
            }
            //Agregar salto de línea al final de cada línea
            sb.append("\n");
            // Bloque sincronizado para asegurar que solo un hilo escriba a la vez
            synchronized (Lenguaje.class) {
                try (FileWriter fw = new FileWriter(fichero, true)) {
                    fw.write(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        System.out.println(Thread.currentThread().getName() + " escribió " + numLineas + " lineas.");
    }
}
