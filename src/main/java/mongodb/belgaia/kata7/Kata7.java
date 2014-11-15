package mongodb.belgaia.kata7;




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
		
		System.out.println("Create Index for Queries and compare execution time.");
		System.out.println("##############################################");
		
		System.out.println("Query for getting Roboflies with status different than OK.");
		System.out.println("Execution without index took " + getDurationOfExecutionForGettingRobofliesWithStatusDifferentThanOk(false) + " ms");
		System.out.println("Execution with index took " + getDurationOfExecutionForGettingRobofliesWithStatusDifferentThanOk(true) + " ms");

		System.out.println("----------------------------------------------");

		System.out.println("Query for getting count of roboflies of type fly.");
		System.out.println("Execution without index took " + getDurationOfExecutionForGettingCountOfRobofliesTypeFly(false) + " ms");
		System.out.println("Execution with index took " + getDurationOfExecutionForGettingCountOfRobofliesTypeFly(true) + " ms");
		
		System.out.println("----------------------------------------------");

		System.out.println("Query for calculating average of sound intensity.");
		System.out.println("Execution without index took " + getDurationOfExecutionForCalculatingAverageOfSoundIntensity(false) + " ms");
		System.out.println("Execution with index took " + getDurationOfExecutionForCalculatingAverageOfSoundIntensity(true) + " ms");	
	}
	
	private long getDurationOfExecutionForGettingRobofliesWithStatusDifferentThanOk(boolean withIndex) {
		
		if(withIndex) {
			mongoConnector.createIndexOnRoboFlyStatus();
		}
		
		long start = System.currentTimeMillis();
		mongoConnector.getRoboFliesByDifferingStatus(RoboFlyStatus.OK);
		return System.currentTimeMillis() - start;
	}
	
	private long getDurationOfExecutionForGettingCountOfRobofliesTypeFly(boolean withIndex) {
		
		if(withIndex) {
			mongoConnector.createIndexOnRoboFlyType();
		}
		
		long start = System.currentTimeMillis();
		mongoConnector.getCountOfRoboFlies(RoboFlyType.FLY);
		return System.currentTimeMillis() - start;		
	}
	
	private long getDurationOfExecutionForCalculatingAverageOfSoundIntensity(boolean withIndex) {
		
		if(withIndex) {
			mongoConnector.createIndexOnSoundIntensity();
		}
		
		long start = System.currentTimeMillis();
		mongoConnector.calculateAverageOfSoundIntensity();
		return System.currentTimeMillis() - start;
	}
}
