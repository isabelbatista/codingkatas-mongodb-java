package mongodb.belgaia.kata4;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	
	public void closeConnection() {
		client.close();
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
	
	private void initDatabaseElements() {
		
		roboFlyCollection = database.getCollection("roboflies");
		measurementCollection = database.getCollection("measurements");
	}
	
	public Double calculateAverage(String averageFieldName) {
		
		DBObject match = new BasicDBObject().append("$match", new BasicDBObject(averageFieldName, new BasicDBObject("$gt", 0.00)));		
		DBObject groupFields = new BasicDBObject("_id", "allRoboFlies");
		groupFields.put("average", new BasicDBObject("$avg", "$" + averageFieldName));
		DBObject group = new BasicDBObject("$group", groupFields);
		
		AggregationOutput output = measurementCollection.aggregate(match, group);
		
		if (output != null) {
			Iterator<DBObject> resultIterator = output.results().iterator();
			if (resultIterator.hasNext()) {
				return (Double) resultIterator.next().get("average");
			}
		}
		return new Double(0.0);
	}
	
	public Set<String> findBadValues() {
		
		final Set<String> badValueDocs = new HashSet<String>();
		
		Set<String> fieldNames = measurementCollection.findOne().keySet();
		Iterator<String> fieldIterator = fieldNames.iterator();
		
		while(fieldIterator.hasNext()) {
			String field = (String) fieldIterator.next();
			DBObject match = new BasicDBObject().append("$match", new BasicDBObject(field, 0));
			AggregationOutput result = measurementCollection.aggregate(match);
			
			for (DBObject doc : result.results()) {
				DBRef roboFly = (DBRef) doc.get("RoboFlyID");
				badValueDocs.add((String) roboFly.getId()); 
			}
		}
		return badValueDocs;
	}
	
	public void markRoboFliesToBeCalibrated(RoboFlyStatus status) {
		
		Set<String> roboFlyIds = findBadValues();
		
		Iterator<String> roboFlyIterator = roboFlyIds.iterator();
		while(roboFlyIterator.hasNext()) {
			
			String currentRoboFly = roboFlyIterator.next();
			DBObject roboFly = roboFlyCollection.findOne(new BasicDBObject("_id", currentRoboFly));
			updateRoboFly((String) currentRoboFly, roboFly, status);
		}
	}
	
	private void updateRoboFly(String roboFlyId, DBObject updateRoboFly, RoboFlyStatus status) {
		
		DBObject query = new BasicDBObject("_id", roboFlyId);
		
		DBObject update = new BasicDBObject().append("$set", new BasicDBObject("status", status.name));
		
		roboFlyCollection.update(query, update);
	}
	
	// Preparation for tests
	
	public void saveRoboflies(List<DBObject> roboFlies) {		
		roboFlyCollection.insert(roboFlies);		
	}
	
	public void saveMeasurements(List<DBObject> measurements) {
		measurementCollection.insert(measurements);
	}
	
	public void createDocumentReference(String roboFlyReferenceId, DBObject measurement) {
					
		DBRef docReference = new DBRef(database, "roboflies", roboFlyReferenceId);
		measurement.put("RoboFlyID", docReference);
		
		DBCollection collection = database.getCollection("measurements");
		collection.save(measurement);
	}
	
	public DBObject getRoboFly(String roboFlyId) {
		return roboFlyCollection.findOne(new BasicDBObject("_id", roboFlyId));
	}
	
	public void dropDatabase() {
		database.dropDatabase();
	}
}
