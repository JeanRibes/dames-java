import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class LesDames {
    /*
    Ne pas oublier d'appeler plateau.update après avoir bougé des pions ou avant d'afficher
     */
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException, JSONException {
        Plateau plateau = new Plateau(10); //coordonnées de 0 à 9
        Rest api = new Rest("https://api.ribes.me"); //crée une connection

        System.out.println("Lancement...");
        //Rest api = new Rest("http://localhost");
        Pion[] pions;
        boolean joueBlanc;
        if (utiliserLobby(api)) //à mettre AVANT Input (ou faire input.close(); puis recréer input)
        {
            pions = RemplirPlateau(plateau, 20);
            joueBlanc = true;
            api.post(pions);
            api.aToiLeTour();
        } else {
            pions = api.get(); // reçoit les pions depuis le serveur
            joueBlanc = false;
        }
        Input input = new Input(); //à mettre après tout ce qui utilise un Scanner
        input.reset(plateau.taille);
        plateau.update(pions); //synchronise les pions dans les cases, à tout le temps appeler
        plateau.afficher(pions); // affiche le plateau actuel, sans le curseur
        //int[] pos = input.getPos(plateau); //va afficher le plateau et demander une position
        //System.out.println("Position: x="+pos[0]+" y="+pos[1]);
        //SocketAPI ws = new SocketAPI("wss://localhost", api.getId());
        SocketAPI ws = new SocketAPI("wss://api.ribes.me", api.getId());
        //ws.test();
        while(pionsVivants(pions)>1){
            /*while(!api.aQuiLeTour()) {
                System.out.print(".");
                Thread.sleep(3000);
            }*/
            ws.attendreTour(joueBlanc);

            pions = api.get();
            System.out.println("Reçu");

            plateau.afficher(pions);
            if (joueBlanc) //lol je me suis déjà mélangé !
                System.out.println("VOUS JOUEZ BLANCS");
            else
                System.out.println("VOUS JOUEZ NOIRS");
            action(pions, plateau, input);
            plateau.afficher(pions);

            api.post(pions);
            System.out.println("Envoi");
            api.aToiLeTour();
        }


        input.close(); //à mettre TOUT à la fin
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
    public static void action(Pion[] pions, Plateau plateau, Input input) {
        System.out.println("Séléctionnez un pion à bouger");
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
