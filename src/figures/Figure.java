package figures;

public abstract class Figure{
	
	public Boolean isAvailable(int x0, int y0, int x1, int y1, Figure f[][]){
		return null;
		
	};
	private char color;
	
	public Boolean sameColor(int x1, int y1, Figure f[][]){
		if(f[x1][y1]!=null && this.getColor()==f[x1][y1].getColor())return true;
		else return false;
	}
	
	public Figure (char colorr){color=colorr;}
	
	public char getFigure(){return 0;}
	
	public char getColor(){return color;}
	
	/*public Boolean isAllowed(int x0, int y0, int x1, int y1, Figure f[][]){
		if(f[y1][x1] == null || color!=f[y1][x1].getColor()){
			return isAvailable(x0, y0, x1, y1, f);
		}
		else
			return false;
	};*/
}
