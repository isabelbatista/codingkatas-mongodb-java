package mongodb.belgaia.kata1;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class RoboFly2DocumentConverter {
	
	public static DBObject convertRoboFly2Document(RoboFly roboFly) {
		
		DBObject document = new BasicDBObject("name", roboFly.name)
									.append("constructionYear", roboFly.constructionYear)
									.append("size", roboFly.size)
									.append("status", roboFly.status.name());
		
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