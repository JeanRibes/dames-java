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
	
	
}
