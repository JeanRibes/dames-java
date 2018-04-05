import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Rest {
    private String server;
    private String token;
    private int id;
    private String username;
    public GsonBuilder builder;
    public Gson gson;

    public Rest(String url, String token) {
        this.server =url;
        this.token = token;
        this.builder = new GsonBuilder();
        this.gson = this.builder.create();
    }
    public void test(Plateau plateau){
        String serialized = gson.toJson(plateau);
        Plateau deserialized = gson.fromJson(serialized, Plateau.class);
        String finale = gson.toJson(serialized);
        System.out.println(finale);

        try {
            URL myURL = new URL("http://eu.httpbin.org/uuid");
            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (myURLConnection.getInputStream())));
            String received = "";
            String output;
            while ((output = br.readLine()) != null) {
                received += output;
                //System.out.println(output);
            }
            System.out.println(received);
            UUID u = gson.fromJson(received, UUID.class);
            System.out.println(u);
        }
        catch (MalformedURLException e) {
            System.out.println("URL incorrecte");
        }
        catch (IOException e) {
            System.out.println("Connection échouée");
        }
    }
}

class UUID {
    String uuid;

    public String toString() {
        return this.uuid;
    }
}