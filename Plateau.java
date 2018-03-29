class Plateau {
    private Case[][] cases;
    public int taille;

    public Plateau(int taille) {
        this.taille = taille;
        this.cases = new Case[taille][taille];
        boolean pair = false;
        for (Case[] ligne: this.cases) {
            //ligne = new Case[taille];
            for (Case une: ligne) {
                une = new Case();
                if (pair) {
                    une.setBlanc();
                    pair = false;
                }
                else
                    pair = true;
                System.out.print(une);
            }
            System.out.println();
        }

    }
    public void initialiser() {
        for (int y = 0; y < this.cases.length; y++) {
            for (int i = 0; i < this.cases[y].length; i += 2) {
                this.cases[y][i] = new Case();
                this.cases[y][i].setBlanc();
                System.out.print(this.cases[y][i]);
            }
            System.out.println();
        }
    }
    public Case[][] getPlateau() {
        return this.cases;
    }
    public void afficherPlateau() {
        for (Case[] ligne: this.cases) {
            for (Case cetteCase: ligne) {
                Case a = cetteCase;
                if (cetteCase.isBlanc())
                    System.out.print("O");
                else
                    System.out.print("@");
            }
        }
    }
}
