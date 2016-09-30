package figures;

public class Bishop extends Figure {

	public Bishop(char colorr) {
		super(colorr);
		// TODO Auto-generated constructor stub
	}
	
	public char getFigure(){return 'B';}
	
	public Boolean isAvailable(int x0, int y0, int x1, int y1, Figure f[][]){
		if(this.sameColor(x1,y1,f))return false;
		int roz_x, roz_y;
		roz_x=Math.abs(x0-x1);
		roz_y=Math.abs(y0-y1);
		if(roz_x==roz_y){
				if(x0>x1 && y0>y1){
					x0--;y0--;
					for(;x0>x1;x0--){
						if(f[x0][y0]!=null)return false;
						y0--;
					}
				return true;

				}
				if(x0<x1 && y0>y1){
					x0++;y0--;
					for(;x0<x1;x0++){
						if(f[x0][y0]!=null)return false;
						y0--;
					}
					return true;
				}
				if(x0>x1 && y0<y1){
					x0--;y0++;
					for(;x0>x1;x0--){
						if(f[x0][y0]!=null)return false;
						y0++;
					}
					return true;
				}
				if(x0<x1 && y0<y1){
					x0++;y0++;
					for(;x0<x1;x0++){
						if(f[x0][y0]!=null)return false;
						y0++;
					}
					return true;
				}

			}
		return false;
	}
}
