import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Main {
    public static void main(String[] args) {

        try (DatagramSocket socketCliente = new DatagramSocket()) {

            InetAddress direccionServidor = InetAddress.getLocalHost();
            int puertoServidor = 12345;


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}