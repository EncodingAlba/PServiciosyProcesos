public class MultihiloThreadDemo extends Thread {
    //BY: ALBA MUÑOZ MIÑANO
    @Override
    public void run() {

        try {
            //Displaying the thread that is running
            System.out.println("Ejecutando el hilo con ID: " + Thread.currentThread().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

//Main Class
class MultihiloThread {
    public static void main(String[] args) {
        System.out.println("Ejecutando los hilos de tipo Thread:");
        int n = 8; //número de hilos
        for (int i = 0; i < n; i++) {
            MultihiloThreadDemo object = new MultihiloThreadDemo();
            object.start();
        }
    }
}