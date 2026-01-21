import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class ClientePOP3 {

    public static void main(String[] args) {

        // Datos del servidor, a modificar para las distintas pruebas
        String servidor = "pop.gmx.com";
        int puerto = 995;                         // POP3 SSL
        String usuario = "alba.prueba@gmx.com";
        String password = "-9arHf!G5g.Y*iS";

        int mensajeALeer = 1; // se puede cambiar por otro entero si se quieren leer mas correos

        POP3SClient pop3 = new POP3SClient(true); // true = SSL implícito
        pop3.setDefaultTimeout(15000);

        try {
            // 1) Conexión
            pop3.connect(servidor, puerto);
            if (pop3.isConnected()) {
                System.out.println("Conectado al servidor POP3S: " + servidor + ":" + puerto);
            }

            // 2) Login
            if (pop3.login(usuario, password)) {
                System.out.println("Inicio de sesión exitoso");
            } else {
                System.out.println("Datos de acceso incorrectos (o POP desactivado en la cuenta)");
                return;
            }

            // 3) Información del buzón
            POP3MessageInfo status = pop3.status();
            if (status == null) {
                System.out.println("No se pudo obtener STAT.");
                return;
            }
            System.out.println("Mensajes en buzón: " + status.number);
            System.out.println("Tamaño total (bytes): " + status.size);

            if (status.number == 0) {
                System.out.println("No hay mensajes que leer.");
                return;
            }

            // Ajuste para no salirse de rango
            if (mensajeALeer < 1 || mensajeALeer > status.number) {
                System.out.println("mensajeALeer fuera de rango. Debe estar entre 1 y " + status.number);
                return;
            }

            // 4) Recuperar mensaje (RETR)
            Reader reader = pop3.retrieveMessage(mensajeALeer);
            if (reader == null) {
                System.out.println("No se pudo recuperar el mensaje #" + mensajeALeer);
                return;
            }

            String raw = leerTodo(reader);

            // Parseo simple: cabeceras hasta línea en blanco, luego cuerpo
            String from = extraerCabecera(raw, "From:");
            String subject = extraerCabecera(raw, "Subject:");
            String body = extraerCuerpo(raw);

            System.out.println("\n--- MENSAJE #" + mensajeALeer + " ---");
            System.out.println("From: " + (from.isEmpty() ? "(no encontrado)" : from));
            System.out.println("Subject: " + (subject.isEmpty() ? "(no encontrado)" : subject));
            System.out.println("Snippet: " + recortar(body, 200));
            System.out.println("---------------------\n");

            // EXTRA “mini-helpdesk”
            // Clasifica el correo y crea un "ticket" por consola
            String categoria = clasificarCategoria(subject + " " + body);
            String prioridad = calcularPrioridad(subject + " " + body);

            System.out.println("EXTRA Helpdesk:");
            System.out.println("Ticket simulado -> Categoria: " + categoria + " | Prioridad: " + prioridad);
            System.out.println("Acción futura: convertir este correo en incidencia automáticamente.");

        } catch (UnknownHostException e) {
            System.out.println("No se encuentra el servidor POP3.");
        } catch (ConnectException e) {
            System.out.println("No se puede conectar con el servidor POP3 (red/firewall/puerto incorrecto).");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // 5) Desconexión correcta (QUIT)
            if (pop3.isConnected()) {
                try {
                    pop3.logout();   // envía QUIT
                    pop3.disconnect();
                    System.out.println("Conexión cerrada (QUIT)");
                } catch (IOException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    // METODOS AUXILIARES

    private static String leerTodo(Reader r) throws IOException {
        BufferedReader br = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private static String extraerCabecera(String raw, String cabecera) {
        // Busca línea que empiece por "From:" o "Subject:"
        String[] lineas = raw.split("\n");
        for (String l : lineas) {
            if (l.startsWith(cabecera)) {
                return l.substring(cabecera.length()).trim();
            }
            // Fin de cabeceras
            if (l.trim().isEmpty()) break;
        }
        return "";
    }

    private static String extraerCuerpo(String raw) {
        // Cuerpo = lo que va después de la primera línea en blanco
        int idx = raw.indexOf("\n\n");
        if (idx == -1) return "";
        return raw.substring(idx + 2).replaceAll("\\s+", " ").trim();
    }

    private static String recortar(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...";
    }

    // EXTRA helpdesk
    private static String clasificarCategoria(String texto) {
        String t = texto.toLowerCase();
        if (t.contains("factura") || t.contains("pago") || t.contains("cobro")) return "Facturación";
        if (t.contains("error") || t.contains("bug") || t.contains("fallo")) return "Soporte técnico";
        if (t.contains("alta") || t.contains("baja") || t.contains("cuenta")) return "Cuentas/Accesos";
        return "General";
    }

    private static String calcularPrioridad(String texto) {
        String t = texto.toLowerCase();
        if (t.contains("urgente") || t.contains("asap") || t.contains("inmediato")) return "ALTA";
        if (t.contains("no funciona") || t.contains("bloqueado")) return "MEDIA";
        return "BAJA";
    }
}
