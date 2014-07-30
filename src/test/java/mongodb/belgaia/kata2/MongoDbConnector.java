package mongodb.belgaia.kata2;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

/**
 * A connector to MongoDB as part of the solution for Kata 2 of the book
 * "Coding Katas for MongoDB".
 * 
 * It's only needed for the tests to get imported data and to cleanup the database.
 * @author isabel.batista
 *
 */
public class MongoDbConnector {
	
	private DB database;
	
	
	public MongoDbConnector(String databaseName) {
		
		try {
			Mongo mongoDbClient = new Mongo("localhost", 27017);
			database = mongoDbClient.getDB(databaseName);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void dropDatabase() {
		
		database.dropDatabase();
	}
	
	/**
	 * Returns the count of documents found in the collection.
	 * 
	 * @param collectionName The name of the mongodb collection.
	 * @return the count of documents found in the collection.
	 */
	public int getDocumentCount(String collectionName) {
		
		DBCollection collection = database.getCollection(collectionName);
		return collection.find().size();
	}
}
