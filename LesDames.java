
public class LesDames {

    public static void main(String[] args) {
        //Case plateau[][] = new Case[10][10];
        //Pion joueur1[] = new Pion[20];
        //Pion joueur2[] = new Pion[20];
        Plateau plateau = new Plateau(10);
        plateau.initialiser();
        plateau.afficherPlateau();
    }


    public static Pion[] RemplirPlateau(int taille, int nbPion, boolean joueur1) {
        Pion tableauPions[] = new Pion[nbPion];
        for (int x = 0; x <= 10; x++) {
            for (int y = 0; y <= 10; y++) {

            }
        }
        return tableauPions;
    }


}
