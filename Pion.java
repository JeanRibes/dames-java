public class Pion {
    private int coordX;
    private int coordY;
    public String typePion;
    public boolean blanc;
    public boolean selectionne;

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
            System.out.println("Position invalide");
            for (int i : newpos)
                System.out.println(" Ceci est I :"+i);
            return reussi;
        }
    }


    public boolean mange(Pion cible, Plateau plateau) {
        this.selectionne = false;
        //if (this.bouge(cible.getPos())) {
        if(plateau.estVide(this.getPosManger(cible)) == true) { // A enlever si vous voulez retrouver le manger de base
                // this.getPosManger(cible);
                this.coordX=this.getPosManger(cible)[0];
                this.coordY=this.getPosManger(cible)[1];// Aussi
                System.out.println("test réussi"); // aussi
            cible.setTypePion("mort");
            return true;
        } else
            return false;
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
        if (System.getProperty("os.name") == "Linux") {
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

        System.out.println("test position : "+mangerpos[0]+" "+mangerpos[1]);
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
}
