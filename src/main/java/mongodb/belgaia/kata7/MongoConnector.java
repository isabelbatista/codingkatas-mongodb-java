package mongodb.belgaia.kata7;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

class MongoConnector {

	private static final String DATABASE_NAME = "mobilerobotics";
	
	private DBCollection robofliesCollection;
	private DBCollection profilesCollection;
	private DBCollection measurementCollection;

	private Mongo client;
	private DB database;
	
	public MongoConnector() {
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			initDatabaseElements();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public MongoConnector(String databaseName) {

		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(databaseName);
			initDatabaseElements();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importData2MongoDb(String fileName, String collectionName) {
		
		String exportType = "csv";
		String mongoimportCommand = "mongoimport -d " + database.getName()
											 + " -c " + collectionName
											 + " --type " + exportType
											 + " --file " + fileName
											 + " --headerline";
		try {
			Runtime.getRuntime().exec(mongoimportCommand);
		} catch (IOException e) {
			System.err.println("Importing data with mongoimport threw an error: " + e.getMessage());
		}
	}
	
	public void addDocReferenceForProfiles(String roboFlyId, String profileType) {
		DBObject profile = profilesCollection.findOne(new BasicDBObject("type", profileType));
		DBRef reference = new DBRef(database, "profiles", (String) profile.get("_id"));
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), new BasicDBObject("$set", new BasicDBObject("typeRef", reference)));
	}
	
	public void addDocReferenceForMeasurement2RoboFly(String measurementId, String roboFlyId) {
		DBRef reference = new DBRef(database, "roboflies", roboFlyId);
		measurementCollection.update(new BasicDBObject("_id", measurementId), new BasicDBObject("$set", new BasicDBObject("roboFlyRef", reference)));
	}
	
	public List<DBObject> getRoboflies() {
		DBCursor robofliesCursor = robofliesCollection.find();
		List<DBObject> roboflies = new ArrayList<DBObject>();
		
		while(robofliesCursor.hasNext()) {
			roboflies.add(robofliesCursor.next());
		}
		return roboflies;
	}
	
	public List<DBObject> getMeasurements() {
		DBCursor measurementCursor = measurementCollection.find();
		List<DBObject> measurements = new ArrayList<DBObject>();
		
		while(measurementCursor.hasNext()) {
			measurements.add(measurementCursor.next());
		}
		return measurements;
	}
	
	public List<DBObject> getRoboFliesByDifferingStatus(RoboFlyStatus status) {
		
		DBObject query = new BasicDBObject("status", new BasicDBObject("$ne", status.name));
		DBCursor result = robofliesCollection.find(query);
		
		List<DBObject> roboFlies = new ArrayList<DBObject>();
		while(result.hasNext()) {
			roboFlies.add(result.next());
		}
		return roboFlies;
	}
	
	public Double calculateAverageOfSoundIntensity() {
		DBObject groupFields = new BasicDBObject("_id", "allRoboFlies");
		groupFields.put("average", new BasicDBObject("$avg", "$" + "soundIntensity"));
		DBObject group = new BasicDBObject("$group", groupFields);
		
		AggregationOutput output = measurementCollection.aggregate(group);
		
		if (output != null) {
			Iterator<DBObject> resultIterator = output.results().iterator();
			if (resultIterator.hasNext()) {
				return (Double) resultIterator.next().get("average");
			}
		}
		return new Double(0.0);
	}
	
	public void createIndexOnRoboFlyStatus() {
		createIndexOn(robofliesCollection, "status");
	}
	
	public int getCountOfRoboFlies(RoboFlyType type) {
		
		String roboFlyType = convertRoboFlyType2ProfileId(type);
		DBRef typeReference = new DBRef(database, "profiles", roboFlyType);
		DBObject query = new BasicDBObject("typeRef", typeReference);
		DBCursor roboFlyCursor = robofliesCollection.find(query);
		return roboFlyCursor.size();
	}

	public void createIndexOnRoboFlyType() {
		createIndexOn(robofliesCollection, "typeRef");
		createIndexOn(profilesCollection, "type");
	}

	public void createIndexOnSoundIntensity() {
		createIndexOn(measurementCollection, "soundIntensity");		
	}

	public void dropDatabase() {
		database.dropDatabase();
		client.close();
	}
	
	private void createIndexOn(DBCollection collection, String fieldName) {
		collection.createIndex(new BasicDBObject(fieldName, 1));
	}
	
	private String convertRoboFlyType2ProfileId(RoboFlyType type) {
		String convertedType = null;
		
		if(type.equals(RoboFlyType.FLY)) {
			convertedType = "ROBOFLY_ID_FLY";
		}
		return convertedType;
	}
	
	private void initDatabaseElements() {
		
		robofliesCollection = database.getCollection("roboflies");
		profilesCollection = database.getCollection("profiles");
		measurementCollection = database.getCollection("measurements");
	}

}
