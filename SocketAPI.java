import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


public class SocketAPI {
    public GsonBuilder builder;
    public Gson gson;
    public String message;
    public WebSocketClient ws;

    /**
     * Cette classe fournit des méthodes pour recevoir & attendre des données depuis un WebSocket
     * la connection au serveur n'a pas besoin d'être ré-étableie à chaque fois, et le serveur peut envoyer les infos
     * @param server le FQDN du serveur, sans http:// ... c'est du websocket !
     * @param id l'id de la partie, je n'ai pas assez creusé Django-Channels pour trouver comment authentifier avec un Token
     */
    public SocketAPI(String server, String id) throws URISyntaxException {
        this.builder = new GsonBuilder();
        this.gson = this.builder.create();
        this.message = "";
        this.ws = new WebSocketClient(new URI("ws://"+server+"/ws/ping/"+id+"/")) {
            @Override
            public void onMessage(String message) {
                //System.out.println("reçcu msg");
                SocketAPI.this.message = message;
                //System.out.println("msg:"+SocketAPI.this.message);
            }

            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("opened connection");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("closed connection");
                System.out.println("Reason: "+reason+ " code");
                System.out.print(code);
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("ERREUR WEBSOCKET");
                ex.printStackTrace();
            }

        };
        ws.connect(); //ne pas l'oublier ...
    }

    /**
     * Bloque l'exécution jusqu'à la réception d'un message
     * @return le message au format texte
     */
    public String receive(){
        while (this.message.equals("")) {
            try {
                Thread.sleep(100);
                System.out.print(".");
            } catch (InterruptedException e) {
                System.out.println("Erreur JAVA");
                e.printStackTrace();
            }
        }
        String recu = this.message;
        this.message = "";
        //System.out.println(recu);
        return recu;
    }

    /**
     * Bloque tant que le tour du joueur n'est pas arrivé
     * @param joueBlanc est ce que le joueur a les pions blancs
     */
    public void attendreTour(boolean joueBlanc) {
        String tour = receive();
        //System.out.println(tour+" "+joueBlanc);
        System.out.println("Attendez l'autre joueur");
        while(tour.equals("blancs") != joueBlanc) //tour.equals("blancs") renvoie true ou false, et on arrête d'attendre si cette valeur correspond à la couleur
            tour = receive();                    // jouée par le joueur

    }
}
