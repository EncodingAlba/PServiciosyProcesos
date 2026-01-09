import org.apache.commons.net.ftp.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class ClienteFTP {

    public static void main(String[] args) {
      //incluir datos servidor de prueba: test.rebex.net
        String servidor = "demo.wftpserver.com";
        String usuario = "demo";
        String password = "demo";

        FTPClient ftpClient = new FTPClient();

        //bloque try catch
        try {
            //Conectamos con el servidor
            ftpClient.connect(servidor);
            ftpClient.enterLocalPassiveMode();
            if (ftpClient.isConnected()) {
                System.out.println("Conectado con el servidor! " + servidor);
            }

            if (ftpClient.login(usuario, password)) {
                System.out.println("Inicio de sesión exitoso");
            } else {
                System.out.println("Datos de acceso incorrectos");
            }


            //Listamos archivos disponibles
            String[] archivos = ftpClient.listNames();
            if (archivos == null || archivos.length == 0) {
                System.out.println("No hay archivos que mostrar");
            } else {
                System.out.println("Listado de archivos: ");
                for (String archivo : archivos) {
                    System.out.println("- " + archivo);
                }
            }

            //Descargamos el archivo
            String archivoRemoto = "download/version.txt";
            try (FileOutputStream archivoLocal = new FileOutputStream("download.txt")) {
                if (ftpClient.retrieveFile(archivoRemoto, archivoLocal)) {
                    System.out.println("Archivo descargado.");
                } else {
                    System.out.println("No se encuentra el archivo. Información adicional: " + ftpClient.getReplyCode());
                }
            } catch (IOException e) {
                System.out.println("Error al crear el archivo.");
            }

        } catch (UnknownHostException e) {
            System.out.println("No se encuentra el servidor FTP.");
        } catch (ConnectException e) {
            System.out.println("No se puede conectar con el servidor.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally { //cerramos aquí la conexión para asegurarnos que sucede aunque haya un error de red
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                    System.out.println("Conexión cerrada");
                } catch (IOException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }





    }
}