public class SimuladorTrafico {
    public static void main(String[] args) {
        Thread moto1 = new Moto(1);
        Thread moto2 = new Moto(2);
        Thread coche1 = new Thread(new Coche(1));
        Thread coche2 = new Thread(new Coche(2));
        moto1.start();
        moto2.start();
        coche1.start();
        coche2.start();

        // Seguimiento de los estados de los hilos
        while (moto1.isAlive() || moto2.isAlive() || coche1.isAlive() || coche2.isAlive()) {
            // Seguimiento de los estados de los 4 hilos: muestra por pantalla:
            //Estado de la moto 1:
            System.out.println("Estado de la moto 1: " + moto1.getState());
            //Estado de la moto 2:
            System.out.println("Estado de la moto 2: " + moto2.getState());
            //Estado del coche 1:
            System.out.println("Estado del coche 1: " + coche1.getState());
            //Estado del coche 2:
            System.out.println("Estado del coche 2: " + coche2.getState());

            try {
                Thread.sleep(200); // Pausa para evitar demasiados mensajes
            } catch (InterruptedException e) {
                System.err.println("Seguimiento de estados interrumpido.");
            }
        }
    }
}