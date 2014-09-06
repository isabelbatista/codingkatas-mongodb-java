package mongodb.belgaia.kata4;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
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
		calculateAverage("roboFlyID", "soundIntensity");
		return null;
	}
	
	public List<DBObject> calculateAverage(String groupingFieldName, String averageFieldName) {
		
		DBObject average = new BasicDBObject();
		average.put("average", new BasicDBObject("$avg", "$soundIntensity"));
		
		if(groupingFieldName != null) {
			average.put("_id", "$" + groupingFieldName);
		} else {
			average.put("_id", "allRoboFlies");
		}
		
		AggregationOutput output = measurementCollection.aggregate(new BasicDBObject("$group", average));
		
		List<DBObject> documentResults = new ArrayList<DBObject>();
		for(DBObject result : output.results()) {
			documentResults.add(result);
			System.out.println("Output: " + result);
		}
		
		return documentResults;

	}
	
//	private DBObject addAverage() {
//		
//		DBObject average = new BasicDBObject("_id", "$roboFlyID").append("average", new BasicDBObject("$avg", "$soundIntensity"));
//		return new BasicDBObject("$group", average);
//	}
		
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
