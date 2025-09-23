import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class Colaborador extends NumRandom {

    public static void main(String[] args) {
        //Creamos la instancia del objeto
        Colaborador colaborador = new Colaborador();

        //Generamos y guardamos los números
        colaborador.generaryGuardarNumeros();

    }

    public void generaryGuardarNumeros(){
        //Generamos el fichero de texto

        try(BufferedWriter escritor = new BufferedWriter(new FileWriter("numeros.txt"))){
            //Repetimos 10 veces cada iteración
            for (int i = 1; i <= 10; i++) {
                List<Integer> numeros = generarNumeros();

                //Escribimos los 35 numeros separados por espacios
                for (Integer numero : numeros) {
                    escritor.write(numero.toString());
                    escritor.write(" ");
                }

                escritor.newLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

} //BY ALBA MUÑOZ MIÑANO
