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
	public void shouldGetRoboFliesByStatusDifferentThanOKWithoutIndex() {
		
		long start = System.currentTimeMillis();
		
		List<DBObject> roboFlies = connector.getRoboFliesByDifferingStatus(RoboFlyStatus.OK);
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("(RoboFlyStatus != OK) - Duration without index: " + duration + " ms");
		
		Assert.assertEquals(2, roboFlies.size());
		Assert.assertEquals(roboFlies.get(0).get("_id"), "RoboFly_ID_2");
	}
	
	@Test
	public void shouldGetRoboFliesByStatusDifferentThanOKWithIndex() {
		
		connector.createIndexOnRoboFlyStatus();
		
		long start = System.currentTimeMillis();
		
		List<DBObject> roboFlies = connector.getRoboFliesByDifferingStatus(RoboFlyStatus.OK);
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("(RoboFlyStatus != OK) - Duration with index: " + duration + " ms");
		
		Assert.assertEquals(2, roboFlies.size());
		Assert.assertEquals(roboFlies.get(0).get("_id"), "RoboFly_ID_2");
	}
	
	@Test
	public void shouldReturnCountOfRobofliesOfTypeFlyWithoutIndex() {
		
		long start = System.currentTimeMillis();
		
		int count = connector.getCountOfRoboFlies(RoboFlyType.FLY);
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("(Count Of Roboflies) - Duration without index: " + duration + " ms");
		
		int expectedCountOfFlies = 3;
		Assert.assertEquals(expectedCountOfFlies, count);
	}
	
	@Test
	public void shouldReturnCountOfRobofliesOfTypeFlyWithIndex() {
		
		connector.createIndexOnRoboFlyType();

		long start = System.currentTimeMillis();

		int count = connector.getCountOfRoboFlies(RoboFlyType.FLY);
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("(Count Of Roboflies) - Duration with index: " + duration + " ms");

		int expectedCountOfFlies = 3;
		Assert.assertEquals(expectedCountOfFlies, count);
	}
	
	@Test
	public void shouldReturnAverageOfMeasuredSoundIntensityWithoutIndex() {
		
		long start = System.currentTimeMillis();
		
		Double average = connector.calculateAverageOfSoundIntensity();
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("(Average of Sound Intensity) - Duration without index: " + duration + " ms");
		
		Assert.assertEquals(new Double(43.333333333333336), average);		
	}
	
	@Test
	public void shouldReturnAverageOfMeasuredSoundIntensityWithIndex() {
		
		connector.createIndexOnSoundIntensity();
		
		long start = System.currentTimeMillis();
		
		Double average = connector.calculateAverageOfSoundIntensity();
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("(Average of Sound Intensity) - Duration with index: " + duration + " ms");
		
		Assert.assertEquals(new Double(43.333333333333336), average);		
	}
}
