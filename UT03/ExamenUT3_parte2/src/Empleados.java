public class Empleados implements Runnable {
    private int id;

    public Empleados(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1500);
            System.out.println("Empleado " + this.id + " ha terminado de procesar un pedido.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("El procesamiento del pedido ha sido interrumpido.");
        }

    }
}
