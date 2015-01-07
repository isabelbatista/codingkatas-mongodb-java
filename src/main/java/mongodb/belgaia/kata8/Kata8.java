package mongodb.belgaia.kata8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mongodb.DBObject;


public class Kata8 {

	private static final String DATABASE_NAME = "mobilerobotics";
	private MongoConnector mongoConnector;
	private RoboFlyUpdater roboFlyUpdater;
	
	public Kata8() {
		mongoConnector = new MongoConnector(DATABASE_NAME);
		roboFlyUpdater = new RoboFlyUpdater();
	}
	
	public Kata8(String databaseName) {
		mongoConnector = new MongoConnector(databaseName);
		roboFlyUpdater = new RoboFlyUpdater();
	}
	
	public void updateRoboFliesWithCoordinates() {
		Map<String, double[]> coordinateMap = roboFlyUpdater.getCoordinateMap();
		
		for(Map.Entry<String, double[]> entry : coordinateMap.entrySet()) {
			mongoConnector.addGeoIndexToRoboFly(entry.getKey(), entry.getValue());
		}
	}
	
	public List<String> findThreeRoboFliesNearToBug() {
		int countOfFlies = 3;
		double[] bugCoordinates = {13.241111f, 52.497222f};
		
//		List<RoboFly> mongoConnector.findRoboFliesNearByBug(bugCoordinates, countOfFlies);
		 
		List<String> nearRoboFlies = new ArrayList<String>();
		 
		 return nearRoboFlies;
	}
	
	public void startKata8() {
		
		System.out.println("");
		System.out.println("Update roboflies with current position coordinates (2D index).");
		System.out.println("##############################################");
		System.out.println("");
		
		updateRoboFliesWithCoordinates();	
	}
}
