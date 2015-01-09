package mongodb.belgaia.kata8;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;

public class TestKata8 {

	private static final String DATABASE_NAME = "kataTest";
	private MongoConnector connector;
	private Kata8 kata;
	private RoboFlyUpdater updater;
	
	@Before
	public void setUp() {
		
		kata = new Kata8(DATABASE_NAME);
		
		connector = new MongoConnector(DATABASE_NAME);
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
		
		updater = new RoboFlyUpdater(DATABASE_NAME);
		updater.updateRoboFliesWithCoordinates();
	}
	
	@After
	public void tearDown() {
		connector.dropDatabase();
	}
	
	@Test
	public void shouldUpdateAllRoboFliesWithCoordinates() {
		
		kata.updateRoboFliesWithCoordinates();
		
		List<DBObject> roboFlies = connector.getRoboflyDocuments();
		
		for(DBObject roboFly : roboFlies) {
			String roboFlyId = (String) roboFly.get("_id");
			double[] coordinates = connector.getRoboFlyCoordinates(roboFlyId);
			Assert.assertNotNull(coordinates);
		}
	}
	
	@Test
	public void shouldReturnRoboFliesCloseToTheBug() {
		
		List<String> roboFlies = kata.findThreeRoboFliesNearToBug();
		
		Assert.assertEquals(3, roboFlies.size());
		Assert.assertEquals("RoboFly_ID_3", roboFlies.get(0));
		Assert.assertEquals("RoboFly_ID_7", roboFlies.get(1));
		Assert.assertEquals("RoboFly_ID_5", roboFlies.get(2));
	}
}
