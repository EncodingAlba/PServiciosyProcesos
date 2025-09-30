import java.util.Random;


public class Suministrador {
public static void main (String[] args) throws InterruptedException {
    Buffer buffer = new Buffer(5);
    Random random = new Random();

    while (buffer.getCount() < buffer.getSize()) {
        buffer.add(random.nextInt());
    }

}





}
