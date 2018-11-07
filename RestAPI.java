import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RestAPI {
    GsonBuilder builder;
    Gson gson;
    String server;

    public RestAPI(String apiUrl) {
        server = apiUrl;
        builder = new GsonBuilder();
        gson = this.builder.create();
    }

    public SocketPartie[] getLobby() {
        try {
            URL myURL = new URL(server + "/dames/socketpartie/?format=json");
            URLConnection conn = myURL.openConnection();
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            StringBuilder received = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                received.append(output);
            }
            //System.out.println("GOT :"+received);
            return gson.fromJson(received.toString(), SocketPartie[].class);
        } catch (MalformedURLException e) {
            System.out.println("URL incorrecte");
        } catch (IOException e) {
            System.out.println("Connection échouée");
            e.printStackTrace();
        }
        return null;
    }
}

class SocketPartie {
    String nom;
    String blanc; //booléens en string, 0/1
    String noir;
}
