package mongodb.belgaia.kata6;

import java.util.List;

import com.mongodb.DBObject;

class DatabasePreparation {
	
	private MongoConnector mongoConnector;
	
	public DatabasePreparation(MongoConnector mongoConnector) {
		this.mongoConnector = mongoConnector;
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
		
		String importDataFile = "src/main/resources/kata6/charging_data.csv";	
		mongoConnector.importData2MongoDb(importDataFile, "charging");
	}
	
	private void addDocumentReferences() {
		
		List<DBObject> chargingSets = getAllChargingSets();		
		
		for(DBObject chargingSet : chargingSets) {
			String chargingSetId = (String) chargingSet.get("_id");
			
			if(chargingSetId.equals("charging_set_1")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_1");
			} else if (chargingSetId.equals("charging_set_2")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_2");
			} else if (chargingSetId.equals("charging_set_3")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_3");
			} else if (chargingSetId.equals("charging_set_4")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_4");
			} else if (chargingSetId.equals("charging_set_5")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_5");
			} else if (chargingSetId.equals("charging_set_6")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_6");
			} else if (chargingSetId.equals("charging_set_7")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_7");
			} else if (chargingSetId.equals("charging_set_8")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_8");
			} else if (chargingSetId.equals("charging_set_9")) {
				mongoConnector.addDocReferenceForCharges((String) chargingSet.get("_id"), "RoboFly_ID_9");
			}
		}
	}
	
	private List<DBObject> getAllChargingSets() {
		return mongoConnector.getChargingSets();
	}
}
