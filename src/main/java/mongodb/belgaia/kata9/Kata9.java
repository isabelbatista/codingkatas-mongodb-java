package mongodb.belgaia.kata9;

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

	public void findRoboFliesWithinBugTerritory() {
		
		
	}
	
	public void insertBugTerritoryCoordinates() {
		fileReader = new CSVFileReader();
		Map<String, double[]> bugrouteCoordinates = fileReader.getContentOfBugrouteCsv();
		
		mongoConnector.createBugRouteDocument(bugrouteCoordinates);		
	}

	public Map<String, double[]> getBugTerritoryCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
