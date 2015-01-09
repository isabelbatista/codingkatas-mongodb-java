package mongodb.belgaia.kata8;

import java.util.List;

import mongodb.belgaia.kata8.RoboFly.Status;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;

public class TestMongoConnector {
	
	private static final String DATABASE_NAME = "kataTest";
	private MongoConnector connector;
	private RoboFlyUpdater updater;
	
	@Before
	public void setUp() {
		connector = new MongoConnector(DATABASE_NAME);
		updater = new RoboFlyUpdater(DATABASE_NAME);
		
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
		
		updater.updateRoboFliesWithCoordinatesAndSetGeoIndex();
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
	
	@Test
	public void shouldReturnOneRoboFlyNearToBugCoordinates() {
		
		double[] bugCoordinates = {13.237033, 52.499789};
		List<RoboFly> nearRoboFly = connector.findRoboFliesNearByBug(bugCoordinates, 1);
		
		Assert.assertEquals(1, nearRoboFly.size());
		Assert.assertEquals("RoboFly_ID_4", nearRoboFly.get(0).getId());
	}
}
