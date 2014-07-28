package mongodb.belgaia.kata2;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMeasurementPersistence {
	
	private static final String DATABASE_NAME = "testmongodbkatas";
	private MeasurementPersistence persistence;
	
	@Before
	public void setUp() {
		
		persistence = new MeasurementPersistence(DATABASE_NAME);
	}
	
	@After
	public void tearDown() {
		
		persistence.dropDatabase(DATABASE_NAME);
	}
	
	@Test
	public void shouldSaveMeasurements() {
		
		String measurementId = persistence.saveMeasurement(getDummyMeasurement());
		Assert.assertNotNull(measurementId);
		
	}
	
	private Measurement getDummyMeasurement() {
		
		Measurement measurement = new Measurement("RoboFly_ID_1");
		
		measurement.setHumidity(20.01f);
		measurement.setBarometicPressure(1013.25f);
		measurement.setLuminosity(600);
		measurement.setSoundIntensity(55);
		measurement.setTemperature(17);
		measurement.setCo2content(0.04f);
		
		return measurement;
	}

}
