
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.io.InputStream;
import javax.xml.bind.DatatypeConverter;
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

    public static String skinGet(String username, String urlType) throws MalformedURLException {
  
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + username;
        // System.out.println(urlString);
        String jsonString = jsonGetRequest(urlString);
        JSONObject json = parseJSON(jsonString);
  
        String uuid = (String) json.get("id");
        urlString = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
        // System.out.println(jsonGetRequest(urlString));
        jsonString = jsonGetRequest(urlString);
        json = parseJSON(jsonString);
        JSONArray propertiesArray = (JSONArray) json.get("properties");
        json = (JSONObject) propertiesArray.get(0);
        String base64 = (String) json.get("value");
        // System.out.println(base64);
        byte[] decoded = DatatypeConverter.parseBase64Binary(base64);
        String skinLinkString = new String(decoded, StandardCharsets.UTF_8);
        // System.out.println(skinLinkString);
        json = parseJSON(skinLinkString);
        JSONObject textures = (JSONObject) json.get("textures");
        String skinUrl;
        try {
          JSONObject skin = (JSONObject) textures.get("SKIN");
          skinUrl = (String) skin.get("url");
        } catch (Exception e) {
          skinUrl = null;
        }
        String capeUrl;
        try {
          JSONObject cape = (JSONObject) textures.get("CAPE");
          capeUrl = (String) cape.get("url");
        } catch (Exception e) {
          capeUrl = null;
        }

        if (urlType.equals("skin"))
          return skinUrl;
        if (urlType.equals("cape"))
          return capeUrl;
        else
          return null;
    }
}
