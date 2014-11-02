package mongodb.belgaia.kata5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TestKata5dot2 {

	private static final String DATABASE_NAME = "kataTest";
	private MongoUpdater kata;
	
	@Before
	public void setUp() {
		kata = new MongoUpdater(DATABASE_NAME);
		
		createInitialRoboFlies();
		addDummyAdditionalInformation();
	}
	
	@After
	public void tearDown() {
		kata.dropDatabase();
	}
	
	@Test
	public void shouldSetEnergyShieldAtEatenRoboFlyTypes() {
		
		kata.setEnergyShieldAtRoboFly();		
		
		DBObject roboFly = kata.getRoboFly("RoboFly_ID_2");
		DBObject roboFlyOfSameType = kata.getRoboFly("RoboFly_ID_1");
		DBObject roboFlyWithDifferentType = kata.getRoboFly("RoboFly_ID_3");
		
		Assert.assertEquals(Equipment.ENERGY_SHIELD.name(), roboFly.get("equipment"));
		Assert.assertEquals(Equipment.ENERGY_SHIELD.name(), roboFlyOfSameType.get("equipment"));
		Assert.assertNull(roboFlyWithDifferentType.get("equipment"));
	}
	
	@Test
	public void shouldSetGPSAtMissedRoboFlyTypes() {
				
		kata.setGPSAtRoboFly();
		
		DBObject roboFly = kata.getRoboFly("RoboFly_ID_3");
		DBObject roboFlyOfSameType = kata.getRoboFly("RoboFly_ID_4");
		DBObject roboFlyWithDifferentType = kata.getRoboFly("RoboFly_ID_1");
		
		Assert.assertEquals(Equipment.GPS.name(), roboFly.get("equipment"));
		Assert.assertEquals(Equipment.GPS.name(), roboFlyOfSameType.get("equipment"));
		Assert.assertNull(roboFlyWithDifferentType.get("equipment"));

	}
	
	private void createInitialRoboFlies() {
		
		List<DBObject> roboFlies = new ArrayList<DBObject>();

		DBObject roboFly1 = new BasicDBObject("name", "Calliphora")
										.append("type", RoboFlyType.FLY.name)
										.append("_id", "RoboFly_ID_1")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		DBObject roboFly2 = new BasicDBObject("name", "Lucilia")
										.append("type", RoboFlyType.FLY.name)
										.append("_id", "RoboFly_ID_2")
										.append("constructionYear", 2014)
										.append("status", "Out of service");

		DBObject roboFly3 = new BasicDBObject("name", "Ischnura")
										.append("type", RoboFlyType.DRAGONFLY.name)
										.append("_id", "RoboFly_ID_3")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		DBObject roboFly4 = new BasicDBObject("name", "Calopteryx")
										.append("type", RoboFlyType.DRAGONFLY.name)
										.append("_id", "RoboFly_ID_4")
										.append("constructionYear", 2014)
										.append("status", "Out of service");
		
		roboFlies.add(roboFly1);
		roboFlies.add(roboFly2);
		roboFlies.add(roboFly3);
		roboFlies.add(roboFly4);
		kata.saveRoboflies(roboFlies);
	}
	
	private void addDummyAdditionalInformation() {
		
		kata.addMoreInformation("RoboFly_ID_1", createDummyAdditionalInformation(StatusDescription.EATEN));
		kata.addMoreInformation("RoboFly_ID_4", createDummyAdditionalInformation(StatusDescription.LOST));
	}
	
	private void createInitialProfiles() {
		
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
