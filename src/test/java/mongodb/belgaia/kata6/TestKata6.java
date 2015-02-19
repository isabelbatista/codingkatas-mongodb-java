package mongodb.belgaia.kata6;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;

public class TestKata6 {
	
	private static final String DATABASE_NAME = "kataTest";
	Kata6 kata;
	MongoConnector mongoConnector;
	
	@Before
	public void setUp() {
		mongoConnector = new MongoConnector(DATABASE_NAME);
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
		kata = new Kata6(DATABASE_NAME);		
	}
	
	@After
	public void tearDown() {
		mongoConnector.dropDatabase();
		mongoConnector.closeConnection();
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
		Assert.assertEquals(5.0, averageTime, 0.0);
	}
	
	@Test
	public void shouldCalculateAverageOfNeededEnergyUnitsByRoboFlyType() {
		RoboFlyType type = RoboFlyType.MOSKITO;
		double averageTime = kata.calculateAverageOfEnergyUnits(type);
		
		Assert.assertEquals(5.0, averageTime, 0.00);
	}
}
