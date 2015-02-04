package mongodb.belgaia.kata9;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

class MongoConnector {
	
	private static final String ROUTE_LOCATION_FIELD_KEY = "location";
	private static final String ROUTE_COORDINATES_FIELD_KEY = "coordinates";
	private static final String ROBOFLY_COORDINATES_FIELD_NAME = "currentLocation_2d";
	private static final String DATABASE_NAME = "mobilerobotics";
	private static final String ROBOFLY_COORDINATES_FIELD_KEY = "currentLocation";

	private Mongo client;
	private DB database;
	
	private DBCollection robofliesCollection;
	private DBCollection profilesCollection;
	private DBCollection routesCollection;
	
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
	
	private void initDatabaseElements() {	
		robofliesCollection = database.getCollection("roboflies");
		profilesCollection = database.getCollection("profiles");
		routesCollection = database.getCollection("routes");
	}

	public void dropDatabase() {
		database.dropDatabase();
	}
	
	
	public void addDocReferenceForProfiles(String roboFlyId, String profileType) {
		
		DBObject profile = profilesCollection.findOne(new BasicDBObject("type", profileType));
		DBRef reference = new DBRef(database, "profiles", (String) profile.get("_id"));
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), new BasicDBObject("$set", new BasicDBObject("typeRef", reference)));
	}
//	
//	public List<RoboFly> getRoboflies() {
//		DBCursor robofliesCursor = robofliesCollection.find();
//		List<RoboFly> roboflies = new ArrayList<RoboFly>();
//		
//		while(robofliesCursor.hasNext()) {
//			RoboFly roboFly = convertRoboFlyDocumentToRoboFly(robofliesCursor.next());
//			roboflies.add(roboFly);
//		}
//		return roboflies;
//	}
//	
	public List<DBObject> getRoboflyDocuments() {
		DBCursor robofliesCursor = robofliesCollection.find();
		List<DBObject> roboflies = new ArrayList<DBObject>();
		
		while(robofliesCursor.hasNext()) {
			roboflies.add(robofliesCursor.next());
		}
		return roboflies;
	}
//
//	
	
//
//	public List<RoboFly> findRoboFliesNearByBug(double[] bugCoordinates, int countOfFlies) {
//		
//		BasicDBList bugLocation = new BasicDBList();
//		bugLocation.put(0, bugCoordinates[0]);
//		bugLocation.put(1, bugCoordinates[1]);
//	
//		DBObject query = new BasicDBObject("currentLocation", new BasicDBObject("$near", bugLocation));
//		
//		DBCursor roboFlies = robofliesCollection.find(query);
//	    
//	    List<RoboFly> nearestRoboFlies = new ArrayList<RoboFly>();
//	    int roboFliesCounter = 0;
//	    
//	    while(roboFliesCounter < countOfFlies && roboFlies.hasNext()) {
//	    	nearestRoboFlies.add(convertRoboFlyDocumentToRoboFly(roboFlies.next()));
//	    	roboFliesCounter++;
//	    }
//		
//		return nearestRoboFlies;
//	}
//	
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
		
		roboFlyDocument.put(ROBOFLY_COORDINATES_FIELD_KEY, coordinates);
		
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), roboFlyDocument);
	}
	
	private void createGeoIndexForRoboflies() {
		robofliesCollection.createIndex(new BasicDBObject("currentLocation", "2d"));
	}
