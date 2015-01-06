package mongodb.belgaia.kata8;

import org.junit.Assert;
import org.junit.Test;


public class TestRoboFlyUpdater {
	
	RoboFlyUpdater updater = new RoboFlyUpdater();
	
	@Test
	public void shouldGetLongitudeOfRoboFlyOne() {
		
		double[] coordinates = updater.getCoordinatesOfRoboFly("RoboFly_ID_1");
		Assert.assertEquals(13.242619, coordinates[0], 0.0);
		
	}

}
