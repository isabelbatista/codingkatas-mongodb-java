package mongodb.belgaia.kata6;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mongodb.DBObject;

public class TestKata6 {
	
	private static final String DATABASE_NAME = "kataTest";
	Kata6 kata;
	MongoConnector mongoConnector = new MongoConnector(DATABASE_NAME);
	
	@Before
	public void setUp() {
		TestPreparation.prepareDatabase();
		kata = new Kata6();		
	}
	
	@After
	public void tearDown() {
		mongoConnector.dropDatabase();
	}
	
	@Test
	public void shouldReturnRoboFliesByType() {
		List<DBObject> roboFlies = mongoConnector.findFliesByType("ROBOFLY_ID_FLY");
		Assert.assertEquals(3, roboFlies.size());		
	}
	
	@Test
	public void shouldReturnCharsetsByRoboFlyId() {
		List<DBObject> charsets = mongoConnector.findCharsetsByRoboFlyId("RoboFly_ID_1");
		Assert.assertEquals(1, charsets.size());	
	}
	
	@Test
	public void shouldReturnChargingSetsByRoboFlyType() {
		Set<DBObject> charsets = mongoConnector.findChargingSetsByRoboFlyType("ROBOFLY_ID_FLY");
		Assert.assertEquals(3, charsets.size());
	}
	
	@Test
	public void shouldCalculateAverageOfLoadingTimeForRoboflyType() {
		double averageTime = kata.calculateAverageLoadingTime(RoboFlyType.FLY);
		Assert.assertEquals(6.33, averageTime, 0.3);
	}
}
