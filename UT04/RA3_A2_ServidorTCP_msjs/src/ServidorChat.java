import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//BY: ALBA MUÑOZ MIÑANO

public class ServidorChat {
    public static void main(String[] args) {

        //1º) Definimos el puerto en el que el servidor estará escuchando conexiones
        final int PUERTO = 12345;

        //2º) Bloque try-with-resources, para crear el ServerSocket como recurso, y que este se cierre al finalizar
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado, esperando conexión... Puerto: " + PUERTO);

                // a) Bucle infinito: el servidor debe estar siempre escuchando para aceptar a los clientes
            while (true) {
                // i) Dentro del bucle: bloquear la ejecución del programa hasta que un cliente se conecta
                Socket socketCliente = serverSocket.accept(); //el metodo bloquea el codigo hasta que un cliente se conecta al server
                System.out.println("Cliente conectado: " + socketCliente.getInetAddress().getHostAddress()
                        + ":" + socketCliente.getPort());

                // ii) Otro try-with-resources (con bloque finally) para gestionar automáticamente los streams de entrada-salida
                //ii.a) Dentro de los resources de este try -> leer los mensajes del cliente linea por linea
                //ii.b) También dentro del bloque de recursos de este try, se incluye el código para enviar
                //      respuestas al cliente -> PrintWriter

                try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);) {
                    String mensajeCliente;
                    //ii.c) Dentro del bloque try (pero ya fuera de los recursos) crear un bucle while que lea los
                    // mensajes mientras que el cliente siga enviando datos (readline () devuelve null cuando el
                    // cliente cierra la conexión)
                    while ((mensajeCliente = entrada.readLine()) != null) {
                        //Dentro del while:
                        //1.- Mostrar en la consola del servidor lo que el cliente ha enviado
                        System.out.println("El cliente dice: " + mensajeCliente);
                        //2.- Construir la respuesta que le vamos a enviar al cliente y enviarla
                        // de vuelta al cliente
                        String respuesta = "Mensaje recibido del cliente. Este es el mensaje: " + mensajeCliente;
                        salida.println(respuesta);
                    }
                    // iii) Cerrar el bloque try, y abrir la parte del catch para capturar excepciones
                } catch (IOException e) {
                    System.out.println("Error al leer el mensaje: " + e.getMessage());
                    // iv) Abrir el subbloque finally, donde cerraremos el socket del cliente
                } finally {
                    socketCliente.close();
                }

            }

        //3º Cerramos el primer bloque try, y abrimos su catch correspondiente para capturar posibles errores que puedan haber al iniciar el sevidor
    } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
    }

    }
}
