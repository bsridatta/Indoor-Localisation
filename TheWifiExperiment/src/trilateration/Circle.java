package trilateration;

public class Circle {

	float R;	//corresponding radii
	float R1;	//corresponding MODIFIED radii
	float X;	//corresponding x-co of center
	float Y;	//corresponding y-co of center

	
	public boolean overlaps(Circle c){//input circle numbers

		//Assuming filled circle intersection 
		//(i.e : One circle inside another is an intersection).
		//x,y,r,r1 = Center and radius and modified radius of this.circle.
		//c.x,c.y,c.r,c.r1 = Center and radius and modified radius of passed.circle.
		
		boolean intersects = Math.hypot(X-c.X, Y-c.Y) < (R1 + c.R);
		//returns if it intersects or no...ii.e true is intersects orelse not
		//hence true is overlaps 
		return intersects;
	}
	
	public boolean isInside(Circle c){//input circle numbers

		if(Math.hypot(X-c.X, Y-c.Y) <= Math.abs(R - c.R)){
				//inside one another
			return true;
		}
		return false;
	
	
	}
	
	public boolean hasPoint(float x,float y){
		float d =(float) Math.hypot(X - x , Y-y);// distance from the center		

		if(d<R1){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	
	
	public float areaInter(Circle c){
		//assuming present circle is bigger
		float Rad =R1;//present circle radius
		float rad =c.R1;//present circle radius
		float d =(float) Math.hypot(X - c.X , Y-c.Y) ;		//distance btw circle centers

		if(Rad < rad){
		    // swap
		    rad = R1;
		    Rad = c.R1;
		}
		float part1 = (float) (rad*rad*Math.acos((d*d + rad*rad - Rad*Rad)/(2*d*rad)));
		float part2 = (float) (Rad*Rad*Math.acos((d*d + Rad*Rad - rad*rad)/(2*d*Rad)));
		float part3 = (float) (0.5*Math.sqrt((-d+rad+Rad)*(d+rad-Rad)*(d-rad+Rad)*(d+rad+Rad)));

		float intersectionArea = part1 + part2 - part3;
		return intersectionArea;
	}
}
