package mongodb.belgaia.kata8;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class TestRoboFlyUpdater {
	
	private static final String INPUT_FILENAME = "src/test/resources/kata8/roboflyPositions.csv"; 
	private static final String TEST_DATABASE_NAME = "kataTest";
	
	private static final String TEST_ROBO_FLY_ID = "RoboFly_ID_1";
	
	private RoboFlyUpdater updater;
	private MongoConnector mongoConnector;
	
	@Before
	public void setUp() {
		updater = new RoboFlyUpdater();
		mongoConnector = new MongoConnector(TEST_DATABASE_NAME);
		
		updater.setInputFileName(INPUT_FILENAME);
	}
	
	@Test
	public void shouldGetLongitudeOfRoboFlyOne() {
		
		double[] coordinates = updater.getCoordinatesOfRoboFly(TEST_ROBO_FLY_ID);
		Assert.assertEquals(13.242619, coordinates[0], 0.0);
	}
	
//	@Test
//	public void shouldUpdateRoboFlyOneWithPlainCoordinateFields() {
//		
//		updater.updateRoboFlyWithCoordinates(TEST_ROBO_FLY_ID);
//		
//		RoboFly roboFly = mongoConnector.getRoboFly(TEST_ROBO_FLY_ID);
//	}
	
	@Ignore
	@Test
	public void shouldUpdateRoboFlyOneWith2DIndexCoordinates() {
		
	}

}
