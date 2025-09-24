//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            //Ejecutar el bloc de notas en Windows
            Process proceso = Runtime.getRuntime().exec("notepad.exe");

            //Esperar que el usuario cierre el bloc de notas
            proceso.waitFor();
            System.out.println("El bloc de notas se cerró. Código de salida: " + proceso.exitValue());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}