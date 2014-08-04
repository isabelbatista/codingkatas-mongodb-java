package mongodb.belgaia.kata1;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mongodb.belgaia.shared.RoboFly;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Class to save roboFlies as solution for Kata 1 of the book
 * "Coding Katas for MongoDB".
 * 
 * @author isabel.batista
 *
 */
public class RoboFliesPersistence {

	private final String DATABASE_NAME = "mobilerobotics";
	private final String COLLECTION_NAME = "roboflies";

	private Mongo mongoDbClient;
	private DB database;
	private DBCollection robofliesCollection;

	public RoboFliesPersistence() throws MongoException {

		try {
			
			mongoDbClient = new Mongo("localhost", 27017);
			database = mongoDbClient.getDB(DATABASE_NAME);
			robofliesCollection = database.getCollection(COLLECTION_NAME);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test instance constructor that offers setting of database
	 * name.
	 * @param databaseName
	 */
	public RoboFliesPersistence(String databaseName) {
				
		try {
			
			mongoDbClient = new Mongo("localhost", 27017);
			database = mongoDbClient.getDB(databaseName);
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
}
