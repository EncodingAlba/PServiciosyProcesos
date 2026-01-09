package org.example;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;

public class App
{
    public static void main( String[] args ) {
        if (args.length < 1) {
            System.out.println("ERROR: Indicar el comando a ejecutar.");
            //Terminamos el programa con código de error 1
            System.exit(1);
        }

        String comando = args[0];

        //Configuración para conectarte al servidor
        String host = "sshtest.net"; //server address
        String user = "test";           //username
        String password = "test";

        try {

            //PASO 1: Inicializar JSCH y establecer sesión SSH
            JSch jsch = new JSch();

            //Según esas librerías (dependencias de JSCH) hemos de crear una sesión SSH con usuario, host y puerto.
            Session session = jsch.getSession(user, host, 22);

            //Establecemos la contraseña para autenticación
            session.setPassword(password);

            //Desactivar la verificación estricta de la clave del host
            //NOTA: En producción esto debería estar activado por seguridad
            session.setConfig("StrictHostKeyChecking", "no");

            //Conexión
            session.connect();
            System.out.println("Conectado al host: " + host);

            //Abrir canal: Un canal SSH permite realizar operaciones dentro de la sesión
            //exec es el tipo de canal para ejecutar comandos
            ChannelExec channel = (ChannelExec) session.openChannel("exec");

            //Definir el comando que se ejecutará en el servidor remoto
            channel.setCommand(comando);

            //No enviaremos datos de entrada al comando
            channel.setInputStream(null);

            //Los errores del servidor se mostrarán en nuestra consola de errores
            channel.setErrStream(System.err);

            //Leer la salida del comando
            InputStream input = channel.getInputStream();

            //Conectar el canal
            channel.connect();

            //Leemos la salida byte por byte y la mostramos por consola
            int data;
            while ((data = input.read()) != -1) {
                //Convertimos el byte a caracter e imprimimos
                System.out.print((char) data);
            }

            //Cerrar la conexión
            channel.disconnect();
            session.disconnect();
            System.out.println("Conexión cerrada.");

        } catch (Exception e) {
            System.err.println("Error en la conexión SSH: " + e.getMessage());
            //Terminamos el programa con código de error 2
            System.exit(2);
        }


    }
}
