class Plateau {
    private Case[][] cases;
    public int taille;

    public Plateau(int taille) {
        this.taille = taille;
        this.cases = new Case[taille][taille];
        for (int y = 0; y < this.cases.length; y++) {
            for (int i = 0; i < this.cases[y].length; i += 2) {
                this.cases[y][i] = new Case();
                this.cases[y][i].setBlanc();
                //on crée la case suivante noire (par défaut)
                this.cases[y][i + 1] = new Case();
            }
        }
    }

    public void afficherPlateau() {
        System.out.println("--------------------");
        for (Case[] ligne : this.cases) {
            for (Case cetteCase : ligne) {
                if(cetteCase.hasPion()){
                    System.out.print("P");
                }
                else {
                    if (cetteCase.isBlanc())
                        System.out.print("| ");
                    else
                        System.out.print("|@");
                }
            }
            System.out.println("|");
            System.out.println("--------------------");
        }
    }

    public Case[][] getPlateau() {
        return this.cases;
    }

    public void tout(Pion[] pions){
        for (Pion pion: pions){
            this.cases[pion.getY()][pion.getX()].setPion(pion);
        }
    }

}
