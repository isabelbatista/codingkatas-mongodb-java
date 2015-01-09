package mongodb.belgaia.kata8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RoboFlyUpdater {

	private static final String FILENAME = "src/main/resources/kata8/roboflyPositions.csv"; 	
	private static String inputFileName = FILENAME;
	private static Map<String, double[]> roboFlyPositions;
	
	MongoConnector connector;
	
	static {
		try {
			roboFlyPositions = readCsvFile();
		} catch (IOException e) {
			System.out.println("Reading from csv file failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public RoboFlyUpdater() {
		connector = new MongoConnector();
	}
	
	public RoboFlyUpdater(String databaseName) {
		connector = new MongoConnector(databaseName);
	}
	
	public void updateRoboFliesWithCoordinatesAndSetGeoIndex() {
		updateRoboFliesWithCoordinates();
		addGeoIndexForRoboflies();
	}
	
	private void addGeoIndexForRoboflies() {
		connector.createGeoIndexForRoboflies();
	}
	
	public void setInputFileName(String customerInputFileName) {
		inputFileName = customerInputFileName;
	}
	
	public void updateRoboFlyWithCoordinates(String roboFlyId) {
		connector.addGeoIndexToRoboFly(roboFlyId, getCoordinatesOfRoboFly(roboFlyId));
	}
	
	public void updateRoboFliesWithCoordinates() {
		
		for(Map.Entry<String, double[]> entry : roboFlyPositions.entrySet()) {
			connector.addGeoIndexToRoboFly(entry.getKey(), entry.getValue());
		}
	}

	public double[] getCoordinatesOfRoboFly(String roboFlyId) {
		
		double[] coordinates = new double[2];
		
		for (Map.Entry<String, double[]> entry : roboFlyPositions.entrySet()) {
			if(entry.getKey().equals(roboFlyId)) {
				coordinates = entry.getValue();
			}
		}
		return coordinates;
	}
	
	public Map<String, double[]> getCoordinateMap() {
		return roboFlyPositions;
	}
	
	private static Map<String, double[]> readCsvFile() throws IOException {
		
		Map<String, double[]> roboFlyPositions = new HashMap<String, double[]>();
		
		FileReader fileReader = new FileReader(inputFileName);
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
