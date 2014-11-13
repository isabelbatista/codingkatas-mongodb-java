package mongodb.belgaia.kata7;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;

public class TestKata7 {
	
	private static final String DATABASE_NAME = "kataTest";
	private MongoConnector connector;
	private Kata7 kata;
	
	@Before
	public void setUp() {
		kata = new Kata7(DATABASE_NAME);
		connector = new MongoConnector(DATABASE_NAME);
		
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
	}
	
	@After
	public void tearDown() {
		connector.dropDatabase();
	}
	
	@Test
	public void shouldGetRoboFliesByStatusDifferentThanOKWithoutIndex() {
		
		boolean withIndex = false;
		long start = System.currentTimeMillis();
		
		List<DBObject> roboFlies = connector.getRoboFliesByDifferingStatus(RoboFlyStatus.OK, withIndex);
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("Duration without index: " + duration + " ms");
		
		Assert.assertEquals(2, roboFlies.size());
		Assert.assertEquals(roboFlies.get(0).get("_id"), "RoboFly_ID_2");
	}
	
	@Test
	public void shouldGetRoboFliesByStatusDifferentThanOKWithIndex() {
		
		boolean withIndex = true;
		long start = System.currentTimeMillis();
		
		List<DBObject> roboFlies = connector.getRoboFliesByDifferingStatus(RoboFlyStatus.OK, withIndex);
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("Duration with index: " + duration + " ms");
		
		Assert.assertEquals(2, roboFlies.size());
		Assert.assertEquals(roboFlies.get(0).get("_id"), "RoboFly_ID_2");
		
	}

}
