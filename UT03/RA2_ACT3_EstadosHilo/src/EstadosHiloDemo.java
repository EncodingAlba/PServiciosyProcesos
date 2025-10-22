public class EstadosHiloDemo {
    //BY: ALBA MUÑOZ MIÑANO
    public static void main(String[] args) {
        Thread hilo1 = new Thread(new MiTarea("imprimir"));
        Thread hilo2 = new Thread(new MiTarea("sumar"));
        Thread hilo3 = new Thread(new MiTarea("saludar"));

        System.out.println("Antes de start(): Hilo1 = " + hilo1.getState() + ", Hilo2 = " + hilo2.getState() + ", Hilo3 = " + hilo3.getState());

        hilo1.start();
        hilo2.start();
        hilo3.start();

        // Estado después de start pero antes de terminar
        System.out.println("Después de start(): Hilo1 = " + hilo1.getState() + ", Hilo2 = " + hilo2.getState() + ", Hilo3 = " + hilo3.getState());

        try {
            hilo1.join(); // Espera a que hilo1 termine
            hilo2.join(); // Espera a que hilo2 termine
            hilo3.join(); //Espera a que hilo3 termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Estado después de terminar
        System.out.println("Después de finalizar: Hilo1 = " + hilo1.getState() + ", Hilo2 = " + hilo2.getState() + ", Hilo3 = " + hilo3.getState());
    }
}
