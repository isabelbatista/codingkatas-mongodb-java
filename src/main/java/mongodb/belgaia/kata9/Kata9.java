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

	public void startKata9() {
		
		System.out.println("");
		System.out.println("Insert bug territory path points as polygon.");
		System.out.println("##############################################");
		System.out.println("");
		
		insertBugTerritoryCoordinates();
		
		System.out.println("");
		System.out.println("Find roboflies with station in bug territory.");
		System.out.println("##############################################");
		System.out.println("");
		
		List<String> roboFlyIds = findRoboFliesWithinBugTerritory();
		
		System.out.print("Following roboflies can be found within the bug territory: ");
		
		for(int position=0; position<=roboFlyIds.size()-1; position++) {
			System.out.print(roboFlyIds.get(position));
			if(position < roboFlyIds.size()-1) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}
}
