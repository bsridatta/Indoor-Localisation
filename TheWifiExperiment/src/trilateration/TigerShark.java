package trilateration;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import trilateration.CSV_handler;
import trilateration.Circle;
import trilateration.DCE;

public class TigerShark {
	public static void main(String [] args) throws IOException{
		//get the data
		//while get each point data , sorted distances from router
			//choose the routers
			//form 3 circles with desired routers
			//get beta of 3 routers
			//do DCE (in tiger shark)
				//proper circles obtained
			//now distribute weights to them
				//while getting the weighted mean , in parallel
		CSV_handler csvHandler=new CSV_handler();
		List<Location> Locs=csvHandler.getFullDataSet(1);
		//the complete data required for the procedure is in Locs.. enjoy :)
		int check=1;
		for(Location location: Locs){//for all rows in the excel file / csv / for all positions
			System.out.println("");
			
			System.out.println("results for position "+check++);
			int n = 3;
			Circle cir[]=new Circle[n];
	        List<Circle> C = new ArrayList<>();
	        
			for(int i=0;i<=n-1;i++){
				int index= (n-1)-i;//coz we need in it descending order but the complete list is in increasing order
		
				 cir[index]=new Circle(location.getSortedCenterX(i), location.getSortedCenterY(i), location.getSortedRadius(i));
				
			}
			for(int i=0;i<n;i++){
			C.add(i, cir[i]);
			}
			
			C.get(0).beta=(C.get(0).R/C.get(2).R)*(float)0.01;
			C.get(1).beta=(C.get(1).R/C.get(2).R)*(float)0.01;
			C.get(2).beta=(C.get(2).R/C.get(2).R)*(float)0.01;
		
			 //	* **************************Distance re-estimation**********************************
			DCE dce =new DCE();
			//overEstimation case1			
			if(C.get(0).isInside(C.get(1))||C.get(1).isInside(C.get(2))||C.get(0).isInside(C.get(2))){
				C.get(0).R1=(float) Math.hypot(C.get(0).X - C.get(1).X , C.get(0).Y-C.get(1).Y) -C.get(1).R1;
				C=dce.CircleIncrease(C);
			}
			//under estimation
			else if(!C.get(0).overlaps(C.get(1))||!C.get(0).overlaps(C.get(2))||!C.get(1).overlaps(C.get(2))){
				C=dce.CircleIncrease(C);
			}
			//overEstimation case2					
			else{
				if((C.get(0).areaInter(C.get(1))>C.get(0).delta(C.get(1)) || C.get(0).areaInter(C.get(2))>C.get(0).delta(C.get(2)) || C.get(1).areaInter(C.get(2))>C.get(1).delta(C.get(2)) ) && !(!C.get(0).overlaps(C.get(1))||!C.get(0).overlaps(C.get(2))||!C.get(1).overlaps(C.get(2))) ){
					// this and underestimation is false then overestimation 2 is true
					//as its in else case its shouldv'e already been covered
					//but as the radius is dynamically reduced it needs to be checked again
					while((C.get(0).areaInter(C.get(1))>C.get(0).delta(C.get(1)) || C.get(0).areaInter(C.get(2))>C.get(0).delta(C.get(2)) || C.get(1).areaInter(C.get(2))>C.get(1).delta(C.get(2)) ) && !(!C.get(0).overlaps(C.get(1))||!C.get(0).overlaps(C.get(2))||!C.get(1).overlaps(C.get(2))) ){
						C.get(0).R1=C.get(0).R1 -C.get(0).beta* C.get(0).R;
						//System.out.println("decrease c[0] to "+C.get(0).R1 );
						C.get(1).R1=C.get(1).R1 -C.get(1).beta* C.get(1).R;
						//System.out.println("decrease c[1] to"+C.get(0).R1);
					}
				}
			}
			
			for(Circle x:C ){
				System.out.println("ReEstimated from "+x.R +"--->"+x.R1);
			}
			
		
		//	/* **************************Location estimation*******************************
			
			float  W[] = new float[1000000] ;
			float xWeight=0;
			float yWeight=0;
			float tWeight=0;
			for(int i=0;i<3;i++){
				//for each circle
				for(int k=0;k<C.get(i).R1;k++){
					//for each layer of circle
					W[k]=(float) ((1/C.get(i).R)*(Math.exp(k/C.get(i).R)));//weight for layer points
					//for every point "j" on the varying circumference of radius "k"
					//360 points on each layer
					int t=0;
					for( t=0;t<360;t++){
						float x_co=(float) (C.get(i).X+k*Math.cos(t*3.14/180));
						float y_co=(float) (C.get(i).Y+k*Math.sin(t*3.14/180));
						// point on the varying k
						//check if it lies inside other circles
						if(C.get((i+1)%3).hasPoint(x_co, y_co)||C.get((i+2)%3).hasPoint(x_co, y_co)){
							if(dce.isTrilaterable(x_co, y_co,C)){// checking if the point is trilaterable
								tWeight+=W[k];		//total weight denominator in finding the mean position	
								xWeight+=W[k]*x_co;	//for weighted mean along x
								yWeight+=W[k]*y_co;	//for weighted mean along y	
							}
							else{
							}
						}
						else{
						}
					}
				}
			}
			location.setExperimentalX(xWeight/tWeight);
			location.setExperimentalY(yWeight/tWeight);
			System.out.println("Original   :"+location.getActualX()+","+location.getActualY() );
			System.out.println("Experimental :"+location.getExperimentalX()+","+location.getExperimentalY() );
		}
		float TotalErrorX = 0,TotalErrorY=0;
		int z=1;
		for(Location location: Locs){//for all rows in the excel file / csv / for all positions
			if(z>85){
				continue;
			}
			location.setErrorX(Math.abs(location.getExperimentalX()-location.getActualX()));
			location.setErrorY(Math.abs(location.getExperimentalY()-location.getActualY()));

			TotalErrorX=TotalErrorX+location.getErrorX();
			TotalErrorY=TotalErrorY+location.getErrorY();
		}
		float AvgErrorX=0;
		float AvgErrorY=0;
		AvgErrorX=TotalErrorX/Locs.size();
		AvgErrorY=TotalErrorY/Locs.size();
		
	System.out.println("average error in x is "+AvgErrorX);
	System.out.println("average error in y is "+AvgErrorY);
	CSV_writer csvWriter=new CSV_writer();
	csvWriter.save(Locs,AvgErrorX,AvgErrorY);
	


	
	}
}
