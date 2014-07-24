package mongodb.belgaia.kata1;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class RoboFliesPersistence {
	
	private final String DATABASE_NAME = "mongodbkatas";
	private final String COLLECTION_NAME= "roboflies";

	private Mongo mongoDbClient;
	private DB database;

	public RoboFliesPersistence() throws MongoException {

		try {
			mongoDbClient = new Mongo("localhost", 27017);
			database = mongoDbClient.getDB(DATABASE_NAME);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public String saveRoboFly(RoboFly roboFly) {
		
		DBObject document = new BasicDBObject();
		document.putAll(RoboFly2DocumentConverter.convertRoboFly2Document(roboFly));
		
		DBCollection collection = database.getCollection(COLLECTION_NAME);
		collection.insert(document);
		
		
		return null;
		
		
	}
}
