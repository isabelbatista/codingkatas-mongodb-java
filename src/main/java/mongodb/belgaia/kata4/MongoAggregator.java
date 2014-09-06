package mongodb.belgaia.kata4;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

public class MongoAggregator {

	private static final String DATABASE_NAME = "mobilerobotics";
	private Mongo client;
	private DB database;
	
	private DBCollection roboFlyCollection;
	private DBCollection measurementCollection;

	public MongoAggregator() {
		
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			
			initDatabaseElements();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MongoAggregator(String databaseName) {
		
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(databaseName);
			
			initDatabaseElements();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveRoboflies(List<DBObject> roboFlies) {		
		roboFlyCollection.insert(roboFlies);		
	}
	
	public void saveMeasurements(List<DBObject> measurements) {
		measurementCollection.insert(measurements);
	}
	
	public RoboFly findRoboFlyWithWrongSoundIntensity() {
		
		// group by roboFly // average of soundIntensity
		calcAverage();
		return null;
	}
	
	private void calcAverage() {
		
		DBObject average = addAverage();
		
		AggregationOutput output = measurementCollection.aggregate(average);
		for(DBObject result : output.results()) {
			System.out.println("Output: " + result);
		}

	}
	
	private DBObject addAverage() {
		
		// calculates the average of documents of each robofly separately
//		DBObject groupFields = new BasicDBObject( "_id", "$roboFlyID");
//		groupFields.put("average", new BasicDBObject( "$avg", "$soundIntensity"));
		
		// calculates the average of all documents of the collection
		DBObject average = new BasicDBObject("_id", "$roboFlyID").append("average", new BasicDBObject("$avg", "$soundIntensity"));
		return new BasicDBObject("$group", average);
	}
		
	private DBObject addProjection() {
		
		DBObject fields = new BasicDBObject("roboFlyId", 1);
		fields.put("soundIntensity", 1);
		return new BasicDBObject("$project", fields);
	}
	
	public void createDocumentReference(String roboFlyReferenceId, DBObject measurement) {
					
		DBRef docReference = new DBRef(database, "roboflies", roboFlyReferenceId);
		measurement.put("robofly", docReference);
		
		DBCollection collection = database.getCollection("measurements");
		collection.save(measurement);
	}
	
	private void initDatabaseElements() {
		
		roboFlyCollection = database.getCollection("roboflies");
		measurementCollection = database.getCollection("measurements");
	}

}
