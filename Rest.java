import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
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
        this.server = url;
        this.token = token;
        this.builder = new GsonBuilder();
        this.gson = this.builder.create();
    }

    public void test(Plateau plateau) {
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

            String output;
            StringBuilder receivedBuilder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                receivedBuilder.append(output);
            }
            String received = receivedBuilder.toString();
            System.out.println(received);
            UUID u = gson.fromJson(received, UUID.class);
            System.out.println(u);
        } catch (MalformedURLException e) {
            System.out.println("URL incorrecte");
        } catch (IOException e) {
            System.out.println("Connection échouée");
        }
    }

    public Pion[] get() {
        try {
            URL myURL = new URL(server + "/dames/sync/?format=json");
            URLConnection conn = myURL.openConnection();
            conn.setRequestProperty("Authorization", "Token " + this.token);
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String received = "";
            String output;
            while ((output = br.readLine()) != null) {
                received += output;
            }
            System.out.println("GOT :"+received);
            return gson.fromJson(received, Pion[].class);
        } catch (MalformedURLException e) {
            System.out.println("URL incorrecte");
            return null;
        } catch (IOException e) {
            System.out.println("Connection échouée");
            return null;
        }
    }

    public void post(Pion[] pions) {
        try {
            URL url = new URL(server + "/dames/sync/?format=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Token " + this.token);
            conn.setRequestProperty("Content-Type", "application/json");

            String input = gson.toJson(pions);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                System.out.println("POST res:"+output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            System.out.println("URL incorrecte");
            e.printStackTrace();

        } catch (IOException e) {
            System.out.println("Connection échouée");
            e.printStackTrace();
        }

    }

    public void asyncPost(Pion[] pions) {
        new Thread(() -> {
            post(pions);
            System.out.println("POST fini");
            return; // to stop the thread
        }).start();
    }
}

class UUID {
    String uuid;

    public String toString() {
        return this.uuid;
    }
}