@SuppressWarnings("ALL")
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
    //@Override
    //public String toString() {
    //	try {
    //		if (this.pion.getTypePion()>0){
    //			return "P";
    //		}
    //	}
    //	catch (Exception e) {
    //		if (this.blanc)
    //			return "_";
    //		else
    //			return "@";
    //	}
    //	return ".";
    //}
}
