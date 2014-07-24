package mongodb.belgaia.kata1;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		document = RoboFly2DocumentConverter.convertRoboFly2Document(roboFly);
//		document.putAll(RoboFly2DocumentConverter.convertRoboFly2Document(roboFly));
		
		DBCollection collection = database.getCollection(COLLECTION_NAME);
		collection.insert(document);	
		
		return document.get("_id").toString();
	}
	
	public List<String> saveRoboFlies(Map<String, RoboFly> roboFlies) {
		
		Map<String, DBObject> convertedDocuments = RoboFly2DocumentConverter.convertRoboFlies2Documents(roboFlies);
		
		DBCollection collection = database.getCollection(COLLECTION_NAME);
		
		for (Map.Entry<String, DBObject> document : convertedDocuments.entrySet()) {
			
			collection.insert(document.getValue());
			
		}
		
		List<String> savedDocumentIds = new ArrayList<String>();
		
		for (Map.Entry<String, DBObject> document : convertedDocuments.entrySet()) {
			savedDocumentIds.add(document.getValue().get("_id").toString());
		}		
		
		return savedDocumentIds;
		
	}
	
	public static void main (String[] args) {
		
		
		
	}

}
