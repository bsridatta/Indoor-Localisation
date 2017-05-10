package trilateration;

public class WALE extends Circle{


	 public WALE(int x, int y, int r) {
		super(x, y, r);
										//dont want you right now !! sorry :(
	}
	 									//factor by which the radius is varied
	final static float beta=(float) 0.1;
										//area required for over estimation rule 2;
	final static float delta=3;
	 
									// C is an array of circles 
									//obtained from available router RSSIs
									//array of circles in decreasing order of radius
	static Circle C[]=new Circle[3];
	
			//*****cyclic  circle increasing function*****
		
		public static void CircleIncrease(){
			//Q is a queue to temporarily save circles for cyclic order
				//say 3 circles
						//obviously can do without queue,just to follow the algorithm
			
			QueueIntf Q= new QueueArray(3);		
				Q.enqueue(0);//circle 0
					Q.enqueue(1);//circle 1
						Q.enqueue(2);//circle 2
				
					int i =0; //counter in while	
			
			while(!Q.isEmpty()){//infinite loop
				
				i=Q.first();	
				if(!C[i].overlaps(C[(i+1)%3])|| !C[i].overlaps(C[(i+2)%3])){
					//if i doesn't intersect with at least 1 of the other 2
					
					C[i].R1=C[i].R1+beta*C[i].R;
						System.out.println("increase c["+i+"]  radius to "+C[i].R1 );
							Q.enqueue(Q.dequeue());//rotating the circles in a cycle							
				}
				else{
					//infinite loop termination point
break;
				}
			}
		}
						//*****THE MAIN FUNCTION******	
		
		public static void main(String [] args){
			
			C[0]=new Circle(100,100,20);
				C[1]=new Circle(200,200,40);
					C[2]=new Circle(150,150,30);
			
					//******Distance re-estimation*******
//overEstimation case1			
			if(C[0].isInside(C[1])||C[1].isInside(C[2])||C[0].isInside(C[2])){
				C[0].R1=(float) Math.hypot(C[0].X - C[1].X , C[0].Y-C[1].Y) -C[1].R1;
					CircleIncrease();
			}
//under estimation
			else if(!C[0].overlaps(C[1])||!C[0].overlaps(C[2])||!C[1].overlaps(C[2])){
					CircleIncrease();
			}

//overEstimation case2					
			else{
				if((C[0].areaInter(C[1])>C[0].delta(C[1]) || C[0].areaInter(C[2])>C[0].delta(C[2]) || C[1].areaInter(C[2])>C[1].delta(C[2]) ) && !(!C[0].overlaps(C[1])||!C[0].overlaps(C[2])||!C[1].overlaps(C[2])) ){
				// this and underestimation is false then overestimation 2 is true
					//as its in else case its shouldv'e already been covered
						//but as the radius is dynamically reduced it needs to be checked again
					
					while((C[0].areaInter(C[1])>C[0].delta(C[1]) || C[0].areaInter(C[2])>C[0].delta(C[2]) || C[1].areaInter(C[2])>C[1].delta(C[2]) ) && !(!C[0].overlaps(C[1])||!C[0].overlaps(C[2])||!C[1].overlaps(C[2])) ){
					
						C[0].R1=C[0].R1 - beta* C[0].R;
							System.out.println("increase c[0]");
						C[1].R1=C[1].R1 - beta* C[1].R;
							System.out.println("increase c[1]");
					}
				}
			}
			
				//*********Location estimation**********
	System.out.println("done dce");
			float  W[] = new float[100] ;
			
			for(int i=0;i<3;i++){
				
				int t=0;
				
				for(int k=0;k<C[i].R1;k++){
					W[k]=(float) ((1/C[i].R)*(Math.exp(k/C[i].R)));
					System.out.println(W[k]);

				//for every point "j" on the varying circumference of radius "k"
					  float x_co=(float) (C[i].X+k*Math.cos(t*3.14/180));
					  	float y_co=(float) (C[i].Y+k*Math.sin(t*3.14/180));
					  		point j= new point();
					  			j.x=x_co;
					  				j.y=y_co;
				// point on the varying k
				//check if it lies inside other circles
					  if(C[(i+1)%3].hasPoint(x_co, y_co)||C[(i+2)%3].hasPoint(x_co, y_co)){
						  if(j.isTrilaterable()){// checking if the point is trilaterable
							  j.weight=j.weight+W[k];
						  }
						  else{
							  j.weight=Math.abs(j.weight-W[k]);
						  }
					  }
					  else{
						  j.weight=W[k];
					  }
				}
				for(float s:W){
					System.out.println(s);
				}
				
			}
		}		
		
}	

	
