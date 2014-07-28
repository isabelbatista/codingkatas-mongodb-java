package mongodb.belgaia.kata2;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MeasurementPersistence {
	
	private final String DATABASE_NAME = "mobilerobotics";
	private final String COLLECTION_NAME = "measurements";

	private Mongo mongoDbClient;
	private DB database;
	private DBCollection measurementCollection;
	
	public MeasurementPersistence() {
		
		try {
			
			mongoDbClient = new Mongo("localhost", 27017);
			database = mongoDbClient.getDB(DATABASE_NAME);
			measurementCollection = database.getCollection(COLLECTION_NAME);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	public MeasurementPersistence(String databaseName) {
		
		try {
			
			mongoDbClient = new Mongo("localhost", 27017);
			database = mongoDbClient.getDB(databaseName);
			measurementCollection = database.getCollection(COLLECTION_NAME);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	public String saveMeasurement(Measurement measurement)  {
		
		DBObject document = Measurement2DocumentConverter.convertMeasurement2Document(measurement);
		
		measurementCollection.insert(document);
		return document.get("_id").toString();
	}
	
	public void dropDatabase(String databaseName) {
		
		mongoDbClient.dropDatabase(databaseName);
	}
	
	/**
	 * Solution for Kata 2 from the Book
	 * "Coding Katas for MongoDB"
	 * 
	 * Inserting an initial set of measurements to mongodb, that
	 * were collected by the robo flies.
	 * @param args
	 */
	public static void main (String[] args) {
		
		System.out.println("Solution for Kata 2");
		System.out.println("###################");
		System.out.println("");
		
		MeasurementPersistence persistenceKata2 = new MeasurementPersistence();
		
		List<Measurement> measurements = persistenceKata2.createMeasurements();
		
		List<String> ids = new ArrayList<String>();
		
		for(Measurement measurement : measurements) {
			ids.add(persistenceKata2.saveMeasurement(measurement));
		}
		
		for (String id : ids) {
			
			System.out.println("Saved measurement with id: " + id);
		}
	}
	
	private List<Measurement> createMeasurements() {
		
		List<Measurement> measurements = new ArrayList<Measurement>();
		
		Measurement measurement = new Measurement("RoboFly_ID_1");
		measurement.setHumidity(20.01f);
		measurement.setBarometicPressure(1013.25f);
		measurement.setLuminosity(600);
		measurement.setSoundIntensity(55);
		measurement.setTemperature(17);
		measurement.setCo2content(0.04f);
		
		measurements.add(measurement);
		
		Measurement measurement2 = new Measurement("RoboFly_ID_2");
		measurement2.setHumidity(21.40f);
		measurement2.setBarometicPressure(1011.78f);
		measurement2.setLuminosity(598);
		measurement2.setSoundIntensity(75);
		measurement2.setTemperature(16.5f);
		measurement2.setCo2content(0.04f);
		
		measurements.add(measurement2);
		
		Measurement measurement3 = new Measurement("RoboFly_ID_3");
		measurement3.setHumidity(16.90f);
		measurement3.setBarometicPressure(1012.25f);
		measurement3.setLuminosity(420);
		measurement3.setSoundIntensity(0);
		measurement3.setTemperature(17.1f);
		measurement3.setCo2content(0.02f);
		
		measurements.add(measurement3);
		
		return measurements;	
		
	}
}
