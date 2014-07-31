package mongodb.belgaia.kata2dot2;


import mongodb.belgaia.kata2.MongoDbConnector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TestMongoDbConnector {
	
	private static final String DATABASE_NAME = "testmongodbkatas";
	private MongoDbConnector mongodbConnector;
	
	@Before
	public void setUp() {
		
		mongodbConnector = new MongoDbConnector(DATABASE_NAME);
		mongodbConnector.saveRoboFly(createRoboFlyDocument());
	}
	
	@Test
	public void shouldSetDocumentReference() {
		
		String roboFlyId = "RoboFly_ID_1";
		
		// one to many
		DBObject document = mongodbConnector.setDocumentReference("roboflies", roboFlyId, "robofly", getDummyMeasurementDocument());
		
		DBObject referencedDoc = mongodbConnector.getDocReference("measurements", "robofly");
		
		Assert.assertNotNull(referencedDoc);
	}
	
	private DBObject getDummyMeasurementDocument() {
		
		DBObject measurementDocument = new BasicDBObject("name", "measurement_average_1")
												.append("robofly", "RoboFly_ID_1")
												.append("luminidity", 20.01f);
												
												
		return measurementDocument;
	}
	
	private DBObject createRoboFlyDocument() {
		
		DBObject roboFlyDocument = new BasicDBObject("name", "Calliphora")
										.append("_id", "RoboFly_ID_1");
		
		return roboFlyDocument;
	}

}
