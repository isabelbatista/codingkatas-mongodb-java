package mongodb.belgaia.kata2;

import com.mongodb.DBObject;

public class Kata2 {
	
	public void startKata2(String collectionName, String exportType, String fileName) {
				
		MongoImporteur importeur = new MongoImporteur();
		importeur.importData2MongoDb(collectionName, exportType, fileName);
		
	}
	
	public void startKata2dot2(String databaseName) {
		
		MongoDbConnector mongodbConnector = new MongoDbConnector(databaseName);
		
		final String referenceCollection = "roboflies";
		final String documentCollection = "measurements";
		final String referenceField = "RoboFlyID";
		
		// Add document reference for measurement 1
		System.out.println("Creating doc reference for robofly 1 and measurement 1");
		
		String referenceId = "RoboFly_ID_1";
		DBObject measurementDoc = mongodbConnector.getMeasurementById("measurement_average_1");
		
		Robofly2MeasurementReference docReference = new Robofly2MeasurementReference(referenceCollection, documentCollection, referenceField, referenceId, measurementDoc);
		mongodbConnector.setDocumentReference(docReference);
		
		// Add document reference for measurement 2
		System.out.println("Creating doc reference for robofly 2 and measurement 2");

		referenceId = "RoboFly_ID_2";
		measurementDoc = mongodbConnector.getMeasurementById("measurement_average_2");
		
		docReference.setReferenceId(referenceId);
		docReference.setDocument(measurementDoc);
		
		mongodbConnector.setDocumentReference(docReference);
		
		// Add document reference for measurement 3
		System.out.println("Creating doc reference for robofly 3 and measurement 3");

		referenceId = "RoboFly_ID_3";
		measurementDoc = mongodbConnector.getMeasurementById("measurement_average_3");
				
		docReference.setReferenceId(referenceId);
		docReference.setDocument(measurementDoc);
		
		mongodbConnector.setDocumentReference(docReference);

	}

}
