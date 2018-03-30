
public class LesDames {
    /*
    Ne pas oublier d'appeler plateau.update après avoir bougé des pions ou avant d'afficher
     */
    public static void main(String[] args) {
        Plateau plateau = new Plateau(10);
        Pion[] pions = RemplirPlateau(plateau, 20);
        plateau.update(pions);
        plateau.afficherPlateau();
    }

    public static Pion[] RemplirPlateau(Plateau plateau, int nbPion) {
        Pion tableauPions[] = new Pion[2 * nbPion];
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
}
