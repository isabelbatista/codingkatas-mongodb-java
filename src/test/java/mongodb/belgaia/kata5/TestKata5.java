package mongodb.belgaia.kata5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TestKata5 {
	
	private static final String DATABASE_NAME = "kataTest";
	private MongoUpdater kata;
	
	@Before
	public void setUp() {
		kata = new MongoUpdater(DATABASE_NAME);
		createInitialRoboFlies();
	}
	
	@After
	public void tearDown() {
		kata.dropDatabase();
		kata.closeConnection();
	}
	
	@Test
	public void shouldChangeStatusOfDamagedRobofly() {
		
		kata.changeStatus("RoboFly_ID_2", RoboFlyStatus.OUT_OF_SERVICE);
		
		DBObject roboFly = kata.getRoboFly("RoboFly_ID_2");
		Assert.assertEquals(RoboFlyStatus.OUT_OF_SERVICE.name, (String) roboFly.get("status"));
	}
	
	@Ignore
	@Test
	public void shouldAddMoreInformationToDamagedFlies() {
		
		Map<String, String> additionalInfo = createDummyAdditionalInformation(StatusDescription.DAMAGED);
		kata.addMoreInformation("RoboFly_ID_2", additionalInfo);
		
		DBObject roboFly = kata.getRoboFly("RoboFly_ID_2");
		Assert.assertEquals(StatusDescription.DAMAGED.name(), (String)roboFly.get("statusDescription"));
	}
	
	private Map<String, String> createDummyAdditionalInformation(StatusDescription statusDesc) {
		
		String statusDescKey = "statusDescription";
		Map<String, String> additionalInfo = new HashMap<String, String>();
		additionalInfo.put(statusDescKey, statusDesc.name());
		additionalInfo.put("comment", "Dummy Text");
		additionalInfo.put("endOfLife", "30.04.14");
		
		return additionalInfo;		
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
										.append("status", "OK");

		DBObject roboFly3 = new BasicDBObject("name", "Ischnura")
										.append("type", RoboFlyType.DRAGONFLY.name)
										.append("_id", "RoboFly_ID_3")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		DBObject roboFly4 = new BasicDBObject("name", "Calopteryx")
										.append("type", RoboFlyType.DRAGONFLY.name)
										.append("_id", "RoboFly_ID_4")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		roboFlies.add(roboFly1);
		roboFlies.add(roboFly2);
		roboFlies.add(roboFly3);
		roboFlies.add(roboFly4);
		kata.saveRoboflies(roboFlies);
	}	
}
