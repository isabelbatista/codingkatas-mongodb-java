package mongodb.belgaia.kata8;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

class MongoConnector {
	
	private static final String DATABASE_NAME = "mobilerobotics";
	private static final String COORDINATES_FIELD_NAME = "currentLocation";

	private Mongo client;
	private DB database;
	
	private DBCollection robofliesCollection;
	private DBCollection profilesCollection;
	
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
		client.close();
	}
	
	public void addDocReferenceForProfiles(String roboFlyId, String profileType) {
		
		DBObject profile = profilesCollection.findOne(new BasicDBObject("type", profileType));
		DBRef reference = new DBRef(database, "profiles", (String) profile.get("_id"));
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), new BasicDBObject("$set", new BasicDBObject("typeRef", reference)));
	}
	
	public List<RoboFly> getRoboflies() {
		DBCursor robofliesCursor = robofliesCollection.find();
		List<RoboFly> roboflies = new ArrayList<RoboFly>();
		
		while(robofliesCursor.hasNext()) {
			RoboFly roboFly = convertRoboFlyDocumentToRoboFly(robofliesCursor.next());
			roboflies.add(roboFly);
		}
		return roboflies;
	}
	
	public List<DBObject> getRoboflyDocuments() {
		DBCursor robofliesCursor = robofliesCollection.find();
		List<DBObject> roboflies = new ArrayList<DBObject>();
		
		while(robofliesCursor.hasNext()) {
			roboflies.add(robofliesCursor.next());
		}
		return roboflies;
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

	public List<RoboFly> findRoboFliesNearByBug(double[] bugCoordinates, int countOfFlies) {
		
		BasicDBList bugLocation = new BasicDBList();
		bugLocation.put(0, bugCoordinates[0]);
		bugLocation.put(1, bugCoordinates[1]);
	
		DBObject query = new BasicDBObject("currentLocation", new BasicDBObject("$near", bugLocation));
		
		DBCursor roboFlies = robofliesCollection.find(query);
	    
	    List<RoboFly> nearestRoboFlies = new ArrayList<RoboFly>();
	    int roboFliesCounter = 0;
	    
	    while(roboFliesCounter < countOfFlies && roboFlies.hasNext()) {
	    	nearestRoboFlies.add(convertRoboFlyDocumentToRoboFly(roboFlies.next()));
	    	roboFliesCounter++;
	    }
		
		return nearestRoboFlies;
	}
	
	/**
	 * Adds a new field for coordinates of the roboflies and creates a new index of all
	 * roboflies as 2D mongodb legacy coordinates.
	 * @param roboFlyId		robofly ID that has to be updated with coordinates field
	 * @param longAndLat	the longitude and latitude array to store at the coordinates field
	 */
	public void addGeoIndexToRoboFly(String roboFlyId, double[] longAndLat) {
		
		addGeoCoordinatesToRoboFly(roboFlyId, longAndLat);
		createGeoIndexForRoboflies();
	}

	private void addGeoCoordinatesToRoboFly(String roboFlyId,
			double[] longAndLat) {
		BasicDBList coordinates = new BasicDBList();
		coordinates.put(0, longAndLat[0]);
		coordinates.put(1, longAndLat[1]);
		
		DBObject searchQuery = new BasicDBObject("_id", roboFlyId);
		DBObject roboFlyDocument = robofliesCollection.findOne(searchQuery);
		
		roboFlyDocument.put(COORDINATES_FIELD_NAME, coordinates);
		
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), roboFlyDocument);
	}
	
	public double[] getRoboFlyCoordinates(String roboFlyId) {
		
		DBObject searchQuery = new BasicDBObject("_id", roboFlyId);
		DBObject roboFly = robofliesCollection.findOne(searchQuery);
		
		BasicDBList coordinateList = (BasicDBList) roboFly.get(COORDINATES_FIELD_NAME);
		
		double[] coordinateArray = { (Double) coordinateList.get(0), (Double) coordinateList.get(1)};
		
 		return coordinateArray;
	}
	
	private RoboFly convertRoboFlyDocumentToRoboFly(DBObject roboFlyDocument) {
				
		Profile profile = getRoboFlyProfileFromRoboFlyDocument(roboFlyDocument);
		
		String id = (String) roboFlyDocument.get("_id");
		String name = (String) roboFlyDocument.get("name");
		int constructionYear = (Integer) roboFlyDocument.get("constructionYear");
				
		RoboFly.Status roboFlyStatus = RoboFly.Status.getStatusByName((String) roboFlyDocument.get("status"));
		
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

	public void createGeoIndexForRoboflies() {
		robofliesCollection.createIndex(new BasicDBObject("currentLocation", "2d"));
	}
	
	public List<DBObject> getIndexesOfCollection(String collectionName) {
		return robofliesCollection.getIndexInfo();
	}	
}
