public class Pion{
	private int coordX;
	private int coordY;
	private int TypePion;
	private Pion[] TabPions; // private => Need des get
	
	public Pion(int coordX, int coordY, int TypePion){
		this.coordX = coordX;
		this.coordY = coordY;
		this.TypePion = TypePion;
		this.pions = new Pion[20]; // 20 pions par joueur		
	}
	
	
	public int getX(){
		return coordX;		
	}
	
	public int getY(){
		return coordY;
	}
	
	public int getTypePion(){
		return TypePion;
	}
	
	public void Bouge(int TypePion){


	// devra faire x déplacements * type pion ? Possibilité utiliser reste de division eucl
		
	}
	
	public void Mange(Pion cible){
		this.coordX = cible.coordX;
		this.coordY = cible.coordY;
		cible.TypePion = 0;
		
		//rajouter la possibilité de manger plusieurs pions + demander dans quel sens ?
	}
	
	
}
