package mongodb.belgaia.kata5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

class TestPreparation {
	
	private static final String DATABASE_NAME = "kataTest";
	private MongoUpdater kata = new MongoUpdater(DATABASE_NAME);
	private static final TestPreparation preparation = new TestPreparation();
	
	public static void prepareDatabase() {

		List<DBObject> profiles = preparation.createInitialProfiles();
		List<DBObject> roboFlies = preparation.createInitialRoboFlies();
		
		preparation.addDummyAdditionalInformation();
		preparation.addDocumentReferences(roboFlies, profiles);
	}
	
	private void addDocumentReferences(List<DBObject> roboFlies, List<DBObject> profiles) {
		
		for(DBObject roboFly : roboFlies) {
			String roboFlyId = (String) roboFly.get("_id");
			if(roboFlyId.equals("RoboFly_ID_1")) {
				kata.addDocumentReference(roboFlyId, RoboFlyType.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_2")) {
				kata.addDocumentReference(roboFlyId, RoboFlyType.FLY.name);
			} else if (roboFlyId.equals("RoboFly_ID_3")) {
				kata.addDocumentReference(roboFlyId, RoboFlyType.MOSKITO.name);
			} else if (roboFlyId.equals("RoboFly_ID_4")) {
				kata.addDocumentReference(roboFlyId, RoboFlyType.MOSKITO.name);
			} else if (roboFlyId.equals("RoboFly_ID_5")) {
				kata.addDocumentReference(roboFlyId, RoboFlyType.MOSKITO.name);
			}
		}
	}
	
	private List<DBObject> createInitialRoboFlies() {
		
		List<DBObject> roboFlies = new ArrayList<DBObject>();

		DBObject roboFly1 = new BasicDBObject("name", "Calliphora")
										.append("_id", "RoboFly_ID_1")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		DBObject roboFly2 = new BasicDBObject("name", "Lucilia")
										.append("_id", "RoboFly_ID_2")
										.append("constructionYear", 2014)
										.append("status", "Out of service");

		DBObject roboFly3 = new BasicDBObject("name", "Onesia")
										.append("_id", "RoboFly_ID_3")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		DBObject roboFly4 = new BasicDBObject("name", "Ischnura")
										.append("_id", "RoboFly_ID_4")
										.append("constructionYear", 2014)
										.append("status", "Out of service");
		
		DBObject roboFly5 = new BasicDBObject("name", "Calopteryx")
										.append("_id", "RoboFly_ID_5")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		roboFlies.add(roboFly1);
		roboFlies.add(roboFly2);
		roboFlies.add(roboFly3);
		roboFlies.add(roboFly4);
		roboFlies.add(roboFly5);
		
		kata.saveRoboflies(roboFlies);
		
		return roboFlies;
	}
	
	private void addDummyAdditionalInformation() {
		
		kata.addMoreInformation("RoboFly_ID_1", createDummyAdditionalInformation(StatusDescription.EATEN));
		kata.addMoreInformation("RoboFly_ID_4", createDummyAdditionalInformation(StatusDescription.LOST));
	}
	
	private List<DBObject> createInitialProfiles() {
		
		List<DBObject> profiles = new ArrayList<DBObject>();
		
		DBObject profile1 = new BasicDBObject("_id", "ROBOFLY_ID_FLY")
					.append("type", RoboFlyType.FLY.name)
					.append("size", 2)
					.append("serviceTime", 60);
		
		DBObject profile2 = new BasicDBObject("_id", "ROBOFLY_ID_DRAGONFLY")
					.append("type", RoboFlyType.DRAGONFLY.name)
					.append("size", 5)
					.append("serviceTime", 25);
		
		DBObject profile3 = new BasicDBObject("_id", "ROBOFLY_ID_MOSKITO")
					.append("type", RoboFlyType.MOSKITO.name)
					.append("size", 2)
					.append("serviceTime", 120);
		
		DBObject profile4 = new BasicDBObject("_id", "ROBOFLY_ID_COPEPOD")
					.append("type", RoboFlyType.COPEPOD.name)
					.append("size", 8)
					.append("serviceTime", 90);
		
		profiles.add(profile1);
		profiles.add(profile2);
		profiles.add(profile3);
		profiles.add(profile4);
	
		kata.saveProfiles(profiles);
		return profiles;
	}
		
	private Map<String, String> createDummyAdditionalInformation(StatusDescription statusDesc) {
		
		String statusDescKey = "statusDescription";
		Map<String, String> additionalInfo = new HashMap<String, String>();
		additionalInfo.put(statusDescKey, statusDesc.name());
		additionalInfo.put("comment", "Dummy Text");
		additionalInfo.put("endOfLife", "30.04.14");
		
		return additionalInfo;		
	}

}
