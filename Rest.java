import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

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
    public Partie[] lobby;
    public boolean joueBlanc;

    public Rest(String url) {
        this.server = url;
        //this.token = token;
        this.builder = new GsonBuilder();
        this.gson = this.builder.create();
    }

    public void setToken(String token) {
        this.token = token;
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
            StringBuilder received = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                received.append(output);
            }
            //System.out.println("GOT :"+received);
            return gson.fromJson(received.toString(), Pion[].class);
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
            /*while ((output = br.readLine()) != null) {
                System.out.println("POST res:"+output);
            }*/

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

    public void creerPartie(String nom) { //crée une partie en attente sur lee lobby
        Partie partie = new Partie(nom);
        this.joueBlanc = true;
        try {
            URL url = new URL(server + "/dames/partie/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = gson.toJson(partie);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            this.token = br.readLine();
            //System.out.println(token);

            conn.disconnect();

        } catch (MalformedURLException e) {
            System.out.println("URL incorrecte");
            e.printStackTrace();

        } catch (IOException e) {
            System.out.println("Connection échouée");
            e.printStackTrace();
        }
    }

    public void supprPartie() throws IOException {
        URL myURL = new URL(server + "/dames/delete/");
        HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Token " + this.token);
        conn.connect();
        conn.getInputStream();
    }

    public void getLobby() {
        //return new String[1];
        try {
            URL myURL = new URL(server + "/dames/lobby/?format=json");
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
            this.lobby = gson.fromJson(received.toString(), Partie[].class);
        } catch (MalformedURLException e) {
            System.out.println("URL incorrecte");
        } catch (IOException e) {
            System.out.println("Connection échouée");
        }
    }

    public void rejoindre(int id, String nom) {
        this.joueBlanc = false;
        try {
            URL myURL = new URL(server + "/dames/join/" + id);
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String input = "{\"nom\":\""+nom+"\"}";
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            this.token = br.readLine();
            System.out.println(token);

        } catch (MalformedURLException e) {
            System.out.println("URL incorrecte");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Connection échouée");
            e.printStackTrace();
        }
    }

    public boolean aQuiLeTour() throws IOException {
        URL myURL = new URL(server + "/dames/a-qui-le-tour/");
        HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
        conn.setRequestMethod("GET");
        //System.out.println(this.token);
        conn.setRequestProperty("Authorization", "Token " + this.token);
        conn.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        boolean tour = gson.fromJson(br.readLine(), boolean.class); //vrai si c'est aux blancs
        //System.out.println(tour);
        if(this.joueBlanc == tour)
            return true;
        else return false;
    }

    public boolean aToiLeTour() throws IOException {
        URL myURL = new URL(server + "/dames/a-toi-le-tour/");
        HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
        conn.setRequestProperty("Authorization", "Token " + this.token);
        conn.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        br.readLine(); //si je ne demande pas le texte JAVA ne fait pas la requête ... :(
        System.out.println("à l'autre !");
        return false;
    }

}

class UUID {
    String uuid;

    public String toString() {
        return this.uuid;
    }
}

class Partie {
    String player1; //joue les pions blancs
    String player2; //joue les pions noirs
    int id;

    Partie(String player1) {
        this.player1 = player1;
    }

    void setPlayer2(String player2) {
        this.player2 = player2;
    }
}