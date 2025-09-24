public class LanzaHilos {
    public static void main(String[] args) {
        //Get the current thread
        Thread thread = Thread.currentThread();
        System.out.println(thread);

        Thread h1=new Thread(new Hilo("H1"));
        Thread h2=new Thread(new Hilo("H2"));
        h1.start();
        h2.start();
        System.out.println("Hilo principal terminado.");
    }
}
