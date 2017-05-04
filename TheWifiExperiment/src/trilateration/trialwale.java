package trilateration;

public class trialwale {

	// C is an array of circles 
	//R is an array with radii as their values
	//obtained from available router RSSIs
	int C[]={0,1,2};	//circles numbered 0,1,2
	float R[]={10,11,12};	//corresponding radii
	float R1[]={10,11,12};	//corresponding MODIFIED radii
	
	float X[]={0,3,9};	//corresponding x-co of center
	float Y[]={0,3,9};	//corresponding y-co of center
	
	 final float alpha=3;
	 final float beta=3;
	 
//***********  circle overlapping check function  *************

	
	public boolean overlaps(int a,int b){//input circle numbers

		//Assuming filled circle intersection 
		//(i.e : One circle inside another is an intersection).
		//x0,y0,r0 = Center and radius of circle 0.
		//x1,y1,r1 = Center and radius of circle 1.
		float x0=X[a];
			float x1=X[b];
				float y0=Y[a];
				float y1=Y[b];
			float r0=R[a];
		float r1=R[b];
		
		boolean intersects = Math.hypot(x0-x1, y0-y1) <= (r0 + r1);
		//returns if it intersects or no...ii.e true is intersects orelse not
		//hence true is overlaps 
		// false is not even touching
		return intersects;
	}

//*********cyclic  circle increasing function****************
	
	public void CircleIncrease(){
		//Q is a queue to temporarily save circles for cyclic order
				//say 3 circles
				QueueIntf Q= new QueueArray(3);
				Q.enqueue(C[0]);//circle 0
				Q.enqueue(C[1]);//circle 1
				Q.enqueue(C[2]);//circle 2
		int i =0; //counter in while	
		while(!Q.isEmpty()){//infinite loop
			
			i=Q.first();
			if(!overlaps(i,(i+1) % 3) || !overlaps(i,(i+2) % 3)){
				//if i doesnt intersect with atleast 1 of the other 2
				R1[i]=R1[i]+beta*R[i];
				Q.enqueue(Q.dequeue());
				//rotating the circles in a cycle
			}
			else{
				continue;
				//infinite loop termination point
			}
			
		}
	}
	

	
}
