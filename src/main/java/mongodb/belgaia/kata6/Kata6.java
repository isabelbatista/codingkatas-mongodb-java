package mongodb.belgaia.kata6;


public class Kata6 {
	
	private static final String LOADING_TIME_FIELDNAME = "charge_length_minutes";
	private static final String ENERGY_UNITS_FIELDNAME = "energy_units";
	
	private MongoConnector mongoConnector;
	
	public Kata6() {
		mongoConnector = new MongoConnector();
	}
	
	public Kata6(String databaseName) {
		mongoConnector = new MongoConnector(databaseName);
	}
	
	public double calculateAverageLoadingTime(RoboFlyType roboflyType) {
		return mongoConnector.calculateAverageOfFieldByRoboFlyType(LOADING_TIME_FIELDNAME, roboflyType);
	}
	
	public double calculateAverageOfEnergyUnits(RoboFlyType roboFlyType) {
		return mongoConnector.calculateAverageOfFieldByRoboFlyType("energy_units", roboFlyType);
	}
}
