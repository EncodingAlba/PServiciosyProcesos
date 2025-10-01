/**
 *
 *  Clase principal que contiene el método main para ejecutar
 *  la aplicación de concurrencia usando un buffer compartido.
 *  Crea un suministrador y un cliente, y los arranca como hilos.
 *
 * Descripción: Tarea para trabajar con concurrencia, hilos y sincronización
 * @author Alba
 * @version 1.0
 * @since 30.09.2025
 */
public class MonitorExample {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(5);
        Suministrador alvaro = new Suministrador(buffer);
        Cliente juanFran = new Cliente(buffer);

        alvaro.start();
        juanFran.start();
    }
}