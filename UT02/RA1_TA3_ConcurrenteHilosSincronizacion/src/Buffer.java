/**
 * Clase que representa un buffer compartido entre hilos.
 * Implementa métodos sincronizados para añadir y extraer elementos,
 * asegurando que los hilos esperen cuando el buffer esté lleno o vacío.
 */

public class Buffer {
    private int[] buffer;
    private int count = 0;
    private int size;

    /**
     * Constructor de Buffer.
     * @param size tamaño máximo del buffer
     */
    public Buffer(int size) {
        this.size = size;
        buffer = new int[size];
    }
    /**
     * Añade un valor al buffer. Si el buffer está lleno, espera hasta que haya espacio.
     * @param value el valor a añadir al buffer
     * @throws InterruptedException si el hilo es interrumpido mientras espera
     */
    public synchronized void add(int value) throws InterruptedException {
        while (count == size) {
            wait();
        }
        buffer[count] = value;
        count++;
        System.out.println("Suministrador añadió: " + value);
        notifyAll();
    }

    /**
     * Extrae un valor del buffer. Si el buffer está vacío, espera hasta que haya elementos.
     * Extrae el último elemento añadido (comportamiento tipo pila).
     *
     * @return el valor extraído del buffer
     * @throws InterruptedException si el hilo es interrumpido mientras espera
     */
    public synchronized int remove() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        int value = buffer[--count];
        System.out.println("Cliente extrajo: " + value);
        notifyAll();
        return value;
    }
}
