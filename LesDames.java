import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.HttpURLConnection;
public class LesDames {
    /*
    Ne pas oublier d'appeler plateau.update après avoir bougé des pions ou avant d'afficher
     */
    public static void main(String[] args) throws IOException {
        Plateau plateau = new Plateau(10); //coordonnées de 0 à 9
        Pion[] pions = RemplirPlateau(plateau, 20);
        plateau.update(pions); //synchronise les pions dans les cases, à tout le temps appeler

        Rest api = new Rest("http://localhost:8000", "3c5b6eb01d4e24b08bc7562cbaff5472034ebbc7");

        api.asyncPost(pions); //envoie les pions au serveur
        plateau.afficher(pions); // affiche le plateau actuel, sans le curseur
        System.out.println("GET maintenant");
        Pion[] nouveauxPions = api.get();
        plateau.afficher(nouveauxPions);

        /*Input input = new Input();
        int[] pos = input.getPos(plateau); //va afficher le plateau et demander une position
        System.out.println("Position: x="+pos[0]+" y="+pos[1]);

        pions[0].bouge(input.getPos(plateau)); //exemple pour bouger un pion
        plateau.afficher(pions);              //si on le met sur une case noire il sera invisible


        input.close(); //à mettre TOUT à la fin*/
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