//	
//	public double[] getRoboFlyCoordinates(String roboFlyId) {
//		
//		DBObject searchQuery = new BasicDBObject("_id", roboFlyId);
//		DBObject roboFly = robofliesCollection.findOne(searchQuery);
//		
//		BasicDBList coordinateList = (BasicDBList) roboFly.get(COORDINATES_FIELD_NAME);
//		
//		double[] coordinateArray = { (Double) coordinateList.get(0), (Double) coordinateList.get(1)};
//		
// 		return coordinateArray;
//	}
//	
//	public List<DBObject> addGeoIndexToBugroute() {
//		bugrouteCollection.createIndex(new BasicDBObject("loc", "2dsphere"));
//		return bugrouteCollection.getIndexInfo();
//	}
//	
//	private RoboFly convertRoboFlyDocumentToRoboFly(DBObject roboFlyDocument) {
//				
//		Profile profile = getRoboFlyProfileFromRoboFlyDocument(roboFlyDocument);
//		
//		String id = (String) roboFlyDocument.get("_id");
//		String name = (String) roboFlyDocument.get("name");
//		int constructionYear = (Integer) roboFlyDocument.get("constructionYear");
//				
//		RoboFly.Status roboFlyStatus = RoboFly.Status.getStatusByName((String) roboFlyDocument.get("status"));
//		
//		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder(id, name);
//		roboFlyBuilder.constructionYear(constructionYear);
//		roboFlyBuilder.serviceTime(profile.getServiceTime());
//		roboFlyBuilder.size(profile.getSize());
//		roboFlyBuilder.status(roboFlyStatus);
//		roboFlyBuilder.type(profile.getRoboflyType());
//		
//		return roboFlyBuilder.build();
//	}
//	

	public void change2dCoordinatesFieldTo2dSphere(String roboFlyId) {
		
		remove2dGeoIndexFromRoboflies();
		add2dSphereGeoIndexToRoboflies();
		
	}
	
	private void add2dSphereGeoIndexToRoboflies() {
		robofliesCollection.createIndex(new BasicDBObject(ROBOFLY_COORDINATES_FIELD_KEY, "2dsphere"));
	}

	private void remove2dGeoIndexFromRoboflies() {
		robofliesCollection.dropIndex(ROBOFLY_COORDINATES_FIELD_NAME);
	}

	
	public String getTypeOfRoboFlyCoordinatesIndex() {
		
		List<DBObject> indexInformation = robofliesCollection.getIndexInfo();
		String typeOfIndex = null;
		
		for(DBObject oneIndexInfo : indexInformation) {
			
			System.out.println(oneIndexInfo);
			
			DBObject keyDoc = (DBObject) oneIndexInfo.get("key");
			System.out.println(keyDoc);
			
			if(keyDoc.containsField(ROBOFLY_COORDINATES_FIELD_KEY)) {
				typeOfIndex = (String) keyDoc.get(ROBOFLY_COORDINATES_FIELD_KEY);
			}
		}
		
		return typeOfIndex;
	}

	public void createBugRouteDocument(Map<String, double[]> bugrouteCoordinates) {
		
		DBObject bugrouteDocument = new BasicDBObject("_id", "BUGROUTE_1");
		DBObject location = new BasicDBObject("type", "Polygon");
		
		double[][] coordinates = createCoordinatesList(bugrouteCoordinates);
		
		DBObject coordinatesList = new BasicDBObject(ROUTE_COORDINATES_FIELD_KEY, coordinates);
		location.putAll(coordinatesList);
		
		bugrouteDocument.put(ROUTE_LOCATION_FIELD_KEY, location);
		
		routesCollection.insert(bugrouteDocument);
	}

	private double[][] createCoordinatesList(
			Map<String, double[]> bugrouteCoordinates) {
		double[][] coordinates = new double[bugrouteCoordinates.size()+1][2];
		int position = 0;
		double[] firstCoordinate = null;
		
		for(Map.Entry<String, double[]> entry : bugrouteCoordinates.entrySet()) {
			
			double[] coordinatePair = entry.getValue();
			if(position == 0) {
				firstCoordinate = coordinatePair;
			}
			
			coordinates[position] = coordinatePair;
			position++;			
		}
		
		if(firstCoordinate != null) {
			coordinates[position] = firstCoordinate;
		}
		return coordinates;
	}

	@SuppressWarnings("unchecked")
	public List<double[]> getBugRouteCoordinateList() {
		DBObject bugRouteDoc = routesCollection.findOne();
		DBObject location = (DBObject) bugRouteDoc.get(ROUTE_LOCATION_FIELD_KEY);
		return (List<double[]>) location.get(ROUTE_COORDINATES_FIELD_KEY);
	}
}
