
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;

public class Affichage extends JFrame implements KeyListener, Case.CaseEvent, SocketAPI.PionReceiver {
    Pion[] mesPions;
    JPanel plateauPanel;
    JPanel infos;
    Plateau plateau;
    Case selectionActive;
    JTextPane messagesErreur;
    static int counter=3;
    static boolean joueBlanc;
    static Color infosColor = new Color(123, 233, 255);
    SocketAPI wsTransport;

    public Affichage(int taille) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Partie de dames");
        setSize(600, 665);
        plateauPanel = new JPanel();
        plateauPanel.setSize(500, 500);
        //plateauPanel.setBackground(Color.BLUE);
        plateauPanel.setLayout(new GridLayout(taille, taille));
        setLocationRelativeTo(null);

        plateau = new Plateau(taille);
        mesPions  = LesDames.RemplirPlateau(plateau, 20);
        Pion.setDisplayToGUI(true);
        plateau.update(mesPions);
        for(int y=0;y<plateau.cases.length; y++) {
            for (int x = 0; x < plateau.cases.length; x++) {
                plateau.cases[y][x].addCaseListener(this);
                plateauPanel.add(plateau.cases[y][x]);
            }
        }
        plateauPanel.setBorder(new EmptyBorder(10,10,10,10));
        JButton cancel = new JButton("Annuler");
        JPanel buttons = new JPanel();
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desactiverSelection();
                plateau.resetSelectionCases();
                deselectionnerTout(mesPions, plateau);
            }
        });
        JButton bouger = new JButton("Action");
        bouger.addActionListener(e -> {
            if(selectionActive()) {
                desactiverSelection();
            }
            else {
                selectionActive = plateau.getCaseSelectionnee();
                selectionActive.setActive(true);
                afficherMessage("Choisissez un pion à manger ou une case où aller");
                if(selectionActive.pion.blanc!=joueBlanc){
                    afficherMessage("Jouez vos pions !");
                    plateau.resetSelectionCases();
                    desactiverSelection();
                }
            }
        });
        infos = new JPanel();
        messagesErreur = new JTextPane();
        messagesErreur.setEditable(false);
        infos.add(messagesErreur);
        buttons.setLayout(new FlowLayout());
        buttons.add(bouger, BorderLayout.WEST);
        buttons.add(cancel, BorderLayout.CENTER);
        buttons.setBackground(new Color(28, 140, 107));
        plateauPanel.setBackground(new Color(255, 166, 18));
        infos.setBackground(infosColor);
        add(buttons, BorderLayout.SOUTH);
        add(plateauPanel, BorderLayout.CENTER);
        add(infos, BorderLayout.NORTH);

        addKeyListener(this);
        setVisible(true);
        mesPions[mesPions.length-1].setTypePion(Pion.TYPE_DAME);

        afficherMessage("Cliquez sur un pion et appuyez sur Action pour continuer");
        try {
            wsTransport = new SocketAPI("wss://api.ribes.me/tchat/room", "1", true);
            wsTransport.addPionListener(this);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
                
    }public void keyTyped(KeyEvent e) {}public void keyReleased(KeyEvent e) {}

    @Override
    public void preSelect() {
        plateau.resetSelectionCases();
    }

    @Override
    public void onClick(int[] pos) {
        //plateau.update(mesPions);
        if(selectionActive()) {
            if(!selectionActive.hasPion()) {
                desactiverSelection();
                afficherMessage("Vous ne pouvez pas activer une case sans pion");
                plateau.resetSelectionCases();
                plateau.update(mesPions);return;
            }
            Pion actif = selectionActive.pion;
            if(actif.blanc!=joueBlanc){
                afficherMessage("Jouez vos pions !");
                plateau.resetSelectionCases();
                desactiverSelection();
                return;
            }
            if (plateau.estVide(pos)) {
                if (selectionActive.pion.bouge(pos)) {
                    plateau.resetSelectionCases();
                    desactiverSelection();
                    plateau.update(mesPions);
                    passerTour();
                }else afficherMessage("Vous ne pouvez pas jouer ici, séléctionnez un autre pion ou case");
            } else {
                Pion cePion = plateau.getPionDepuisCase(pos);
                if (actif.mange(cePion, plateau)) {
                    plateau.resetSelectionCases();
                    plateau.update(mesPions);
                    if(plateau.peutIlManger(actif)){
                        afficherMessage("REJOUEZ !");
                        desactiverSelection();
                    } else {passerTour();}
                } else afficherMessage("Vous ne pouvez pas manger ce pion");
                plateau.resetSelectionCases();
                plateau.update(mesPions);

            }
        }
        testFin();
    }

    public static void deselectionnerTout(Pion[] pions, Plateau plateau){
        for(int i=0;i<pions.length;i++){
            pions[i].deselectionner();
        }
        plateau.update(pions);
    }
    public boolean selectionActive() {
        if(selectionActive!=null)
            return true;
        else return false;
    }

    public void desactiverSelection() {
        try{
            selectionActive.setActive(false);
            selectionActive = null;
        } catch (NullPointerException ex){}
    }

    public static void main(String[] args) throws IOException {
        /*new Thread(()->{
            new MultiChat();
        }).start();*/
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) { //merci StackOverflow
                System.out.println(info.getClassName());
                if ("javax.swing.plaf.nimbus.NimbusLookAndFeel".equals(info.getClassName())) { //le plus beau
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) { //y'a des problèmes avec les bordures c'est un peu moche
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e) {}
        new Affichage(10);
    }
    public void testFin(){
        if(LesDames.pionsVivants(mesPions) ==0) {
            JFrame a = new JFrame();
            a.setSize(200,150);
            a.setLocationRelativeTo(null);
            a.add(new JLabel("bravo, gagné"));
            a.setVisible(true);
        }
    }

    public static boolean joueBlanc() {
        return joueBlanc;
    }
    public void aVousDeJouer(){
        joueBlanc = !joueBlanc;
        if(joueBlanc)
            afficherMessage("Au tour des BLANCS");
        else
            afficherMessage("Au tour des NOIRS");
    }
    public void passerTour() {
        //aVousDeJouer();
        wsTransport.post(mesPions);
    }

    public void afficherMessage(String msg) {
        messagesErreur.setText(msg);
        infos.setBackground(Color.BLUE);
        new Thread(()->{
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            infos.setBackground(infosColor);
        }).start();
    }
    public void receive(Pion[] pions) {
        mesPions = null;
        mesPions = pions;
        plateau.update(mesPions);
        aVousDeJouer();
    }

}
