package mongodb.belgaia.kata4;

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
	
	@Test
	public void shouldReturnRoboFlyWhereSoundIntensityIsTooLow() {
		
		RoboFly roboFlyToBeCalibrated = aggregator.findRoboFlyWithWrongSoundIntensity();
		
		
		
		
		String expectedRoboFlyId = "RoboFly_ID_2";
		
		
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
					.append("co2Content", 0.04);
		
		DBObject measurement2 = new BasicDBObject("_id", "measurement_average_2")
					.append("timestamp", System.currentTimeMillis())
					.append("roboFlyID", "RoboFly_ID_2")
					.append("humidity", 20.01)
					.append("airPressure", 1013.25)
					.append("luminosity", 600)
					.append("soundIntensity", 0)		// shows difference -- no value
					.append("temperature", 17)
					.append("co2Content", 2.17);		// shows difference -- very high value
			
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
