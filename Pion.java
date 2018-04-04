public class Pion {
    private int coordX;
    private int coordY;
    public String typePion;
    public boolean blanc;
    public boolean curseur;

    /*
    typePion: mort, pion, dame
    blanc: si false, représente joueur 2
     */
    public Pion(int coordX, int coordY, String typePion, boolean blanc) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.typePion = typePion;
        this.blanc = blanc;
        this.curseur = false;
    }


    public int getX() {
        return coordX;
    }

    public int getY() {
        return coordY;
    }

    public void setX(int NewX) {
        coordX = NewX;
    }

    public void setY(int NewY) {
        coordY = NewY;
    }

    public String getTypePion() {
        return typePion;
    }

    public void setTypePion(String typePion) {
        this.typePion = typePion;
    }

    public void bouge(int[] newpos, int taille) {
        if(newpos.length == 2) {
            if (newpos[0] >=0 && newpos[0] <taille && newpos[1] >=0 && newpos[1] < taille) {
                this.coordX = newpos[0];
                this.coordY = newpos[1];
            }
        }
        else
            System.out.println("Il faut une coordonnée X et Y pour bouger");
    }

    public void mange(Pion cible) {
        this.coordX = cible.getX();
        this.coordY = cible.getY();
        cible.setTypePion("mort");
    }

    public String toString() {
        if (this.typePion.equals("mort")) {
            return "x";
        } else {
            if (this.typePion.equals("pion")) {
                if (blanc)
                    return "◯";
                else
                    return "◉";
            }
            else { //une Dame
                if (blanc)
                    return "▢";
                else
                    return "▣";
            }
        }
    }

    public void makeCurseur() {
        this.curseur = true;
        blanc = true;
        typePion = "curseur";
    }
    public int[] getPos() {
        int[] pos = new int[2];
        pos[0]=this.coordX;
        pos[1]=this.coordY;
        return pos;
    }
}
