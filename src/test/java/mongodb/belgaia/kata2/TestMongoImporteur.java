package mongodb.belgaia.kata2;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMongoImporteur {
	
	private static final String DATABASE_NAME = "testmongodbkatas";
	private MongoImporteur importeur;
	private MongoDbConnector mongodbConnector;
	
	@Before
	public void setUp() {
		importeur = new MongoImporteur(DATABASE_NAME);
		mongodbConnector = new MongoDbConnector(DATABASE_NAME);
	}
	
	@After
	public void tearDown() {
		mongodbConnector.dropDatabase();
	}
	
	@Test
	public void shouldImportMeasurements() {
		
		String collectionName = "measurements";
		String exportType = "csv";
		String fileName = "src/test/resources/kata2/kata2measurements.csv";
		
		importeur.importData2MongoDb(collectionName, exportType, fileName);
		
		int documentCount = mongodbConnector.getDocumentCount(collectionName);
		Assert.assertEquals(3, documentCount);		
	}
}
