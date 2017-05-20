package trilateration;

public class point {
	 //constant alpha
	 float x;
	 float y;
	 float weight;
	 public point(float x_co, float y_co,float w){
		 x=x_co;
		 y=y_co;
		 weight=w;
	 }
	 public boolean isTrilaterable(){
			
			
			return true;
			
		}

}
