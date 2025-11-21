import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

//BY:ALBA MUÑOZ MIÑANO

public class ListNetworkInterfacesPartA {
    public static void main(String[] args) {

        try {
            Enumeration<NetworkInterface> listaDeRedes = NetworkInterface.getNetworkInterfaces(); //obtenemos la enumeración

            if (!listaDeRedes.hasMoreElements()) {
                System.out.println("No interfaces");
                return;
            }

            //iteramos sobre cada interfaz de red encontrada
            while (listaDeRedes.hasMoreElements()) {
                //dentro del while nextElement obtiene la siguiente interfaz de la enumeracion
                NetworkInterface networkInterface = listaDeRedes.nextElement();

                //getname() devuelve el nombre de la interfaz del sistema (eth0, alan0, lo), identificador corto usado por el SO
                String name = networkInterface.getName();

                //getdisplayname() devuelve nombre descriptivo más legible, en windows suele ser el nombre completo del adaptador
                String displayName = networkInterface.getDisplayName();

                //isUp() comprueba si la interfaz está actualmente activa/operativa. True si está funcionando false en caso contrario
                boolean activa = networkInterface.isUp();

                //Generamos mensaje
                System.out.println("Interfaz: " + name + ". Descripción: " + displayName + ". ¿Está activa? " + (activa ? "si" : "no"));
            }

        } catch (SocketException e) {
            System.out.println("Error al intentar conectar el servidor");
            e.printStackTrace();
        }
    }
}