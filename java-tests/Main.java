import java.net.MalformedURLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        System.out.printf("Give a username: ");
        Scanner in = new Scanner(System.in);
        String username = in.nextLine();
        in.close();

        System.out.println(skinFix.skinGet(username, "skin"));
        System.out.println(skinFix.skinGet(username, "cape"));

    }
}
