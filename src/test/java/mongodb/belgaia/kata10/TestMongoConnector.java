package mongodb.belgaia.kata10;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMongoConnector {

	private static final String DATABASE_NAME = "kataTest";
	private static final String COST_DATASHEET_FILENAME = "src/test/resources/kata10/cost_items.csv";

	private MongoConnector mongodbConnector;
	
	@Before
	public void setUp() {
		mongodbConnector = new MongoConnector(DATABASE_NAME);
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
	}
	
	@After
	public void tearDown() {
//		mongodbConnector.dropDatabase();
	}
	
	@Test
	public void shouldImportCostsDatasheetToDatabase() {
		
		mongodbConnector.importCostsDataToMongoDb();
		
		try {
			System.out.println("--- Waiting for some seconds ---");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int countOfCostItems = 20;
		List<CostItem> costItems = mongodbConnector.getCostItemsByRoboFly("RoboFly_ID_1", countOfCostItems);
		
		Assert.assertEquals(countOfCostItems, costItems.size());
	}
}
