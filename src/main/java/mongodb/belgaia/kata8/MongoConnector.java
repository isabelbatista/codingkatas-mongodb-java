package mongodb.belgaia.kata8;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import mongodb.belgaia.kata8.RoboFly.Status;

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

	private void initDatabaseElements() {	
		robofliesCollection = database.getCollection("roboflies");
	}
	
	public RoboFly getRoboFlyById(String roboFlyId) {
		
		DBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", roboFlyId);
		
		DBObject roboFlyDocument = robofliesCollection.findOne(searchQuery);
		return convertRoboFlyDocumentToRoboFly(roboFlyDocument);
	}

	public List<DBObject> findRoboFliesNearByBug(int countOfFlies) {
		
		return null;
	}
	
	public List<DBObject> addGeoIndexToBugroute() {
		bugrouteCollection.createIndex(new BasicDBObject("loc", "2dsphere"));
		return bugrouteCollection.getIndexInfo();
	}
	
	private RoboFly convertRoboFlyDocumentToRoboFly(DBObject roboFlyDocument) {
				
		String id = (String) roboFlyDocument.get("_id");
		String name = (String) roboFlyDocument.get("name");
		int constructionYear = (Integer) roboFlyDocument.get("constructionYear");
//		int serviceTime = (Integer) roboFlyDocument.get("serviceTime");
//		int size = (Integer) roboFlyDocument.get("size");
				
		RoboFly.Status roboFlyStatus = RoboFly.Status.valueOf((String) roboFlyDocument.get("status"));
//		RoboFly.Type roboFlyType = RoboFly.Type.valueOf((String) roboFlyDocument.get("type"));
		
		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder(id, name);
		roboFlyBuilder.constructionYear(constructionYear);
//		roboFlyBuilder.serviceTime(serviceTime);
//		roboFlyBuilder.size(size);
		roboFlyBuilder.status(roboFlyStatus);
//		roboFlyBuilder.type(roboFlyType);
		
		return roboFlyBuilder.build();
	}
}
