package mongodb.belgaia;

import mongodb.belgaia.kata1.Kata1;
import mongodb.belgaia.kata2.Kata2;
import mongodb.belgaia.kata3.CollectionDoesNotExistExc;
import mongodb.belgaia.kata3.Kata3;
import mongodb.belgaia.kata4.Kata4;
import mongodb.belgaia.kata5.Kata5;

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
		Kata1 kata1 = new Kata1();
		kata1.startKata1();
	}
	
	private void startKataTwo() {
		
		System.out.println("Executing Kata 2");
		System.out.println("##################");
		
		String collectionName = "measurements";
		String exportType = "csv";
		String fileName = "src/main/resources/kata2/kata2measurements.csv";

		Kata2 kataSolution = new Kata2();
		kataSolution.startKata2(collectionName, exportType, fileName);
		
		System.out.println("--- Waiting for some seconds ---");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Executing Kata 2.2");
		System.out.println("##################");
		kataSolution.startKata2dot2(DATABASE_NAME);
	}
	
	private void startKataThree() throws CollectionDoesNotExistExc {
		
		System.out.println("Executing Kata 3");
		System.out.println("##################");
		
		Kata3 kataSolution = new Kata3(DATABASE_NAME);
		kataSolution.startKata3();
		kataSolution.startKata3dot2(); // FIXME: Does not work properly (removes fields only from the first document)
		
	}
	
	private void startKataFour() {
		
		System.out.println("Executing Kata 4");
		System.out.println("################");
		
		Kata4 kataSolution = new Kata4();
		kataSolution.startKata4();
	}
	
	private void startKataFive() {
		
		System.out.println("Executing Kata 5");
		System.out.println("################");
		
		Kata5 kataSolution = new Kata5();
		kataSolution.startKata5();
	}
	
	public static final void main (String[] args) {
		
		try {
			ExecutorOfAllKatas executor = new ExecutorOfAllKatas();
			executor.startKataOne();
			executor.startKataTwo();
			executor.startKataThree();
			executor.startKataFour();
			executor.startKataFive();
		} catch (CollectionDoesNotExistExc e) {
			e.printStackTrace();
		}
		
		System.out.println("##################");
		System.out.println("Executed all Katas");
	}
}
