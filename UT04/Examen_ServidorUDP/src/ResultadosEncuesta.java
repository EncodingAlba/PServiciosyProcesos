import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Clase que almacena las respuestas de todas las zonas.
 * Utiliza estructuras concurrentes para permitir el acceso desde varios hilos a la vez.
 * (Cada hilo corresponde a una petición UDP en el servidor)
 */
public class ResultadosEncuesta {

    // Mapa que asocia cada zona con una lista de respuestas
    private final Map<String, List<String>> datos = new ConcurrentHashMap<>();

    /**
     * Añade una nueva respuesta a la zona indicada.
     * Los métodos son synchronized para evitar problemas de concurrencia.
     */
    public synchronized void agregarRespuesta(String zona, String respuesta) {
        String z = zona.trim().toLowerCase(); // Normalizamos nombres
        String r = respuesta.trim();
        datos.computeIfAbsent(z, k -> new ArrayList<>()).add(r);
    }

    /**
     * Devuelve un resumen de todas las respuestas registradas en una zona concreta.
     */
    public synchronized String getResumenZona(String zona) {
        String z = zona.trim().toLowerCase();
        List<String> respuestas = datos.get(z);
        if (respuestas == null || respuestas.isEmpty()) {
            return "Zona '" + z + "': sin respuestas registradas.";
        }
        StringBuilder sb = new StringBuilder("Resumen de zona '" + z + "':\n");
        for (int i = 0; i < respuestas.size(); i++) {
            sb.append("  ").append(i + 1).append(") ").append(respuestas.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Devuelve el resumen global con todas las zonas.
     */
    public synchronized String getResumenGlobal() {
        if (datos.isEmpty()) return "No hay resultados registrados.";
        StringBuilder sb = new StringBuilder("=== RESUMEN GLOBAL ===\n");
        for (String zona : datos.keySet().stream().sorted().collect(Collectors.toList())) {
            sb.append(getResumenZona(zona)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Exporta los resultados en formato XML.
     * (Es una mejora adicional para la tarea)
     */
    public synchronized String exportarXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<resultados>\n");
        for (Map.Entry<String, List<String>> e : datos.entrySet()) {
            sb.append("  <zona nombre=\"").append(esc(e.getKey())).append("\">\n");
            for (String r : e.getValue()) {
                sb.append("    <respuesta>").append(esc(r)).append("</respuesta>\n");
            }
            sb.append("  </zona>\n");
        }
        sb.append("</resultados>\n");
        return sb.toString();
    }

    // Método auxiliar para escapar caracteres especiales en XML
    private static String esc(String s) {
        return s.replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;")
                .replace("\"","&quot;")
                .replace("'","&apos;");
    }
}
