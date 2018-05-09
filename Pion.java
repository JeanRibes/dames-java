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
        if (this.typePion.equals("pion") && (this.blanc == true && (newpos[0] >= (this.coordX - 1) && newpos[0] <=
                (this.coordX + 1)) && (newpos[1] >= this.coordY && newpos[1] < (this.coordY + 2)))) {
            this.coordX = newpos[0];
            this.coordY = newpos[1];
            this.selectionne = false;
            System.out.println("Pion BLANC, descend");
            reussi = true;
        }
        else {
            if (this.typePion.equals("pion") && (this.blanc == false && (newpos[0] >= (this.coordX - 1) && newpos[0] <= (this.coordX + 1)) && (newpos[1] <= this.coordY && newpos[1] < (this.coordY + 2)))) {
                this.coordX = newpos[0];
                this.coordY = newpos[1];
                this.selectionne = false;
                System.out.println("Pion NOIR, MONTE");
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
        }
        else {
            System.out.println("Position invalide");
            for(int i: newpos)
                System.out.println(i);
            return reussi;
        }
    }


    public boolean mange(Pion cible) {
        this.selectionne = false;
        if (this.bouge(cible.getPos())) {
            cible.setTypePion("mort");
            if (this.typePion.equals("pion") && ((this.blanc && this.coordY == 9) || (!this.blanc && this.coordY == 0))) { //Taille commençant à 0 donc 9
                this.typePion = "dame";
                System.out.println("Vous avez une dame!!!");
            }
            return true;
        }
        else
            return false;
    }

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
