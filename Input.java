import java.io.IOException;

public class Input {
    public int curX;
    public int curY;

    public Input()  {
        this.curX = 0;
        this.curY = 0;
        System.out.println("Pour séléctionner un pion, dux méthodes sont disponibles :");
        System.out.println("* flèches du clavier puis entrée");
        System.out.println("* si votre terminal n'est pac compatible, utilisez ZQSD pour se déplacer et Esapce pour valider");
        System.out.println("    et entre chaque touche il faudra appuyer sur enter");

    }

    public void close() throws IOException {
        RawConsoleInput.resetConsoleMode();
    }

    public void reset() { //remet les coordonnées du curseur à 0; par défaut il se souvient de sa précédente séléction
        this.curX = 0;
        this.curY = 0;
    }

    public String getKeyCode() {
        try {
            int code = RawConsoleInput.read(true);
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
                    case 57416: //windows
                        return "DOWN";
                    case 57419:
                        return "LEFT";
                    case 57421:
                        return "RIGHT";
                    case 57424:
                        return "UP";
                    case 27:
                        break; //pour enlever les codes envoyés avant les flèches (sur linux)
                    case 91:
                        break;
                    case 10:
                        break;
                    case 3: //CTRL+C sur Windows qui est échappé
                        System.exit(0);
                    //noinspection ConstantConditions
                    case 32:
                        return "ENTER";
                    default:
                        System.out.println("Espace ou Entrée pour valider, et pour se déplacer");
                        System.out.print("flèches ou ZQSD >");
                        System.out.println(code);

                }
                code = RawConsoleInput.read(true);
            }
            RawConsoleInput.resetConsoleMode();
            return "ENTER";
        } catch (IOException e) {
            System.out.println("Erreur interne");
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
        return new int[]{this.curX, this.curY};
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
    public Pion getPion(Plateau plateau) { //renvoie un pion sans le passer en "séléctionné"
        int[] pos = getPos(plateau);
        while (plateau.estVide(pos)) {
            System.out.println("Cette case est vide !");
            pos = getPos(plateau);
        }
        return plateau.getPionDepuisCase(pos);
    }
}
