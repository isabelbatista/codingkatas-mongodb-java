package mongodb.belgaia.kata4;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import mongodb.belgaia.kata1.RoboFliesPersistence;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

public class MongoAggregator {

	private static final String DATABASE_NAME = "mobilerobotics";
	
	private static final Double TOLERANCE_AVG_SOUND_INTENSITY = new Double(20.00);
	private Mongo client;
	private DB database;
	
	private DBCollection roboFlyCollection;
	private DBCollection measurementCollection;

	public MongoAggregator() {
		
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			
			initDatabaseElements();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MongoAggregator(String databaseName) {
		
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(databaseName);
			
			initDatabaseElements();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveRoboflies(List<DBObject> roboFlies) {		
		roboFlyCollection.insert(roboFlies);		
	}
	
	public void saveMeasurements(List<DBObject> measurements) {
		measurementCollection.insert(measurements);
	}
	
	public List<DBObject> findRoboFliesWithWrongSoundIntensity() {
		
		// group by roboFly // average of soundIntensity
		List<DBObject> roboFliesAverage = calculateAverage("roboFlyID", "soundIntensity");
		
		Double soundIntensityAvg = new Double(0.0);
		for(DBObject roboFlyAvg : roboFliesAverage) {
			soundIntensityAvg += (Double)roboFlyAvg.get("average");
		}
		
		System.out.println("Average so far: " + soundIntensityAvg / roboFliesAverage.size());

		return checkRoboFliesAgainstAverage(soundIntensityAvg/roboFliesAverage.size());
	}
	
	private List<DBObject> checkRoboFliesAgainstAverage(Double average) {
		
		DBCursor measurements = measurementCollection.find();
		
		Double minValue = average - TOLERANCE_AVG_SOUND_INTENSITY;
		Double maxValue = average + TOLERANCE_AVG_SOUND_INTENSITY;
		
		List<DBObject> wrongRoboFlies = new ArrayList<DBObject>();
		while(measurements.hasNext()) {
			DBObject measurementDoc = measurements.next();
						
			Integer avg = (Integer) measurementDoc.get("soundIntensity");
			if((avg >= minValue) && (avg <= maxValue)) {
				System.out.println("The value " + avg + " lies in the average range of " + minValue + "-" + maxValue);
			} else {
				System.out.println("This value is not in the expected average range: " + avg + ". The average range is " + minValue + "-" + maxValue);
				
				DBObject roboFlyByFailingMeasurement = new BasicDBObject();
				roboFlyByFailingMeasurement.put("_id", measurementDoc.get("roboFlyID"));
				
				DBCursor roboFlyResult = roboFlyCollection.find(roboFlyByFailingMeasurement);
				wrongRoboFlies.addAll(roboFlyResult.toArray());
			}			
		}
		return wrongRoboFlies;		
	}
	
	
	public List<DBObject> calculateAverage(String groupingFieldName, String averageFieldName) {
		
		DBObject average = new BasicDBObject();
		average.put("average", new BasicDBObject("$avg", "$soundIntensity"));
		
		if(groupingFieldName != null) {
			average.put("_id", "$" + groupingFieldName);
		} else {
			average.put("_id", "allRoboFlies");
		}
		
		AggregationOutput output = measurementCollection.aggregate(new BasicDBObject("$group", average));
		
		List<DBObject> documentResults = new ArrayList<DBObject>();
		for(DBObject result : output.results()) {
			documentResults.add(result);
			System.out.println("Output: " + result);
		}
		
		return documentResults;

	}
		
	private DBObject addProjection() {
		
		DBObject fields = new BasicDBObject("roboFlyId", 1);
		fields.put("soundIntensity", 1);
		return new BasicDBObject("$project", fields);
	}
	
	public void createDocumentReference(String roboFlyReferenceId, DBObject measurement) {
					
		DBRef docReference = new DBRef(database, "roboflies", roboFlyReferenceId);
		measurement.put("robofly", docReference);
		
		DBCollection collection = database.getCollection("measurements");
		collection.save(measurement);
	}
	
	private void initDatabaseElements() {
		
		roboFlyCollection = database.getCollection("roboflies");
		measurementCollection = database.getCollection("measurements");
	}

}
