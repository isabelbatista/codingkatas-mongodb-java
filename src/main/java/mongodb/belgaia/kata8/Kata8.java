package mongodb.belgaia.kata8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Kata8 {

	private static final String DATABASE_NAME = "mobilerobotics";
	private static final double[] BUG_COORDINATES = {13.237033, 52.499789};
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
		List<RoboFly> roboFlies = mongoConnector.findRoboFliesNearByBug(BUG_COORDINATES, countOfFlies);
		
		if(roboFlies.size() < countOfFlies) {
			System.out.println("Found less than 3 roboflies near to the bug.");
		}
		
		List<String> nearRoboFlies = new ArrayList<String>();
		for(RoboFly roboFly : roboFlies) {
			nearRoboFlies.add(roboFly.getId());
		}
		
		return nearRoboFlies;
	}
	
	public void startKata8() {
		
		System.out.println("");
		System.out.println("Update roboflies with current position coordinates (2D index).");
		System.out.println("##############################################");
		System.out.println("");
		
		updateRoboFliesWithCoordinates();	
		
		System.out.println("Find 3 roboflies near to the bug.");
		System.out.println("#################################");
		System.out.println("");
		
		List<String> roboFlies = findThreeRoboFliesNearToBug();
		System.out.print("Near to the bug are: ");
		
		int counter = 0;
		for(String roboFly : roboFlies) {
			System.out.print(roboFly);
						
			counter++;
			if(counter == 3) {
				break;
			}
			
			System.out.print(", ");
		}
		System.out.println("");
	}
}
