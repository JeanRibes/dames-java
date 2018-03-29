public class Affichage {
	public void afficherPlateau(Case[][] plateau) {
		for(Case[] ligne: plateau) {
			for(Case case: ligne) {
				if(case.blanc)
					System.out.print("O");
				else
					System.out.print("@");
			}
		}
	}
}
