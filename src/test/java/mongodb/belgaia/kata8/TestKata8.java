package mongodb.belgaia.kata8;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mongodb.DBObject;

public class TestKata8 {

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
	public void shouldReturnRoboFlyOne() {
		
		RoboFly roboFly = connector.getRoboFlyById("RoboFly_ID_1");
		
		Assert.assertNotNull(roboFly);
	}

	@Test
	public void shouldAddGeoIndexToBugroute() {
		List<DBObject> indexInfo = connector.addGeoIndexToBugroute();
		
		DBObject keyInfo = indexInfo.get(0);
		Assert.assertEquals("2dsphere", keyInfo.get("loc"));
	}
	
	public void shouldAddGeoIndexToRoboFlyStations() {
		
	}
	
	@Ignore
	@Test
	public void shouldReturnRoboFliesNearestByTheBug() {
		
		int countOfFlies = 2;
		List<DBObject> roboFlies = connector.findRoboFliesNearByBug(countOfFlies);
		
		Assert.assertEquals("RoboFly_ID_4", roboFlies.get(0));
	}
}
