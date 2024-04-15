
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.io.InputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;




public class skinFix {


    public static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }
    
    public static String jsonGetRequest(String urlString) {
        String json = null;
        try {
          URL url = new URL(urlString);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setDoOutput(true);
          connection.setInstanceFollowRedirects(false);
          connection.setRequestMethod("GET");
          connection.setRequestProperty("Content-Type", "application/json");
          connection.setRequestProperty("charset", "utf-8");
          connection.connect();
          InputStream inStream = connection.getInputStream();
          json = streamToString(inStream); // input stream to string
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        return json;
    }

    public static JSONObject parseJSON(String jsonString) {
      JSONObject object = null;
      try {
        JSONParser parser = new JSONParser();
        object = (JSONObject) parser.parse(jsonString);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return object;
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.printf("Give a username: ");
        Scanner in = new Scanner(System.in);
        String user_name = in.nextLine();
        in.close();
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + user_name;

        System.out.println(urlString);

        String jsonString = jsonGetRequest(urlString);

        System.out.println("before parsing: "+ jsonString);
        JSONObject json = parseJSON(jsonString);
    
        String uuid = (String) json.get("id");
        System.out.println("UUID: " + uuid);

        urlString = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
        System.out.println(jsonGetRequest(urlString));

        jsonString = jsonGetRequest(urlString);

        json = parseJSON(jsonString);

        JSONArray propertiesArray = (JSONArray) json.get("properties");
        json = (JSONObject) propertiesArray.get(0);

        String base64 = (String) json.get("value");

        System.out.println(base64);
        
       

    }
}
