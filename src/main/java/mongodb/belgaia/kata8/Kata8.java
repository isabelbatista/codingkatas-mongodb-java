package mongodb.belgaia.kata8;

import java.util.Map;


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
}
