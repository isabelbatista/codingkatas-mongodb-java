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
	private Kata8 kata;
	
	@Before
	public void setUp() {
		
		kata = new Kata8(DATABASE_NAME);
		
		connector = new MongoConnector(DATABASE_NAME);
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
	}
	
	@After
	public void tearDown() {
		connector.dropDatabase();
	}
	
	@Test
	public void shouldUpdateAllRoboFliesWithCoordinates() {
		
		kata.updateRoboFliesWithCoordinates();
		
		List<RoboFly> roboFlies = connector.getRoboflies();
		
		for(RoboFly roboFly : roboFlies) {
//			roboFly.get
		}
	}
	
	@Ignore
	@Test
	public void shouldReturnRoboFliesNearestByTheBug() {
		
		int countOfFlies = 2;
		List<DBObject> roboFlies = connector.findRoboFliesNearByBug(countOfFlies);
		
		Assert.assertEquals("RoboFly_ID_4", roboFlies.get(0));
	}
}
