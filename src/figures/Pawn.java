package figures;

public class Pawn extends Figure{

	public Pawn(char colorr) {
		super(colorr);
		// TODO Auto-generated constructor stub
	}
	
	public char getFigure(){return 'P';}
	
	public Boolean isAvailable(int x0, int y0, int x1, int y1, Figure f[][]){
		if(this.sameColor(x1,y1,f))return false;
		int g=-1;
		if(this.getColor()=='W') g=1;
		if(x1==x0 && y1==(y0+g)) if(f[x1][y1]==null)return true;
			  if((x1==x0 && y1==(y0+2*g)) && ((y0==1 && g==1) || (y0==6 && g==-1))) 
				 if(f[x1][y1-g]==null && f[x1][y1]==null)return true;
			  if(( x0-1==x1 || x0+1==x1) && Math.abs(y0-y1)==1)if(f[x1][y1]!=null)return true;
		return false;
	}

}
