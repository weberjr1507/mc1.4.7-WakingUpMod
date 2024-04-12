import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        System.out.printf("Give a username: ");
        Scanner in = new Scanner(System.in);
        String user_name = in.nextLine();
        in.close();
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + user_name;
        URL url = new URL(urlString); 
        
        

        System.out.println(url.toString());

    }
}
