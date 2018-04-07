import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.Reader;

public class Input {
    public Reader reader;
    public Terminal terminal;
    public int curX;
    public int curY;

    public Input() throws IOException {
        this.curX = 0;
        this.curY = 0;
        this.terminal = TerminalBuilder.builder()
                .jna(true)
                .system(true)
                .build();
        System.out.println("Pour séléctionner un pion, dux méthodes sont disponibles :");
        System.out.println("* flèches du clavier puis entrée");
        System.out.println("* si votre terminal n'est pac compatible, utilisez ZQSD pour se déplacer et Esapce pour valider");
        System.out.println("    et entre chaque touche il faudra appuyer sur enter");

// raw mode means we get keypresses rather than line buffered input
        this.terminal.enterRawMode();
        this.reader = terminal.reader();
    }

    public void close() throws IOException {
        this.reader.close();
        this.terminal.close();
    }

    public void reset() { //remet les coordonnées du curseur à 0; par défaut il se souvient de sa précédente séléction
        this.curX = 0;
        this.curY = 0;
    }

    public String getKeyCode() {
        try {
            int code = reader.read();
            while (!(code == 13 || code == 32)) { //32=SPACE, 13=ENTER
                switch (code) {
                    case 65:
                        return "DOWN";
                    case 68:
                        return "LEFT";
                    case 67:
                        return "RIGHT";
                    case 66:
                        return "UP";
                    case 122:
                        return "DOWN";
                    case 113:
                        return "LEFT";
                    case 100:
                        return "RIGHT";
                    case 115:
                        return "UP";
                    case 27:
                        break; //pour enlever les codes envoyés avant les flèches (sur linux)
                    case 91:
                        break;
                    case 10:
                        break;
                    case 32:
                        return "ENTER";
                    default:
                        System.out.println("Espace ou Entrée pour valider, et pour se déplacer");
                        System.out.println("flèches ou ZQSD >");

                }
                code = reader.read();
            }
            return "ENTER";
        } catch (IOException e) {
            System.out.println("Erreur interne");
            e.printStackTrace();
            return "ERROR";
        }
    }

    public int[] updatePos(int taille, String key) {
        switch (key) {
            case "UP":
                if (this.curY < taille - 1)
                    this.curY += 1;
                break;
            case "RIGHT":
                if (this.curX < taille - 1)
                    curX += 1;
                break;
            case "LEFT":
                if (curX > 0)
                    this.curX -= 1;
                break;
            case "DOWN":
                if (curY > 0)
                    this.curY -= 1;
                break;
        }
        int[] pos = {this.curX, this.curY};
        return pos;
    }

    public int[] getPos(Plateau plateau) {
        /*curX = plateau.taille / 2;
        curY = plateau.taille / 2;*/
        int[] pos = {this.curX, this.curY}; //
        do {
            plateau.afficherPlateau(pos);
            String key = this.getKeyCode();
            while (!key.equals("ENTER")) {
                pos = this.updatePos(plateau.taille, key);
                plateau.afficherPlateau(pos);
                key = this.getKeyCode();
            }
            if(plateau.getPlateau()[pos[1]][pos[0]].isNoir())
                System.out.println("Veuillez séléctionner une case blanche !");

        } while (plateau.getPlateau()[pos[1]][pos[0]].isNoir());
        System.out.println(curX + " " + curY);
        return pos;
    }

    public int[] selectPion(Plateau plateau) { // pour séléctionner les coordonnées d'un pion
        int[] pos = getPos(plateau);
        while (plateau.estVide(pos)) {
            System.out.println("Cette case est vide !");
            pos = getPos(plateau);
        }
        plateau.getPionDepuisCase(pos).selectionner();
        return pos;
    }

    public int[] selectCase(Plateau plateau) { //pour séléctionner les coordonnées d'un case vide
        int[] pos = getPos(plateau);
        while (plateau.hasPion(pos)){
            System.out.println("Cette case est déjà occupée");
            pos = getPos(plateau);
        }
        return pos;
    }
    public Pion getPion(Plateau plateau) { //donne le pion directement
        int[] pos = getPos(plateau);
        while (plateau.estVide(pos)) {
            System.out.println("Cette case est vide !");
            pos = getPos(plateau);
        }
        Pion pion = plateau.getPionDepuisCase(pos);
        pion.selectionner();
        return pion;
    }
}
