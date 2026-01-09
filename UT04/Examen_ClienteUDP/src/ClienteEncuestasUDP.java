import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Cliente UDP que permite participar en la encuesta de forma interactiva.
 * Muestra un menú y envía los comandos al servidor según la opción elegida.
 */
//BY:ALBA MUÑOZ MIÑANO
public class ClienteEncuestasUDP {

    private static final String HOST = "127.0.0.1";  // Servidor local
    private static final int PORT = 12345;           // Puerto fijo
    private static final int BUFFER = 8192;
    private static final int TIMEOUT_MS = 3000;

    // Zonas disponibles en la encuesta
    private static final List<String> ZONAS = List.of("zona1", "zona2", "zona3", "zona4", "zona5");

    public static void main(String[] args) {
        System.out.println("=== Cliente de encuestas UDP ===");
        System.out.println("Conectando con " + HOST + ":" + PORT);

        try (DatagramSocket socket = new DatagramSocket(); Scanner sc = new Scanner(System.in)) {
            socket.setSoTimeout(TIMEOUT_MS);
            InetAddress server = InetAddress.getByName(HOST);

            boolean salir = false;
            while (!salir) {
                mostrarMenu();
                System.out.print("Opción: ");
                String op = sc.nextLine().trim();

                switch (op) {
                    case "1" -> { // Enviar respuesta
                        String zona = elegirZona(sc);
                        String resp = pedirNoVacio(sc, "Escribe tu respuesta");
                        String cmd = "@resp#" + zona + "#" + resp + "@";
                        enviarYMostrar(socket, server, PORT, cmd);
                    }
                    case "2" -> { // Cerrar zona y ver resumen
                        String zona = elegirZona(sc);
                        String cmd = "@fin#" + zona + "@";
                        enviarYMostrar(socket, server, PORT, cmd);
                    }
                    case "3" -> enviarYMostrar(socket, server, PORT, "@resultados@");
                    case "4" -> enviarYMostrar(socket, server, PORT, "@ayuda@");
                    case "5" -> enviarYMostrar(socket, server, PORT, "@exportxml@");
                    case "0" -> {
                        salir = true;
                        System.out.println("Saliendo del cliente...");
                    }
                    default -> System.out.println("Opción no válida.");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Muestra el menú principal
    private static void mostrarMenu() {
        System.out.println("""
                -----------------------------
                1) Enviar respuesta (@resp)
                2) Cerrar zona y ver resumen (@fin)
                3) Ver resultados globales (@resultados)
                4) Ayuda (@ayuda)
                5) Exportar XML (@exportxml)
                0) Salir
                -----------------------------""");
    }

    // Permite seleccionar la zona por número
    private static String elegirZona(Scanner sc) {
        System.out.println("Zonas disponibles:");
        for (int i = 0; i < ZONAS.size(); i++) {
            System.out.printf("  %d) %s%n", i + 1, ZONAS.get(i));
        }
        while (true) {
            System.out.print("Elige zona (número): ");
            String s = sc.nextLine().trim();
            try {
                int idx = Integer.parseInt(s);
                if (idx >= 1 && idx <= ZONAS.size()) return ZONAS.get(idx - 1);
            } catch (NumberFormatException ignored) {}
            System.out.println("Selección inválida.");
        }
    }

    // Pide una cadena no vacía
    private static String pedirNoVacio(Scanner sc, String etiqueta) {
        while (true) {
            System.out.print(etiqueta + ": ");
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("El campo no puede estar vacío.");
        }
    }

    /**
     * Envía el mensaje al servidor y muestra la respuesta recibida.
     */
    private static void enviarYMostrar(DatagramSocket socket, InetAddress server, int port, String mensaje) throws IOException {
        byte[] out = mensaje.getBytes(StandardCharsets.UTF_8);
        DatagramPacket p = new DatagramPacket(out, out.length, server, port);
        System.out.println(">> " + mensaje);
        socket.send(p);

        byte[] in = new byte[BUFFER];
        DatagramPacket r = new DatagramPacket(in, in.length);

        try {
            socket.receive(r);
            String respuesta = new String(r.getData(), 0, r.getLength(), StandardCharsets.UTF_8);
            System.out.println("<< " + respuesta);
        } catch (SocketTimeoutException e) {
            System.out.println("<< (sin respuesta del servidor en " + TIMEOUT_MS + " ms)");
        }
    }
}
