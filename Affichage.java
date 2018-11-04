
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

    public Affichage(int taille) {
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Partie de dames");
        setSize(600, 600);
        plateauPanel = new JPanel();
        plateauPanel.setSize(500, 500);
        //plateauPanel.setBackground(Color.BLUE);
        plateauPanel.setLayout(new GridLayout(taille, taille));
        setLocationRelativeTo(null);

        plateau = new Plateau(taille);
        mesPions  = LesDames.RemplirPlateau(plateau, taille);
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
        JButton bouger = new JButton("Bouger");
        bouger.addActionListener(e -> {
            if(selectionActive()) {
                desactiverSelection();
            }
            else {
                selectionActive = plateau.getCaseSelectionnee();
                selectionActive.setActive(true);

            }
        });
        buttons.setLayout(new FlowLayout());
        buttons.add(bouger, BorderLayout.WEST);
        buttons.add(cancel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        add(plateauPanel, BorderLayout.CENTER);

        addKeyListener(this);
        setVisible(true);
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
        //Pion cePion = plateau.getPionDepuisCase(pos);
        if(selectionActive()){
            System.out.println(selectionActive.pion);
            if(selectionActive.pion.bouge(pos))
                plateau.resetSelectionCases(); desactiverSelection();
            plateau.update(mesPions);
        }
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
        new Thread(()->{
            new Affichage(10);}).run();
        try {
            LesDames.main(args);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
