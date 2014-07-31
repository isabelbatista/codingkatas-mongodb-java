package mongodb.belgaia;

import mongodb.belgaia.kata1.RoboFliesPersistence;
import mongodb.belgaia.kata2.Kata2;

/**
 * Class to run all kata solutions from the book "Coding Katas for MongoDB".
 * 
 * This is for getting the complete database.
 * 
 * @author isabel.batista
 *
 */
public class ExecutorOfAllKatas {
	
	private static final String DATABASE_NAME = "mobilerobotics";
	
	private void startKataOne() {
		
		System.out.println("Executing Kata 1");
		RoboFliesPersistence.main(null);
	}
	
	private void startKataTwo() {
		
		System.out.println("Executing Kata 2");
		System.out.println("##################");
		
		String collectionName = "measurements";
		String exportType = "csv";
		String fileName = "src/main/resources/kata2/kata2measurements.csv";

		Kata2 kataSolution = new Kata2();
		kataSolution.startKata2(collectionName, exportType, fileName);
		
		System.out.println("Executing Kata 2.2");
		System.out.println("##################");
		kataSolution.startKata2dot2(DATABASE_NAME);
	}
	
	public static final void main (String[] args) {
		
		ExecutorOfAllKatas executor = new ExecutorOfAllKatas();
		executor.startKataOne();
		executor.startKataTwo();
		
		System.out.println("##################");
		System.out.println("Executed all Katas");
	}
}
