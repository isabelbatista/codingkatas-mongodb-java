package mongodb.belgaia.kata1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRoboFliesPersistence {
	
	private final static String DATABASE_NAME = "testmongodbkatas";
	private RoboFliesPersistence persistence;
		
	
	@Before
	public void setUp() {
		persistence = new RoboFliesPersistence(DATABASE_NAME);
	}
	
	@After
	public void tearDown() {
		
		persistence.dropDatabase();
		persistence.closeConnection();
	}
	
	@Test
	public void shouldSaveRoboFly() {
		
		String objectId = persistence.saveRoboFly(getDummyRoboFly());
		Assert.assertNotNull(objectId);
		
	}
	
	@Test
	public void shouldSaveMultipleRoboFlies() {
		
		List<String> objectIds = persistence.saveRoboFlies(getDummyRoboFlies());
		Assert.assertNotNull(objectIds);
		Assert.assertEquals(3, objectIds.size());
	}
	
	public RoboFly getDummyRoboFly() {
		
		return new RoboFly("Calliphora", 2014, 2, 60, RoboFlyStatus.OK);
		
	}
	
	public Map<String, RoboFly> getDummyRoboFlies() {
		
		Map<String, RoboFly> roboFlyMap = new HashMap<String, RoboFly>();
		
		RoboFly roboFly1 = new RoboFly("Calliphora", 2014, 2, 60, RoboFlyStatus.OK);
		roboFly1.setId("RoboFly_ID_1");
		roboFlyMap.put(roboFly1.getId(), roboFly1);
		
		RoboFly roboFly2 = new RoboFly("Lucilia", 2014, 2, 60, RoboFlyStatus.OK);
		roboFly2.setId("RoboFly_ID_2");
		roboFlyMap.put(roboFly2.getId(), roboFly2);
		
		RoboFly roboFly3 = new RoboFly("Onesia", 2014, 2, 60, RoboFlyStatus.OK);
		roboFly3.setId("RoboFly_ID_3");
		roboFlyMap.put(roboFly3.getId(), roboFly3);
		
		return roboFlyMap;
	}
}
