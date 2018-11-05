public class Pion {
    private int coordX;
    private int coordY;
    public String typePion;
    public boolean blanc;
    public boolean selectionne;
    public static boolean displayToGUI;
    public static String TYPE_PION = "pion";
    public static String TYPE_DAME = "dame";

    /*
    typePion: mort, pion, dame
    blanc: si false, représente joueur 2
     */
    public Pion(int coordX, int coordY, String typePion, boolean blanc) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.typePion = typePion;
        this.blanc = blanc;
        this.selectionne = false;
    }


    public int getX() {
        return coordX;
    }

    public int getY() {
        return coordY;
    }

    public String getTypePion() {
        return typePion;
    }

    public void setTypePion(String typePion) {
        this.typePion = typePion;
    }

    public boolean bouge(int[] newpos) {
        boolean reussi = false;
        if (this.typePion.equals("pion") && (this.blanc == true && (newpos[0] >= (this.coordX - 1) && newpos[0] <= (this.coordX + 1)) && (newpos[1] >= this.coordY && newpos[1] < (this.coordY + 2)))) {
            this.coordX = newpos[0];
            this.coordY = newpos[1];
            this.selectionne = false;
            reussi = true;
        } else {
            if (this.typePion.equals("pion") && (this.blanc == false && (newpos[0] >= (this.coordX - 1) && newpos[0] <= (this.coordX + 1)) && (newpos[1] <= this.coordY && newpos[1] < (this.coordY + 2)))) {
                this.coordX = newpos[0];
                this.coordY = newpos[1];
                this.selectionne = false;
                reussi = true;
            }
        }
        if (typePion.equals("dame") && ((newpos[0] != this.coordX) && newpos[1] != this.coordY)) { // On bloque pas les diagonales, le joueur est intelligent sa mère.
            this.coordX = newpos[0];
            this.coordY = newpos[1];
            this.selectionne = false;
            reussi = true;
        }

        if (reussi && (this.typePion.equals("pion") && ((this.blanc && this.coordY == 9) || (!this.blanc && this.coordY == 0)))) { //Taille commençant à 0 donc 9
            this.typePion = "dame";
            System.out.println("Vous avez une dame!!!");
            return true;
        } else {
            //System.out.println("Position invalide");
            //for (int i : newpos)
            //    System.out.println(" Ceci est I :"+i);
            return reussi;
        }
    }

    /**
     * la distance maximale de coup est de 1 si pion, 20 si dame
     * @param cible le pion à manger
     * @param plateau pour le fonctionnement interne
     * @return false si le coup était valide
     */
    public boolean mange(Pion cible, Plateau plateau) {
        this.selectionne = false;
        int [] posApresManger = this.getPosManger(cible);
        //System.out.println("Distance à la cible: "+distanceAvec(cible)+" test:"+distanceValide(distanceAvec(cible)));
        int[] oldPos = getPos();
        //if (this.bouge(cible.getPos())) {
        if(!cible.isInvicible() && plateau.estVide(posApresManger) && this.blanc != cible.blanc
                && distanceValide(distanceAvec(cible))) {
            //System.out.println(posApresManger[0]+" "+posApresManger[1]);
            //System.out.println(cible.getY()+" "+cible.getX());
            setPos(cible.getPos());

            if(this.bouge(posApresManger)) { //si il y a une case où le pion peut atterrir
                cible.tuer(); //note : deux pions peuvent être sur une MÊME case ! (mais un seul sera affiché)
                return true;
            }
            else {
                setPos(oldPos);//on annule les changements
                System.out.println("Erreur: impossible de manger vers cette case");
                return false;}
        } else {
            //System.out.print("mangeage échoué");
            return false;
        }
    }

    /* public void verifmange(Pion cible, Plateau plateau) {
        int pos1[] = {(this.coordX + 2), (this.coordY - 2)};
        if ((this.coordX == cible.coordX - 1) && (this.coordY == cible.coordY + 1) && (plateau.estVide(pos1))) {
            this.coordX += 2;
            this.coordY -= 2;
            System.out.println("Text");
        }
        int pos2[] = {(this.coordX - 2), (this.coordY - 2)};
        if ((this.coordX == cible.coordX - 1) && (this.coordY == cible.coordY - 1) && (plateau.estVide(pos2))) {
            this.coordX -= 2;
            this.coordY -= 2;
            System.out.println("Text");
        }

        int pos3[] = {(this.coordX + 2), (this.coordY + 2)};
        if ((this.coordX == cible.coordX + 1) && (this.coordY == cible.coordY + 1) && (plateau.estVide(pos3))) {
            this.coordX += 2;
            this.coordY += 2;
            System.out.println("Text");
        }

        int pos4[] = {(this.coordX - 2), (this.coordY + 2)};
        if ((this.coordX == cible.coordX - 1) && (this.coordY == cible.coordY + 1) && (plateau.estVide(pos4))) {
            this.coordX -= 2;
            this.coordY += 2;
            System.out.println("Text");
        }
    } */

    public String toString() {
        if(displayToGUI){
            if (this.typePion.equals("pion")) {
                return "◉";
            } else {
                if (this.typePion.equals("dame")) { //une Dame
                     return "▣";
                } else return "x";
            }
        } else {
            if (System.getProperty("os.name").equals("Linux")) {
                if (this.selectionne) {
                    return "@";
                } else {
                    if (this.typePion.equals("pion")) {
                        if (!blanc)
                            return "◯";
                        else
                            return "◉";
                    } else {
                        if (this.typePion.equals("dame")) { //une Dame
                            if (!blanc)
                                return "▢";
                            else
                                return "▣";
                        } else return "x";
                    }
                }
            } else {
                if (this.selectionne) {
                    return "@";
                } else {
                    if (this.typePion.equals("pion")) {
                        if (blanc)
                            return "b";
                        else
                            return "n";
                    } else {
                        if (this.typePion.equals("dame")) { //une Dame
                            if (blanc)
                                return "B";
                            else
                                return "N";
                        } else return "x";
                    }
                }
            }
        }
    }

    public int[] getPosManger(Pion cible){
        int[] mangerpos = new int[2];
        int k=0;
        int j=0;
        if(this.coordX <= cible.coordX )
            k = 1; // cas 1 => Cible est à droite de pion
        else
            k = -1; // cas 2 => Cible est à gauche de pion
        if(this.coordY <= cible.coordY)
            j = -1; // cas 1 => Cible est en dessus de pion
        else
            j = 1; // cas 2 => Cible est au dessous de pion

        if(k==1)
            mangerpos[0]=(cible.coordX+1);
        else
            mangerpos[0]=(cible.coordX-1);
        if(j==1)
            mangerpos[1]=(cible.coordY-1);
        else
            mangerpos[1]=(cible.coordY+1);

        //System.out.println("position pion :"+this.coordX+" "+this.coordY);
        //System.out.println("position cible: "+mangerpos[0]+" "+mangerpos[1]);
        return mangerpos;
    }

    public int[] getPos() {
        int[] pos = new int[2];
        pos[0] = this.coordX;
        pos[1] = this.coordY;
        return pos;
    }

    public void selectionner() { //pour montrer visuellement que ce pion va être bougé par le joueur
        this.selectionne = true;
    }

    public void deselectionner() {
        this.selectionne = false;
    }
    public boolean isVivant() {
        return !typePion.equals("mort");
    }
    public boolean isDame() {
        return typePion.equals("dame");
    }
    public boolean isPion() {
        return typePion.equals("pion");
    }
    public void tuer() {
        if(!isVivant())
            System.out.println("Un pion mort a été tué !!!");
        this.typePion = "mort";
    }
    public void setPos(int[] pos){
        if(pos.length==2){
            coordX = pos[0];
            coordY = pos[1];
        }
        else
            System.out.println("Erreur: il faut une composante X et Y");
    }
    public int distanceAvec(Pion cible) {
        return (int)Math.sqrt(
                Math.pow(Math.abs(this.coordX-cible.coordX), 2)
                +Math.pow(Math.abs(this.coordY-cible.coordY),2)
                ); //racine de deltaX² + deltaY²
    }

    public boolean distanceValide(int distance) {
        return this.isDame() || distance==1; //faire un tableau de vérité; j'ai inversé la colonne distance
    }

    /**
     *
     * @return vrai si le pion est sur le pourtout du cercle
     */
    public boolean isInvicible() {
        return (coordX==0||coordX==9||coordY==0||coordY==9);
    }

    public boolean equals(Pion pion) {
        if(pion.blanc==this.blanc)
            return true;
        else return false;
    }

    public static void setDisplayToGUI(boolean displayToGUI) {
        Pion.displayToGUI = displayToGUI;
    }
}
