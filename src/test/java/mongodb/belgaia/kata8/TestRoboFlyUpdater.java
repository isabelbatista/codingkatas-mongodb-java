package mongodb.belgaia.kata8;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestRoboFlyUpdater {
	
	private static final String INPUT_FILENAME = "src/test/resources/kata8/roboflyPositions.csv"; 
	
	private RoboFlyUpdater updater;
	
	@Before
	public void setUp() {
		updater = new RoboFlyUpdater();
		
		updater.setInputFileName(INPUT_FILENAME);
	}
	
	@Test
	public void shouldGetLongitudeOfRoboFlyOne() {
		
		double[] coordinates = updater.getCoordinatesOfRoboFly("RoboFly_ID_1");
		Assert.assertEquals(13.242619, coordinates[0], 0.0);
		
	}

}
