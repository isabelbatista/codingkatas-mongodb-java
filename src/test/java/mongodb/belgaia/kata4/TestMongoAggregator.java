package mongodb.belgaia.kata4;

import java.util.ArrayList;
import java.util.List;

import mongodb.belgaia.shared.RoboFly;

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
	public void testGetRoboFliesWithDifferingVolume() {
		
		List<RoboFly> toBeCalibratedRoboFlies = aggregator.getRoboFliesWithDifferingVolume();
		
		Assert.assertNotNull(toBeCalibratedRoboFlies);
		Assert.assertEquals(1, toBeCalibratedRoboFlies.size());
		
	}
	
	private void createInitialRoboFlies() {
		
		List<DBObject> roboFlies = new ArrayList<DBObject>();

		DBObject roboFly1 = new BasicDBObject("name", "Calliphora")
										.append("_id", "RoboFly_ID_1")
										.append("constructionYear", 2014)
										.append("size", 2)
										.append("serviceTime", 60)
										.append("status", "OK");
		
		DBObject roboFly2 = new BasicDBObject("name", "Lucilia")
										.append("_id", "RoboFly_ID_2")
										.append("constructionYear", 2014)
										.append("size", 2)
										.append("serviceTime", 60)
										.append("status", "OK");

		DBObject roboFly3 = new BasicDBObject("name", "Onesia")
										.append("_id", "RoboFly_ID_2")
										.append("constructionYear", 2014)
										.append("size", 2)
										.append("serviceTime", 60)
										.append("status", "OK");
		
		roboFlies.add(roboFly1);
		roboFlies.add(roboFly2);
		roboFlies.add(roboFly3);
		
		aggregator.saveRoboflies(roboFlies);
		
	}

	private void createInitialMeasurements() {
		
		System.out.println("To be implemented.");

		
	}
}
