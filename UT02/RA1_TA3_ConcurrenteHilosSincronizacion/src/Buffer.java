/**
 * Descripción: Tarea para trabajar con concurrencia, hilos y sincronización
 * @author Alba
 * @version 1.0
 * @since 30.09.2025
 */

public class Buffer {

    private int[] buffer; //declaramos que el buffer es un array de números enteros
    private int count = 0; //número de elementos q contiene el buffer
    private int size; //tamaño del buffer

    public Buffer(int size) {
        this.size = size;
        buffer = new int[size];
    }

    //Método sincronizado para añadir un elemento por el suministrador
    public synchronized void add(int value) throws InterruptedException {
        while (count == size) {
            wait(); //esperar porque el buffer está lleno
        }
        buffer[count] = value; //añade un nuevo valor al buffer
        count++;
        notifyAll();
    }

    //Método sincronizado para extraer un elemento por el cliente
    //Solo extrae el último, es cómo una pila.
    public synchronized int remove() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        int value = buffer[count--];
        System.out.println("Cliente extrajo: " + value);
        notifyAll();
        return value;
    }
}