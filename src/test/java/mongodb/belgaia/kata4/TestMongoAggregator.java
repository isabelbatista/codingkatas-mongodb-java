package mongodb.belgaia.kata4;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TestMongoAggregator {
	
	private static final String DATABASE_NAME = "testmongodbkatas";
	private MongoAggregator aggregator;
	
	
	@Before
	public void setUp() {
		
		aggregator = new MongoAggregator(DATABASE_NAME);
		createInitialRoboFlies();
		createInitialMeasurements();
	}
	
//	@Test
//	public void shouldReturnRoboFlyWhereSoundIntensityIsTooLow() {
//		
//		RoboFly roboFlyToBeCalibrated = aggregator.findRoboFlyWithWrongSoundIntensity();
//		
//		
//		
//		
//		String expectedRoboFlyId = "RoboFly_ID_2";
//		
//		
//	}
	
	@Test
	public void shouldReturnAverageOfAllMeasurements() {
		
		List<DBObject> result = aggregator.calculateAverage(null, "soundIntensity");
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(37.333333333333336, result.get(0).get("average"));
		
	}
	
	@Test
	public void shouldReturnAverageOfMeasurementsGroupedByRoboFly() {
		
		List<DBObject> results = aggregator.calculateAverage("roboFlyID", "soundIntensity");
		
		Assert.assertEquals(2, results.size());
		
		for(DBObject result : results) {
			
			String roboFlyID = String.valueOf(result.get("_id"));
			
			if(roboFlyID.equals("RoboFly_ID_1")) {
				Assert.assertEquals(55.0, result.get("average"));
			} else if (roboFlyID.equals("RoboFly_ID_2")) {
				Assert.assertEquals(28.5, result.get("average"));
			} else {
				fail();
			}
		}		
	}
	
	@Test
	public void shouldReturnRoboFlyWithWrongSoundIntensity() {
		
		List<DBObject> roboFlies = aggregator.findRoboFliesWithWrongSoundIntensity();
		
		Assert.assertEquals(1, roboFlies.size());
		Assert.assertEquals("RoboFly_ID_2", roboFlies.get(0).get("_id"));
	}
	
	
	
	private void createInitialRoboFlies() {
		
		List<DBObject> roboFlies = new ArrayList<DBObject>();

		DBObject roboFly1 = new BasicDBObject("name", "Calliphora")
										.append("_id", "RoboFly_ID_1")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		DBObject roboFly2 = new BasicDBObject("name", "Lucilia")
										.append("_id", "RoboFly_ID_2")
										.append("constructionYear", 2014)
										.append("status", "OK");

		DBObject roboFly3 = new BasicDBObject("name", "Onesia")
										.append("_id", "RoboFly_ID_3")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		roboFlies.add(roboFly1);
		roboFlies.add(roboFly2);
		roboFlies.add(roboFly3);
		
		aggregator.saveRoboflies(roboFlies);
		
	}

	private void createInitialMeasurements() {
		
		List<DBObject> measurements = new ArrayList<DBObject>();
		
		DBObject measurement1 = new BasicDBObject("_id", "measurement_average_1")
					.append("timestamp", System.currentTimeMillis())
					.append("roboFlyID", "RoboFly_ID_1")
					.append("humidity", 20.01)
					.append("airPressure", 1013.25)
					.append("luminosity", 600)
					.append("soundIntensity", 55)
					.append("temperature", 17)
					.append("co2Content", 2.17); // shows difference -- very high value
		
		DBObject measurement2 = new BasicDBObject("_id", "measurement_average_2")
					.append("timestamp", System.currentTimeMillis())
					.append("roboFlyID", "RoboFly_ID_2")
					.append("humidity", 20.01)
					.append("airPressure", 1013.25)
					.append("luminosity", 600)
					.append("soundIntensity", 0)		// shows difference -- no value
					.append("temperature", 17)
					.append("co2Content", 0.04);		
			
		DBObject measurement3 = new BasicDBObject("_id", "measurement_average_3")
					.append("timestamp", System.currentTimeMillis())
					.append("roboFlyID", "RoboFly_ID_2")
					.append("humidity", 20.01)
					.append("airPressure", 1013.25)
					.append("luminosity", 600)
					.append("soundIntensity", 57)
					.append("temperature", 17)
					.append("co2Content", 0.5);
	
		
		aggregator.createDocumentReference("RoboFly_ID_1", measurement1);
		aggregator.createDocumentReference("RoboFly_ID_2", measurement2);
		aggregator.createDocumentReference("RoboFly_ID_2", measurement3);
		
		measurements.add(measurement1);
		measurements.add(measurement2);
		measurements.add(measurement3);
		
		aggregator.saveMeasurements(measurements);
	}
}
