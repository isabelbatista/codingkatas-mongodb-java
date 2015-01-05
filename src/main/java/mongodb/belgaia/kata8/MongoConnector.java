package mongodb.belgaia.kata8;

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

	private Mongo client;
	private DB database;
	
	private DBCollection robofliesCollection;
	private DBCollection roboflyStationsCollection;
	private DBCollection bugrouteCollection;
	
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
	
	public void dropDatabase() {
		database.dropDatabase();
	}
	
	public List<DBObject> getAllRoboFlyStations() {
		DBCursor roboFlyStationCursor = roboflyStationsCollection.find();
		
		List<DBObject> roboFlyStations = new ArrayList<DBObject>();
		while(roboFlyStationCursor.hasNext()) {
			roboFlyStations.add(roboFlyStationCursor.next());
		}
		return roboFlyStations;
	}
	
	public void addDocReferenceForRoboFly2Station(String stationId, String roboFlyId) {
		DBRef reference = new DBRef(database, "roboflies", roboFlyId);
		roboflyStationsCollection.update(new BasicDBObject("_id", stationId), new BasicDBObject("$set", new BasicDBObject("roboFlyRef", reference)));
	}

	private void initDatabaseElements() {	
		robofliesCollection = database.getCollection("roboflies");
		roboflyStationsCollection = database.getCollection("roboflystations");
		bugrouteCollection = database.getCollection("bugroute");
	}

	public List<DBObject> findRoboFliesNearByBug(int countOfFlies) {
		
		return null;
	}
	
	public List<DBObject> addGeoIndexToBugroute() {
		bugrouteCollection.createIndex(new BasicDBObject("loc", "2dsphere"));
		return bugrouteCollection.getIndexInfo();
	}
	
	public List<DBObject> addGeoIndexForRoboFlyStations() {
		roboflyStationsCollection.createIndex(new BasicDBObject("loc", "2dsphere"));
		return roboflyStationsCollection.getIndexInfo();
	}
}
