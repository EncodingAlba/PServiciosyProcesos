public class MiTarea implements Runnable {

    private String tarea; //indica qué tarea hacer

    public MiTarea(String tarea) {
        this.tarea = tarea;
    }

    @Override
    public void run() {
        System.out.println("Hilo ID: " + Thread.currentThread().getId() + " iniciando tarea: " + tarea);

        try {
            switch (tarea) {
                case "imprimir":
                    imprimirNumeros();
                    break;
                case "sumar":
                    sumarNumeros();
                    break;
                case "saludar":
                    saludar();
                    break;
                default:
                    System.out.println("Tarea no definida.");}

            } catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("Hilo ID: " + Thread.currentThread().getId() + " finalizó tarea: " + tarea);
        }

        private void imprimirNumeros() throws InterruptedException {
            for (int i = 1; i<=5; i++) {
                System.out.println("Número: " + i);
                Thread.sleep(200);
            }
        }
        private void sumarNumeros() throws InterruptedException {
            int suma = 0;
            for (int i = 1; i <= 5; i++) {
                suma += i;
                Thread.sleep(200);
            }
            System.out.println("Suma total: " + suma);
        }

        private void saludar() throws InterruptedException {
            String[] saludos = {"Hola", "¿Qué tal?", "¡Buenos días!"};
            for (String s : saludos) {
                System.out.println(s);
                Thread.sleep(200);
            }
        }
    }
