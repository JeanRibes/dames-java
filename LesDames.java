import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class LesDames {
    /*
    Ne pas oublier d'appeler plateau.update après avoir bougé des pions ou avant d'afficher
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        Plateau plateau = new Plateau(10); //coordonnées de 0 à 9
        Rest api = new Rest("https://api.ribes.me"); //crée une connection
        //Rest api = new Rest("http://localhost:8000");

        System.out.println("Lancement...");
        Pion[] pions;
        boolean joueBlanc;
        if (utiliserLobby(api)) //à mettre AVANT Input (ou faire input.close(); puis recréer input)
        {
            pions = RemplirPlateau(plateau, 1);
            joueBlanc = true;
            api.post(pions);
            //api.aToiLeTour();
        } else {
            //pions = api.get(); // reçoit les pions depuis le serveur
            joueBlanc = false;
        }
        Input input = new Input(); //à mettre après tout ce qui utilise un Scanner
        input.reset(plateau.taille-1);
        //plateau.update(pions); //synchronise les pions dans les cases, à tout le temps appeler
        //plateau.afficher(pions); // affiche le plateau actuel, sans le curseur

        //SocketAPI ws = new SocketAPI("ws://localhost:8000", api.getId(), joueBlanc);
        SocketAPI ws = new SocketAPI("wss://api.ribes.me", api.getId(), joueBlanc);
        pions = ws.waitGet();
        while(pionsVivants(pions)>1){
            System.out.println("À vous!");
            plateau.update(pions);

            action(pions, plateau, input);
            plateau.afficher(pions);

            ws.post(pions);

            System.out.println("Attendez l'autre joueur");
            pions = ws.waitGet(); //attend jusqu'à la fin du tour de l'autre (donc jusqu'à réception des données)
        }
        ws.post(pions); //sinon waitGet fait attendre la boucle et ne reçoit jamais les data de l'autre joueur
        ws.sync.close();
        if(joueBlanc)
            api.supprPartie();
        System.out.println("bravo !");


        input.close(); //à mettre TOUT à la fin
        System.exit(0);
    }

    public static Pion[] RemplirPlateau(Plateau plateau, int nbPion) {
        System.out.println("Plateau crée !");
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

    /**
     * Helper pour utiliser le lobby multjoueur:
     *   On peut créer ou rejoindre une partie en attente
     * @param api l'intance Rest utilisée
     * @return vrai si le joueur joue les pions blancs
     */
    public static boolean utiliserLobby(Rest api) { //retourne TRUE si le joueur joue les pions blancs
        System.out.println("Tapez un pseudo pour jouer en ligne");
        Scanner sc = new Scanner(System.in);
        String nom = sc.nextLine();
        api.getLobby();
        for (int i = 0; i < api.lobby.length; i++) {
            System.out.println(api.lobby[i].player1 + " #" + i);
        }
        System.out.println("Créer une partie : #-1");
        System.out.println("Faites votre choix : (entrez un nombre) >");
        int choix = sc.nextInt();
        if (choix >= 0) {
            System.out.println("OUI");
            System.out.println(api.lobby[choix].player1);
            api.rejoindre(api.lobby[choix].id, nom);
            return false;
        } else {
            api.creerPartie(nom);
            return true;
        }
    }

    /**
     *  méthode pour une action générique :
     *  bouger ou manger un pion
     * @param pions
     * @param plateau
     * @param input
     */
    public static void action(Pion[] pions, Plateau plateau, Input input) {
        System.out.println("Séléctionnez un pion à bouger");
        plateau.update(pions);
        int[] pos = input.selectPion(plateau);
        Pion pion = plateau.getPionDepuisCase(pos);
        System.out.println("Pion en x=" + pion.getX() + " y=" + pion.getY() + " séléctionné. Mangez ou bougez");

        pos = input.getPos(plateau);
        vide:
        if (plateau.estVide(pos))
            pion.bouge(pos);
        else {
            Pion cible = plateau.getPionDepuisCase(pos);
            while (cible.blanc == pion.blanc) { //tant qu'il choisit des pions alliés
                pos = input.getPos(plateau);
                if(plateau.estVide(pos)) { //le joueur peut choisir une case vide après avoir essayé de manger ses alliés
                    pion.bouge(pos);
                    break vide; //si la case est vide, on sort du "if" et on termine
                }
                cible = plateau.getPionDepuisCase(pos);
                //cible = input.getPion(plateau);
            }
            pion.mange(cible); //si le mec séléctionne un pion allié puis une case vide, ce code fait crash
            //plateau.afficher(pions);
        }
        //System.out.println("Pion bougé en x=" + pion.getX() + " y=" + pion.getY());
        //plateau.afficher(pions);
    }

    public static int pionsVivants(Pion[] pions) {
        int n=0;
        for(Pion pion: pions) {
            if(!pion.getTypePion().equals("mort"))
                n+=1;
        }
        return n;
    }
}
