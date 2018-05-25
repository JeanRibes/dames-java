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
    }

    public void afficherPlateau(int[] posCurseur) {
        //System.out.println("-----------------------------------------");
        System.out.println("┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐");
        for (int y=0;y<this.cases.length; y++) {
            for(int x=0; x<this.cases[y].length; x++) {
                Case cetteCase = this.cases[y][x];
                if(x == posCurseur[0] && y == posCurseur[1]) { //le curseur est sur la case
                    System.out.print("│["+cetteCase+"]");
                }
                else{
                    if(cetteCase.isBlanc())
                        System.out.print("│░"+cetteCase+"░");
                    else
                        System.out.print("│   ");

                }
            }
            System.out.println("│");
            if(y!=this.cases.length-1)
                System.out.println("├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤");
            else
                System.out.println("└───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘");
        }
    }

    public void afficher(Pion[] pions) {
        this.update(pions);
        afficherPlateau(new int[]{-1, -1}); //petit hack pour ne pas avoir à copier/coller du code
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
            if (pions[i].isVivant()) // on ne rajoute pas les pions morts à l'affichage
                this.cases[pions[i].getY()][pions[i].getX()].setPion(pions[i]);
        }
    }

    public Pion getPionDepuisCase(int[] pos) { //ça ne fera pas recommencer en cas d'erreur

        try {
            return this.cases[pos[1]][pos[0]].pion;
        } catch (Exception e) {
            System.out.println("Veuillez séléctionner un pion correct");
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasPion(int[] pos) {
        return this.cases[pos[1]][pos[0]].hasPion();
    }

    public boolean estVide(int[] pos) {
        return !this.cases[pos[1]][pos[0]].hasPion();
    }
}
