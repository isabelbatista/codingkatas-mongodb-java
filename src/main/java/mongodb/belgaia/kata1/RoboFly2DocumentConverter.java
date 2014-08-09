package mongodb.belgaia.kata1;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Converter for RoboFly to mongodb document as part of the solution for Kata 1 of the book
 * "Coding Katas for MongoDB".
 * 
 * Converter to convert objects of type RoboFly to mongodb documents.
 * 
 * @author isabel.batista
 *
 */
public class RoboFly2DocumentConverter {
	
	public static DBObject convertRoboFly2Document(RoboFly roboFly) {
		
		DBObject document = new BasicDBObject("name", roboFly.getName())
									.append("_id", roboFly.getId())
									.append("constructionYear", roboFly.getConstructionYear())
									.append("size", roboFly.getSize())
									.append("serviceTime", roboFly.serviceTime)
									.append("status", roboFly.getStatus().name);
		
//		if (roboFly.getId() != null) {
//			document.put("_id", roboFly.getId());
//		}
		
		return document;
	}
	
	public static Map<String, DBObject> convertRoboFlies2Documents(Map<String, RoboFly> roboFlies) {
		
		Map<String, DBObject> roboFlyDocuments = new HashMap<String, DBObject>();
		
		for (Map.Entry<String, RoboFly> roboFly : roboFlies.entrySet()) {
			
			DBObject document = convertRoboFly2Document(roboFly.getValue());
			
			roboFlyDocuments.put(roboFly.getKey(), document);
		}
		
		return roboFlyDocuments;		
	}
}