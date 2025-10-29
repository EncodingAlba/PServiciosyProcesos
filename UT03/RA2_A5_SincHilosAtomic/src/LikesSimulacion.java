import java.util.concurrent.atomic.AtomicInteger;

public class LikesSimulacion { //BY: ALBA MUÑOZ MIÑANO

    //Declara un AtomicInteger llamado contadorLikes con un valor de 0
    static AtomicInteger contadorLikes = new AtomicInteger(0);



    //Crear clase usuario interna q extienda Thread
    static class Usuario extends Thread {
        private final String nombre;
        public Usuario(String nombre) {
            this.nombre = nombre;
        }

        //En el metodo run implementa un bucle que incremente contadorLikes
        @Override
        public void run() {
            for  (int i = 1; i <= 100; i++) {
                int likesActuales = contadorLikes.incrementAndGet();
                System.out.println(nombre + " dio un like. Total de likes: " + likesActuales);
                System.out.println("Likes por hilo del " + nombre + ": " + i);

            //Opcional: simulación de retardo aleatorio entre likes
                try {
                    Thread.sleep((long) (Math.random() * 100));

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(nombre + " fue interrumpido.");
                }
            }

            }
        }
    public static void main (String[] args) {

        //Crear hilos
        Thread usuario1 = new Thread(new Usuario("usuario1"));
        Thread usuario2 = new Thread(new Usuario("usuario2"));
        Thread usuario3 = new Thread(new Usuario("usuario3"));

        //Iniciar hilos
        usuario1.start();
        usuario2.start();
        usuario3.start();

        //Esperar a que los hilos terminen con join
        try {
            usuario1.join();
            usuario2.join();
            usuario3.join();

        }catch (InterruptedException e) {
            System.out.println("Error al esperar la finalización de los hilos");
        }

        //Mostrar el resultado final
        System.out.println("Total final de likes: " + contadorLikes.get());
    }

    }


