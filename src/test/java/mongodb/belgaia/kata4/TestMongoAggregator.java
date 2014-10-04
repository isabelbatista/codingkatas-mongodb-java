package mongodb.belgaia.kata4;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mongodb.belgaia.kata4.MongoAggregator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TestMongoAggregator {
	
	private static final String DATABASE_NAME = "test";
	private MongoAggregator aggregator;
	
	@Before
	public void setUp() {
		aggregator = new MongoAggregator(DATABASE_NAME);
		createInitialRoboFlies();
		createInitialMeasurements();
	}
	
	@Test
	public void shouldReturnBadValueRoboFlies() {
		
		Set<String> roboFlyIds = aggregator.findBadValues();
		
		// 1, 2, 4
		Assert.assertEquals(2, roboFlyIds.size());
		Assert.assertTrue(roboFlyIds.contains("RoboFly_ID_1"));
		Assert.assertTrue(roboFlyIds.contains("RoboFly_ID_2"));
	}
	
	@Test
	public void shouldCalculateAverageOfSoundIntensity() {
		
		List<DBObject> averages = aggregator.calculateAverage("soundIntensity");
		Assert.assertEquals(1, averages.size());
		Assert.assertEquals(55.333333333333336, averages.get(0).get("average"));
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
					.append("luminosity", 600.0)
					.append("soundIntensity", 55.0)
					.append("temperature", 17.0)
					.append("co2Content", 0.00); // no value
		
		DBObject measurement2 = new BasicDBObject("_id", "measurement_average_2")
					.append("timestamp", System.currentTimeMillis())
					.append("roboFlyID", "RoboFly_ID_2")
					.append("humidity", 20.01)
					.append("airPressure", 1013.25)
					.append("luminosity", 600.0)
					.append("soundIntensity", 0.0)		// no value
					.append("temperature", 17.0)
					.append("co2Content", 0.4);		
			
		DBObject measurement3 = new BasicDBObject("_id", "measurement_average_3")
					.append("timestamp", System.currentTimeMillis())
					.append("roboFlyID", "RoboFly_ID_3")
					.append("humidity", 20.01)
					.append("airPressure", 1013.25)
					.append("luminosity", 600.0)
					.append("soundIntensity", 57.0)
					.append("temperature", 17.0)
					.append("co2Content", 0.5);
		
		DBObject measurement4 = new BasicDBObject("_id", "measurement_average_4")
					.append("timestamp", System.currentTimeMillis())
					.append("roboFlyID", "RoboFly_ID_1")
					.append("humidity", 0.00) // no value
					.append("airPressure", 1013.25)
					.append("luminosity", 600.0)
					.append("soundIntensity", 54.0)
					.append("temperature", 17.0)
					.append("co2Content", 4.17); 
		
		aggregator.createDocumentReference("RoboFly_ID_1", measurement1);
		aggregator.createDocumentReference("RoboFly_ID_1", measurement4);
		aggregator.createDocumentReference("RoboFly_ID_2", measurement2);
		aggregator.createDocumentReference("RoboFly_ID_2", measurement3);
		
		measurements.add(measurement1);
		measurements.add(measurement2);
		measurements.add(measurement3);
		measurements.add(measurement4);
		
		aggregator.saveMeasurements(measurements);
	}
}
