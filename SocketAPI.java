import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Cette classe fournit des méthodes pour recevoir & attendre des données depuis un WebSocket
 * la connection au serveur n'a pas besoin d'être ré-étableie à chaque fois, et le serveur peut push les infos
 */
public class SocketAPI {
    public GsonBuilder builder;
    public Gson gson;
    public String data;
    public WebSocketClient sync;
    public boolean joueBlanc;
    public String couleur;

    /**
     *
     * @param server URI Websocket de la forme ws://example.com ou wss://secure.com
     * @param id l'id de la partie reçu de RestAPI
     * @param joueBlanc booléean valant true si le mec joue les blancs
     */
    public SocketAPI(String server, String id, boolean joueBlanc) throws URISyntaxException {
        this.builder = new GsonBuilder();
        this.gson = this.builder.create();
        this.data = "";
        this.joueBlanc = joueBlanc;

        if (this.joueBlanc)
            couleur = "blancs";
        else
            couleur = "noirs";
        //System.out.println(couleur);

        this.sync = new WebSocketClient(new URI(server + "/ws/sync/" + id + "/" + couleur)) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("opened connection");
            }

            @Override
            public void onMessage(String message) {
                //System.out.println(message);
                SocketAPI.this.data = message;
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("closed connection");
                System.out.println("Reason: " + reason + " code");
                System.out.print(code);
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("ERREUR WEBSOCKET");
                ex.printStackTrace();
            }
        };
        sync.connect(); //ne pas l'oublier ...
    }
    /**
     * Attend jusqu'à la fin du tour de l'autre joueur
     * reçoit les pions du serveur via websockets
     * @return le tableau 1D des pions
     */
    public Pion[] waitGet() {
        while (this.data.equals("")) {
            try {
                Thread.sleep(50);
                System.out.print(".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Pion[] pions = gson.fromJson(this.data, Pion[].class);
        this.data = "";
        return pions;
    }

    /**
     * Envoie les pions au serveur via websockets
     * @param pions le tableau des pions
     */
    public void post(Pion[] pions) {
        this.sync.send(gson.toJson(pions));
        //System.out.println("envoyé");
        //System.out.println(gson.toJson(pions));
    }
}
