class Plateau {
    private Case[][] cases;
    public int taille;

    public Plateau(int taille) {
        this.taille = taille;
        this.cases = new Case[taille][taille];
        boolean doitEtreBlanc;
        for (int y = 0; y < this.cases.length; y++) {
            doitEtreBlanc = (y%2 == 0);
            for (int x = 0; x < this.cases[y].length; x++) {
                if (doitEtreBlanc) {
                    this.cases[y][x] = new Case();
                    this.cases[y][x].setBlanc();
                    doitEtreBlanc = false;
                } else {
                    this.cases[y][x] = new Case();
                    doitEtreBlanc = true;
                }
            }
        }
        int a = 1;
    }
    public void afficherPlateau(int[] posCurseur) {
        System.out.println("-----------------------------------------");
        for (int y=0;y<this.cases.length; y++) {
            for(int x=0; x<this.cases[y].length; x++) {
                Case cetteCase = this.cases[y][x];
                if(x == posCurseur[0] && y == posCurseur[1]) { //le curseur est sur la case
                    System.out.print("|C"+cetteCase+"C");
                }
                else{
                    if(cetteCase.isBlanc())
                        System.out.print("|░"+cetteCase+"░");
                    else
                        System.out.print("|   ");

                }
            }
            System.out.println("|");
            System.out.println("-----------------------------------------");
        }
        /*for (Case[] ligne : this.cases) {
            for (Case cetteCase : ligne) {
                if (cetteCase.hasPion()) {
                    if(cetteCase.pion.getX() == posCurseur[0] && cetteCase.pion.getY() == posCurseur[1])
                        System.out.print("|C"+cetteCase.pion+"C");
                    else
                        System.out.print("|░"+cetteCase.pion+"░");
                } else {
                    if (cetteCase.isBlanc())
                        System.out.print("|▉▉▉");
                    else
                        System.out.print("|   ");
                }
            }
            System.out.println("|");
            System.out.println("-----------------------------------------");
        }*/
    }

    public Case[][] getPlateau() {
        return this.cases;
    }

    public void update(Pion[] pions) {
        for (Case[] ligne: this.cases) {
            for(Case cetteCase: ligne)
                cetteCase.pion = null;
        }
        for (int i = 0; i < pions.length; i++) {
            Pion a = pions[i];
            this.cases[pions[i].getY()][pions[i].getX()].setPion(pions[i]);
        }
    }


}
