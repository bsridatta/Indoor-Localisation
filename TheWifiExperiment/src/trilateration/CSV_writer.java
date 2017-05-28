package trilateration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class CSV_writer {
	
	public List<String[]> toStringArray(List<Location> Locs) {
		List<String[]> records = new ArrayList<String[]>();
		// adding header record
		records.add(new String[] { "X", "Y", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8","actualX","actualY","experimentalX","experimentalY","errorX","errorY" });
		Iterator<Location> it = Locs.iterator();
		while (it.hasNext()) {
			Location loc = it.next();
			records.add(new String[] { String.valueOf(loc.getActualX()),String.valueOf(loc.getActualY()),String.valueOf(loc.getD1()),String.valueOf(loc.getD2()),String.valueOf(loc.getD3()),String.valueOf(loc.getD4()),String.valueOf(loc.getD5()),String.valueOf(loc.getD6()),String.valueOf(loc.getD7()),String.valueOf(loc.getD8()),String.valueOf(loc.getActualX()),String.valueOf(loc.getActualY()),String.valueOf(loc.getExperimentalX()),String.valueOf(loc.getExperimentalY()),String.valueOf(loc.getErrorX()),String.valueOf(loc.getErrorY())});
		}
		return records;
	}
	
	public void save(List<Location> Locs,float avgErrX,float avgErrY) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter("Resources/results.csv"), ',');
	     // feed in your array (or convert your data to an array)
		List<String[]> records = new ArrayList<String[]>();
		records=toStringArray(Locs);
		for(String[]record:records){
		     writer.writeNext(record);
		}
		String[] skip={"\n"};
		writer.writeNext(skip);
		String[] average={"Average ","error in ","x and y",String.valueOf(avgErrX),String.valueOf(avgErrY)};
		writer.writeNext(average);
		//appending the final average error
		
	
		 writer.close();
	}
}
