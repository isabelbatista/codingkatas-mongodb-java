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
		String chargingDataFile = "src/test/resources/kata6/charging_data.csv";
		String profilesFile = "src/test/resources/kata6/profiles.csv";
		String robofliesFile = "src/test/resources/kata6/roboflies.csv";
				
		connector.importData2MongoDb(chargingDataFile, "charging");
		connector.importData2MongoDb(profilesFile, "profiles");
		connector.importData2MongoDb(robofliesFile, "roboflies");
	}
	
	private void addDocumentReferences() {
		
		List<DBObject> roboFlies = getAllRoboflies();
		List<DBObject> chargingSets = getAllChargingSets();		
		
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
		
		for(DBObject chargingSet : chargingSets) {
			String chargingSetId = (String) chargingSet.get("_id");
			
			if(chargingSetId.equals("charging_set_1")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_1");
			} else if (chargingSetId.equals("charging_set_2")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_2");
			} else if (chargingSetId.equals("charging_set_3")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_3");
			} else if (chargingSetId.equals("charging_set_4")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_4");
			} else if (chargingSetId.equals("charging_set_5")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_5");
			} else if (chargingSetId.equals("charging_set_6")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_6");
			} else if (chargingSetId.equals("charging_set_7")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_7");
			} else if (chargingSetId.equals("charging_set_8")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_8");
			} else if (chargingSetId.equals("charging_set_9")) {
				connector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_9");
			}
		}
	}
	
	private List<DBObject> getAllRoboflies() {
		return connector.getRoboflies();
	}
	
	private List<DBObject> getAllChargingSets() {
		return connector.getChargingSets();
	}
}
