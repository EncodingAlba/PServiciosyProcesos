import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Servidor {
    //Definimos el puerto en el que el servidor escuchará
    public static final int PUERTO = 12345;

    //metodo principal
    public static void main(String[] args) {
        //bloque try with resources: en resources creamos socket UDP en el puerto definido
        try (DatagramSocket serverSocket = new DatagramSocket(PUERTO)) {
            //informamos
            System.out.println("Servidor UDP iniciado en el puerto " +  PUERTO);

            //bucle while infinito para escuchar continuamente las peticiones de los clientes
            while (true) {
                //Preparar al servidor para que cuando reciba el mensaje lo pueda almacenar
                //Preparar un buffer para recibir mensajes
                byte[] receivebuffer = new byte[1024];
                //y un datagram packet para  almacenar el datagrama recibido
                DatagramPacket receivePacket = new DatagramPacket(receivebuffer, receivebuffer.length);


                //Esperar hasta recibir la petición del cliente
                //informamos
                System.out.println("Esperando datagrama el cliente...");
                //Escuchar mensajes entrantes
                serverSocket.receive(receivePacket);

                //Extraemos el mensaje en formato String del msj recibido
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Mensaje recibido desde el cliente: " + clientMessage);

                //procesamos o analizamos el mensaje para identificar la respuesta adecuada
                String serverResponse = processMessage(clientMessage);

                //Responder:
                //Preparar respuesta: convertir respuesta en bytes para envio
                byte[] sendBuffer = serverResponse.getBytes();

                DatagramPacket paqueteRespuesta = new DatagramPacket(
                        sendBuffer,
                        sendBuffer.length,
                        receivePacket.getAddress(),
                        receivePacket.getPort()
                );

                //Enviar respuesta
                serverSocket.send(paqueteRespuesta);
                System.out.println("Mensaje enviado al cliente: " + serverResponse);
            }


        } catch (SocketException e) {
            System.err.println("Error al crear el socket: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }

    }

    private static String processMessage(String message) {
        //convertimos todo el msj a minusculas para procesamiento mas sencillo
        String lowerCaseMessage = message.toLowerCase();
        //Manejamos los distintos comandos
        if (lowerCaseMessage.contains("@sensor#")){
                //extraemos el nombre del sensor
            //mensaje de ejemplo: @sensor#temperatura@
            return handleSensorMessage(message);
        } else if (lowerCaseMessage.equals("@listadosensores@")) {
            return "Sensores disponibles: temperatura, humedad, viento";
        } else if (lowerCaseMessage.equals("@ayuda@")) {
            return getHelpMessage();
        } else if (lowerCaseMessage.equals("@salir@")) {
            return "Cerrando interacción con el servidor. ¡Hasta luego!";
        } else  {
            return "Comando no reconocido. Escribe @ayuda@ para más información.";
        }
    }

    //Procesamiento de solicitudes
    private static String handleSensorMessage(String fullMessage) {
        //ejemplo de fullmessage: @sensor#temperatura@, separamos por #,
        //split divide el string en un array usando # como delimitador
        String[] parts = fullMessage.split("#");

        //Si no hay dos elementos en parts, el formato es incorrecto
        if (parts.length < 2) {
            return "Formato de solicitud de sensor inválido. Usa @sensor#nombreSensor@";
        }

        //Extraemos el nombre del sensor (ej: 'temperatura@')
        String sensorPart = parts[1];
        //A veces viene con el '@' al final, lo quitamos
        String sensorName = sensorPart.replace("@", "").toLowerCase();

        //Generamos un valor aleatorio para simular el sensor
        switch (sensorName) {
            case "temperatura":
                int temp = 20 + (int) (Math.random() * 16);
                return "Temperatura actual: " + temp + "ºC";
            case "humedad":
                int humedad = 20 + (int) (Math.random() * 101);
                return "Humedad actual: " + humedad + "%";
            case "viento":
                int viento = 20 + (int) (Math.random() * 50);
                return "Viento actual: " + viento + " km/h";
            default:
                return "Sensor " + sensorName + " no reconocido.";
        }
    }


    private static String getHelpMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== COMANDOS DISPONIBLES ===\n");
        sb.append("@sensor#nombreSensor@ -> Devuelve el estado del sensor (por ejemplo: @sensor#temperatura@\n");
        sb.append("@listadoSensores@ -> devuelve un listado de los sensores disponibles\n");
        sb.append("@ayuda@ -> Muestar este mensaje de ayuda\n");
        sb.append("@salir@ -> Finaliza la interacción con el servidor\n");

        return sb.toString();
    }

}