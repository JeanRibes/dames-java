import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.TreeMap;

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
    public boolean spinner;

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
                //System.out.println("opened connection");
            }

            @Override
            public void onMessage(String message) {
                //System.out.println(message);
                SocketAPI.this.data = message;
                try {
                    Pion[] pions = gson.fromJson(SocketAPI.this.data, Pion[].class);
                } catch (JsonParseException e) {
                    System.out.println(SocketAPI.this.data);
                }
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
        /*spinner = true;
        while (this.data.equals("")) {
            try {
                Thread.sleep(10);
                if(spinner)
                    joliTruc();
                spinner = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //if(!this.data.equals(""))
        }*/
        Thread spinner = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    spinner(true);
                } catch (InterruptedException e) {}
            }
        });
        spinner.start();
        while (this.data.equals("")) {
            //Thread.yield();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}
        }
        spinner.interrupt();
        //System.out.println("now stop");
        //spinner.stop();

        Pion[] pions = gson.fromJson(this.data, Pion[].class);
        this.data = "";
        System.out.println("");
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

    public void joliTruc() {
        new Thread(()->{
            try {
                //System.out.println(Thread.currentThread().getName());
                System.out.print("\r[/]  [<->      ]");
                Thread.sleep(100);
                System.out.print("\r[|]  [ <->     ]");
                Thread.sleep(100);
                System.out.print("\r[\\]  [  <->    ]");
                Thread.sleep(100);
                System.out.print("\r[-]  [   <->   ]");
                Thread.sleep(100);
                System.out.print("\r[/]  [    <->  ]");
                Thread.sleep(100);
                System.out.print("\r[|]  [     <-> ]");
                Thread.sleep(100);
                System.out.print("\r[\\]  [      <->]");
                Thread.sleep(100);
                System.out.print("\r[-]  [     <-> ]");
                Thread.sleep(100);
                System.out.print("\r[/]  [    <->  ]");
                Thread.sleep(100);
                System.out.print("\r[|]  [   <->   ]");
                Thread.sleep(100);
                System.out.print("\r[\\]  [  <->    ]");
                Thread.sleep(100);
                System.out.print("\r[-]  [ <->     ]");
                Thread.sleep(100);
                spinner = true;
            } catch (InterruptedException e) {}
        }).start();

    }
    public void spinner(boolean doSpin) throws InterruptedException {
        while(doSpin) {
            //System.out.println(Thread.currentThread().getName());
            System.out.print("\r[/]  [<->      ]");
            Thread.sleep(100);
            System.out.print("\r[|]  [ <->     ]");
            Thread.sleep(100);
            System.out.print("\r[\\]  [  <->    ]");
            Thread.sleep(100);
            System.out.print("\r[-]  [   <->   ]");
            Thread.sleep(100);
            System.out.print("\r[/]  [    <->  ]");
            Thread.sleep(100);
            System.out.print("\r[|]  [     <-> ]");
            Thread.sleep(100);
            System.out.print("\r[\\]  [      <->]");
            Thread.sleep(100);
            System.out.print("\r[-]  [     <-> ]");
            Thread.sleep(100);
            System.out.print("\r[/]  [    <->  ]");
            Thread.sleep(100);
            System.out.print("\r[|]  [   <->   ]");
            Thread.sleep(100);
            System.out.print("\r[\\]  [  <->    ]");
            Thread.sleep(100);
            System.out.print("\r[-]  [ <->     ]");
            Thread.sleep(100);
        }
    }
}

class JoliTruc extends Thread {
    public boolean doSpin;
    public boolean doitStop;

    public JoliTruc(){
        doSpin = true;
    }

    public void arreter(){
        System.out.println("stop"+Thread.currentThread().getName());
        doSpin = false;
        System.out.println("arret");
        //Thread.currentThread().stop();

    }
    @Override
    public void run() {
        System.out.println("run"+Thread.currentThread().getName());
        try {
            while(doSpin) {
                //System.out.println(Thread.currentThread().getName());
                System.out.print("\r[/]  [<->      ]");
                Thread.sleep(100);
                System.out.print("\r[|]  [ <->     ]");
                Thread.sleep(100);
                System.out.print("\r[\\]  [  <->    ]");
                Thread.sleep(100);
                System.out.print("\r[-]  [   <->   ]");
                Thread.sleep(100);
                System.out.print("\r[/]  [    <->  ]");
                Thread.sleep(100);
                System.out.print("\r[|]  [     <-> ]");
                Thread.sleep(100);
                System.out.print("\r[\\]  [      <->]");
                Thread.sleep(100);
                System.out.print("\r[-]  [     <-> ]");
                Thread.sleep(100);
                System.out.print("\r[/]  [    <->  ]");
                Thread.sleep(100);
                System.out.print("\r[|]  [   <->   ]");
                Thread.sleep(100);
                System.out.print("\r[\\]  [  <->    ]");
                Thread.sleep(100);
                System.out.print("\r[-]  [ <->     ]");
                Thread.sleep(100);
            }
    } catch (InterruptedException e) {}
    }
        }