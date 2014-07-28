package mongodb.belgaia;

import mongodb.belgaia.kata1.RoboFliesPersistence;
import mongodb.belgaia.kata2.MeasurementPersistence;
import mongodb.belgaia.kata2.MongoImporteur;

public class ExecutorOfAllKatas {
	
	public static final void main (String[] args) {
		
		System.out.println("Executing Kata 1");
		RoboFliesPersistence.main(null);
		
		System.out.println("Executing Kata 2");
		
		String collectionName = "measurements";
		String fileExtension = "csv";
		String fileName = "src/main/resources/kata2/kata2measurements.csv";
		String[] commandParameters = {collectionName, fileExtension, fileName};
		MongoImporteur.main(commandParameters);
	}

}
