public class Pion {
    private int coordX;
    private int coordY;
    public String typePion;
    public boolean blanc;

    /*
    typePion: mort, pion, dame
    blanc: si false, représente joueur 2
     */
    public Pion(int coordX, int coordY, String typePion, boolean blanc) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.typePion = typePion;
        this.blanc = blanc;
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

    public void bouge(int TypePion) {
    }

    public void mange(Pion cible) {
        this.coordX = cible.getX();
        this.coordY = cible.getY();
        cible.setTypePion("mort");
    }

    public String toString() {
        if (this.typePion == "mort") {
            return " ";
        } else {
            if (this.typePion == "pion") {
                if (blanc)
                    return "◯";
                else
                    return "◉";
            }
            else {
                if (blanc)
                    return "▢";
                else
                    return "▣";
            }
        }
    }

}
