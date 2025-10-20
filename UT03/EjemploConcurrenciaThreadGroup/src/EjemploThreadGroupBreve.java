public class EjemploThreadGroupBreve {
    private static final Object monitor = new Object();
    private static boolean tareaDisponible = false;
    public static void main(String[] args) {
        // Crear un grupo de hilos
        ThreadGroup grupo = new ThreadGroup("GrupoEjemplo");
        // Crear e iniciar hilos dentro del grupo
        for (int i = 1; i <= 3; i++) {
            new Thread(grupo, new Tarea(), "Hilo " + i).start();
        }
        // Simular preparación de la tarea
        try {
            Thread.sleep(500);
            synchronized (monitor) {
                tareaDisponible = true;
                monitor.notifyAll(); // Notifica a todos los hilos
                System.out.println("Hilo principal: Tarea disponible, notificación enviada.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    // Clase que representa la tarea que espera hasta ser notificada
    static class Tarea implements Runnable {
        @Override
        public void run() {
            synchronized (monitor) {
                while (!tareaDisponible) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " espera la tarea...");
                        monitor.wait(); // Espera hasta recibir la notificación
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " ha comenzado su tarea.");
            }
        }
    }
}