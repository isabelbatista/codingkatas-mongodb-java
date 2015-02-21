package mongodb.belgaia.kata10;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMongoConnector {

	private static final String DATABASE_NAME = "kataTest";
	private MongoConnector mongodbConnector;
	
	@Before
	public void setUp() {
		mongodbConnector = new MongoConnector(DATABASE_NAME);
		TestPreparation preparation = new TestPreparation(DATABASE_NAME);
		preparation.prepareDatabase();
	}
	
	@After
	public void tearDown() {
		mongodbConnector.dropDatabase();
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
	
	@Test
	public void shouldReturnMostExpensiveRoboFly() {
		
		mongodbConnector.importCostsDataToMongoDb();
		
		try {
			System.out.println("--- Waiting for some seconds ---");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Map<String, Double> roboFly = mongodbConnector.calculateMostExpensiveRoboFly();
		Entry<String, Double> roboFlyEntry = roboFly.entrySet().iterator().next();
		
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		String formatedCosts = decimalFormat.format(roboFlyEntry.getValue());
		
		Assert.assertEquals("RoboFly_ID_7", roboFlyEntry.getKey());
		Assert.assertEquals("5622281,68", formatedCosts);
	}
	
	@Test
	public void shouldReturnCostTypeWithHighestExpenses() {
		
		mongodbConnector.importCostsDataToMongoDb();
		
		try {
			System.out.println("--- Waiting for some seconds ---");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Map<String, Double> costTypeMap = mongodbConnector.calculateCostTypeWithHighestExpenses();
		Entry<String, Double> costTypeEntry = costTypeMap.entrySet().iterator().next();
		
		DecimalFormat format = new DecimalFormat("#.00");
		String formatedCosts = format.format(costTypeEntry.getValue());
		
		Assert.assertEquals("REPAIR", costTypeEntry.getKey());
		Assert.assertEquals("28422110,76", formatedCosts);
	}
}