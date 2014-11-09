package mongodb.belgaia.kata6;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
		
		Set<DBObject> chargingSets = findChargingSetsByRoboFlyType(convertRoboFlyTypeToProfileType(flyType));
		
		Double sumOfLoadingTime = new Double(0.0);
		
		Iterator<DBObject> iteratorForChargingSets = chargingSets.iterator();
		while(iteratorForChargingSets.hasNext()) {
			DBObject chargingSet = iteratorForChargingSets.next();
			Integer lengthForCharging = (Integer) chargingSet.get("charge_length_minutes");
			sumOfLoadingTime += lengthForCharging.doubleValue();
		}
		
		double average = sumOfLoadingTime / chargingSets.size();
		return average;
	}
	
	public double calculateAverageOfFieldByRoboFlyType(String fieldName, RoboFlyType roboFlyType) {
		
		Set<DBObject> chargingSets = findChargingSetsByRoboFlyType(convertRoboFlyTypeToProfileType(roboFlyType));

		Double sumOfLoadingTime = new Double(0.0);
		
		Iterator<DBObject> iteratorForChargingSets = chargingSets.iterator();
		
		while(iteratorForChargingSets.hasNext()) {
			DBObject chargingSet = iteratorForChargingSets.next();
			Integer lengthForCharging = (Integer) chargingSet.get(fieldName);
			sumOfLoadingTime += lengthForCharging.doubleValue();
		}
		
		return sumOfLoadingTime / chargingSets.size();
	}
	
	private String convertRoboFlyTypeToProfileType(RoboFlyType roboFlyType) {
		return "ROBOFLY_ID_" + roboFlyType.name;
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
		return findChargingSetsByRoboFlyType_ComplicatedVersion(roboFlyType);		
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
	
	public void dropDatabase() {
		database.dropDatabase();
	}
	
	// FIXME: use this instead of calculating averages manually
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
		
		return new Double(0.0);
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
