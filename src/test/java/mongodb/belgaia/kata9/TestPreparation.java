package mongodb.belgaia.kata9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.DBObject;

class TestPreparation {
	
	private static final String DATABASE_NAME = "kataTest";
	private static Map<String, double[]> roboFlyPositions;

	
	private MongoConnector connector;
	private CSVFileReader fileReader;
	
	public TestPreparation(String databaseName) {
		connector = new MongoConnector(DATABASE_NAME);
		fileReader = new CSVFileReader();
	}
	
	public void prepareDatabase() {
		
		importDataFiles();
		
		System.out.println("--- Waiting for some seconds ---");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		
		addDocumentReferences();
		
		roboFlyPositions = fileReader.getContentOfRoboFliesPositionsCsv();
		updateRoboFliesWithCoordinates();
	}
	
	private void importDataFiles() {
		String robofliesFile = "src/test/resources/kata9/roboflies.csv";
		String profilesFile = "src/test/resources/kata9/profiles.csv";
		
		connector.importData2MongoDb(robofliesFile, "roboflies");
		connector.importData2MongoDb(profilesFile, "profiles");		
	}
	
	private void addDocumentReferences() {
		
		List<DBObject> roboFlies = connector.getRoboflyDocuments();
		
		for(DBObject roboFly : roboFlies) {
			String roboFlyId = (String) roboFly.get("_id");
			if(roboFlyId.equals("RoboFly_ID_1")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_2")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_3")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_4")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.DRAGONFLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_5")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.DRAGONFLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_6")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.MOSKITO.name);
			} else if (roboFlyId.equals("RoboFly_ID_7")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.MOSKITO.name);
			} else if (roboFlyId.equals("RoboFly_ID_8")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.COPEPOD.name);
			} else if (roboFlyId.equals("RoboFly_ID_9")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFly.Type.COPEPOD.name);
			}
		}
	}
	
	public void updateRoboFliesWithCoordinates() {
		
		for(Map.Entry<String, double[]> entry : roboFlyPositions.entrySet()) {
			connector.addGeoIndexToRoboFly(entry.getKey(), entry.getValue());
		}
	}
	
//	private static Map<String, double[]> readCsvFile() throws IOException {
//		
//		Map<String, double[]> roboFlyPositions = new HashMap<String, double[]>();
//		
//		FileReader fileReader = new FileReader(inputFileName);
//		BufferedReader br = new BufferedReader(fileReader);
//		String line;
//		int lineCounter = 0;
//		while((line = br.readLine()) != null) {
//
//			if(lineCounter >= 1) {
//				String[] tokens = line.split(",");
//				
//				double longitude = Double.parseDouble(tokens[1]);
//				double latitude = Double.parseDouble(tokens[2]);
//				double[] coordinates = {longitude, latitude};
//				
//				roboFlyPositions.put(tokens[0], coordinates);
//			}
//			lineCounter++;
//		}
//		fileReader.close();
//		
//		return roboFlyPositions;
//	}
}
