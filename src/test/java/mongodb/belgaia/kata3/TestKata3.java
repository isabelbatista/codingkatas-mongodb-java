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
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void shouldCreateNewRoboFlies() {
	
		final List<RoboFly> roboFlies = createDummyFlies();
		List<String> flyIds = kata.saveRoboFlies(roboFlies);
		
		Assert.assertNotNull(flyIds);
		
	}
	
	private List<RoboFly> createDummyFlies() {
		
		List<RoboFly> roboFlies = new ArrayList<RoboFly>();
		
		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_4", "Ischnura");
		
		RoboFly roboFly1 = roboFlyBuilder.constructionYear(2014)
										 .size(5)
										 .serviceTime(25)
										 .status(Status.OK)
										 .type(Type.DRAGONFLY).build();
				
		roboFlies.add(roboFly1);
		
		return roboFlies;
		
	}
	

}
