import java.io.IOException;
public class LesDames {
    /*
    Ne pas oublier d'appeler plateau.update après avoir bougé des pions ou avant d'afficher
     */
    public static void main(String[] args) throws IOException {
        Plateau plateau = new Plateau(10); //coordonnées de 0 à 9
        Pion[] pions = RemplirPlateau(plateau, 20);
        plateau.update(pions); //synchronise les pions dans les cases, à tout le temps appeler
        System.out.println("Lancement...");
        Rest api = new Rest("https://api.ribes.me", "8d87985af8599b5a519f467742ec978a50bf93b3"); //crée une connection

        //api.asyncPost(pions); //envoie les pions au serveur de manière asynchrone
        api.asyncPost(RemplirPlateau(plateau, 10));
        plateau.afficher(pions); // affiche le plateau actuel, sans le curseur
        System.out.println("GET maintenant");
        Pion[] nouveauxPions = api.get(); // reçoit les pions depuis le serveur

        //api.post(nouveauxPions); //envoie les pions de manière synchrone

        Input input = new Input();
        int[] pos = input.getPos(plateau); //va afficher le plateau et demander une position
        System.out.println("Position: x="+pos[0]+" y="+pos[1]);

        pions[0].bouge(input.getPos(plateau)); //exemple pour bouger un pion
        plateau.afficher(pions);              //si on le met sur une case noire il sera invisible
        plateau.afficher(nouveauxPions);


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
}
