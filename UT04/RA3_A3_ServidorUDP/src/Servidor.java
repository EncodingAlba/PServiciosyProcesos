import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Servidor {

    public static void main(String[] args) {
        final int PUERTO = 12345;
        try (DatagramSocket socketServidor = new DatagramSocket(PUERTO)) {
            System.out.println("Servidor iniciado");

            //Preparar un buffer para recibir mensajes
            byte[] buffer = new byte[1024];
            DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);

            while (true) {
                //Escuchar mensajes entrantes
                socketServidor.receive(paqueteRecibido);

                String mensaje = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());

                String respuesta = "";

                if (mensaje.startsWith("@sensor#") && mensaje.endsWith("@")) {
                    String nombreSensor = mensaje.substring(8, mensaje.length() - 1);
                    respuesta = "Estado del sensor " + nombreSensor + ": ACTIVO";
                } else if (mensaje.equals("@listadoSensores@")) {
                    respuesta = "Sensores disponibles: temperatura, luz, humedad";
                } else if (mensaje.equals("@salir@")) {
                    respuesta = "Hasta luego!";
                }

                byte[] datosRespuesta = respuesta.getBytes();
                DatagramPacket paqueteRespuesta = new DatagramPacket(
                        datosRespuesta,
                        datosRespuesta.length,
                        paqueteRecibido.getAddress(),
                        paqueteRecibido.getPort()
                );
                socketServidor.send(paqueteRespuesta);
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}