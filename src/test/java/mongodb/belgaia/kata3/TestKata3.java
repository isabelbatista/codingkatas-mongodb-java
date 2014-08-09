package mongodb.belgaia.kata3;


import java.util.ArrayList;
import java.util.List;

import mongodb.belgaia.kata3.RoboFly.Status;
import mongodb.belgaia.kata3.RoboFly.Type;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestKata3 {
	
	private static final String TEST_DATABASE_NAME = "testmongodbkatas";
	private Kata3 kata;
	
	@Before
	public void setUp() {
		
		kata = new Kata3(TEST_DATABASE_NAME);
		
		saveMockRoboFlyWithoutType();
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void shouldCreateNewRoboFlies() {
	
		final List<RoboFly> roboFlies = createDummyFlies();
		List<String> flyIds = kata.saveRoboFlies(roboFlies);
		
		Assert.assertNotNull(flyIds);
		Assert.assertEquals(2, flyIds.size());
		Assert.assertNotEquals(flyIds.get(0), flyIds.get(1));
	}
	
	@Test
	public void shouldAddTypeToExistingFliesIfNotAlreadySet() {
		
		final String idOfExistingFly = "RoboFly_ID_1";
		final boolean overwrite = false;
		
		RoboFly updatedRoboFly = kata.addTypeToFly(idOfExistingFly, RoboFly.Type.FLY, overwrite);
		
		Assert.assertNotNull(updatedRoboFly);
		Assert.assertEquals(idOfExistingFly, updatedRoboFly.getId());
		Assert.assertEquals(RoboFly.Type.FLY.name(), updatedRoboFly.getType().toString());
		
	}
		
	private List<RoboFly> createDummyFlies() {
		
		List<RoboFly> roboFlies = new ArrayList<RoboFly>();
		
		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_4", "Ischnura");
		
		RoboFly roboFly1 = roboFlyBuilder.constructionYear(2014)
										 .size(5)
										 .serviceTime(25)
										 .status(Status.OK)
										 .type(Type.DRAGONFLY).build();
		
		roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_5", "Calopteryx");
		RoboFly roboFly2 = roboFlyBuilder.constructionYear(2014)
										 .size(5)
										 .serviceTime(25)
										 .status(Status.OK)
										 .type(Type.DRAGONFLY).build();
				
		roboFlies.add(roboFly1);
		roboFlies.add(roboFly2);
		
		return roboFlies;
		
	}
	
	/**
	 * Creates a new robo fly mock without a type.
	 * 
	 * @return	The robotic fly mock without a given type.
	 */
	private void saveMockRoboFlyWithoutType() {
		
		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_1", "Calliphora");
		
		RoboFly roboFly = roboFlyBuilder.constructionYear(2014)
										 .size(2)
										 .serviceTime(1)
										 .status(Status.OK).build();
		
		kata.saveRoboFly(roboFly);									
	}
	

}
