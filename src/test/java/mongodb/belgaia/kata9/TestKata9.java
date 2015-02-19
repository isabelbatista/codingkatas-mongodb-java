package mongodb.belgaia.kata9;

import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestKata9 {
	
	private static final String DATABASE_NAME = "kataTest";
	
	private Kata9 kata;
	private MongoConnector connector;

	
	@Before
	public void setUp() {
		kata = new Kata9(DATABASE_NAME);
		
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
		
		connector = new MongoConnector(DATABASE_NAME);
	}
	
	@After
	public void tearDown() {
		connector.dropDatabase();
		connector.closeConnection();
	}
	
	@Test
	public void shouldInsertBugrouteToDatabase() {
		
		kata.insertBugTerritoryCoordinates();
		
		Map<String, double[]> bugrouteCoordinates = kata.getBugTerritoryCoordinates();
		Assert.assertNotNull(bugrouteCoordinates);
		
		for(Map.Entry<String, double[]> entry: bugrouteCoordinates.entrySet()) {
			
			if(entry.getKey().equals("BUG_POSITION_1")) {
				double[] coordinates = entry.getValue();
				Assert.assertEquals(coordinates[0], 13.239067, 0.00);
				break;
			}
		}	
	}
	
	@Test
	public void shouldReturnRoboFlyIdsWithinBugTerritory() {
				
		kata.insertBugTerritoryCoordinates();
		
		List<String> roboFlyIds = kata.findRoboFliesWithinBugTerritory();
				
		Assert.assertEquals(1, roboFlyIds.size());
		Assert.assertEquals("RoboFly_ID_5", roboFlyIds.get(0));		
		
	}
}