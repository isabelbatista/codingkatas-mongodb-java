package mongodb.belgaia.kata3;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;


public class Kata3 {
	
	private static final String DATABASE_NAME = "mobilerobotics";
	private static final String ROBOFLIES_COLLECTION_NAME = "roboflies";
	
	private Mongo mongodbClient;
	private DB database;
	private DBCollection roboFliesCollection;
	
	public Kata3(String databaseName) {
	
		try {
			mongodbClient = new Mongo("localhost", 27017);
			
			if(databaseName == null || databaseName.isEmpty()) {
				databaseName = DATABASE_NAME;
			} 
			
			database = mongodbClient.getDB(databaseName);
			roboFliesCollection = database.getCollection(ROBOFLIES_COLLECTION_NAME);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Saves a list of robotic flies and returns the initial IDs of these flies
	 * that are set by the database.
	 * 
	 * @param roboFlies	The List of robotic flies to save in the database.
	 * @return			The List of IDs of the newly set robotic flies.
	 */
	public List<String> saveRoboFlies(List<RoboFly> roboFlies) {
		
		final List<String> flyIds = new ArrayList<String>();
		
		for(RoboFly fly : roboFlies) {
			flyIds.add(saveRoboFly(fly));
		}
		
		return flyIds;
	}
	
	
	/**
	 * Saves a single robotic fly and returns the initial ID of this fly
	 * that is set by the database.
	 * 
	 * @param roboFly	The robotic fly to save in the database.
	 * @return			The ID of the newly saved robotic fly.
	 */
	public String saveRoboFly(RoboFly roboFly ) {
		
		DBObject roboFlyDocument = convertRoboFly2Document(roboFly);
		
		roboFliesCollection.save(roboFlyDocument);
		return roboFlyDocument.get("_id").toString();
	}
	
	private DBObject convertRoboFly2Document(RoboFly roboFly) {
		
		DBObject roboFlyDoc = new BasicDBObject();
		roboFlyDoc.put("name", roboFly.getName());
		roboFlyDoc.put("constructionYear", roboFly.getConstructionYear());
		roboFlyDoc.put("size", roboFly.getSize());
		roboFlyDoc.put("serviceTime", roboFly.getServiceTime());
		roboFlyDoc.put("status", roboFly.getStatus().toString());
		roboFlyDoc.put("type", roboFly.getType().toString());
		
		return roboFlyDoc;
		
	}

	
}
