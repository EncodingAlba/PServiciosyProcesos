import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class IntercambioHilosDemoCola { //BY: ALBA MUÑOZ MIÑANO
    public static void main(String[] args) {
        //Creamos una cola de 1 elemento para que vayan creando (put) y sacando (take)
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(1);

        //Creamos los hilos del productor y consumidor
        Thread hiloProductor = new Thread(new Productor(cola, 5));
        Thread hiloConsumidor = new Thread(new Consumidor(cola, 5));

        //Iniciamos los hilos
        hiloProductor.start();
        hiloConsumidor.start();
    }
}

class Productor implements Runnable {
    private BlockingQueue<Integer> cola;
    private int cantidad;

    public Productor(BlockingQueue<Integer> cola, int cantidad) {
        this.cola = cola;
        this.cantidad = cantidad;
    }
    @Override
    public void run() {
        Random r = new Random();
        for (int i = 0; i < cantidad; i++) {
            int numero = r.nextInt(100) + 1;
            try {
                cola.put(numero);
                System.out.println("Productor: Generé dato: " + numero);
            } catch (InterruptedException e) {
                System.err.println("Productor interrumpido al intentar poner el dato: " + numero);
                Thread.currentThread().interrupt();
                return; //salimos del bucle si se interrumpe
            }
        }
    }
}

class Consumidor implements Runnable {
    private BlockingQueue<Integer> cola;
    private int cantidad;

    public Consumidor(BlockingQueue<Integer> cola, int cantidad) {
        this.cola = cola;
        this.cantidad = cantidad;
    }
    @Override
    public void run() {
        for (int i = 0; i < cantidad; i++) {
            try {
                int numero = cola.take();
                System.out.println("Consumidor: Procesé dato: " + numero);
            } catch (InterruptedException e) {
                System.err.println("Consumidor interrumpido al intentar tomar un dato");
                Thread.currentThread().interrupt();
                return; // salimos del bucle si se interrumpe
            }
        }
    }
}
