package mongodb.belgaia.kata1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRoboFliesPersistence {
	
	RoboFliesPersistence persistence;
	
	@Before
	public void setUp() {
		persistence = new RoboFliesPersistence();
	}
	
	@Test
	public void shouldSaveRoboFly() {
		
		String objectId = persistence.saveRoboFly(getDummyRoboFly());
		Assert.assertNotNull(objectId);
		
	}
	
	public RoboFly getDummyRoboFly() {
		
		return new RoboFly("Calliphora", 2014, 2, Status.OK);
		
	}
	
	

}
