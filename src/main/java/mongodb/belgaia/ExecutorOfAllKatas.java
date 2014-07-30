package mongodb.belgaia;

import mongodb.belgaia.kata1.RoboFliesPersistence;
import mongodb.belgaia.kata2.MongoImporteur;

/**
 * Class to run all kata solutions from the book "Coding Katas for MongoDB".
 * 
 * This is for getting the complete database.
 * 
 * @author isabel.batista
 *
 */
public class ExecutorOfAllKatas {
	
	private void startKataOne() {
		
		System.out.println("Executing Kata 1");
		RoboFliesPersistence.main(null);
	}
	
	private void startKataTwo() {
		
		System.out.println("Executing Kata 2");
		
		String collectionName = "measurements";
		String fileExtension = "csv";
		String fileName = "src/main/resources/kata2/kata2measurements.csv";
		String[] commandParameters = {collectionName, fileExtension, fileName};
		MongoImporteur.main(commandParameters);
	}
	
	public static final void main (String[] args) {
		
		ExecutorOfAllKatas executor = new ExecutorOfAllKatas();
		executor.startKataOne();
		executor.startKataTwo();		
	}
}
