import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//@SuppressWarnings("ALL")
public class Case extends JPanel implements MouseListener {
    public boolean blanc;
    public Pion pion;
    private JLabel textPion;
    public int x=-1;
    public int y=-1;
    CaseEvent cListener;
    private boolean selectionnee;
    private boolean active; //séléctionnée comme source

    public Case() {
        super();
        this.blanc = false;
        resetColor();
        textPion = new JLabel("u");
        textPion.setFont(textPion.getFont().deriveFont((float) 30.1));
        add(textPion);
        addMouseListener(this);
    }
    public Case(int X,int Y){
        this();
        x = X;
        y = Y;
        textPion.setText(x + "," + y);
    }
    public void addCaseListener(Object that){
        cListener = (CaseEvent) that;
    }

    public void setBlanc() {
        this.blanc = true;
        if(pion!=null)
            textPion.setText(""+pion);
        else
            textPion.setText("");
        resetColor();
        setBackground(Color.lightGray);
    }

    public boolean isBlanc() {
        return blanc;
    }

    public boolean isNoir() { return !blanc;}

    public void setPion(Pion pion) {
        this.pion = pion;
        setCouleurPion(pion.blanc);
        textPion.setText(""+pion);
        resetColor();
    }

    public boolean hasPion() {
        try {
            //noinspection ResultOfMethodCallIgnored,ResultOfMethodCallIgnored
            pion.getTypePion();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String toString() {
        try {
            //noinspection ResultOfMethodCallIgnored
            this.pion.getX();
            return this.pion.toString();
        } catch (Exception e) {
            if (this.blanc)
                return "░";
            else
                return " ";
        }
    }
    public void setCouleurPion(boolean couleurPion) {
        if(couleurPion){
            textPion.setForeground(Color.WHITE);
        }
        else {
            textPion.setForeground(Color.BLACK);
        }
    }
    public void resetColor(){
        if(blanc) {
            setBackground(Color.lightGray);
        }
        else {setBackground(Color.BLACK);}
        if(selectionnee)
            selectionnerCase();
        if(active)
            setBackground(Color.GREEN);

    }

    public int[] getPos(){int[] s = {x,y};return s;}

    public void deletePion() {
        this.pion = null;
        textPion.setText("");
    }

    public interface CaseEvent {
        void onClick(int[] pos);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!blanc){return;} //on ne séléctionne pas les cases noires
        if(pion!=null){
            if(this.pion.selectionne){this.pion.deselectionner();}
            else{this.pion.selectionner();}
        }
        if(selectionnee){this.deselectionnerCase();}
        else{this.selectionnerCase();}
        resetColor();
        int[] pos = {x,y};
        cListener.onClick(pos);
    }

    public void selectionnerCase() {
        if(pion!=null)
            pion.selectionner();
        selectionnee  = true;
        setBackground(Color.RED);
    }
    public void deselectionnerCase() {
        if(pion!=null)
            pion.deselectionner();
        selectionnee = false;
        resetColor();
    }

    public boolean isSelectionnee() {
        return selectionnee;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        resetColor();
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
