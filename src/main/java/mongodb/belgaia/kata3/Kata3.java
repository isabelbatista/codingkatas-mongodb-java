package mongodb.belgaia.kata3;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import mongodb.belgaia.kata3.RoboFly.Type;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;


public class Kata3 {
	
	private static final String DATABASE_NAME = "mobilerobotics";
	private static final String ROBOFLIES_COLLECTION_NAME = "roboflies";
	private static final String PROFILES_COLLECTION_NAME = "profiles";
	
	private Mongo mongodbClient;
	private DB database;
	private DBCollection roboFliesCollection;
	private DBCollection profilesCollection;
	
	public Kata3(String databaseName) {
	
		try {
			mongodbClient = new Mongo("localhost", 27017);
			
			if(databaseName == null || databaseName.isEmpty()) {
				databaseName = DATABASE_NAME;
			} 
			
			database = mongodbClient.getDB(databaseName);
			roboFliesCollection = database.getCollection(ROBOFLIES_COLLECTION_NAME);
			profilesCollection = database.getCollection(PROFILES_COLLECTION_NAME);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}
	
	public void startKata3() {
		
		System.out.println("Save additional robotic files of type dragonfly, moskito and copepod.");
		saveRoboFlies(getRoboFlyList(), true);
		
		System.out.println("Add type 'Fly' to the three already existing robotic flies.");
		addTypeToFly("RoboFly_ID_1", RoboFly.Type.FLY, false);
		addTypeToFly("RoboFly_ID_2", RoboFly.Type.FLY, false);
		addTypeToFly("RoboFly_ID_3", RoboFly.Type.FLY, false);
		
	}
	
	
	/**
	 * Saves a list of robotic flies and returns the initial IDs of these flies
	 * that are set by the database.
	 * 
	 * @param roboFlies		The List of robotic flies to save in the database.
	 * @param typeIncluded 	Whether the type is included in the document.
	 * 						Needed to create initial test set of robotic flies without a type (set to false for test case).
	 * @return				The List of IDs of the newly set robotic flies.
	 */
	public List<String> saveRoboFlies(List<RoboFly> roboFlies, boolean typeIncluded) {
		
		final List<String> flyIds = new ArrayList<String>();
		
		for(RoboFly fly : roboFlies) {
			flyIds.add(saveRoboFly(fly, typeIncluded));
		}
		
		return flyIds;
	}
	
	
	/**
	 * Saves a single robotic fly and returns the initial ID of this fly
	 * that is set by the database.
	 * 
	 * @param roboFly		The robotic fly to save in the database.
	 * @param typeIncluded 	Whether the type is included in the document.
	 * 						Needed to create initial test set of robotic flies without a type (set to false for test case).
	 * @return				The ID of the newly saved robotic fly.
	 */
	public String saveRoboFly(RoboFly roboFly, boolean typeIncluded) {
		
		DBObject roboFlyDocument = convertRoboFly2Document(roboFly, typeIncluded);
		
		roboFliesCollection.save(roboFlyDocument);
		return roboFlyDocument.get("_id").toString();
	}
	
	/**
	 * Adds a type to a robotic fly with given ID. Overwrites existing type if
	 * "overwrite" is set to true.
	 * 
	 * @param roboFlyId		The robotic fly ID where the type has to be set.
	 * @param type			The type of the robotic fly to set.
	 * @param overwrite		whether the type has to be overwritten if it already exists
	 * @return
	 */
	public RoboFly addTypeToFly(String roboFlyId, RoboFly.Type type, boolean overwrite) {
		
		// create document with query parameters
		DBObject queryDoc = new BasicDBObject("_id", roboFlyId);
		
		// create update document by finding the document with query parameters from database
		DBObject newDoc = roboFliesCollection.findOne(queryDoc);
		newDoc.put("type", type.name());
		
		// update
		roboFliesCollection.update(queryDoc, newDoc);		

		return convertDocument2RoboFly(newDoc);
	}
	
	public void dropDatabase() {
		
		database.dropDatabase();
	}
	
	public void refactorDatabase(String sourceCollection, String targetCollection) throws CollectionDoesNotExistExc {
		
		extractFieldsToProfile(sourceCollection, targetCollection);
		removeFieldsFromCollection(sourceCollection);
	}
	
	/**
	 * Needed for Kata 3.2
	 * 
	 * @param listOfFields
	 * @param sourceCollection
	 * @param targetCollection
	 * @throws CollectionDoesNotExistExc 
	 */
	public void extractFieldsToProfile(String sourceCollection, String targetCollection) throws CollectionDoesNotExistExc {
		
		if(database.collectionExists(sourceCollection)) {
			
			// get all documents of the source collection and extract the fields from each document
			DBCursor sourceCollectionCursor = database.getCollection(sourceCollection).find();
			
			List<String> foundTypes = new ArrayList<String>();
						
			while(sourceCollectionCursor.hasNext()) {
				DBObject document = sourceCollectionCursor.next();
				
				String type = String.valueOf(document.get("type"));
				if(foundTypes.contains(type)) {
					
					continue;
					
				} else {
					
					foundTypes.add(type);
				
					Integer size = (Integer) document.get("size");
					Integer serviceTime = (Integer) document.get("serviceTime");
										
					Profile profile = new Profile();
					profile.setId("ROBOFLY_ID_" + type);
					profile.setRoboflyType(Type.valueOf(type));
					profile.setSize(size);
					profile.setServiceTime(serviceTime);
					
					// create entry in profile collection
					DBCollection roboflyProfilesCollection = database.getCollection(targetCollection);
					roboflyProfilesCollection.save(convertProfile2Document(profile));
				}
			}
		} else {
			throw new CollectionDoesNotExistExc("Collection does not exist: " + sourceCollection);
		}
	}
	
	public void removeFieldsFromCollection(String sourceCollection) {
		
		// remove fields "type", "size" and "serviceTime"
		DBCollection collection = database.getCollection(sourceCollection);
	
		DBCursor roboFliesCursor = collection.find();
		while(roboFliesCursor.hasNext()) {
			DBObject currentDocument = roboFliesCursor.next();
			DBObject updateDocument = currentDocument;
			updateDocument.removeField("type");
			updateDocument.removeField("size");
			updateDocument.removeField("serviceTime");
			
			collection.update(currentDocument, updateDocument);
		}		
	}
	
	public List<Profile> getProfiles() {
		
		List<Profile> profiles = new ArrayList<Profile>();
		
		DBCursor profileDocuments = profilesCollection.find();
		while(profileDocuments.hasNext()) {
			profiles.add(convertDocument2Profile(profileDocuments.next()));
		}
		return profiles;
	}
	
	public List<RoboFly> getRoboFlies() {
		
		List<RoboFly> roboFlies = new ArrayList<RoboFly>();
		
		DBCursor roboFlyDocuments = roboFliesCollection.find();
		while(roboFlyDocuments.hasNext()) {
			
			roboFlies.add(convertDocument2RoboFly(roboFlyDocuments.next()));
			
		}
		
		return roboFlies;
	}
	
	
	/**
	 * Converts a robotic fly of type RoboFly to a mongodb compatible document type.
	 * 
	 * @param roboFly		The robotic fly to convert.
	 * @param typeIncluded 	Whether the type is included in the document.
	 * 						Needed to create initial test set of robotic flies without a type (set to false for test case).
	 * @return				The mongodb compatible document that can be inserted in mongodb.
	 */
	private DBObject convertRoboFly2Document(RoboFly roboFly, boolean typeIncluded) {
		
		DBObject roboFlyDoc = new BasicDBObject();
		roboFlyDoc.put("_id", roboFly.getId());
		roboFlyDoc.put("name", roboFly.getName());
		roboFlyDoc.put("constructionYear", roboFly.getConstructionYear());
		roboFlyDoc.put("size", roboFly.getSize());
		roboFlyDoc.put("serviceTime", roboFly.getServiceTime());		
		roboFlyDoc.put("status", roboFly.getStatus() != null ? roboFly.getStatus().toString() : null);
		
		if (typeIncluded) {
			roboFlyDoc.put("type", roboFly.getType() != null ? roboFly.getType().toString() : null);
		}
		
		return roboFlyDoc;
		
	}
	
	private DBObject convertProfile2Document(Profile profile) {
		
		DBObject profileDoc = new BasicDBObject();
		profileDoc.put("_id", profile.getId());
		profileDoc.put("type", profile.getRoboflyType().name);
		profileDoc.put("size", profile.getSize());
		profileDoc.put("serviceTime", profile.getServiceTime());

		return profileDoc;
		
	}
	
	private Profile convertDocument2Profile(DBObject document) {
		
		Profile profile = new Profile();
		profile.setId(String.valueOf(document.get("_id")));
		profile.setRoboflyType(Type.valueOf(String.valueOf(document.get("type"))));
		profile.setSize((Integer)document.get("size"));
		profile.setServiceTime((Integer)document.get("serviceTime"));
		return profile;
	}
	
	/**
	 * Converts a mongodb database document to a robotic fly of type RoboFly.
	 * 
	 * @param document		The mongodb document to convert.
	 * @return				The converted RoboFly object from the values of the mongodb document.
	 */
	private RoboFly convertDocument2RoboFly(DBObject document) {
		
		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder((String)document.get("_id"), (String) document.get("name"));
		
		String roboFlyDocStatus = document.get("status") != null ? document.get("status").toString() : null;
		RoboFly.Status roboFlyStatus = RoboFly.Status.valueOf(roboFlyDocStatus);
		
		String roboFlyDocType = document.get("type") != null ? document.get("type").toString() : null;
		
		roboFlyBuilder
				.constructionYear((Integer) document.get("constructionYear"))
				.status(roboFlyStatus);
		
		if(roboFlyDocType != null) {
			RoboFly.Type roboFlyType = RoboFly.Type.valueOf(roboFlyDocType);
			roboFlyBuilder.type(roboFlyType);
		}
		
		if(document.get("size") != null) {
			roboFlyBuilder.size((Integer) document.get("size"));
		}
		
		if(document.get("serviceTime") != null) {
			roboFlyBuilder.serviceTime((Integer) document.get("serviceTime"));
		}
		
		return roboFlyBuilder.build();
	}	
	
	private List<RoboFly> getRoboFlyList() {
		
		final int consYear = 2014;
		
		final int dragonFlySize = 5;
		final int dragonFlyServiceTime = 25; // in minutes
		
		final int moskitoSize = 2;
		final int moskitoServiceTime = 120; // in minutes
		
		final int copepodSize = 8;
		final int copepodServiceTime = 90; // in minutes
		
		List<RoboFly> roboFlies = new ArrayList<RoboFly>();
		
		RoboFly.Builder roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_4", "Ischnura");
		RoboFly dragonFly1 = roboFlyBuilder.constructionYear(consYear)
										.size(dragonFlySize)
										.serviceTime(dragonFlyServiceTime)
										.status(RoboFly.Status.OK)
										.type(RoboFly.Type.DRAGONFLY)
										.build();
		
		roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_5", "Calopteryx");
		RoboFly dragonFly2 = roboFlyBuilder.constructionYear(consYear)
										.size(dragonFlySize)
										.serviceTime(dragonFlyServiceTime)
										.status(RoboFly.Status.OK)
										.type(RoboFly.Type.DRAGONFLY)
										.build();
		
		roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_6", "Dahliana");
		RoboFly moskito1 = roboFlyBuilder.constructionYear(consYear)
										.size(moskitoSize)
										.serviceTime(moskitoServiceTime)
										.status(RoboFly.Status.OK)
										.type(RoboFly.Type.MOSKITO)
										.build();
		
		roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_7", "Finlaya");
		RoboFly moskito2 = roboFlyBuilder.constructionYear(consYear)
										.size(moskitoSize)
										.serviceTime(moskitoServiceTime)
										.status(RoboFly.Status.OK)
										.type(RoboFly.Type.MOSKITO)
										.build();
		
		roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_8", "Mormon");
		RoboFly copepod1 = roboFlyBuilder.constructionYear(consYear)
										.size(copepodSize)
										.serviceTime(copepodServiceTime)
										.status(RoboFly.Status.OK)
										.type(RoboFly.Type.COPEPOD)
										.build();
		
		roboFlyBuilder = new RoboFly.Builder("RoboFly_ID_9", "Harpa");
		RoboFly copepod2 = roboFlyBuilder.constructionYear(consYear)
										.size(copepodSize)
										.serviceTime(copepodServiceTime)
										.status(RoboFly.Status.OK)
										.type(RoboFly.Type.COPEPOD)
										.build();
		
		roboFlies.add(dragonFly1);
		roboFlies.add(dragonFly2);
		roboFlies.add(moskito1);
		roboFlies.add(moskito2);
		roboFlies.add(copepod1);
		roboFlies.add(copepod2);		
		
		return roboFlies;
	}
}