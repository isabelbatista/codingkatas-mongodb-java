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
	
	/**
	 * Adds a type to a robotic fly with given ID. Overwrites existing type if
	 * "overwrite" is set to true.
	 * 
	 * @param roboFlyId		The robotic fly ID where the type has to be set.
	 * @param type			The type of the robotic fly to set.
	 * @param overwrite		whether the type has to be overwritten if it already exists
	 * @return
	 */
	public RoboFly addTypeToFly(String roboFlyId, RoboFly.Type type, boolean overwrite) {
		
		// create document with query parameters
		DBObject queryDoc = new BasicDBObject("_id", roboFlyId);
		
		// create update document by finding the document with query parameters from database
		DBObject newDoc = roboFliesCollection.findOne(queryDoc);
		newDoc.put("type", type.name());
		
		// update
		roboFliesCollection.update(queryDoc, newDoc);		

		return convertDocument2RoboFly(newDoc);
	}
	
	/**
	 * Converts a robotic fly of type RoboFly to a mongodb compatible document type.
	 * 
	 * @param roboFly	The robotic fly to convert.
	 * @return			The mongodb compatible document that can be inserted in mongodb.
	 */
	private DBObject convertRoboFly2Document(RoboFly roboFly) {
		
		DBObject roboFlyDoc = new BasicDBObject();
		roboFlyDoc.put("_id", roboFly.getId());
		roboFlyDoc.put("name", roboFly.getName());
		roboFlyDoc.put("constructionYear", roboFly.getConstructionYear());
		roboFlyDoc.put("size", roboFly.getSize());
		roboFlyDoc.put("serviceTime", roboFly.getServiceTime());
		
		
		roboFlyDoc.put("status", roboFly.getStatus() != null ? roboFly.getStatus().toString() : null);
//		roboFlyDoc.put("type", roboFly.getType() != null ? roboFly.getType().toString() : null);
		
		return roboFlyDoc;
		
	}
	
	/**
	 * Converts a mongodb database document to a robotic fly of type RoboFly.
	 * 
	 * @param document		The mongodb document to convert.
	 * @return				The converted RoboFly object from the values of the mongodb document.
	 */
	private RoboFly convertDocument2RoboFly(DBObject document) {
		
		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder((String)document.get("_id"), (String) document.get("name"));
		
		String roboFlyDocStatus = document.get("status") != null ? document.get("status").toString() : null;
		RoboFly.Status roboFlyStatus = RoboFly.Status.valueOf(roboFlyDocStatus);
		
		String roboFlyDocType = document.get("type") != null ? document.get("type").toString() : null;
		RoboFly.Type roboFlyType = RoboFly.Type.valueOf(roboFlyDocType);
		
		
		RoboFly roboFly = roboFlyBuilder
									.constructionYear((Integer) document.get("constructionYear"))
									.size((Integer) document.get("size"))
									.serviceTime((Integer) document.get("serviceTime"))
									.status(roboFlyStatus)
									.type(roboFlyType)
									.build();
		
		return roboFly;
	}	
}