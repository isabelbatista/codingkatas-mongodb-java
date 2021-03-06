package mongodb.belgaia.kata2;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

/**
 * A connector to MongoDB as part of the solution for Kata 2 and 2.2 of the book
 * "Coding Katas for MongoDB".
 * 
 * It's also needed for the tests to check the number of
 * imported data sets and to cleanup the database.
 * 
 * @author isabel.batista
 *
 */
public class MongoDbConnector {
	
	private DB database;
	private Mongo mongoDbClient;
	
	public MongoDbConnector(String databaseName) {
		
		try {
			mongoDbClient = new Mongo("localhost", 27017);
			database = mongoDbClient.getDB(databaseName);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void dropDatabase() {
		database.dropDatabase();
		mongoDbClient.close();
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
	
	public DBObject getMeasurementById(String docId) {
		
		DBCollection collection = database.getCollection("measurements");
		DBObject document2find = new BasicDBObject();
		document2find.put("_id", docId);
		
		return collection.findOne(document2find);
	}
	
	public DBObject getMeasurementByName(String name) {
		
		DBCollection collection = database.getCollection("measurements");
		DBObject document2find = new BasicDBObject();
		document2find.put("name", name);
		
		return collection.findOne(document2find);
	}
	
	public void saveRoboFly(DBObject roboFlyDocument) {
		
		DBCollection collection = database.getCollection("roboflies");
		collection.insert(roboFlyDocument);
		
	}
	
	/**
	 * Sets a reference of the document with the given referenceId of the
	 * given collection to the given document object.
	 * 
	 * @param refCollectionName name of the collection where to find the referenced document
	 * @param referenceId the id of the referenced document
	 * @param fieldKey the key of the document field that contains the reference
	 * @param docCollectionName name of the collection where to find the given document to set the reference
	 * @param document the document where the reference must be set
	 * @return
	 */
	public void setDocumentReference(Robofly2MeasurementReference documentReference) {
		
		DBRef reference = new DBRef(database, documentReference.getReferenceCollection(), documentReference.getReferenceId());
		
		DBObject document = documentReference.getDocument();
		document.put(documentReference.getReferenceField(), reference);
				
		DBCollection collection = database.getCollection(documentReference.getDocumentCollection());
		collection.save(document);

	}
	
	public DBObject getDocReference(String collectionName, String refFieldName) {
		
		DBCollection collection = database.getCollection(collectionName);
		DBObject document = collection.findOne();
		DBRef referencedDoc = (DBRef) document.get(refFieldName);
		
		return referencedDoc.fetch();
	}
}
