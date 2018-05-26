import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Classe permettant de simplifier l'interfa&ccedil;age avec l'utilisateur
 * jline est tr&egrave;s peu utilis&eacute;, c'est uniquement pour avoir les codes clavier sans que l'utilisateur appuie sur enter
 */
public class Input {
    public int curX;
    public int curY;
    private int compteurKonami;

    public Input() {
        this.curX = 0;
        this.curY = 0;
        System.out.println("Pour séléctionner un pion, deux méthodes sont disponibles :");
        System.out.println("* flèches du clavier puis entrée");
        System.out.println("* si votre terminal n'est pac compatible, utilisez ZQSD pour vous déplacer et Espace pour valider,");
        System.out.println("    et entre chaque touche il faudra appuyer sur enter");

        compteurKonami = 0;
    }

    public void close() throws IOException {
        RawConsoleInput.resetConsoleMode();
    }

    /**
     * Permet de repositionner le curseur, mis habituellement au milieu de l'&eacute;cran
     * @param taille la taille du plateau
     */
    public void reset(int taille) { //remet les coordonnées du curseur à 0; par défaut il se souvient de sa précédente séléction
        this.curX = taille/2;
        this.curY = taille/2;
    }

    /**
     * M&eacute;thode interne pour traduire les codes clavier
     * termine l'ex&eacute;cution si on appuie sur Entr&eacute;e ou Espace
     * @return une touche press&eacute;e
     */
    public String getKeyCode() {
        try {
            int code = RawConsoleInput.read(true);
            //while (true) {
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
                    case 97:
                        return "A";
                    case 98:
                        return "B";
                    case 27:
                        break; //pour enlever les codes envoyés avant les flèches (sur linux)
                    case 91:
                        break;
                    case 10:
                        //return "ENTER";
                    case 3: //CTRL+C sur Windows qui est échappé
                        System.exit(0);
                        break;
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
            e.printStackTrace();
            return "ERROR";
        }
    }

    /**
     *
     * @param taille la taille du plateau une instance de Plateau
     * @see Plateau#taille
     * @param key la touche press&eacute;e par l'utilisateur
     * @return un tableau de deux entiers [X,Y] repr&eacute;sentation la position du curseur
     */
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

    /**
     * Permet de s&eacute;l&eacute;ctionner une case sur le plateau en d&eacute;placant le curseur
     * emp&ecirc;che de choisir une case noire (logque pour les Dames)
     * @param plateau une instance de Plateau
     * @see Plateau
     * @return un tableau de deux entiers [X,Y]
     */
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
                konami(key);
                key = this.getKeyCode();
            }
            if (plateau.getPlateau()[pos[1]][pos[0]].isNoir())
                System.out.println("Veuillez séléctionner une case blanche !");

        } while (plateau.getPlateau()[pos[1]][pos[0]].isNoir());
        //System.out.println(curX + " " + curY);
        return pos;
    }

    /**
     * S&eacute;l&eacute;ctionne un pion et effectue les v&eacute;rification.
     * Cette m&eacute;thode "surcharge" getPos
     * @see #getPos(Plateau)
     * @param plateau une instance
     * @return la position [x,y]
     */
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
        while (plateau.hasPion(pos)) {
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

    public void konami(String key){
        switch (key){
            case "DOWN": //flèche du haut
                //System.out.println("haut");
                if(compteurKonami<2)
                    compteurKonami+=1;
                else
                    compteurKonami = 0;
                break;
            case "UP": //flcèhe du bas
                //System.out.println("base");
                if(compteurKonami==2 || compteurKonami ==3)
                    compteurKonami+=1;
                else
                    compteurKonami = 0;
                break;
            case "LEFT":
                //System.out.println("gauche");
                if(compteurKonami == 4 || compteurKonami==6)
                    compteurKonami+=1;
                else
                    compteurKonami=0;
                break;
            case "RIGHT":
                //System.out.println("droite");
                if(compteurKonami==5 || compteurKonami==7)
                    compteurKonami+=1;
                break;
            case "B":
                if(compteurKonami==8)
                    compteurKonami+=1;
                else
                    compteurKonami=0;
                break;
            case "A":
                if(compteurKonami==9) {
                    for (int i = 0; i < 60; i++) {
                        System.out.println("Vous avez truché");
                    }
                    try {
                        getUnsafe().getByte(0);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    System.exit(1);
                }
                else
                    compteurKonami=0;
        }
    }
    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (Unsafe) theUnsafe.get(null);
    }
}
