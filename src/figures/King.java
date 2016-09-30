package figures;

public class King extends Figure {
	
	public King(char colorr) {
		super(colorr);
		// TODO Auto-generated constructor stub
	}
	
	public char getFigure(){return 'K';}
	
	public Boolean isAvailable(int x0, int y0, int x1, int y1, Figure f[][]){
		if(this.sameColor(x1,y1,f))return false;
		if(Math.abs(x1-x0)<2 && Math.abs(y1-y0)<2)return true;
		return false;
	}	
}
