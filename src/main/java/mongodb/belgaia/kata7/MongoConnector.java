package mongodb.belgaia.kata7;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
	private DBCollection chargingCollection;

	private Mongo client;
	private DB database;
	
	public MongoConnector() {
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			initDatabaseElements();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
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
	
	public void addDocReferenceForCharges(String chargingSetId, String roboFlyId) {
		
		DBObject roboFly = robofliesCollection.findOne(new BasicDBObject("_id", roboFlyId));
		DBRef reference = new DBRef(database, "roboflies", (String) roboFly.get("_id"));
		chargingCollection.update(new BasicDBObject("_id", chargingSetId), new BasicDBObject("$set", new BasicDBObject("roboFlyRef", reference)));
	}
	
	public List<DBObject> getRoboflies() {
		DBCursor robofliesCursor = robofliesCollection.find();
		List<DBObject> roboflies = new ArrayList<DBObject>();
		
		while(robofliesCursor.hasNext()) {
			roboflies.add(robofliesCursor.next());
		}
		return roboflies;
	}
	
	public List<DBObject> getRoboFliesByDifferingStatus(RoboFlyStatus status, boolean withIndex) {
		
		DBObject query = new BasicDBObject("status", new BasicDBObject("$ne", status.name));

		if(withIndex) {
			// TODO: create Index
			createIndexOn(robofliesCollection, "status");
		}
		
		DBCursor result = robofliesCollection.find(query);
		
		List<DBObject> roboFlies = new ArrayList<DBObject>();
		while(result.hasNext()) {
			roboFlies.add(result.next());
		}
		return roboFlies;
	}
	
	public List<DBObject> getChargingSets() {
		DBCursor chargingCursor = chargingCollection.find();
		List<DBObject> charging = new ArrayList<DBObject>();
		
		while(chargingCursor.hasNext()) {
			charging.add(chargingCursor.next());
		}
		return charging;
	}
	
	public void dropDatabase() {
		database.dropDatabase();
	}
	
	private void createIndexOn(DBCollection collectionName, String fieldName) {
		collectionName.createIndex(new BasicDBObject(fieldName, 1));
	}
	
	private void initDatabaseElements() {
		
		robofliesCollection = database.getCollection("roboflies");
		profilesCollection = database.getCollection("profiles");
		chargingCollection = database.getCollection("charging");
	}

}
