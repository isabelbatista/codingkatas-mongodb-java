package mongodb.belgaia.kata9;

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
//		connector.dropDatabase();
	}
	
	@Test
	public void shouldChangeTypeOfRoboFlyCoordinatesTo2dsphere() {
		
		String roboFlyId = "RoboFly_ID_1";
		
		// pre-check
		String coordinatesFieldType = connector.getTypeOfRoboFlyCoordinatesIndex();
		Assert.assertEquals("2d", coordinatesFieldType);
		
		// manipulation
		connector.change2dCoordinatesFieldTo2dSphere(roboFlyId);
		
		// verfiy
		coordinatesFieldType = connector.getTypeOfRoboFlyCoordinatesIndex();
		Assert.assertEquals("2dsphere", coordinatesFieldType);
	}
}
