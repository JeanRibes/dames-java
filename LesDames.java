import java.io.IOException;
import java.util.Scanner;

public class LesDames {
    /*
    Ne pas oublier d'appeler plateau.update après avoir bougé des pions ou avant d'afficher
     */
    public static void main(String[] args) throws IOException {
        Plateau plateau = new Plateau(10); //coordonnées de 0 à 9

        System.out.println("Lancement...");

        Rest api = new Rest("https://api.ribes.me"); //crée une connection
        //Rest api = new Rest("http://localhost:8000");
        Pion[] pions;
        if(utiliserLobby(api)) //à mettre AVANT Input (ou faire input.close(); puis recréer input)
        {
             pions = RemplirPlateau(plateau, 20);
        }
        else {
            pions = api.get(); // reçoit les pions depuis le serveur
        }
        plateau.update(pions); //synchronise les pions dans les cases, à tout le temps appeler
        Input input = new Input(); //à mettre après l'entrée utilisateur

        plateau.update(pions);
        //api.post(pions);

        bougerPion(pions, plateau, input);
        //api.asyncPost(pions); //envoie les pions au serveur de manière asynchrone
        api.post(pions); //envoie au serveur de manière synchrone
        plateau.afficher(pions); // affiche le plateau actuel, sans le curseur
        //input.getKeyCode();
        pions = sync(api, plateau);
        bougerPion(pions, plateau, input);
        api.post(pions);

        //api.supprPartie(); quand c'est fini, suopprimer pour éviter de flood


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

    public static Pion[] sync(Rest api, Plateau plateau) {
        Pion[] pions = api.get();
        plateau.afficher(pions);
        System.out.println("Pions recus du serveur");
        return pions;
    }

    public static boolean utiliserLobby(Rest api) { //retourne TRUE si le joueur joue les pions blancs
        System.out.println("Appuyez sur Entrée pour rejoindre une partie, ou tapez un pseudo");
        Scanner sc = new Scanner(System.in);
        String nom = sc.nextLine();
        if (nom.length() > 0) {
            api.creerPartie(nom);
            return true;
        } else {
            api.getLobby();
            for (int i = 0; i < api.lobby.length; i++) {
                System.out.println(api.lobby[i].player1 + " #" + i);
            }
            System.out.println("Faites votre choix : (entrez un nombre) >");
            int choix = sc.nextInt();
            System.out.println("OUI");
            System.out.println(api.lobby[choix].player1);
            api.rejoindre(api.lobby[choix].id);
            return false;
        }
    }
}
