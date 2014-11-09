package mongodb.belgaia.kata6;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;
import com.mongodb.QueryBuilder;

public class MongoConnector {
	
	private static final String DATABASE_NAME = "mobilerobotics";

	private Mongo client;
	private DB database;
	
	private DBCollection robofliesCollection;
	private DBCollection profilesCollection;
	private DBCollection chargingCollection;
	
	public MongoConnector() {
		
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			
			initDatabaseElements();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
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
		chargingCollection = database.getCollection("charging");
	}
	
	
	public double calculateAverageLoadingTime(RoboFlyType flyType) {
		
		List<DBObject> roboFlies = getRoboFliesByType(flyType);
		List<String> roboFlyIds = new ArrayList<String>();
		for(DBObject roboFly : roboFlies) {
			roboFlyIds.add((String) roboFly.get("_id")); 
		}
		
		double average = calculate(roboFlyIds);
		
		return 0;
	}
	
	public List<DBObject> findFliesByType(String roboFlyType) {
		
		DBObject query = buildQueryForFindingRoboFliesByType(roboFlyType);
		DBCursor result = robofliesCollection.find(query);
		
		List<DBObject> roboflies = new ArrayList<DBObject>();
		while(result.hasNext()) {
			roboflies.add(result.next());
		}
		return roboflies;
	}
	
	
	
	public List<DBObject> findCharsetsByRoboFlyId(String roboFlyId) {
		
		DBObject query = buildQueryForFindingChargingsetsByRoboFlyId(roboFlyId);
		DBCursor result = chargingCollection.find(query);
		
		List<DBObject> charsets = new ArrayList<DBObject>();
		while(result.hasNext()) {
			charsets.add(result.next());
		}
		return charsets;
	}
	
	public Set<DBObject> findChargingSetsByRoboFlyType(String roboFlyType) {
		
//		Set<DBObject> chargingSets = findChargingSetsByRoboFlyType_ComplicatedVersion(roboFlyType);
		Set<DBObject> chargingSets = findChargingSetsByRoboFlyType_BetterVersion(roboFlyType);
		
		return chargingSets;
	}
	
	private Set<DBObject> findChargingSetsByRoboFlyType_BetterVersion(String roboFlyType) {
		
		DBObject queryForRoboFliesByType = buildQueryForFindingRoboFliesByType(roboFlyType);
		DBCursor roboFliesByType = robofliesCollection.find(queryForRoboFliesByType);
//		QueryBuilder.start().in(object)
//		new BasicDBObject("$and", )
		
		return null;
	}
	
	private Set<DBObject> findChargingSetsByRoboFlyType_ComplicatedVersion(String roboFlyType) {
		
		// create a list of all robotic flies with this type
		List<DBObject> roboFlies = findFliesByType(roboFlyType);
		List<String> roboFlyIds = new ArrayList<String>();
		for(DBObject roboFly : roboFlies) {
			roboFlyIds.add((String) roboFly.get("_id"));
		}
		
		// get charging sets per fly
		Set<DBObject> charsets = new HashSet<DBObject>();
		for(String roboFlyId : roboFlyIds) {
			DBObject query = buildQueryForFindingChargingsetsByRoboFlyId(roboFlyId);
			DBCursor result = chargingCollection.find(query);

			while(result.hasNext()) {
				charsets.add(result.next());
			}
		}
		return charsets;
	}
	
	private DBObject buildQueryForFindingRoboFliesByType(String roboFlyType) {
		DBRef typeRef = new DBRef(database, "profiles", roboFlyType);
		DBObject query = new BasicDBObject("typeRef", typeRef);
		return query;
	}
	
	private DBObject buildQueryForFindingChargingsetsByRoboFlyId(String roboFlyId) {
		DBRef roboFlyRef = new DBRef(database, "roboflies", roboFlyId);
		DBObject query = new BasicDBObject("roboFlyRef", roboFlyRef);
		return query;
	}
	
	private DBObject buildQueryForFindingChargingSetsByRoboFlyType(String roboFlyType) {
		
		
		
		return null;
	}
	
	public void dropDatabase() {
		database.dropDatabase();
	}
	
