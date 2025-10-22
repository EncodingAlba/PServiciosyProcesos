class Panaderia {
    //La estantería solo tiene hueco para poner 1 pan
    private int pan; //Identificamos el pan como un entero, pan num1, pan num2, etc.
    private boolean disponible = false; // Si disponible es falso, es que no hay una barra de pan en la panaderia

    public synchronized void producirPan(int valor) {
        //Cuando hay una barra en la estantería
        while (disponible) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        pan = valor;
        disponible = true;
        System.out.println("Producido el pan número: " + valor);
        notify();

    }
    public synchronized int consumirPan() {
        //Cuando no hay un pan disponible el consumidor tiene que esperar
        while (!disponible) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        disponible = false;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Consumido el pan número: " + pan);
        notify();
        return pan;
    }
}

public class ComunicacionEntreHilos {
    public static void main(String[] args) {
        Panaderia panaderiaPan = new Panaderia();

        //Hilo productor: el panadero puede hacer un maximo de 5 barras
        Thread panadero = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                panaderiaPan.producirPan(i);
            }
        });

        //Hilo consumidor -> Cliente
        Thread cliente = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                panaderiaPan.consumirPan();
            }
        });

        panadero.start();
        cliente.start();

    }
}