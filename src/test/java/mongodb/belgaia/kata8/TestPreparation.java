package mongodb.belgaia.kata8;

import java.util.List;

import com.mongodb.DBObject;


class TestPreparation {
	
	private static final String DATABASE_NAME = "kataTest";
	private MongoConnector connector;
	
	public TestPreparation(String databaseName) {
		connector = new MongoConnector(DATABASE_NAME);
	}
	
	public void prepareDatabase() {
		
		importDataFiles();
		
		System.out.println("--- Waiting for some seconds ---");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		addDocumentReferencesForRoboFlyStations();
	}
	
	private void importDataFiles() {
		String roboFlyStationsFile = "src/test/resources/kata8/roboflyStations.csv";
		String bugrouteFile = "src/test/resources/kata8/bugroute.csv";
		String robofliesFile = "src/test/resources/kata8/roboflies.csv";
		
		connector.importData2MongoDb(robofliesFile, "roboflies");
		connector.importData2MongoDb(roboFlyStationsFile, "roboflystations");
		connector.importData2MongoDb(bugrouteFile, "bugroute");
		
	}
	
	private void addDocumentReferencesForRoboFlyStations() {
		
		List<DBObject> roboFlyStationLocations = connector.getAllRoboFlyStations();
		for(DBObject roboFlyStation : roboFlyStationLocations) {
			String roboFlyStationId = (String) roboFlyStation.get("_id");
			
			String[] idTokens = roboFlyStationId.split("_");
			connector.addDocReferenceForRoboFly2Station(roboFlyStationId, "RoboFly_ID_" + idTokens[2]);
		}
	}

}
