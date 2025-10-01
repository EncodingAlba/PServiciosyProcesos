/**
 * Clase que representa un suministrador de datos.
 * Añade valores al buffer en un hilo independiente.
 */
public class Suministrador extends Thread {
    private Buffer buffer;
    /**
     * Constructor de Suministrador.
     * @param buffer buffer compartido donde se añadirán los valores
     */
    public Suministrador(Buffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Método que se ejecuta al arrancar el hilo.
     * Añade los números del 1 al 10 al buffer, con pausas de 500 ms.
     */
    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                buffer.add(i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
