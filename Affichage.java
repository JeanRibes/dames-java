
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;

public class Affichage extends JFrame implements KeyListener, Case.CaseEvent {
    Pion[] mesPions;
    JPanel plateauPanel;
    Plateau plateau;
    Case selectionActive;
    JTextPane messagesErreur;
    static int counter=3;

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
        plateau.update(mesPions);
        for(int y=0;y<plateau.cases.length; y++) {
            for (int x = 0; x < plateau.cases.length; x++) {
                plateau.cases[y][x].addCaseListener(this);
                plateauPanel.add(plateau.cases[y][x]);
            }
        }

        /*pions = new JButton[taille][taille];
        cases = new JPanel[taille][taille];
        boolean doitEtreBlanc;
        for (int y = 0; y < this.pions.length; y++) {
            doitEtreBlanc = (y % 2 == 0);
            for (int x = 0; x < this.pions[y].length; x++) {
                pions[y][x] = new JButton();
                pions[y][x].setSize(100,100);
                cases[y][x] = new JPanel();
                //cases[y][x].setLayout();
                if (doitEtreBlanc) {
                    cases[y][x].setBackground(Color.lightGray);
                    pions[y][x].setBackground(Color.RED);
                    doitEtreBlanc = false;
                    pions[y][x].setText(x + "," + y);
                } else {
                    cases[y][x].setBackground(Color.BLACK);
                    //pions[y][x].setForeground(Color.WHITE);
                    doitEtreBlanc = true;
                }
                //pions[y][x].setHorizontalTextPosition(SwingConstants.CENTER);
                //pions[y][x].setVerticalTextPosition(SwingConstants.CENTER);
                cases[y][x].add(pions[y][x]);
                plateauPanel.add(cases[y][x]);
            }
        }*/
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
                messagesErreur.setText("Choisissez un pion à manger ou une case où aller");
            }
        });
        JPanel infos = new JPanel();
        messagesErreur = new JTextPane();
        messagesErreur.setEditable(false);
        infos.add(messagesErreur);
        buttons.setLayout(new FlowLayout());
        buttons.add(bouger, BorderLayout.WEST);
        buttons.add(cancel, BorderLayout.CENTER);
        buttons.setBackground(new Color(28, 140, 107));
        plateauPanel.setBackground(new Color(255, 166, 18));
        infos.setBackground(new Color(123, 233, 255));
        add(buttons, BorderLayout.SOUTH);
        add(plateauPanel, BorderLayout.CENTER);
        add(infos, BorderLayout.NORTH);

        addKeyListener(this);
        setVisible(true);

        messagesErreur.setText("Cliquez sur un pion et appuyez sur Action pour continuer");
    }

    /*public void setPion(Pion pion){
        if(pion.blanc){pions[pion.getY()][pion.getX()].setForeground(Color.WHITE);}
        else {pions[pion.getY()][pion.getX()].setForeground(Color.BLACK);}
        pions[pion.getY()][pion.getX()].setText(pion.toString());
    }
*/
    @Override
    public void keyPressed(KeyEvent e) {
                
    }public void keyTyped(KeyEvent e) {}public void keyReleased(KeyEvent e) {}


    @Override
    public void onClick(int[] pos) {
        plateau.update(mesPions);
        if(selectionActive()) {
            if(!selectionActive.hasPion()) {
                desactiverSelection();
                messagesErreur.setText("Vous ne pouvez pas activer une case sans pion");
                plateau.resetSelectionCases();
                plateau.update(mesPions);return;
            }
            Pion actif = selectionActive.pion;
            if (plateau.estVide(pos)) {
                if (selectionActive.pion.bouge(pos)) {
                    plateau.resetSelectionCases();
                    desactiverSelection();
                    plateau.update(mesPions);
                }else messagesErreur.setText("Vous ne pouvez pas jouer ici, séléctionnez un autre pion ou case");
            } else {
                Pion cePion = plateau.getPionDepuisCase(pos);
                if (actif.mange(cePion, plateau)) {
                    plateau.resetSelectionCases();
                    if(plateau.peutIlManger(actif)){
                        messagesErreur.setText("REJOUEZ !");
                        desactiverSelection();
                    }
                } else messagesErreur.setText("Vous ne pouvez pas manger ce pion");
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
        System.out.println("selection desactivée");
        try{
            selectionActive.setActive(false);
            selectionActive = null;
        } catch (NullPointerException ex){}
    }

    public static void main(String[] args) throws IOException {
        new Affichage(10);
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
}
