/**
 * Clase que representa un cliente de datos.
 * Extrae valores del buffer en un hilo independiente.
 */
 public class Cliente extends Thread {
    private Buffer buffer;

    /**
     * Constructor de Cliente.
     * @param buffer buffer compartido del que se extraerán los valores
     */
    public Cliente(Buffer buffer) {
        this.buffer = buffer;
    }
    /**
     * Método que se ejecuta al arrancar el hilo.
     * Extrae 10 valores del buffer, con pausas de 1000 ms.
     */
    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                buffer.remove();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
