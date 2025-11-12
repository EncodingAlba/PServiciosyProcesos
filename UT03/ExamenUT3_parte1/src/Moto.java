public class Moto extends Thread {

private final int ID;

public Moto(int ID){
    this.ID = ID;
}

@Override
    public void run() {

    try {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Moto con ID: " + this.ID + " avanzando al punto: " + i);
            Thread.sleep(500);
        }

        System.out.println("Moto con ID: " + this.ID + " ha completado su recorrido.");
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}


}
