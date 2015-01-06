package mongodb.belgaia.kata8;

import mongodb.belgaia.kata8.RoboFly.Status;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMongoConnector {
	
	private static final String DATABASE_NAME = "kataTest";
	private MongoConnector connector;
	
	@Before
	public void setUp() {
		connector = new MongoConnector(DATABASE_NAME);
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
	}
	
	@After
	public void tearDown() {
		connector.dropDatabase();
	}
	
	@Test
	public void shouldReturnRoboFlyOne() {
		
		RoboFly roboFly = connector.getRoboFlyById("RoboFly_ID_1");
		
		
		Assert.assertNotNull(roboFly);
		Assert.assertEquals("RoboFly_ID_1", roboFly.getId());
		Assert.assertEquals(Status.OK.name, roboFly.getStatus().name);
		Assert.assertEquals(RoboFly.Type.FLY.name, roboFly.getType().name);
	}

}
