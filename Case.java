//@SuppressWarnings("ALL")
public class Case {
    public boolean blanc;
    public Pion pion;

    public Case() {
        this.blanc = false;
    }

    public void setBlanc() {
        this.blanc = true;
    }

    public boolean isBlanc() {
        return blanc;
    }

    public void setPion(Pion pion) {
        this.pion = pion;
    }

    public boolean hasPion() {
        try {
            //noinspection ResultOfMethodCallIgnored,ResultOfMethodCallIgnored
            pion.getTypePion();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String toString() {
        try {
            this.pion.getX();
            return this.pion.toString();
        } catch (Exception e) {
            if (this.blanc)
                return "░";
            else
                return " ";
        }
    }
}
