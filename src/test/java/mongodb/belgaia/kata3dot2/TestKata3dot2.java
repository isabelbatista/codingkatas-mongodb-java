package mongodb.belgaia.kata3dot2;

import java.util.ArrayList;
import java.util.List;

import mongodb.belgaia.kata3.Kata3;
import mongodb.belgaia.kata3.Profile;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestKata3dot2 {
	
	private static final String DATABASE_NAME = "testmongodbkatas";
	private Kata3 kata;
	
	@Before
	public void setUp() {
		kata = new Kata3(DATABASE_NAME);		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void shouldExtractGeneralInformationFromRoboFliesToProfile() {
		
		final List<String> listOfFields = new ArrayList<String>();
		final String sourceCollection = "roboflies";
		final String targetCollection = "robofly_profiles";
		List<Profile> profiles = kata.extractFieldsToProfile(listOfFields, sourceCollection, targetCollection);
		
		Assert.assertNotNull(profiles);
		Assert.assertEquals(4, profiles.size());
		
	}

}
