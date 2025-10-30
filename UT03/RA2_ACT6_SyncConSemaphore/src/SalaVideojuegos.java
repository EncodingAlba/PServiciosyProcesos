import java.util.Random;
import java.util.concurrent.Semaphore;

public class SalaVideojuegos { //BY: Alba Muñoz Miñano
    //Creación de un semáforo con 4 permisos
    private static Semaphore salaDisponible = new Semaphore(4);

    public static void main (String[] args) {
        for (int i = 1; i <= 10; i++) {
            new Thread(new Jugador(i)).start();
        }

    }
    //Clase que representa un jugador que intenta acceder al juego
    static class Jugador implements Runnable {
        private int id;
        public Jugador (int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println("Jugador " + id + " intenta acceder al juego.");

                //Adquiere un permiso del semáforo para acceder al juego
                salaDisponible.acquire();
                System.out.println("El jugador " + id + " ha adquirido acceso al juego.");

                //Simulación de uso del juego por un tiempo
                int msUso = (int) (Math.random() * (5000 - 2000 + 1)) + 2000;
                Thread.sleep(msUso);

                //Libera el permiso para que otros jugadores puedan acceder
                int segsUso = msUso/1000;
                System.out.println("Jugador " + id + " libera la sala de juegos. Tiempo aproximado de juego: " + segsUso + "s.");
                salaDisponible.release();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

