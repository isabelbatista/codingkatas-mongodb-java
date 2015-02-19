package mongodb.belgaia.kata7;

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

		addDocumentReferences();
	}
	
	private void importDataFiles() {
		String profilesFile = "src/test/resources/kata7/profiles.csv";
		String robofliesFile = "src/test/resources/kata7/roboflies.csv";
		String measurementsFile = "src/test/resources/kata7/measurements.csv";
				
		connector.importData2MongoDb(profilesFile, "profiles");
		connector.importData2MongoDb(robofliesFile, "roboflies");
		connector.importData2MongoDb(measurementsFile, "measurements");
	}
	
	private void addDocumentReferences() {
		
		List<DBObject> roboFlies = getAllRoboflies();
		
		for(DBObject roboFly : roboFlies) {
			String roboFlyId = (String) roboFly.get("_id");
			if(roboFlyId.equals("RoboFly_ID_1")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_2")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_3")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_4")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.DRAGONFLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_5")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.DRAGONFLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_6")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.MOSKITO.name);
			} else if (roboFlyId.equals("RoboFly_ID_7")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.MOSKITO.name);
			} else if (roboFlyId.equals("RoboFly_ID_8")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.COPEPOD.name);
			} else if (roboFlyId.equals("RoboFly_ID_9")) {
				connector.addDocReferenceForProfiles(roboFlyId, RoboFlyType.COPEPOD.name);
			}
		}
		
		List<DBObject> measurements = getAllMeasurements();

		for(DBObject measurement : measurements) {
			String measurementId = (String) measurement.get("_id");
			
			if(measurementId.equals("measurement_average_1")) {
				connector.addDocReferenceForMeasurement2RoboFly(measurementId, "RoboFly_ID_1");
			} else if(measurementId.equals("measurement_average_2")) {
				connector.addDocReferenceForMeasurement2RoboFly(measurementId, "RoboFly_ID_2");
			} else if(measurementId.equals("measurement_average_3")) {
				connector.addDocReferenceForMeasurement2RoboFly(measurementId, "RoboFly_ID_3");
			} 
		}
	}
	
	private List<DBObject> getAllRoboflies() {
		return connector.getRoboflies();
	}
	
	private List<DBObject> getAllMeasurements() {
		return connector.getMeasurements();
	}
}
