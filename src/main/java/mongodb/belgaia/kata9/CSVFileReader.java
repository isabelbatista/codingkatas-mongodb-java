package mongodb.belgaia.kata9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class CSVFileReader {
	
	private static final String ROBOFLY_POSITION_FILENAME = "src/test/resources/kata9/roboflyPositions.csv"; 	
	private static final String BUGROUTE_FILENAME = "src/test/resources/kata9/bugroute.csv"; 		
	
	public Map<String, double[]> getContentOfRoboFliesPositionsCsv() {
		
		Map<String, double[]> content = null;
		try {
			content = getContentOfCsv(ROBOFLY_POSITION_FILENAME);
		} catch (IOException e) {
			System.out.println("Could not load data from roboflies positions csv: " + e.getMessage());
		}
		return content;
	}
	
	public Map<String, double[]> getContentOfBugrouteCsv() {
		
		Map<String, double[]> content = null;
		try {
			content = getContentOfCsv(BUGROUTE_FILENAME);
		} catch (IOException e) {
			System.out.println("Could not load data from bugroute csv: " + e.getMessage());
		}
		return content;
	}
	
	private static Map<String, double[]> getContentOfCsv(String filename) throws IOException {
		
		Map<String, double[]> roboFlyPositions = new HashMap<String, double[]>();
		
		FileReader fileReader = new FileReader(filename);
		BufferedReader br = new BufferedReader(fileReader);
		String line;
		int lineCounter = 0;
		while((line = br.readLine()) != null) {

			if(lineCounter >= 1) {
				String[] tokens = line.split(",");
				
				double longitude = Double.parseDouble(tokens[1]);
				double latitude = Double.parseDouble(tokens[2]);
				double[] coordinates = {longitude, latitude};
				
				roboFlyPositions.put(tokens[0], coordinates);
			}
			lineCounter++;
		}
		fileReader.close();
		
		return roboFlyPositions;
	}

}
