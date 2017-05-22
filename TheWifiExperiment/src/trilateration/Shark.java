package trilateration;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

public class Shark extends Circle{
	
	public Shark(int x, int y, int r) {
		 super(x, y, r);				//dont want you right now !! sorry :(
	}
	 
	 								//factor by which the radius is varied
	static	float [] beta= new float [3];
									// C is an array of circles 
									//obtained from available router RSSIs
									//array of circles in decreasing order of radius
	static Circle C[]=new Circle[3];
		/*************************************************************************
					is in a triangle function
 		*************************************************************************/
	
	public static boolean isTrilaterable(float x, float y){

		//formula to calculate area of triangle : double ABC = Math.abs (C[0].X * (C[1].Y - C[2].Y) + C[1].X * (C[2].Y - C[0].Y) + C[2].X * (C[0].Y - C[1].Y)) / 2;
		// no need to divide by 2.0 here, since it is not necessary in the equation
		double ABC = Math.abs (C[0].X * (C[1].Y - C[2].Y) + C[1].X * (C[2].Y - C[0].Y) + C[2].X * (C[0].Y - C[1].Y)) ;
		double ABP = Math.abs (C[0].X * (C[1].Y - y) + C[1].X * (y - C[0].Y) + x * (C[0].Y - C[1].Y));
		double APC = Math.abs (C[0].X * (y - C[2].Y) + x * (C[2].Y - C[0].Y) + C[2].X * (C[0].Y - y));
		double PBC = Math.abs (x * (C[1].Y - C[2].Y) + C[1].X * (C[2].Y- y) + C[2].X * (y - C[1].Y));

		boolean isInTriangle = ABP + APC + PBC == ABC;
		
		return isInTriangle;
		
	}
		/*************************************************************************
					cyclic  circle increasing function
 		*************************************************************************/

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
			C[i].R1=C[i].R1+beta[i]*C[i].R;
			System.out.println("increase c["+i+"]  radius to "+C[i].R1 );
			Q.enqueue(Q.dequeue());//rotating the circles in a cycle							
			}
	
			else{
				//infinite loop termination point
				break;
			}
		}
	}

	/*************************************************************************
								THE MAIN FUNCTION
	 * @throws IOException 
	*************************************************************************/
	
	public static void main(String [] args) throws IOException{
		
		//get the 3 best circles in decreasing order of radius
		//pass array of their router numbers and distances
		
		float actualX=0;
		float actualY=0;
		float d1=0,d2=0,d3=0,d4=0,d5=0,d6=0,d7=0,d8=0;
		/*************************************************************************
							reading from csv
 		*************************************************************************/
		
		// open file input stream

				BufferedReader reader = new BufferedReader(new FileReader(
						"resources/Data.csv"));

				// read file line by line
				String line = null;
				Scanner scanner = null;
				int index = 0;
				 reader.readLine();
				 reader.readLine();
				

				 line = reader.readLine();
					
					scanner = new Scanner(line);
					scanner.useDelimiter(",");
					while (scanner.hasNext()) {
						String data = scanner.next();
																			//System.out.println(data);
						if (index == 0)
							actualX=Float.parseFloat(data);
						else if (index == 1)
							actualY=Float.parseFloat(data);
						else if (index == 2)
							d1=Float.parseFloat(data);
						else if (index == 3)
							d2=Float.parseFloat(data);
						else if (index == 4)
							d3=Float.parseFloat(data);
						else if (index == 5)
							d4=Float.parseFloat(data);
						else if (index == 6)
							d5=Float.parseFloat(data);
						else if (index == 7)
							d6=Float.parseFloat(data);
						else if (index == 8)
							d7=Float.parseFloat(data);
						else if (index == 9)
							d8=Float.parseFloat(data);
						else{}
										//System.out.println("invalid data::" + data);
						index++;
					}
					index = 0;
				
				//close reader
				reader.close();
				System.out.println("actualX--->" + actualX);
				System.out.println("actualY--->" + actualY);

				//sort the distances
				 Map<String, Float> map = new HashMap<String, Float>();
				    map.put("1", d1);
				    map.put("2", d2);
				    map.put("3", d3);
				    map.put("4", d4);
				    map.put("5", d5);
				    map.put("6", d6);
				    map.put("7", d7);
				    map.put("8", d8);
				    
				    Set<Entry<String, Float>> set = map.entrySet();
				    ArrayList<Entry<String, Float>> list = new ArrayList<Entry<String, Float>>(set);
				    Collections.sort( list, new Comparator<Map.Entry<String, Float>>()
				    {
				        public int compare( Map.Entry<String, Float> o1, Map.Entry<String, Float> o2 )
				        {
				            return (o1.getValue()).compareTo( o2.getValue() );//Ascending order
				            //return (o2.getValue()).compareTo( o1.getValue() );//Descending order
				        }
				    } );
				    for(Map.Entry<String, Float> entry:list){
				        System.out.println("router"+entry.getKey()+" -> "+entry.getValue());
				    }


			

		/*************************************************************************
					retrieving info from config file
 		*************************************************************************/
			        String [] routerNumber={
			        		list.get(3).getKey(),
			        		list.get(2).getKey(),
			        		list.get(1).getKey()
			        };
					float [] radius = {
			        		list.get(3).getValue(),
			        		list.get(2).getValue(),
			        		list.get(1).getValue()
			        };
			        
		//retrieving info from config file
		try {
		    Properties props = new Properties();
		    InputStream configFile=WALE.class.getClassLoader().getResourceAsStream("config.properties");		 
		    props.load(configFile);

		    String host = props.getProperty("host");
		    									//System.out.print("Host name is: " + host+"\n");

		    //for each router
		    for(int i=0;i<3;i++){
		    String input="router"+routerNumber[i];
		    String routerLocation = props.getProperty(input);
		    String [] routerCenter=routerLocation.split(",");
		    System.out.print("router"+routerNumber[i]+" location in x:" + routerCenter[0]+" in y:" + routerCenter[1]+"\n");
			C[i]=new Circle(Integer.valueOf(routerCenter[0]),Integer.valueOf(routerCenter[1]),radius[i]);
			//one circle created
		    }
			
			
			
			
		    configFile.close();
		} catch (FileNotFoundException ex) {
		    							// file does not exist
		} catch (IOException ex) {
										// I/O error
		}
		
		
		
			
		
		
		//beta estimation
			//as we know c[0].R > c[1].R
			//C[0].R:C[1].R:C[2].R :: C[0].R/C[2].R:C[1].R/C[2].R:1
		
		beta[0]=C[0].R/C[2].R;
		beta[1]=C[1].R/C[2].R;
		beta[2]=1;
				
	/*************************************************************************
							Distance re-estimation
	*************************************************************************/

		
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
					
					C[0].R1=C[0].R1 - beta[0]* C[0].R;
					System.out.println("increase c[0]");
					C[1].R1=C[1].R1 - beta[1]* C[1].R;
					System.out.println("increase c[1]");
				}
			}
		}

		System.out.println("done dce");
		
		
		/*************************************************************************
								Location estimation
		*************************************************************************/
		System.out.println("****weight assignment initialised****\n");

		float  W[] = new float[1000000] ;

		float xWeight=0;
		float yWeight=0;
		float tWeight=0;

		for(int i=0;i<3;i++){
			
			System.out.println("****circle "+i+"****\n");
		
			//for each circle
			for(int k=0;k<C[i].R1;k++){
				
				//System.out.println("****layer "+k+"****\n");
				
				//for each layer of circle
				W[k]=(float) ((1/C[i].R)*(Math.exp(k/C[i].R)));//weight for layer points
				System.out.print(W[k]);
				
				//for every point "j" on the varying circumference of radius "k"
				//360 points on each layer
				
																																//point  j[] =new point[360]; 
				int t=0;
				for( t=0;t<360;t++){
					float x_co=(float) (C[i].X+k*Math.cos(t*3.14/180));
					float y_co=(float) (C[i].Y+k*Math.sin(t*3.14/180));
			
					//initial weight is equal to layer's weight
					// point on the varying k
																																//j[t].x=x_co;
																																//j[t].y=y_co;
					//tWeight+=W[k];		//total weight denominator in finding the mean position	
					//xWeight+=W[k]*x_co;	//for weighted mean along x
					//yWeight+=W[k]*y_co;	//for weighted mean along y
					
					
					//check if it lies inside other circles
					if(C[(i+1)%3].hasPoint(x_co, y_co)||C[(i+2)%3].hasPoint(x_co, y_co)){
						if(isTrilaterable(x_co, y_co)){// checking if the point is trilaterable
																																//if(j[t].isTrilaterable()){// checking if the point is trilaterable
																																//j[t].weight=j[t].weight+W[k];
							tWeight+=W[k];		//total weight denominator in finding the mean position	
							xWeight+=W[k]*x_co;	//for weighted mean along x
							yWeight+=W[k]*y_co;	//for weighted mean along y	
						}
						else{
								//j[t].weight=Math.abs(j[t].weight-W[k]);
//didnt understand the reason for taking absolute value
							//tWeight-=W[k];		//total weight denominator in finding the mean position	
							//xWeight-=W[k]*x_co;	//for weighted mean along x
							//yWeight-=W[k]*y_co;	//for weighted mean along y	
						
						}
					}
					else{
						//j[t].weight=W[k];
					}
				}
				//print all the weights of all points on all layers
				//for(point p:j){
					//System.out.println(p.weight);
				//}
				
				
			}
			
			//for(float s:W){
				//System.out.println(s);
			//}
			System.out.println("\n");

		}
		
		
		float finalx=xWeight/tWeight;
		float finaly=yWeight/tWeight;
		
		System.out.println("\n");

		System.out.println("*******************************************\n");
		System.out.println(finalx+" is the mobile position in x\n");
		System.out.println(finaly+" is the mobile position in y\n");
		System.out.println("*******************************************\n");
		
	}		
		
}	

	
