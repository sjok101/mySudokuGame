import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class test {
    public static void main(String[] args) {

        char[] array = new char[3];

        char[] test = new char[2];

        array = shortenArray(array);

        System.out.println(array.length);
        String time = LocalTime.now().toString().replaceAll("[\\:\\.]", "");
        System.out.println(time);

    }

    public static char[] shortenArray(char[] a) {
        char[] test = new char[2];

        return test;
    }
}
