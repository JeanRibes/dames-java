import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Lobby extends JFrame {
    JButton connectServer;
    JTextField serverName;
    JCheckBox ssl;
    JPanel serveurSelect;
    JPanel listJeux;
    JPanel boutons;
    JButton fetchLobby;
    String wsUrl;
    String apiUrl;
    RestAPI lobbyAPI;
    SocketPartie[] lobby;
    public Lobby() {
        super();
        setTitle("Jeu de dames - Lobby");
        setLocationRelativeTo(null);
        setSize(500,400);
        getContentPane().setBackground(Color.ORANGE);
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        serverName = new JTextField("5093937a.ngrok.io");
        ssl = new JCheckBox();
        connectServer = new JButton("Connecter");
        serveurSelect = new JPanel();
        serveurSelect.setLayout(new FlowLayout());
        serveurSelect.add(new JLabel("Serveur de jeu"));
        serveurSelect.add(serverName);
        serveurSelect.add(ssl);
        serveurSelect.add(connectServer);

        listJeux = new JPanel();
        boutons = new JPanel();
        boutons.setLayout(new FlowLayout());
        fetchLobby = new JButton("Lobby");
        boutons.add(fetchLobby);
        listJeux.add(boutons, BorderLayout.NORTH);


        add(serveurSelect, BorderLayout.NORTH);
        add(listJeux, BorderLayout.CENTER);
        setVisible(true);

        connectServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] serverUrls = buildServerURL(serverName.getText(), ssl.isSelected());
                wsUrl = serverUrls[0];
                apiUrl = serverUrls[1];
                System.out.println(wsUrl+"\n"+apiUrl);
                lobbyAPI = new RestAPI(apiUrl);
                new Affichage(10, wsUrl+"/tchat/room", "1", true);
            }
        });
        fetchLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lobby = lobbyAPI.getLobby();
                for (SocketPartie partie: lobby) {
                    System.out.println(partie.nom);
                }
            }
        });
    }
    public static void main(String[] args){
        setupUiTheme();
        new Lobby();
    }

    public String[] buildServerURL(String serverIp, boolean ssl){
        String websocket;
        String api_url;
        if(ssl){
            websocket = "wss://";
            api_url = "https://";
        } else {
            websocket = "ws://";
            api_url = "http://";
        }
        String[] url = {websocket+serverIp, api_url+serverIp};
        return url;
    }

    public static void setupUiTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) { //merci StackOverflow
                System.out.println(info.getClassName());
                if ("javax.swing.plaf.nimbus.NimbusLookAndFeel".equals(info.getClassName())) { //le plus beau
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) { //y'a des problèmes avec les bordures c'est un peu moche
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e) {}
    }
}
