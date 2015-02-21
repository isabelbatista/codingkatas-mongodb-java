package mongodb.belgaia.kata10;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;

public class MongoConnector {

	private static final String DATABASE_NAME = "mobilerobotics";
	private static final String COSTS_COLLECTION_NAME = "costs";
	
	// Queries
	private static final String MAP_COSTS_ON_ROBOFLIES = "function() { emit(this.roboFlyId, this.costs_in_euro); };";
	private static final String REDUCE_COSTS_PER_ROBOFLY_ID = "function(roboFlyId, costs) { return Array.sum(costs); };";
	private static final String MAP_COSTS_ON_COST_TYPES = "function() { emit(this.cost_type, this.costs_in_euro); };";
	private static final String REDUCE_COSTS_PER_COST_TYPE = "function(cost_type, costs) { return Array.sum(costs); };";	
	
	private static final String COST_DATASHEET_FILENAME = "src/main/resources/kata10/cost_items.csv";

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
		costsCollection = database.getCollection(COSTS_COLLECTION_NAME);
	}
	
	public void dropDatabase() {
		database.dropDatabase();
		client.close();
	}
	
	public void importCostsDataToMongoDb() {
		importData2MongoDb(COST_DATASHEET_FILENAME, COSTS_COLLECTION_NAME);
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

	public Map<String, Double> calculateMostExpensiveRoboFly() {
		
		MapReduceCommand command = new MapReduceCommand(costsCollection,
												MAP_COSTS_ON_ROBOFLIES,
												REDUCE_COSTS_PER_ROBOFLY_ID,
												null,
												MapReduceCommand.OutputType.INLINE,
												null);
		MapReduceOutput out = costsCollection.mapReduce(command);
		
		Map<String, Double> mostExpensiveRoboFly = pickHighestValue(out.results());
		return mostExpensiveRoboFly;
	}

	public Map<String, Double> calculateCostTypeWithHighestExpenses() {
	
		MapReduceCommand command = new MapReduceCommand(costsCollection,
										MAP_COSTS_ON_COST_TYPES,
										REDUCE_COSTS_PER_COST_TYPE,
										null,
										MapReduceCommand.OutputType.INLINE,
										null);
		MapReduceOutput out = costsCollection.mapReduce(command);
		
		Map<String, Double> mostExpensiveCostTypeMap = pickHighestValue(out.results());
		return mostExpensiveCostTypeMap;
	}

	private Map<String, Double> pickHighestValue(Iterable<DBObject> results) {
		
		double highestValue = 0;
		String highestValueId = null;
		
		for(DBObject result : results) {
			double currentValue = (Double) result.get("value");
			if(currentValue > highestValue) {
				highestValue = currentValue;
				highestValueId = (String) result.get("_id");
			}
		}
		Map<String, Double> highestValueMap = new HashMap<String, Double>();
		highestValueMap.put(highestValueId, highestValue);
		return highestValueMap;
	}
}
