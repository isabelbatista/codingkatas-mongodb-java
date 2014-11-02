package mongodb.belgaia.kata6;

import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class Kata6 {

	private static final String DATABASE_NAME = "mobilerobotics";

	private Mongo client;
	private DB database;
	
	private DBCollection roboFlyCollection;
	private DBCollection measurementCollection;
	
	public Kata6() {
		
		try {
			client = new Mongo("localhost", 27017);
			database = client.getDB(DATABASE_NAME);
			
			initDatabaseElements();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Kata6(String databaseName) {
		
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
		
		roboFlyCollection = database.getCollection("roboflies");
		measurementCollection = database.getCollection("measurements");
	}
	
	
	public double calculateAverageLoadingTime(RoboFlyType flyType) {
		
		
		return 0;
	}
	
	public void importData2MongoDb(String fileName) {
		
		String exportType = "csv";
		String mongoimportCommand = "mongoimport -d " + database.getName()
											 + " -c " + "roboflies"
											 + " --type " + exportType
											 + " --file " + fileName
											 + " --headerline";
		
		try {
			Runtime.getRuntime().exec(mongoimportCommand);
		} catch (IOException e) {
			System.err.println("Importing data with mongoimport threw an error: " + e.getMessage());
		}
	}
}
