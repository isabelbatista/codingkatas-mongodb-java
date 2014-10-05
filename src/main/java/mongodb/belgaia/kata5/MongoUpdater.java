package mongodb.belgaia.kata5;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

public class MongoUpdater {

	private static final String DATABASE_NAME = "mobilerobotics";

	private Mongo client;
	private DB database;

	private DBCollection robofliesCollection;
	private DBCollection measurementsCollection;

	public MongoUpdater() {
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			initCollections();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public MongoUpdater(String databaseName) {
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(databaseName);
			initCollections();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	private void initCollections() {
		robofliesCollection = database.getCollection("roboflies");
		measurementsCollection = database.getCollection("measurements");
	}

	public void dropDatabase() {
		database.dropDatabase();
	}

	public void changeStatus(String roboFlyId, RoboFlyStatus status) {

		DBObject query = new BasicDBObject("_id", roboFlyId);
		DBObject update = new BasicDBObject().append("$set", new BasicDBObject("status", status.name));
		
		robofliesCollection.update(query, update);
	}
	
	public void addMoreInformation(String roboFlyId, Map<String, String> additionalInfo) {
		
		Iterator<Entry<String, String>> infoIterator = additionalInfo.entrySet().iterator();
		while(infoIterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) infoIterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			
			DBObject query = new BasicDBObject("_id", roboFlyId);
			DBObject update = new BasicDBObject("$set", new BasicDBObject(key, value));
			
			robofliesCollection.update(query, update);
		}
	}

	// Preparation for tests

	public void saveRoboflies(List<DBObject> roboFlies) {
		robofliesCollection.insert(roboFlies);
	}

	public void saveMeasurements(List<DBObject> measurements) {
		measurementsCollection.insert(measurements);
	}

	public void createDocumentReference(String roboFlyReferenceId,
			DBObject measurement) {

		DBRef docReference = new DBRef(database, "roboflies",
				roboFlyReferenceId);
		measurement.put("RoboFlyID", docReference);

		DBCollection collection = database.getCollection("measurements");
		collection.save(measurement);
	}

	public DBObject getRoboFly(String roboFlyId) {
		return robofliesCollection.findOne(new BasicDBObject("_id", roboFlyId));
	}
}
