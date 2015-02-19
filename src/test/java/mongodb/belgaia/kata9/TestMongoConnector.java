package mongodb.belgaia.kata9;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		connector.closeConnection();
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
	
	@Test
	public void shouldCreateOneBugrouteDocumentInRoutesCollection() {
		
		connector.createBugRouteDocument(getDummyBugCoordinatesMap());
		
		List<double[]> bugrouteCoordinates = connector.getBugRouteCoordinateList();
		
		Assert.assertEquals(6, bugrouteCoordinates.size());
	}
	
	@Test
	public void findRoboFlyWithinAreaOfBugRoute() {
		
		connector.createBugRouteDocument(getDummyBugCoordinatesMap());
		
		List<String> roboFlyIds = connector.findRoboFlyWithinBugTerritory();
		
		Assert.assertEquals(1, roboFlyIds.size());
		Assert.assertEquals("RoboFly_ID_5", roboFlyIds.get(0));		
	}
	
	private Map<String, double[]> getDummyBugCoordinatesMap() {
		
		double[] pos1 = {13.239067,52.499475};
		double[] pos2 = {13.241011,52.499425};
		double[] pos3 = {13.241819,52.499925};
		double[] pos4 = {13.241819,52.50021};
		double[] pos5 = {13.238067,52.500447};
		
 		Map<String, double[]> bugRoute = new HashMap<String, double[]>();
		bugRoute.put("BUG_POSITION_1", pos1);
		bugRoute.put("BUG_POSITION_2", pos2);
		bugRoute.put("BUG_POSITION_3", pos3);
		bugRoute.put("BUG_POSITION_4", pos4);
		bugRoute.put("BUG_POSITION_5", pos5);
		
		return bugRoute;
	}
}
