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

	public static final String FIELD_CO_CONTENT = "co2Content";
	public static final String FIELD_SOUND_INTENSITY = "soundIntensity";
	
	private static final String DATABASE_NAME = "mobilerobotics";
	
	private static final Double TOLERANCE_AVG_SOUND_INTENSITY = new Double(20.00);
	private static final Double TOLERANCE_AVG_CO2_CONTENT = new Double(1.00);
	
	
	
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
		
		List<DBObject> roboFliesAverage = calculateAverage("roboFlyID", "soundIntensity");
		
		Double soundIntensityAvg = new Double(0.0);
		for(DBObject roboFlyAvg : roboFliesAverage) {
			soundIntensityAvg += (Double)roboFlyAvg.get("average");
		}
		
		Double average = soundIntensityAvg / roboFliesAverage.size();
		System.out.println("Average so far: " + average);

		return checkRoboFliesAgainstAverage("soundIntensity", average, average-TOLERANCE_AVG_SOUND_INTENSITY, average+TOLERANCE_AVG_SOUND_INTENSITY);
	}
	
	private List<DBObject> checkRoboFliesAgainstAverage(String fieldName, Double average, Double toleranceMin, Double toleranceMax) {
		
		DBCursor measurements = measurementCollection.find();
		

		
		List<DBObject> wrongRoboFlies = new ArrayList<DBObject>();
		while(measurements.hasNext()) {
			DBObject measurementDoc = measurements.next();
						
			Double avg = (Double) measurementDoc.get(fieldName);
			if((avg >= toleranceMin) && (avg <= toleranceMax)) {
				System.out.println("The value " + avg + " lies in the average range of " + toleranceMin + "-" + toleranceMax);
			} else {
				System.out.println("This value is not in the expected average range: " + avg + ". The average range is " + toleranceMin + "-" + toleranceMax);
				
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
		average.put("average", new BasicDBObject("$avg", "$" + averageFieldName));
		
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

	public List<DBObject> findRoboFliesWithWrongDoubleValues(String fieldName) {
		
		List<DBObject> roboFliesAverage = calculateAverage("roboFlyID", fieldName);
		
		Double averageValue = new Double(0.0);
		for(DBObject roboFlyAvg : roboFliesAverage) {
			averageValue += (Double)roboFlyAvg.get("average");
		}
		
		Double average = averageValue / roboFliesAverage.size();
		System.out.println("Average so far: " + average);

		return checkRoboFliesAgainstAverage("co2Content", average, average-TOLERANCE_AVG_CO2_CONTENT, average + TOLERANCE_AVG_CO2_CONTENT);
	}

}
