import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
//BY: ALBA MUÑOZ MIÑANO
public class UDPClient {
    //Dirección IP o nombre del host donde se ejecuta el servidor
    private static final String SERVER_ADDRESS = "localhost";

    //Puerto en el que el servidor está escuchando
    private static final int SERVER_PORT = 12345;

    //Metodo principal
    public static void main(String[] args) {
        //Informamos
        System.out.println("Cliente UDP iniciado");

        //1ºLeemos lo que introduce el usuario (cliente) por teclado
        //Utilizamos scanner para leer la entrada del teclado del usuario
        //System.in es el flujo de entrada estándar (teclado).
        Scanner scanner = new Scanner(System.in);


        //Bloque try-w-resources (dentro de r creamos DatagramSocket)
        try (DatagramSocket socketCliente = new DatagramSocket()) {

            boolean keeprunning = true; //variable para controlar la salida del bucle

            //Bucle principal del cliente
            while (keeprunning) {
                mostrarMenu();

                //Leemos la opción elegida por el cliente
                int opcion = scanner.nextInt();
                scanner.nextLine();

                //Variable donde almacenaré el mensaje que posteriormente le mande al servidor
                String messageToSend;

                //Estructura del switch para las acciones a realizar por cada opción
                switch (opcion) {
                    case 1:
                        //Pedimos el nombre del sensor al usuario
                        System.out.println("Ingresa el nombre del sensor (temperatura/humedad/viento): ");
                        String sensorName = scanner.nextLine();

                        //Formato del mensaje: @sensor#temperatura@
                        messageToSend = "@sensor#"  + sensorName + "@";
                        break;
                        //Listar sensores disponibles
                    case 2:
                        messageToSend = "@listadoSensores@";
                        break;
                        //Solicitar ayuda
                    case 3:
                        messageToSend = "@ayuda@";
                        break;
                        //Salir del programa
                    case 4:
                        messageToSend = "@salir@";
                        keeprunning = false;
                        break;
                    default:
                        System.out.println("Opción no valida. Enviando comando no Reconocido.");
                        messageToSend = "ComandoNoValido";
                        break;

                }

                //Enviamos el mensaje al servidor
                sendMessageToServer(socketCliente, messageToSend);

                //PASO 4: RECIBIR Y MOSTRAR LA RESPUESTA DEL SERVIDOR

                //Recibimos la respuesta dle servidor (excepto si estamos saliendo)
                if (!messageToSend.equals("@salir@")) {
                    String response = receiveMessageFromServer(socketCliente);
                    System.out.println("Respuesta del servidor: " + response);
                } else {
                    //Mensaje de despedida
                    System.out.println("Saliendo del cliente...");
                }

            }

        } catch (SocketException e) {
            System.err.println("Error al crear el socket del cliente: " + e.getMessage());
        } finally {
            //el socket se cierra automaticamente por estar dentro de recursos
            scanner.close();
        }
    }

    public static void mostrarMenu() {
        //Mostramos un menú simple
        System.out.println("=== MENÚ DE OPCIONES ===");
        System.out.println("1. Consultar sensor (ejemplo: temperatura, humedad, viento)");
        System.out.println("2. Listar sensores disponibles");
        System.out.println("3. Mostrar ayuda");
        System.out.println("4. Salir");
        System.out.println("Elige una opción: ");
    }

    //metodo para enviar mensajes al servidor
    private static void sendMessageToServer(DatagramSocket socket, String messageToSend) {
        try {
            //PASO 1: CONVERTIR EL MENSAJE A BYTES
            byte[] sendBuffer = messageToSend.getBytes();

            //PASO 2: OBTENER LA DIRECCIÓN IP DEL SERVIDOR
            InetAddress serverIP = InetAddress.getByName(SERVER_ADDRESS);

            //PASO 3: CREAR EL DATAGRAMA DE ENVÍO
            //Creamos el paquete con la dirección del servidor y puerto
            DatagramPacket sendPacket = new DatagramPacket(
                    sendBuffer,
                    sendBuffer.length,
                    serverIP,
                    SERVER_PORT
            );

            //PASO 4: ENVIAR EL DATAGRAMA
            socket.send(sendPacket);

        } catch (IOException e) {
            System.err.println("Error al enviar el datagrama al servidor: " + e.getMessage());
        }
    }

    //metodo para recibir mensajes del servidor
    private static String receiveMessageFromServer(DatagramSocket socket) {
        try {
            //PASO 1: PREPARAR EL BUFFER DE RECEPCIÓN
            byte[] receiveBuffer = new byte[1024];

            //Preparamos el datagrama de recepcion
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            //PASO 2: ESPERAR LA RESPUESTA DEL SERVIDOR
            socket.receive(receivePacket);

            //PASO 3: EXTRAEMOS EL MENSAJE DEL DATAGRAMA
            return new String(receivePacket.getData(), 0, receivePacket.getLength());

        } catch (IOException e) {
            System.err.println("Error al recibir el datagrama del servidor: " + e.getMessage());
        }
        //Mensaje por defecto
        return "No se recibió respuesta del servidor.";
    }
}

