package mongodb.belgaia.kata9;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestKata9 {
	
	private static final String DATABASE_NAME = "kataTest";
	
	Kata9 kata;
	
	@Before
	public void setUp() {
		kata = new Kata9(DATABASE_NAME);
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
}