package mongodb.belgaia.kata6;



public class Kata6 {
	
	private static final String LOADING_TIME_FIELDNAME = "charge_length_minutes";
	private static final String ENERGY_UNITS_FIELDNAME = "energy_units";
	
	private static final String DATABASE_NAME = "mobilerobotics";
	private MongoConnector mongoConnector;
	
	public Kata6() {
		mongoConnector = new MongoConnector(DATABASE_NAME);
	}
	
	public Kata6(String databaseName) {
		mongoConnector = new MongoConnector(databaseName);
	}
	
	public void startKata6() {
		
		System.out.println("Import data about charging of robotic flies.");
		prepareDatabase();
		
		System.out.println("Calculate averages of loading time for each robofly type.");
		System.out.println("Type FLY: " + calculateAverageLoadingTime(RoboFlyType.FLY));
		System.out.println("Type MOSKITO: " + calculateAverageLoadingTime(RoboFlyType.MOSKITO));
		System.out.println("Type DRAGONFLY: " + calculateAverageLoadingTime(RoboFlyType.DRAGONFLY));
		System.out.println("Type COPEPOD: " + calculateAverageLoadingTime(RoboFlyType.COPEPOD));
		
		System.out.println("Calculate averages of needed energy units for each robofly type.");
		System.out.println("Type FLY: " + calculateAverageOfEnergyUnits(RoboFlyType.FLY));
		System.out.println("Type MOSKITO: " + calculateAverageOfEnergyUnits(RoboFlyType.MOSKITO));
		System.out.println("Type DRAGONFLY: " + calculateAverageOfEnergyUnits(RoboFlyType.DRAGONFLY));
		System.out.println("Type COPEPOD: " + calculateAverageOfEnergyUnits(RoboFlyType.COPEPOD));
	}
	
	
	
	public double calculateAverageLoadingTime(RoboFlyType roboflyType) {
		return mongoConnector.calculateAverageOfFieldByRoboFlyType(LOADING_TIME_FIELDNAME, roboflyType);
	}
	
	public double calculateAverageOfEnergyUnits(RoboFlyType roboFlyType) {
		return mongoConnector.calculateAverageOfFieldByRoboFlyType(ENERGY_UNITS_FIELDNAME, roboFlyType);
	}
	
	private void prepareDatabase() {
		DatabasePreparation preparation = new DatabasePreparation(mongoConnector);
		preparation.prepareDatabase();
	}
}
