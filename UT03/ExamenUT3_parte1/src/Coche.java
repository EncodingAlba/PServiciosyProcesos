public class Coche implements Runnable {
    private final int ID;

    public Coche(int ID){
        this.ID = ID;
    }

    @Override
    public void run() {

        try {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Coche con ID: " + this.ID + " avanzando al punto: " + i);
                Thread.sleep(450);
            }

            System.out.println("Coche con ID: " + this.ID + " ha completado su recorrido.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
