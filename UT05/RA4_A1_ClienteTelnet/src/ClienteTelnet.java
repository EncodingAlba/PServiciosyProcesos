import org.apache.commons.net.examples.util.IOUtil;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;

public class ClienteTelnet {
    public static void main(String[] args) {

        //Comprobar q se han pasado los argumentos correctos
        if (args.length < 2) {
            System.out.println("ERROR: Indicar servidor y puerto.");
            //Terminamos el programa con código de error 1
            System.exit(1);
        }

        //Obtenemos el servidor y el puerto introducidos desde la línea de comandos
        String servidor = args[0];
        int puerto = Integer.parseInt(args[1]);

        //Crear un objeto TelnetClient de Apache Commons Net
        TelnetClient cliente = new TelnetClient();

        //Conectarse al servidor
        try {
            System.out.println("Conectando a " + servidor + " en el puerto " + puerto + "...");
            //El método connect establece la conexión TCP con el servidor
            cliente.connect(servidor, puerto);
            System.out.println("Conexión establecida. Escribe los comandos y presione Enter.");

            //Este método se ejecuta en bucle hasta que la conexión se cierra
            IOUtil.readWrite(cliente.getInputStream(), cliente.getOutputStream(), System.in, System.out);

            //Nos desconectamos
            cliente.disconnect();
            System.out.println("Conexión cerrada.");

        } catch (IOException e) {
            System.err.println("Error en la conexión TELNET: " + e.getMessage());
            //Terminamos el programa con código de error 2
            System.exit(2);
        }

    }
}