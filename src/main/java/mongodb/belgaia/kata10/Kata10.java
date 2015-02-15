package mongodb.belgaia.kata10;

/**
 * Solution for Kata 10 from the Book
 * "Coding Katas for MongoDB"
 * 
 * Inserting the costs for multiple roboflies (e.g. reparation, updates and energy costs)
 * and finding the most expensive robofly type by using map/reduce.
 * 
 * @param args
 */
public class Kata10 {

	private MongoConnector connector = new MongoConnector();
	
	public void startKata10() {
		
		System.out.println("Solution for Kata 10");
		System.out.println("###################");
		System.out.println("");

		System.out.println("Import costs data from csv file.");
		System.out.println("--------------------------------");
		
		connector.importCostsDataToMongoDb();
	}
}
