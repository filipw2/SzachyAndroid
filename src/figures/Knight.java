package figures;

public class Knight extends Figure {

	public Knight(char colorr) {
		super(colorr);
		// TODO Auto-generated constructor stub
	}
	
	public char getFigure(){return 'N';}
	
	public Boolean isAvailable(int x0, int y0, int x1, int y1, Figure f[][]){
		if(this.sameColor(x1,y1,f))return false;
		if(((x1==x0+1 && (y1==y0+2 || y1==y0-2)) || (x1==x0-1 && (y1==y0+2 || y1==y0-2)) || (x1==x0+2 && (y1==y0+1 || y1==y0-1)) || (x1==x0-2 && (y1==y0+1 || y1==y0-1)))) return true;
		return false;
	}
	
}
