import java.util.concurrent.Semaphore;

public class TiendaOnline {
    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(2);

        Thread cliente1 = new Thread(new Clientes(semaphore, "Ana"));
        Thread cliente2 = new Thread(new Clientes(semaphore, "Raul"));
        Thread cliente3 = new Thread(new Clientes(semaphore, "Gonzalo"));
        Thread cliente4 = new Thread(new Clientes(semaphore, "Marina"));

        cliente1.start();
        cliente2.start();
        cliente3.start();
        cliente4.start();

        try {
            cliente1.join();
            cliente2.join();
            cliente3.join();
            cliente4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Thread empleado1 = new Thread(new Empleados(1));
        empleado1.setPriority(Thread.MIN_PRIORITY);
        Thread empleado2 = new Thread(new Empleados(2));
        empleado2.setPriority(Thread.MAX_PRIORITY);
        Thread empleado3 = new Thread(new Empleados(3));
        empleado3.setPriority(Thread.NORM_PRIORITY);
        Thread empleado4 = new Thread(new Empleados(4));
        empleado4.setPriority(Thread.NORM_PRIORITY);

        empleado1.start();
        empleado2.start();
        empleado3.start();
        empleado4.start();

        try {
            empleado1.join();
            empleado2.join();
            empleado3.join();
            empleado4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Todos los pedidos han sido procesados");


    }
}