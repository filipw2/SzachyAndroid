package figures;

public class Rook extends Figure {
	
	public Rook(char colorr) {
		super(colorr);
		// TODO Auto-generated constructor stub
	}
	
	public char getFigure(){return 'R';}
	
	public Boolean isAvailable(int x0, int y0, int x1, int y1, Figure f[][]){
		
		int roz_x, roz_y;
		roz_x=Math.abs(x0-x1);
		roz_y=Math.abs(y0-y1);
		
		if(this.sameColor(x1,y1,f))return false;
		
			if(roz_x==0){
				if(y0>y1){y0--;
					for(;y0>y1;y0--){
						if(f[x0][y0]!=null)return false;
					}
					return true;
				}
				if(y0<y1){y0++;
					for(;y0<y1;y0++){
						if(f[x0][y0]!=null){System.out.println("asd");return false;}
					}
					return true;
				}

			}
			if(roz_y==0){
				if(x0>x1){x0--;
					for(;x0>x1;x0--){
						if(f[x0][y1]!=null)return false;
					}
					return true;
				}
				if(x0<x1){x0++;
					for(;x0<x1;x0++){
						if(f[x0][y1]!=null)return false;
					}
					return true;
				}

			}
			
			return false;
	}
}
