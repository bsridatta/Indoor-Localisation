package trilateration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.opencsv.CSVReader;
import trilateration.Location;



public class CSV_handler {

	

	public List<Location>  getFullDataSet(int n) throws IOException{

		//last parameter to skip no of lines as first row of csv is variable names..so 1 is the first point data
		CSVReader  reader = new CSVReader (new FileReader("resources/Data.csv"), ',', '"', 1);
		
		List<Location> Locs = new ArrayList<Location>();
		List<String[]> records = reader.readAll();
		Iterator<String[]> iterator = records.iterator();
		while (iterator.hasNext() ) {
			String[] record = iterator.next();
			Location loc = new Location();
			loc.setActualX(Float.parseFloat(record[0]));
			loc.setActualY(Float.parseFloat(record[1]));
			loc.setD1(Float.parseFloat(record[2]));
			loc.setD2(Float.parseFloat(record[3]));
			loc.setD3(Float.parseFloat(record[4]));
			loc.setD4(Float.parseFloat(record[5]));
			loc.setD5(Float.parseFloat(record[6]));
			loc.setD6(Float.parseFloat(record[7]));
			loc.setD7(Float.parseFloat(record[8]));
			loc.setD8(Float.parseFloat(record[9]));
			
			Locs.add(getDesiredRouters(loc));

		}
		//reader.close();
		//returns the whole data from csv saved to arraylist with the best router readings 
		return Locs;
		
		
	}

	public Location getDesiredRouters(Location loc) {
		// TODO Auto-generated method stub

		//sort the distances
		 Map<String, Float> map = new HashMap<String, Float>();
		    map.put("1", loc.getD1());
		    map.put("2", loc.getD2());
		    map.put("3", loc.getD3());
		    map.put("4", loc.getD4());
		    map.put("5", loc.getD5());
		    map.put("6", loc.getD6());
		    map.put("7", loc.getD7());
		    map.put("8", loc.getD8());
		    
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
		    int count=0;//counter
		    for(Map.Entry<String, Float> entry:list){
		       // System.out.println("router"+entry.getKey()+" -> "+entry.getValue());
		        //the sorted entires are called here hence use counter to rearrange the d1,d2,...
		    	if(entry.getValue()>=1){
		    		loc.setSortedRouterNumbers(entry.getKey(), count);
		    		loc.setSortedRadius(entry.getValue(), count);
		    		count++;
		    	}
		    }
		    //till here loc  is just as it was passed just with the best results added to best router and radius arrays
		
		   //loc now has the sorted routers hence get the sorted centers as well
		    
	
		    return 	getSortedCenters(loc);
	}
	
	
	public Location getSortedCenters(Location loc) {
		//retrieving info from configuration file
		try {
			Properties props = new Properties();
			InputStream configFile=CSV_handler.class.getClassLoader().getResourceAsStream("config.properties");		 
			props.load(configFile);
			//for each router			
			for(int i=0;i<8;i++){
				String input="router"+loc.getSortedRouterNumbers(i);
				String routerCenters = props.getProperty(input);
				//System.out.println("*************************");
				//System.out.println(routerCenters);
				
				if(routerCenters==null){
					
					loc.setSortedCenterX(0, i);
					loc.setSortedCenterY(0, i);	
				}
				else{
				String [] routerCenter=routerCenters.split(",");
				//System.out.print("router"+routerNumber[i]+" location in x:" + routerCenter[0]+" in y:" + routerCenter[1]+"\n");
				loc.setSortedCenterX(Float.parseFloat(routerCenter[0]), i);
				loc.setSortedCenterY(Float.parseFloat(routerCenter[1]), i);	
				
				}
			}
			configFile.close();
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}			
		return loc;
	}
	
	//error possible where the sorted array may not be full because of 0 values...
	
	
}








