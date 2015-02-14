package mongodb.belgaia.kata10;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetGenerator {
	
	private static final String ROBOFLY_ID_PREFIX = "RoboFly_ID_";
	private static final String SEPARATOR = ";";
	private static final String DEFAULT_CURRENCY = "Eur";
	private static final String COSTS_FIELD_NAME = "costs_in_euro";
	private static final String COST_ID_FIELD_NAME = "_id";
	private static final String ROBOFLY_ID_FIELD_NAME = "robofly_id";
	private static final String COST_TYPE_FIELD_NAME = "cost_type";
	
	private static final String FILEPATH = "src/test/resources/kata10/";
	private static final String FILENAME = "costs.csv";
	
	private static final int MAX_ENERGY_COST_ITEMS = 1;
	
	private static final Map<String, List<CostItem>> costItems = new HashMap<String, List<CostItem>>();
	
	private static void writeFile() throws FileNotFoundException, UnsupportedEncodingException {
		
		// collect all cost datas in the Map
		collectCostItems();
		
		// Create writer and write	
		PrintWriter writer = new PrintWriter(FILEPATH + FILENAME, "UTF-8");
		
		writer.println(generateHeader());
		addCostsToWriter(writer);
		
		writer.close();
	}	

	private static String generateHeader() {
		return COST_ID_FIELD_NAME + "," + ROBOFLY_ID_FIELD_NAME + "," + COST_TYPE_FIELD_NAME + "," + COSTS_FIELD_NAME;
	}
	
	private static String generateCostsLine(String roboFlyId, String costType, String costs) {
		StringBuilder costsLine = new StringBuilder(roboFlyId);
		costsLine.append(SEPARATOR);
		costsLine.append(costType);
		costsLine.append(SEPARATOR);
		costsLine.append(costs);
		return costsLine.toString();
	}
	
	private static final void addCostItem(String roboFlyId, CostType costType, String amount) {
		
		CostItem costItem = new CostItem.Builder(roboFlyId).type(costType).currency(DEFAULT_CURRENCY).amount(amount).build();
		
		boolean roboFlyEntryFoundInMap = false;
		
		for(Map.Entry<String, List<CostItem>> roboFlyMap : costItems.entrySet()) {
			if(roboFlyMap.getKey().equals(roboFlyId)) {
				roboFlyEntryFoundInMap = true;
				List<CostItem> costItems = roboFlyMap.getValue();
				costItems.add(costItem);
			}
		}
		
		if(roboFlyEntryFoundInMap == false) {
			List<CostItem> costItemList = new ArrayList<CostItem>();
			costItemList.add(costItem);
			costItems.put(roboFlyId, costItemList);
		}
	}
	
	private static final void collectCostItems() {
		
		// INVESTEMENT cost items
		addCostItem(ROBOFLY_ID_PREFIX + "1", CostType.INVESTMENT, "25000,00");
		addCostItem(ROBOFLY_ID_PREFIX + "2", CostType.INVESTMENT, "25000,00");
		addCostItem(ROBOFLY_ID_PREFIX + "3", CostType.INVESTMENT, "25000,00");
		addCostItem(ROBOFLY_ID_PREFIX + "4", CostType.INVESTMENT, "32000,00");
		addCostItem(ROBOFLY_ID_PREFIX + "5", CostType.INVESTMENT, "32000,00");
		addCostItem(ROBOFLY_ID_PREFIX + "6", CostType.INVESTMENT, "18300,00");
		addCostItem(ROBOFLY_ID_PREFIX + "7", CostType.INVESTMENT, "18300,00");
		addCostItem(ROBOFLY_ID_PREFIX + "8", CostType.INVESTMENT, "8000,00");
		addCostItem(ROBOFLY_ID_PREFIX + "9", CostType.INVESTMENT, "8000,00");
		
		// ENERGY cost items
		double min = 0.25;
		double max = 5.10;
		for(int i=0; i<=MAX_ENERGY_COST_ITEMS-1; i++) {
			double randomEnergyCostAmount = Math.random() * (max - min) + min;
			addCostItem(ROBOFLY_ID_PREFIX + "1", CostType.ENERGY, String.valueOf(randomEnergyCostAmount));
		}		
	}
	
	private static void addCostsToWriter(PrintWriter writer) {
		for(Map.Entry<String, List<CostItem>> item : costItems.entrySet()) {
			List<CostItem> costItems = item.getValue();
			
			for(CostItem costItem : costItems) {
				StringBuilder line = new StringBuilder();
				line.append(generateCostsLine(item.getKey(), costItem.getType().name(), costItem.getAmount()));
				System.out.println(line.toString());
				writer.println(line.toString());
			}
		}
	}
	 
	public static void main(String[] args) {
		
		try {
			writeFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
