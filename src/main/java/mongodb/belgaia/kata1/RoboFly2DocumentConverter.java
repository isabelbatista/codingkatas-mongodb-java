package mongodb.belgaia.kata1;

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

}
