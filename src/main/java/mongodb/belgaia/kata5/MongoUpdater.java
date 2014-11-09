package mongodb.belgaia.kata5;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

class MongoUpdater {

	private static final String STATUS_FIELD_NAME = "status";
	private static final String ID_FIELD_NAME = "_id";
	private static final String TYPE_REF_FIELD_NAME = "typeRef";
	private static final String STATUS_DESCRIPTION_FIELD_NAME = "statusDescription";
	private static final String DATABASE_NAME = "mobilerobotics";

	private Mongo client;
	private DB database;

	private DBCollection robofliesCollection;
	private DBCollection measurementsCollection;
	private DBCollection profilesCollection;

	public MongoUpdater() {
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			initCollections();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public MongoUpdater(String databaseName) {
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(databaseName);
			initCollections();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	private void initCollections() {
		robofliesCollection = database.getCollection("roboflies");
		measurementsCollection = database.getCollection("measurements");
		profilesCollection = database.getCollection("profiles");
	}

	public void dropDatabase() {
		database.dropDatabase();
	}

	public void changeStatus(String roboFlyId, RoboFlyStatus status) {

		DBObject query = new BasicDBObject(ID_FIELD_NAME, roboFlyId);
		DBObject update = new BasicDBObject().append("$set", new BasicDBObject(STATUS_FIELD_NAME, status.name));
		
		robofliesCollection.update(query, update);
	}
	
	/**
	 * Adds more information about the robofly status.
	 * Additional information includes a description key of what happened to the robofly (if status is "OUT_OF_SERVICE),
	 * a comment about what happened exactly and the service end date.
	 * 
	 * @param roboFlyId			The robot fly that gets the additional information.
	 * @param additionalInfo	The information about the robot fly mapped in a HashMap<key, value>.
	 */
	public void addMoreInformation(String roboFlyId, Map<String, String> additionalInfo) {
		
		Iterator<Entry<String, String>> infoIterator = additionalInfo.entrySet().iterator();
		while(infoIterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) infoIterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			
			DBObject query = new BasicDBObject(ID_FIELD_NAME, roboFlyId);
			DBObject update = new BasicDBObject("$set", new BasicDBObject(key, value));
			
			robofliesCollection.update(query, update);
		}
	}
	
	public void setEnergyShieldAtRoboFly() {
		setEquipmentAtRoboFly(StatusDescription.EATEN);
	}
	
	public void setGPSAtRoboFly() {
		setEquipmentAtRoboFly(StatusDescription.LOST);
	}
	
	public void setEquipmentAtRoboFly(StatusDescription statusDesc) {
		
		Equipment equipment = matchEquipmentToStatus(statusDesc);
		
		DBObject findQuery = new BasicDBObject(STATUS_DESCRIPTION_FIELD_NAME, statusDesc.name());
		DBCursor roboFlies = robofliesCollection.find(findQuery);
		
		while(roboFlies.hasNext()) {
			
			DBObject roboFly = roboFlies.next();
			DBRef roboFlyTypeRef = (DBRef) roboFly.get(TYPE_REF_FIELD_NAME);
			String roboFlyId = (String) roboFly.get(ID_FIELD_NAME);
			
			DBObject idQuery = new BasicDBObject(ID_FIELD_NAME, roboFlyId);
			DBObject typeQuery = new BasicDBObject(TYPE_REF_FIELD_NAME, new DBRef(database, "profiles", roboFlyTypeRef.getId()));
			
			// be aware that more than one document should be updated
			boolean multiUpdate = true;
			
			robofliesCollection.update(idQuery, new BasicDBObject("$set", new BasicDBObject("equipment", equipment.name())));
			robofliesCollection.update(typeQuery, new BasicDBObject("$set", new BasicDBObject("equipment", equipment.name())), false, multiUpdate);	
		}
	}
	
	private Equipment matchEquipmentToStatus(StatusDescription statusDesc) {
		Equipment equipment = null;
		if(statusDesc.equals(StatusDescription.EATEN)) {
			equipment = Equipment.ENERGY_SHIELD;
		} else if (statusDesc.equals(StatusDescription.LOST)) {
			equipment = Equipment.GPS;
		} else {
			throw new IllegalArgumentException("Unknown status description: " + statusDesc);
		}
		return equipment;
	}
	
	public void addDocumentReference(String roboFlyId, String profileType) {
		
		DBObject profile = profilesCollection.findOne(new BasicDBObject("type", profileType));
		DBRef reference = new DBRef(database, "profiles", (String) profile.get("_id"));
		robofliesCollection.update(new BasicDBObject("_id", roboFlyId), new BasicDBObject("$set", new BasicDBObject("typeRef", reference)));
	}

	// Preparation for tests

	public void saveRoboflies(List<DBObject> roboFlies) {
		robofliesCollection.insert(roboFlies);
	}

	public void saveMeasurements(List<DBObject> measurements) {
		measurementsCollection.insert(measurements);
	}
	
	public void saveProfiles(List<DBObject> profiles) {
		profilesCollection.insert(profiles);
	}
	
	public DBObject getRoboFly(String roboFlyId) {
		return robofliesCollection.findOne(new BasicDBObject(ID_FIELD_NAME, roboFlyId));
	}
	
	public DBObject getProfileById(String profileId) {
		return profilesCollection.findOne(new BasicDBObject(ID_FIELD_NAME, profileId));

	}
}
