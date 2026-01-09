import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servidor UDP multihilo que gestiona una encuesta por zonas.
 * Recibe los mensajes del cliente y responde con confirmaciones o resúmenes.
 */
public class ServidorEncuestasUDP {//BY:ALBA MUÑOZ MIÑANO

    public static final int PUERTO = 12345;    // Puerto fijo
    private static final int BUFFER = 4096;    // Tamaño del buffer

    // Expresiones regulares para validar los comandos que llegan del cliente
    private static final Pattern P_RESP   = Pattern.compile("^@resp#([^@#]+)#([^@#]+)@$", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_FIN    = Pattern.compile("^@fin#([^@#]+)@$", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_RES    = Pattern.compile("^@resultados@$", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_AYUDA  = Pattern.compile("^@ayuda@$", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_EXPORT = Pattern.compile("^@exportxml@$", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        // Instancia compartida con todos los hilos (mantiene los datos de la encuesta)
        ResultadosEncuesta resultados = new ResultadosEncuesta();

        System.out.println("Servidor UDP multihilo escuchando en puerto " + PUERTO + " ...");

        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            while (true) {
                // Esperamos datagramas del cliente
                byte[] buffer = new byte[BUFFER];
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                socket.receive(paquete);

                // Cada petición se atiende en un hilo independiente
                new Thread(() -> atender(paquete, socket, resultados)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que se ejecuta en cada hilo para procesar un datagrama recibido.
     */
    private static void atender(DatagramPacket paquete, DatagramSocket socket, ResultadosEncuesta resultados) {
        String recibido = new String(paquete.getData(), 0, paquete.getLength(), StandardCharsets.UTF_8).trim();
        String host = paquete.getAddress().getHostAddress() + ":" + paquete.getPort();
        System.out.println("[RX " + host + "] " + recibido);

        String respuesta;
        try {
            respuesta = procesar(recibido, resultados);
        } catch (Exception ex) {
            respuesta = "ERROR: " + ex.getMessage();
        }

        // Enviamos la respuesta al cliente
        byte[] bytes = respuesta.getBytes(StandardCharsets.UTF_8);
        DatagramPacket out = new DatagramPacket(bytes, bytes.length, paquete.getAddress(), paquete.getPort());
        try {
            socket.send(out);
            System.out.println("[TX " + host + "] " + resumenLinea(respuesta));
        } catch (IOException e) {
            System.err.println("Error enviando respuesta a " + host + ": " + e.getMessage());
        }
    }

    /**
     * Procesa los comandos enviados por el cliente.
     */
    private static String procesar(String msg, ResultadosEncuesta resultados) {
        Matcher m;

        m = P_RESP.matcher(msg);
        if (m.matches()) {
            // Añadir respuesta
            String zona = m.group(1).trim().toLowerCase();
            String resp = m.group(2).trim();
            resultados.agregarRespuesta(zona, resp);
            return "OK: respuesta almacenada en zona '" + zona + "': \"" + resp + "\"";
        }

        m = P_FIN.matcher(msg);
        if (m.matches()) {
            // Cerrar zona y mostrar resumen
            String zona = m.group(1).trim().toLowerCase();
            return resultados.getResumenZona(zona);
        }

        if (P_RES.matcher(msg).matches()) {
            return resultados.getResumenGlobal();
        }

        if (P_AYUDA.matcher(msg).matches()) {
            return """
                    === COMANDOS DISPONIBLES ===
                    @resp#zona#respuesta@   -> Almacena una respuesta
                    @fin#zona@              -> Devuelve resumen de la zona
                    @resultados@            -> Muestra resumen global
                    @ayuda@                 -> Muestra esta ayuda
                    @exportxml@             -> Exporta resultados en XML
                    """;
        }

        if (P_EXPORT.matcher(msg).matches()) {
            return resultados.exportarXML();
        }

        return "Comando no reconocido. Usa @ayuda@ para ver los comandos válidos.";
    }

    // Método auxiliar para no mostrar respuestas demasiado largas por consola
    private static String resumenLinea(String s) {
        String one = s.replaceAll("\\s+", " ");
        return one.length() > 120 ? one.substring(0, 117) + "..." : one;
    }
}
