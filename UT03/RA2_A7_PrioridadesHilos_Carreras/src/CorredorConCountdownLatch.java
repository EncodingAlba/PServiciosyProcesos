import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CorredorConCountdownLatch implements Runnable { //BY ALBA MUÑOZ MIÑANO
    private String nombre;
    private List<String> metaCompartida;
    private CountDownLatch salida; // ← latch para sincronizar la salida

    public CorredorConCountdownLatch(String nombre, List<String> metaCompartida, CountDownLatch salida) {
        this.nombre = nombre;
        this.metaCompartida = metaCompartida;
        this.salida = salida;
    }

    @Override
    public void run() {
        try {
            // Esperamos a que se dé la señal de salida
            System.out.println(nombre + " está listo en la línea de salida...");
            salida.await(); // Espera hasta que el latch llegue a 0
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Empieza la carrera
        for (int i = 1; i <= 10; i++) {
            System.out.println(nombre + " ha avanzado al punto " + i);
            try {
                Thread.sleep(100); // solo para simular el avance
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        synchronized (metaCompartida) {
            metaCompartida.add(nombre);
        }
        System.out.println(nombre + " ha cruzado la meta.");
    }

    public static void main(String[] args) {
        List<String> metaCompartida = Collections.synchronizedList(new ArrayList<>());

        // Latch con 1 permiso (una cuenta regresiva)
        CountDownLatch salida = new CountDownLatch(1);

        Thread corredor1 = new Thread(new CorredorConCountdownLatch("Corredor2 1", metaCompartida, salida));
        Thread corredor2 = new Thread(new CorredorConCountdownLatch("Corredor2 2", metaCompartida, salida));
        Thread corredor3 = new Thread(new CorredorConCountdownLatch("Corredor2 3", metaCompartida, salida));
        Thread corredor4 = new Thread(new CorredorConCountdownLatch("Corredor2 4", metaCompartida, salida));
        Thread corredor5 = new Thread(new CorredorConCountdownLatch("Corredor2 5", metaCompartida, salida));

        corredor1.start();
        corredor2.start();
        corredor3.start();
        corredor4.start();
        corredor5.start();

        try {
            Thread.sleep(1000); // Simulamos la preparación antes de la salida
            System.out.println("¡¡Preparados... Listos... YA!!");
            salida.countDown(); // 🔔 Da la señal de salida: todos arrancan
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Esperamos a que todos terminen
        try {
            corredor1.join();
            corredor2.join();
            corredor3.join();
            corredor4.join();
            corredor5.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n🏁 Orden de llegada:");
        for (String corredor : metaCompartida) {
            System.out.println(corredor);
        }
    }
}