	private double calculate(List<String> roboFlyIds) {
		
		DBObject inQuery = new BasicDBObject("$in", roboFlyIds);
		DBRef roboFlyRef = new DBRef(database, "roboflies", inQuery);
		
		DBObject match = new BasicDBObject("$match", new BasicDBObject("roboFlyRef", roboFlyRef));		
		DBObject average = new BasicDBObject("averageLoadingTime", new BasicDBObject("$avg", "charge_length_minutes"));
		
		
		DBObject groupFields = new BasicDBObject("_id", "allRoboFlies");
		DBObject group = new BasicDBObject("$group", groupFields);
		
		AggregationOutput result = chargingCollection.aggregate(average, match, group);
		
		if(result.results().iterator().hasNext()) {
			System.out.println("Found results: " + result.results().iterator().next().get("averageLoadingTime"));
		} else {
			System.out.println("No results found.");
		}
		
//		
//		AggregationOutput result = chargingCollection.aggregate(match, group);
//		if (result != null) {
//			Iterator<DBObject> resultIterator = result.results().iterator();
//			if (resultIterator.hasNext()) {
//				return (Double) resultIterator.next().get("averageLoadingTime");
//			}
//		}
		return new Double(0.0);
	}
	
	
	private List<DBObject> getRoboFliesByType(RoboFlyType flyType) {
		
//		DBObject fred = collection.findOne();
//		DBRef addressObj = (DBRef)fred.get("address");
//		addressObj.fetch()
		DBRef profileReference = new DBRef(database, "profiles", "ROBOFLY_ID_FLY");
		DBObject profileRefDoc = profileReference.fetch();
		DBCursor roboFliesByType = robofliesCollection.find(new BasicDBObject("typeRef", profileRefDoc));
		
		
//		DBObject typeQuery = new BasicDBObject("typeRef", new DBRef(database, "profiles", flyType.name));
//		DBCursor roboFliesByType = robofliesCollection.find(typeQuery);
		
		List<DBObject> roboFlies = new ArrayList<DBObject>();
		while(roboFliesByType.hasNext()) {
			roboFlies.add(roboFliesByType.next());
		}
		return roboFlies;
	}
	
	private List<DBObject> getChargingSetsByRoboFlyId(String roboFlyId) {
		
		DBObject query = new BasicDBObject("roboFlyRef", new DBRef(database, "roboflies", roboFlyId));
		DBCursor chargingSetsByFlyId = chargingCollection.find(query);
		
		List<DBObject> chargingSets = new ArrayList<DBObject>();
		while(chargingSetsByFlyId.hasNext()) {
			chargingSets.add(chargingSetsByFlyId.next());
		}
		return chargingSets;
	}
	
	
	public void importData2MongoDb(String fileName, String collectionName) {
		
		String exportType = "csv";
		String mongoimportCommand = "mongoimport -d " + database.getName()
											 + " -c " + collectionName
											 + " --type " + exportType
											 + " --file " + fileName
											 + " --headerline";
		
		try {
			Runtime.getRuntime().exec(mongoimportCommand);
		} catch (IOException e) {
			System.err.println("Importing data with mongoimport threw an error: " + e.getMessage());
		}
	}
	
	public void addDocReferenceForProfiles(String roboFlyId, String profileType) {
		
		DBObject profile = profilesCollection.findOne(new BasicDBObject("type", profileType));
		DBRef reference = new DBRef(database, "profiles", (String) profile.get("_id"));
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), new BasicDBObject("$set", new BasicDBObject("typeRef", reference)));
	}
	
	public void addDocReferenceForCharges(String chargingSetId, String roboFlyId) {
		
		DBObject roboFly = robofliesCollection.findOne(new BasicDBObject("_id", roboFlyId));
		DBRef reference = new DBRef(database, "roboflies", (String) roboFly.get("_id"));
		chargingCollection.update(new BasicDBObject("_id", chargingSetId), new BasicDBObject("$set", new BasicDBObject("roboFlyRef", reference)));
	}

	public List<DBObject> getProfiles() {
		DBCursor profilesCursor = profilesCollection.find();
		List<DBObject> profiles = new ArrayList<DBObject>();
		
		while(profilesCursor.hasNext()) {
			profiles.add(profilesCursor.next());
		}
		return profiles;
	}
	
	public List<DBObject> getRoboflies() {
		DBCursor robofliesCursor = robofliesCollection.find();
		List<DBObject> roboflies = new ArrayList<DBObject>();
		
		while(robofliesCursor.hasNext()) {
			roboflies.add(robofliesCursor.next());
		}
		return roboflies;
	}
	
	public List<DBObject> getChargingSets() {
		DBCursor chargingCursor = chargingCollection.find();
		List<DBObject> charging = new ArrayList<DBObject>();
		
		while(chargingCursor.hasNext()) {
			charging.add(chargingCursor.next());
		}
		return charging;
	}
}
