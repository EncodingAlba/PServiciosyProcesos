import java.util.ArrayList;
import java.util.List;

/**
 * @author Alba Muñoz Miñano
 * @since 07.10.25
 * @version 1.0
 */
public class Colaborar {
    public static void main(String[] args) {
String fichero = "miFicheroDeLenguaje.txt";
int numProcesos = 10;
int numLetrasPorLinea = 10;
int incrementoLineas = 10;

        //Iniciamos una lista para almacenar los hilos
        List<Thread> hilos = new ArrayList<>();

        //Creamos y lanzamos los hilos
        for (int i = 1; i <= numProcesos; i++) {
            //Número de líneas por hacer en cada instancia
            int lineasPorHacer =i * incrementoLineas;

            //Creamos el proceso (o tarea) del objeto Lenguaje
            Lenguaje lenguaje = new Lenguaje(fichero, lineasPorHacer, numLetrasPorLinea);

            //Creamos el hilo para ejecutar la tarea
            Thread thread = new Thread(lenguaje);
            hilos.add(thread);
            thread.start();
        }

        //Esperamos a que todos los hilos terminen
        for (Thread thread : hilos) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Error al esperar el hilo: " + e.getMessage());
            }

        }

    }
}