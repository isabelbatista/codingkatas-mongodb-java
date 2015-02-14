package mongodb.belgaia.kata10;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

public class MongoConnector {

	private static final String DATABASE_NAME = "mobilerobotics";
	
	private Mongo client;
	private DB database;
	
	private DBCollection robofliesCollection;
	private DBCollection profilesCollection;
	private DBCollection costsCollection;
	
	public MongoConnector() {
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			initDatabaseElements();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public MongoConnector(String databaseName) {
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(databaseName);
			initDatabaseElements();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initDatabaseElements() {	
		robofliesCollection = database.getCollection("roboflies");
		profilesCollection = database.getCollection("profiles");
		costsCollection = database.getCollection("costs");
	}
	
	public void dropDatabase() {
		database.dropDatabase();
	}
	
	public void importData2MongoDb(String fileName, String collectionName) {

		String exportType = "csv";
		String mongoimportCommand = "mongoimport -d " + database.getName()
				+ " -c " + collectionName + " --type " + exportType
				+ " --file " + fileName + " --headerline";
		try {
			Runtime.getRuntime().exec(mongoimportCommand);
		} catch (IOException e) {
			System.err
					.println("Importing data with mongoimport threw an error: "
							+ e.getMessage());
		}
	}
	
	public void addDocReferenceForProfiles(String roboFlyId, String profileType) {
		
		DBObject profile = profilesCollection.findOne(new BasicDBObject("type", profileType));
		DBRef reference = new DBRef(database, "profiles", (String) profile.get("_id"));
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), new BasicDBObject("$set", new BasicDBObject("typeRef", reference)));
	}
	
	public List<DBObject> getRoboflyDocuments() {
		DBCursor robofliesCursor = robofliesCollection.find();
		List<DBObject> roboflies = new ArrayList<DBObject>();
		
		while(robofliesCursor.hasNext()) {
			roboflies.add(robofliesCursor.next());
		}
		return roboflies;
	}
	
	public List<CostItem> getCostItemsByRoboFly(String roboFlyId, int countOfItems) {
		DBObject query = new BasicDBObject("roboFlyId", roboFlyId);
		DBCursor costsCursor = costsCollection.find(query).limit(countOfItems);
		
		List<CostItem> costItemList = new ArrayList<CostItem>();
		for(DBObject costsDoc : costsCursor) {
			costItemList.add(convertCostDoc2CostItem(costsDoc));
		}
		return costItemList;
	}
	
	private CostItem convertCostDoc2CostItem(DBObject costDoc) {
		
		CostItem costItem = new CostItem.Builder((String) costDoc.get("roboFlyId"))
											.costItemId((String) costDoc.get("_id"))
											.amount((String) costDoc.get("costs_in_eur"))
											.build();
		
		return costItem;
	}
}
