public class IntercambiaHilosDemoWaitNot { //BY: ALBA MUÑOZ

    private static class Compartido {
        private int dato;
        private boolean disponible = false;

        // Método para el productor
        public synchronized void producir(int valor) {
            while (disponible) {
                try {
                    wait(); // Espera a que el consumidor consuma
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Hilo productor interrumpido");
                }
            }
            dato = valor;
            disponible = true;
            System.out.println("Productor (WaitNot) produjo: " + valor);
            notify(); // Notifica al consumidor
        }

        // Método para el consumidor
        public synchronized int consumir() {
            while (!disponible) {
                try {
                    wait(); // Espera a que el productor produzca
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Hilo consumidor interrumpido");
                }
            }
            disponible = false;
            System.out.println("Consumidor (WaitNot) consumió: " + dato);
            notify(); // Notifica al productor
            return dato;
        }
    }

    // Clase Productor
    private static class Productor extends Thread {
        private final Compartido compartido;

        public Productor(Compartido compartido) {
            this.compartido = compartido;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) { // Producir 5 valores
                int randomNumber = (int) (Math.random() * 100) + 1;
                compartido.producir(randomNumber);
                try {
                    Thread.sleep(500); // Simula tiempo de producción
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Clase Consumidor
    private static class Consumidor extends Thread {
        private final Compartido compartido;

        public Consumidor(Compartido compartido) {
            this.compartido = compartido;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) { // Consumir 5 valores
                compartido.consumir();
                try {
                    Thread.sleep(500); // Simula tiempo de consumo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        Compartido compartido = new Compartido();

        // Crear y lanzar los hilos de productor y consumidor
        Productor productor = new Productor(compartido);
        Consumidor consumidor = new Consumidor(compartido);

        productor.start();
        consumidor.start();
    }
}

