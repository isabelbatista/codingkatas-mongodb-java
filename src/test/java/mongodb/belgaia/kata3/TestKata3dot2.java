package mongodb.belgaia.kata3;

import java.util.ArrayList;
import java.util.List;

import mongodb.belgaia.kata3.RoboFly.Status;
import mongodb.belgaia.kata3.RoboFly.Type;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestKata3dot2 {
	
	private static final String DATABASE_NAME = "kataTest";
	private Kata3 kata;
	
	@Before
	public void setUp() {
		kata = new Kata3(DATABASE_NAME);
		kata.saveRoboFlies(createDummyRoboFlies(), true);
	}
	
	@After
	public void tearDown() {
		kata.dropDatabase();
	}
	
	@Test
	public void shouldExtractInformationFromRoboFliesAndAddItToProfile() throws CollectionDoesNotExistExc {
		
		final String sourceCollection = "roboflies";
		final String targetCollection = "profiles";
		kata.refactorDatabase(sourceCollection, targetCollection);
		
		List<Profile> profiles = kata.getProfiles();
		List<RoboFly> roboFlies = kata.getRoboFlies();
		
		// expect 4 new profiles
		Assert.assertNotNull(profiles);
		Assert.assertEquals(4, profiles.size());
		
		// expect fields size and service time are not set in roboflies any more
		for(RoboFly roboFly : roboFlies) {
			Assert.assertNotNull(roboFly.getType());
			Assert.assertNull(roboFly.getSize());
			Assert.assertNull(roboFly.getServiceTime());
		}
	}
	
	// FIXME: should not be green
	@Test
	public void shouldReturnTypeOfRoboFlyOnlyByCheckingProfiles() throws CollectionDoesNotExistExc {
		
		kata.startKata3dot2();
			
		String roboFlyTypeFly = kata.getRoboFlyType("RoboFly_ID_1");
		String roboFlyTypeMoskito= kata.getRoboFlyType("RoboFly_ID_2");
		String roboFlyTypeDragonfly= kata.getRoboFlyType("RoboFly_ID_3");
		String roboFlyTypeCopepod= kata.getRoboFlyType("RoboFly_ID_4");
		
		Assert.assertEquals(RoboFlyType.FLY.name, roboFlyTypeFly);
		Assert.assertEquals(RoboFlyType.MOSKITO.name, roboFlyTypeMoskito);
		Assert.assertEquals(RoboFlyType.DRAGONFLY.name, roboFlyTypeDragonfly);
		Assert.assertEquals(RoboFlyType.COPEPOD.name, roboFlyTypeCopepod);
	}
	
	private List<RoboFly> createDummyRoboFlies() {
		
		List<RoboFly> roboFlies = new ArrayList<RoboFly>();
		RoboFly.Builder builder = new RoboFly.Builder("RoboFly_ID_1", "Fly1");
		RoboFly fly = builder.constructionYear(2014)
				.serviceTime(60)
				.size(2)
				.status(Status.OK)
				.type(Type.FLY)
				.build();
		
				
		builder = new RoboFly.Builder("RoboFly_ID_2", "Moskito1");
		RoboFly moskito = builder.constructionYear(2014)
				.serviceTime(120)
				.size(2)
				.status(Status.OK)
				.type(Type.MOSKITO)
				.build();
		
		builder = new RoboFly.Builder("RoboFly_ID_3", "Dragonfly1");
		RoboFly dragonFly = builder.constructionYear(2014)
				.serviceTime(25)
				.size(5)
				.status(Status.OK)
				.type(Type.DRAGONFLY)
				.build();
		
		builder = new RoboFly.Builder("RoboFly_ID_4", "Copepod");
		RoboFly copepod = builder.constructionYear(2014)
				.serviceTime(90)
				.size(8)
				.status(Status.OK)
				.type(Type.COPEPOD)
				.build();
		
		roboFlies.add(fly);
		roboFlies.add(moskito);
		roboFlies.add(dragonFly);
		roboFlies.add(copepod);
		
		return roboFlies;
	}

}
