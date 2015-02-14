package mongodb.belgaia.kata10;

import java.util.List;

import com.mongodb.DBObject;

public class TestPreparation {

	private static final String DATABASE_NAME = "kataTest";
	private static final String ROBOFLIES_FILENAME = "src/test/resources/kata10/roboflies.csv";
	private static final String PROFILES_FILENAME = "src/test/resources/kata10/profiles.csv";

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
		connector.importData2MongoDb(ROBOFLIES_FILENAME, "roboflies");
		connector.importData2MongoDb(PROFILES_FILENAME, "profiles");
	}

	private void addDocumentReferences() {

		List<DBObject> roboFlies = connector.getRoboflyDocuments();

		for (DBObject roboFly : roboFlies) {
			String roboFlyId = (String) roboFly.get("_id");
			if (roboFlyId.equals("RoboFly_ID_1")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_2")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_3")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_4")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.DRAGONFLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_5")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.DRAGONFLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_6")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.MOSKITO.name);
			} else if (roboFlyId.equals("RoboFly_ID_7")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.MOSKITO.name);
			} else if (roboFlyId.equals("RoboFly_ID_8")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.COPEPOD.name);
			} else if (roboFlyId.equals("RoboFly_ID_9")) {
				connector.addDocReferenceForProfiles(roboFlyId,
						RoboFly.Type.COPEPOD.name);
			}
		}
	}
}
