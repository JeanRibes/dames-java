
public class LesDames {

    public static void main(String[] args) {
        //Case plateau[][] = new Case[10][10];
        //Pion joueur1[] = new Pion[20];
        //Pion joueur2[] = new Pion[20];
        Plateau plateau = new Plateau(10);
        Pion[] pions = RemplirPlateau(plateau, 20, true);
        plateau.tout(pions);
        plateau.afficherPlateau();
    }


    public static Pion[] RemplirPlateau(Plateau plateau, int nbPion, boolean joueur1) {
        Pion tableauPions[] = new Pion[nbPion];
        int i = 0;
        for (int y = 0; y < plateau.taille; y++) {
            for (int x = 0; x < plateau.taille; x++) {
                if (i < nbPion && plateau.getPlateau()[y][x].isBlanc())
                    tableauPions[i] = new Pion(x, y, 1);
                i++;
            }
        }
        return tableauPions;
    }


}
