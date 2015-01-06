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
	private DBCollection profilesCollection;
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
	
	public void addDocReferenceForProfiles(String roboFlyId, String profileType) {
		
		DBObject profile = profilesCollection.findOne(new BasicDBObject("type", profileType));
		DBRef reference = new DBRef(database, "profiles", (String) profile.get("_id"));
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), new BasicDBObject("$set", new BasicDBObject("typeRef", reference)));
	}
	
	public List<DBObject> getRoboflies() {
		DBCursor robofliesCursor = robofliesCollection.find();
		List<DBObject> roboflies = new ArrayList<DBObject>();
		
		while(robofliesCursor.hasNext()) {
			roboflies.add(robofliesCursor.next());
		}
		return roboflies;
	}
	
	public List<DBObject> getProfiles() {
		DBCursor profilesCursor = profilesCollection.find();
		List<DBObject> profiles = new ArrayList<DBObject>();
		
		while(profilesCursor.hasNext()) {
			profiles.add(profilesCursor.next());
		}
		return profiles;
	}

	private void initDatabaseElements() {	
		robofliesCollection = database.getCollection("roboflies");
		profilesCollection = database.getCollection("profiles");
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
				
		Profile profile = getRoboFlyProfileFromRoboFlyDocument(roboFlyDocument);
		
		String id = (String) roboFlyDocument.get("_id");
		String name = (String) roboFlyDocument.get("name");
		int constructionYear = (Integer) roboFlyDocument.get("constructionYear");
		RoboFly.Status roboFlyStatus = RoboFly.Status.valueOf((String) roboFlyDocument.get("status"));
		
		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder(id, name);
		roboFlyBuilder.constructionYear(constructionYear);
		roboFlyBuilder.serviceTime(profile.getServiceTime());
		roboFlyBuilder.size(profile.getSize());
		roboFlyBuilder.status(roboFlyStatus);
		roboFlyBuilder.type(profile.getRoboflyType());
		
		return roboFlyBuilder.build();
	}
	
	private Profile getRoboFlyProfileFromRoboFlyDocument(DBObject roboFly) {
		
		DBRef databaseReference = (DBRef) roboFly.get("typeRef");
		DBObject profileDocument = profilesCollection.findOne(databaseReference.getId());
	
		Profile profile = new Profile();
		profile.setId((String) profileDocument.get("_id"));
		profile.setServiceTime((Integer) profileDocument.get("serviceTime"));
		profile.setSize((Integer) profileDocument.get("size"));
		
		RoboFly.Type roboFlyType = RoboFly.Type.valueOf((String) profileDocument.get("type"));
		profile.setRoboflyType(roboFlyType);
		
		return profile;
	}
}
