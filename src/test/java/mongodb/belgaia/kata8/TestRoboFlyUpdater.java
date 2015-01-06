package mongodb.belgaia.kata8;

import org.junit.After;
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
		updater = new RoboFlyUpdater(TEST_DATABASE_NAME);
		mongoConnector = new MongoConnector(TEST_DATABASE_NAME);
		
		TestPreparation preparation = new TestPreparation(TEST_DATABASE_NAME);
		preparation.prepareDatabase();
		
		updater.setInputFileName(INPUT_FILENAME);
	}
	
	@After
	public void tearDown() {
		mongoConnector.dropDatabase();
	}
	
	@Test
	public void shouldGetLongitudeOfRoboFlyOne() {
		
		double[] coordinates = updater.getCoordinatesOfRoboFly(TEST_ROBO_FLY_ID);
		Assert.assertEquals(13.242619, coordinates[0], 0.0);
	}
	
	@Test
	public void shouldUpdateRoboFlyOneWith2DCoordinates() {
		
		updater.updateRoboFlyWithCoordinates(TEST_ROBO_FLY_ID);
		
		double[] coordinates = mongoConnector.getRoboFlyCoordinates(TEST_ROBO_FLY_ID);
		
		Assert.assertNotNull(coordinates);
		Assert.assertEquals(13.242619, coordinates[0], 0.00);
		Assert.assertEquals(52.498092, coordinates[1], 0.00);
	}
}