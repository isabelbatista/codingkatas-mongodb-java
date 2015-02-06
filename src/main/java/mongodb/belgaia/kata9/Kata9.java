package mongodb.belgaia.kata9;

import java.util.List;
import java.util.Map;


public class Kata9 {

	private static final String DATABASE_NAME = "mobilerobotics";

	private MongoConnector mongoConnector;
	private CSVFileReader fileReader;
	
	public Kata9() {
		mongoConnector = new MongoConnector(DATABASE_NAME);
	}
	
	public Kata9(String databaseName) {
		mongoConnector = new MongoConnector(databaseName);
	}

	public List<String> findRoboFliesWithinBugTerritory() {
		return mongoConnector.findRoboFlyWithinBugTerritory();
	}
	
	public void insertBugTerritoryCoordinates() {
				
		mongoConnector.createBugRouteDocument(getBugTerritoryCoordinates());		
	}

	public Map<String, double[]> getBugTerritoryCoordinates() {
		fileReader = new CSVFileReader();
		return fileReader.getContentOfBugrouteCsv();
	}
}
