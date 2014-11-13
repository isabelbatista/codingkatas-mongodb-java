package mongodb.belgaia.kata7;

import java.util.List;

import org.junit.Assert;

import com.mongodb.DBObject;



public class Kata7 {

	private static final String DATABASE_NAME = "mobilerobotics";
	private MongoConnector mongoConnector;
	
	public Kata7() {
		mongoConnector = new MongoConnector(DATABASE_NAME);
	}
	
	public Kata7(String databaseName) {
		mongoConnector = new MongoConnector(databaseName);
	}
	
	public void startKata7() {
		
		System.out.println("Erstelle Index und vergleiche Geschwindigkeit.");
		System.out.println("##############################################");
		System.out.println("");
		System.out.println("Abfrage 'RoboFly mit Status ungleich OK'");
		
		long difference1 = getDurationOfExecutionForGettingRoboFliesOfDifferingStatus();
		System.out.println("Difference of execution with and without index (in ms): " + difference1);
	}
	
	// FIXME: wrong logic -- this is not the difference, its the duration of only one execution
	private long getDurationOfExecutionForGettingRoboFliesOfDifferingStatus() {
		
		boolean withIndex = true;
		long start = System.currentTimeMillis();
		
		mongoConnector.getRoboFliesByDifferingStatus(RoboFlyStatus.OK, withIndex);
		
		return System.currentTimeMillis() - start;
	}
}
