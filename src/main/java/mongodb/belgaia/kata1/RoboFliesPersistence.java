package mongodb.belgaia.kata1;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class RoboFliesPersistence {

	private final String DATABASE_NAME = "mongodbkatas";
	private final String COLLECTION_NAME = "roboflies";

	private Mongo mongoDbClient;
	private DB database;
	private DBCollection robofliesCollection;

	public RoboFliesPersistence() throws MongoException {

		try {
			
			mongoDbClient = new Mongo("localhost", 27017);
			database = mongoDbClient.getDB(DATABASE_NAME);
			
			boolean collectionExists = database.collectionExists("COLLECTION_NAME");
		    if (collectionExists == false) {
		        robofliesCollection = database.createCollection("COLLECTION_NAME", null);
		    }
			
			robofliesCollection = database.getCollection(COLLECTION_NAME);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public String saveRoboFly(RoboFly roboFly) {

		DBObject document = RoboFly2DocumentConverter
				.convertRoboFly2Document(roboFly);

		robofliesCollection.insert(document);

		return document.get("_id").toString();
	}

	public List<String> saveRoboFlies(Map<String, RoboFly> roboFlies) {

		Map<String, DBObject> convertedDocuments = RoboFly2DocumentConverter
				.convertRoboFlies2Documents(roboFlies);
		
		for (Map.Entry<String, DBObject> document : convertedDocuments
				.entrySet()) {

			robofliesCollection.insert(document.getValue());

		}

		List<String> savedDocumentIds = new ArrayList<String>();

		for (Map.Entry<String, DBObject> document : convertedDocuments
				.entrySet()) {
			savedDocumentIds.add(document.getValue().get("_id").toString());
		}

		return savedDocumentIds;

	}

	public void dropDatabase() {

		database.dropDatabase();
	}

	private Map<String, RoboFly> getInitialSetOfFlies() {

		Map<String, RoboFly> roboFlyMap = new HashMap<String, RoboFly>();

		RoboFly roboFly1 = new RoboFly("Calliphora", 2014, 2, 60, Status.OK);
		roboFly1.setId("RoboFly_ID_1");
		roboFlyMap.put(roboFly1.getId(), roboFly1);

		RoboFly roboFly2 = new RoboFly("Lucilia", 2014, 2, 60, Status.OK);
		roboFly2.setId("RoboFly_ID_2");
		roboFlyMap.put(roboFly2.getId(), roboFly2);

		RoboFly roboFly3 = new RoboFly("Onesia", 2014, 2, 60, Status.OK);
		roboFly3.setId("RoboFly_ID_3");
		roboFlyMap.put(roboFly3.getId(), roboFly3);

		return roboFlyMap;
	}
	
	/**
	 * Solution for Kata 1 from the Book
	 * "Coding Katas for MongoDB"
	 * 
	 * Inserting an initial set of robo flies to mongodb.
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Solution for Kata 1");
		System.out.println("###################");
		System.out.println("");

		RoboFliesPersistence persistence = new RoboFliesPersistence();
		List<String> savedRoboFlyIds = persistence.saveRoboFlies(persistence.getInitialSetOfFlies());
		
		for (String roboFlyId : savedRoboFlyIds) {
			
			System.out.println("Saved robo fly with id: " + roboFlyId);
		}
		
	}
}
