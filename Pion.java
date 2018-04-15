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

    public void bouge(int[] newpos) {
        if (newpos.length == 2) {
            this.coordX = newpos[0];
            this.coordY = newpos[1];
            this.selectionne = false;
        } else
            System.out.println("Il faut une coordonnée X et Y pour bouger");
    }

    public void mange(Pion cible) {
        this.coordX = cible.getX();
        this.coordY = cible.getY();
        this.selectionne = false;
        cible.setTypePion("mort");
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
