public class Pion{
	private int coordX;
	private int coordY;
	private int TypePion;
	
	
	public Pion(int coordX, int coordY, int TypePion){
		this.coordX = coordX;
		this.coordY = coordY;
		this.TypePion = TypePion;		
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
		
		
	}
	
	public void Mange(Pion cible){
		this.coordX = cible.coordX;
		this.coordY = cible.coordY;
		cible.TypePion = 0;
		
	}
	
	
}
