package mongodb.belgaia.kata4;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoAggregator {

	private static final String DATABASE_NAME = "mobilerobotics";
	private Mongo client;
	private DB database;

	public MongoAggregator() {
		
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MongoAggregator(String databaseName) {
		
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(databaseName);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveRoboflies(List<DBObject> roboFlies) {
				
		DBCollection collection = database.getCollection("roboflies");
		
		collection.insert(roboFlies);		
	}
	
	public List<RoboFly> getRoboFliesWithDifferingVolume() {
		
		DBCollection collection = database.getCollection("measurements");
		
//		DBObject average = new BasicDBObject("$avg", new BasicDBObject("SoundIntensitiy (in decibel)"));
//		
//		DBObject match = new BasicDBObject("$match", new BasicDBObject("SoundIntensity", 0));
		
		return null;
	}

}
