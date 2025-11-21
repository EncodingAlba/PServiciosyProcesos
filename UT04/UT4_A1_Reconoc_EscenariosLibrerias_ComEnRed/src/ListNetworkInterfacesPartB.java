import java.net.*;
import java.util.Enumeration;
import java.util.List;

public class ListNetworkInterfacesPartB {
    //BY:ALBA MUÑOZ MIÑANO

    public static void main(String[] args)  {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null || !interfaces.hasMoreElements()) {
                System.out.println("No hay interfaces");
                return;
            }
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                System.out.println("Interfaz: " + ni.getName());
                System.out.println("Descripción: " + ni.getDisplayName());

                List<InterfaceAddress> direcciones = ni.getInterfaceAddresses();

                if (direcciones.isEmpty()) {
                    System.out.println("Sin IP asignada");
                } else {
                    for (InterfaceAddress ia : direcciones) {
                        InetAddress inetAddress = ia.getAddress();
                        short prefixLength = ia.getNetworkPrefixLength();

                        String tipoIP;
                        String mascara = "-";
                        String broadcastString = "-";

                        if (inetAddress instanceof Inet4Address) {
                            tipoIP = "IPv4";
                            mascara = prefixLengthToMaskIpv4(prefixLength);
                            InetAddress broadcast = ia.getBroadcast();

                            if (broadcast != null) {
                                broadcastString = broadcast.getHostAddress();
                            }
                        } else if (inetAddress instanceof Inet6Address) {
                            tipoIP = "IPv6";

                        } else {
                            tipoIP = "Otro";
                        }

                        System.out.println("Tipo IP: " + tipoIP
                                + " Dirección IP: " + inetAddress.getHostAddress()
                                + " Prefijo: " + prefixLength
                                + " Mascara: " + mascara
                                + " Broadcast: " + broadcastString);

                        System.out.println();
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("Error al intentar obtener las interfaces de red");
            e.printStackTrace();
        }

    }

    private static String prefixLengthToMaskIpv4(short prefixLength) {
        if (prefixLength < 0 || prefixLength > 32) {
            return "-";
        }
        int mask = 0xFFFFFFFF << (32 - prefixLength);

        int b1 = (mask >>> 24) & 0xFF;
        int b2 = (mask >>> 16) & 0xFF;
        int b3 = (mask >>> 8) & 0xFF;
        int b4 = mask & 0xFF;

        return b1 + "." + b2 + "." + b3 + "." + b4;
    }
}
