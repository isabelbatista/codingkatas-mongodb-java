package mongodb.belgaia.kata8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RoboFlyUpdater {

	private static final String FILENAME = "src/main/resources/kata8/roboflyPositions.csv"; 
	
	private static String inputFileName = FILENAME;
	
	public void setInputFileName(String customerInputFileName) {
		inputFileName = customerInputFileName;
	}
	
	public double[] getCoordinatesOfRoboFly(String roboFlyId) {
		
		double[] coordinates = new double[2];
		
		try {
			Map<String, double[]> roboFlyPositions = readCsvFile();

			for (Map.Entry<String, double[]> entry : roboFlyPositions.entrySet()) {
				if(entry.getKey().equals(roboFlyId)) {
					coordinates = entry.getValue();
				}
			}
			
		} catch (IOException e) {
			System.out.println("Reading from csv file failed: " + e.getMessage());
			e.printStackTrace();
		}
		return coordinates;
	}
	
	private Map<String, double[]> readCsvFile() throws IOException {
		
		Map<String, double[]> roboFlyPositions = new HashMap<String, double[]>();
		
		FileReader fileReader = new FileReader(inputFileName);
		BufferedReader br = new BufferedReader(fileReader);
		String line;
		int lineCounter = 0;
		while((line = br.readLine()) != null) {
			System.out.println(line);

			if(lineCounter >= 1) {
				String[] tokens = line.split(",");
				
				double longitude = Double.valueOf(tokens[1]);
				double latitude = Double.valueOf(tokens[2]);
				double[] coordinates = {longitude, latitude};
				
				roboFlyPositions.put(tokens[0], coordinates);
			}
			lineCounter++;
		}
		fileReader.close();
		
		return roboFlyPositions;
	}

}
