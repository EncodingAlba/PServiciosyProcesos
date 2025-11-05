import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Corredor implements Runnable { //BY ALBA MUÑOZ MIÑANO
    String nombre;
    List<String> metaCompartida;

    public Corredor(String nombre, List<String> metaCompartida) {
        this.nombre = nombre;
        this.metaCompartida = metaCompartida;
    }

    @Override
    public void run() {
       for (int i=1; i<10; i++) {
           System.out.println(this.nombre + " ha avanzado al punto " + i);
       }

       try {
           Thread.sleep(500);

       } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
       }

       synchronized (this.metaCompartida) {
           this.metaCompartida.add(this.nombre);
       }
       System.out.println(this.nombre + " ha cruzado la meta.");
    }

    public static void main(String[] args) {

        List<String> metaCompartida = Collections.synchronizedList(new ArrayList<>());


        Thread corredor1 = new Thread(new Corredor("Corredor 1", metaCompartida));
        corredor1.setPriority(Thread.MIN_PRIORITY);
        Thread corredor2 = new Thread(new Corredor("Corredor 2", metaCompartida));
        corredor2.setPriority(Thread.NORM_PRIORITY);
        Thread corredor3 = new Thread(new Corredor("Corredor 3", metaCompartida));
        corredor3.setPriority(Thread.MAX_PRIORITY);
        Thread corredor4 = new Thread(new Corredor("Corredor 4", metaCompartida));
        corredor4.setPriority(Thread.NORM_PRIORITY);
        Thread corredor5 = new Thread(new Corredor("Corredor 5", metaCompartida));
        corredor5.setPriority(Thread.MIN_PRIORITY);

        corredor1.start();
        corredor2.start();
        corredor3.start();
        corredor4.start();
        corredor5.start();

        //Esperamos a que todos terminen
        try {
            corredor1.join();
            corredor2.join();
            corredor3.join();
            corredor4.join();
            corredor5.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Orden de llegada:");

        //Recorremos la lista compartida para imprimir
        for (String corredor : metaCompartida)  {
            System.out.println(corredor);
        }
    }

}