import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;

public class LesDames {
    /*
    Ne pas oublier d'appeler plateau.update après avoir bougé des pions ou avant d'afficher
     */
    public static void main(String[] args) throws IOException {
        Plateau plateau = new Plateau(10);
        Pion[] pions = RemplirPlateau(plateau, 20);
        plateau.update(pions);
        //plateau.afficherPlateau(pions[pions.length-1].getPos());
        int[] initpos = {0,0};
        plateau.afficherPlateau(initpos);
        Affichage af = new Affichage(); //permet de tester les flèches, et affiche les codes pour les autres touches
        //af.test();

        Input input = new Input();
        String key = input.getKeyCode();
        while (!key.equals("ENTER")){
            key = input.getKeyCode();
            int[] posCurseur = input.getPos(plateau.taille, key);
            //pions[pions.length-1].bouge(posCurseur, plateau.taille);
            plateau.update(pions);
            plateau.afficherPlateau(posCurseur);
        }
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
        //tableauPions[i] = new Pion(0,0, "curseur", false);
        //tableauPions[i].makeCurseur();
        return tableauPions;
    }
}
