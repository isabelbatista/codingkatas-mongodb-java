package mongodb.belgaia.kata6;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestKata6 {
	
	private static final String DATABASE_NAME = "kataTest";
	Kata6 kata;
	
	@Before
	public void setUp() {
		kata = new Kata6(DATABASE_NAME);
		
		String fileName = "src/test/resources/kata6/charging_data.csv";
		kata.importData2MongoDb(fileName);
	}
	
	@Test
	public void shouldCalculateAverageOfLoadingTimeForRoboflyType() {
		double averageTime = kata.calculateAverageLoadingTime(RoboFlyType.FLY);
		
		Assert.assertEquals(6.33, averageTime, 0.3);
	}
}
