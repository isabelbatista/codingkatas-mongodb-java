package mongodb.belgaia.kata5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TestKata5 {
	
	private static final String DATABASE_NAME = "kataTest";
	private MongoUpdater kata;
	
	@Before
	public void setUp() {
		kata = new MongoUpdater(DATABASE_NAME);
		
		createInitialRoboFlies();
		createInitialMeasurements();
	}
	
	@After
	public void tearDown() {
		kata.dropDatabase();
	}
	
	@Test
	public void shouldChangeStatusOfDamagedRobofly() {
		
		kata.changeStatus("RoboFly_ID_2", RoboFlyStatus.OUT_OF_SERVICE);
		
		DBObject roboFly = kata.getRoboFly("RoboFly_ID_2");
		Assert.assertEquals(RoboFlyStatus.OUT_OF_SERVICE.name, (String) roboFly.get("status"));
	}
	
	@Ignore
	@Test
	public void shouldAddMoreInformationToDamagedFlies() {
		
		Map<String, String> additionalInfo = createDummyAdditionalInformation(StatusDescription.DAMAGED);
		kata.addMoreInformation("RoboFly_ID_2", additionalInfo);
		
		DBObject roboFly = kata.getRoboFly("RoboFly_ID_2");
		Assert.assertEquals(StatusDescription.DAMAGED.name(), (String)roboFly.get("statusDescription"));
	}
	
	private Map<String, String> createDummyAdditionalInformation(StatusDescription statusDesc) {
		
		String statusDescKey = "statusDescription";
		Map<String, String> additionalInfo = new HashMap<String, String>();
		additionalInfo.put(statusDescKey, statusDesc.name());
		additionalInfo.put("comment", "Dummy Text");
		additionalInfo.put("endOfLife", "30.04.14");
		
		return additionalInfo;		
	}
	
	private void createInitialRoboFlies() {
		
		List<DBObject> roboFlies = new ArrayList<DBObject>();

		DBObject roboFly1 = new BasicDBObject("name", "Calliphora")
										.append("type", RoboFlyType.FLY.name)
										.append("_id", "RoboFly_ID_1")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		DBObject roboFly2 = new BasicDBObject("name", "Lucilia")
										.append("type", RoboFlyType.FLY.name)
										.append("_id", "RoboFly_ID_2")
										.append("constructionYear", 2014)
										.append("status", "OK");

		DBObject roboFly3 = new BasicDBObject("name", "Ischnura")
										.append("type", RoboFlyType.DRAGONFLY.name)
										.append("_id", "RoboFly_ID_3")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		DBObject roboFly4 = new BasicDBObject("name", "Calopteryx")
										.append("type", RoboFlyType.DRAGONFLY.name)
										.append("_id", "RoboFly_ID_4")
										.append("constructionYear", 2014)
										.append("status", "OK");
		
		roboFlies.add(roboFly1);
		roboFlies.add(roboFly2);
		roboFlies.add(roboFly3);
		roboFlies.add(roboFly4);
		kata.saveRoboflies(roboFlies);
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
		
		kata.createDocumentReference("RoboFly_ID_1", measurement1);
		kata.createDocumentReference("RoboFly_ID_1", measurement4);
		kata.createDocumentReference("RoboFly_ID_2", measurement2);
		kata.createDocumentReference("RoboFly_ID_2", measurement3);
		
		measurements.add(measurement1);
		measurements.add(measurement2);
		measurements.add(measurement3);
		measurements.add(measurement4);
		
		kata.saveMeasurements(measurements);
	}
	
	
}
