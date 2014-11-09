package mongodb.belgaia.kata5;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;

public class TestKata5dot2 {

	private static final String DATABASE_NAME = "kataTest";
	private MongoUpdater kata;
	
	@Before
	public void setUp() {
		kata = new MongoUpdater(DATABASE_NAME);
		TestPreparation.prepareDatabase();
	}
	
	@After
	public void tearDown() {
//		kata.dropDatabase();
	}
	
	@Test
	public void shouldSetEnergyShieldAtEatenRoboFlyTypes() {
		
		kata.setEnergyShieldAtRoboFly();		
		
		DBObject roboFly = kata.getRoboFly("RoboFly_ID_2");
		DBObject roboFlyOfSameType = kata.getRoboFly("RoboFly_ID_1");
		DBObject roboFlyWithDifferentType = kata.getRoboFly("RoboFly_ID_3");
		
		Assert.assertEquals(Equipment.ENERGY_SHIELD.name(), (String) roboFly.get("equipment"));
		Assert.assertEquals(Equipment.ENERGY_SHIELD.name(), (String) roboFlyOfSameType.get("equipment"));
		Assert.assertNull(roboFlyWithDifferentType.get("equipment"));
	}
	
	@Test
	public void shouldSetGPSAtMissedRoboFlyTypes() {
				
		kata.setGPSAtRoboFly();
		
		DBObject roboFly = kata.getRoboFly("RoboFly_ID_3");
		DBObject roboFlyOfSameType = kata.getRoboFly("RoboFly_ID_4");
		DBObject roboFlyOfSameType2 = kata.getRoboFly("RoboFly_ID_5");
		DBObject roboFlyWithDifferentType = kata.getRoboFly("RoboFly_ID_1");
		
		Assert.assertEquals(Equipment.GPS.name(), roboFly.get("equipment"));
		Assert.assertEquals(Equipment.GPS.name(), roboFlyOfSameType.get("equipment"));
		Assert.assertEquals(Equipment.GPS.name(), roboFlyOfSameType2.get("equipment"));
		Assert.assertNull(roboFlyWithDifferentType.get("equipment"));

	}
}
