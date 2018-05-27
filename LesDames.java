import java.io.IOException;

public class LesDames {
    /*
    Ne pas oublier d'appeler plateau.update après avoir bougé des pions ou avant d'afficher
     */
    public static void main(String[] args) throws IOException {
        Input input = new Input();
        Plateau plateau = new Plateau(10); //coordonnées de 0 à 9
        Pion[] pions = RemplirPlateau(plateau, 20);

        plateau.update(pions); //synchronise les pions dans les cases, à tout le temps appeler
        //plateau.afficher(); // affiche le plateau actuel, sans le curseur
        //int[] pos = input.getPos(plateau); //va afficher le plateau et demander une position
        //System.out.println("Position: x="+pos[0]+" y="+pos[1]);

        //bougerPion(pions, plateau, input);
        boolean joueBlanc=true;//les blancs commencent
        while (pionsVivants(pions) > 1) {
            if(joueBlanc)
                System.out.println("Vous jouez les pions BLANCS");
            else
                System.out.println("Vous jouez les pions NOIRS");
            action2(pions, plateau, input, joueBlanc);
            joueBlanc = !joueBlanc; //on change le tour
        }


        input.close(); //à mettre TOUT à la fin
    }

    public static Pion[] RemplirPlateau(Plateau plateau, int nbPion) {
        Pion tableauPions[] = new Pion[(2 * nbPion)];
        //Pion tableauPions[] = new Pion[1+(2 * nbPion)];
        int i = 0;
        for (int y = 0; y < plateau.taille; y++) {
            for (int x = 0; x < plateau.taille; x++) {
                if (i < nbPion && plateau.getPlateau()[y][x].isBlanc()) {
                    tableauPions[i] = new Pion(x, y, "pion", true);
                    i++;
                }
            }
        }
        for (int y = plateau.taille - 1; y >= 0; y--) {
            for (int x = 0; x < plateau.taille; x++) {
                if (i < 2 * nbPion && plateau.getPlateau()[y][x].isBlanc()) { //attention on ne joue que sur les cases noires
                    tableauPions[i] = new Pion(x, y, "pion", false);
                    i++;//on finit le compteur qui est déjà à nbPions
                }
            }
        }
        return tableauPions;
    }

    public static void bougerPion(Pion[] pions, Plateau plateau, Input input) {
        System.out.println("Séléctionnez un pion à bouger");
        int[] pos = input.selectPion(plateau);
        Pion pion = plateau.getPionDepuisCase(pos);
        System.out.println("Pion en x=" + pion.getX() + " y=" + pion.getY() + " séléctionné");
        pos = input.selectCase(plateau);
        pion.bouge(pos);
        System.out.println("Pion bougé en x=" + pion.getX() + " y=" + pion.getY());
        plateau.afficher(pions);
    }

    public static int getPionIndex(Pion[] pions, Pion cePion) {
        int returned = -1; //valeur par défaut pour "pas trouvé"
        for (int i = 0; i < pions.length; i++) {
            if (cePion.equals(pions[i]))
                returned = i;
        }
        return returned;
    }

    public static void action(Pion[] pions, Plateau plateau, Input input) {
        System.out.println("Séléctionnez un pion à bouger");
        int[] pos = input.selectPion(plateau);
        Pion pion = plateau.getPionDepuisCase(pos);
        System.out.println("Pion en x=" + pion.getX() + " y=" + pion.getY() + " séléctionné. Mangez ou bougez");
        pos = input.getPos(plateau);
        vide:
        //pour break correctement la boucle secondement supérieure
        if (plateau.estVide(pos)) {
            boolean reussi = pion.bouge(pos);
            while (!reussi) {
                System.out.println("Ne trichez pas");
                pos = input.getPos(plateau);
                //pos = input.selectCase(plateau);
                pion.bouge(pos);
            }
        } else {
            Pion cible = plateau.getPionDepuisCase(pos);
            while (cible.blanc == pion.blanc) { //tant qu'il choisit des pions alliés
                pos = input.getPos(plateau);
                if (plateau.estVide(pos)) { //le joueur peut choisir une case vide après avoir essayé de manger ses alliés
                    boolean reussi = pion.bouge(pos);
                    while (!reussi) {
                        System.out.println("Faut pas tricher !");
                        reussi = pion.bouge(pos);
                    }
                    break vide; //si la case est vide, on sort du "if" et on termine
                }
                cible = plateau.getPionDepuisCase(pos);
                //cible = input.getPion(plateau);
            }
            pion.mange(cible, plateau); //si le mec séléctionne un pion allié puis une case vide, ce code fait crash
            plateau.afficher(pions);
        }
        System.out.println("Pion bougé en x=" + pion.getX() + " y=" + pion.getY());
        plateau.afficher(pions);
    }

    public static int pionsVivants(Pion[] pions) {
        int n = 0;
        for (Pion pion : pions) {
            if (!pion.getTypePion().equals("mort"))
                n += 1;
        }
        return n;
    }

    public static void action2(Pion[] pions, Plateau plateau, Input input, boolean joueBlanc) {
        boolean reussi = false;
        while (!reussi) {
            Pion pion = input.getPion(plateau);
            if(pion.blanc!=joueBlanc) { //le joueur ne doit pas utiliser les pions de l'adversaire
                reussi = false;     // il n'y a pas besoin de plus de restriction sur ça, la méthode mange est bien faite
                System.out.println("PRENEZ VOS PIONS");
                continue; //on relance la boucle
            }
            pion.selectionner();
            System.out.println("Maintenant choisissez une destination pour ce pion");
            int pos[] = input.getPos(plateau);
            if (plateau.estVide(pos)) {
                reussi = pion.bouge(pos);
                plateau.update(pions);
                if(!reussi)
                    System.out.println("Action interdite, re-séléctionnez un pion");
            }
            else { //case avec un pion
                Pion cible = plateau.getPionDepuisCase(pos);
                reussi = pion.mange(cible, plateau);
                if(!reussi)
                    System.out.println("Action interdite, re-séléctionnez un pion");
                else { //le joueur a mangé, il a donc droit à un 2e tour
                    plateau.update(pions);
                    if(plateau.peutIlManger(pion)) {
                        reussi = false; //pour relancer la boucle
                        pion.selectionne = false;
                        plateau.update(pions); //pour éviter les petits "x" dans le plateau
                        continue;
                    }
                }
            }
            pion.selectionne = false;
        }
        plateau.update(pions);
        //plateau.afficher(pions);

    }
}
