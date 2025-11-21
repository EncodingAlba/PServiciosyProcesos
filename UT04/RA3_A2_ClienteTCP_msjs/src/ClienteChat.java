import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

//BY: ALBA MUÑOZ MIÑANO

public class ClienteChat {
    public static void main(String[] args) {
    //Declaro la IP del host como String
    String ipHost = "";

    //Declaro el puerto en el que está escuchando el servidor
    final int PUERTO = 12345;
        try(//instancio un objeto de la clase socket que represente la conexion client-server
            Socket socket = new Socket(ipHost, PUERTO);
            //instancio un objeto de la clase BufferedReader para leer las respuestas q envía el server
            BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //instancio objeto clase PrintWriter para enviar msjs al server
            PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);
            //instancio obj clase BufferedReader para leer lo q escribe el usuario
            BufferedReader entradaConsola = new BufferedReader(new InputStreamReader(System.in));
        ) {
            //Confirmamos la conexión
            System.out.println("Conectado al servidor " + ipHost + " en el puerto: " + PUERTO);

            //Bucle infinito para q el cliente este activo y listo para enviar mensajes continuamente
            //De esta manera no necesita reconectarse cada vez

            while(true){
                //1.Solicitamos por consola al usuario que escriba un msj
                System.out.println("Usuario: ");

                //2.metodo readLine() lee la linea completa que el usuario escribe por consola
                String mensaje = entradaConsola.readLine();

                //3.Enviamos un mensaje al servidor utilizando println
                salidaServidor.println(mensaje);

                //4.Leemos la respuesta del servidor con readline() - es BLOQUEANTE
                String respuestaServidor = entradaServidor.readLine();

                //5. hacemos comprobación de si la respuesta del server es nula o no:
                if (respuestaServidor != null) {
                    System.out.println("Servidor: " + respuestaServidor);
                } else {
                    System.out.println("El servidor cerró la conexión");
                }
                //si no es nula, significa q el server nos ha contestado algo: imprimimos la respuesta
                //si es nula, significa q el server cerró la conexión

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}