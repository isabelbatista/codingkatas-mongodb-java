package mongodb.belgaia.kata10;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetGenerator {
	
	private static final int MAX_REPAIRS_TYPE_COPEPOD = 439; //1; //439;
	private static final int MAX_REPAIRS_TYPE_MOSKITO = 3814; //4; //3814;
	private static final int MAX_REPAIRS_TYPE_DRAGONFLY = 1916; //2; //1916;
	private static final int MAX_REPAIRS_TYPE_FLY = 2500; //3; //2500;
	
	private static final String ROBOFLY_ID_PREFIX = "RoboFly_ID_";
	private static final int ROBOFLY_ID_MAX_NUMBER = 9;
	private static final String SEPARATOR = ";";
	private static final String DEFAULT_CURRENCY = "EUR";
	private static final String COSTS_FIELD_NAME = "costs_in_euro";
	private static final String COST_ID_FIELD_NAME = "_id";
	private static final String ROBOFLY_ID_FIELD_NAME = "robofly_id";
	private static final String COST_TYPE_FIELD_NAME = "cost_type";
	
	private static final String FILEPATH = "src/test/resources/kata10/";
	private static final String FILENAME = "costs.csv";
	
	private static final int MAX_ENERGY_COST_ITEMS = 2000;
	private static final int MAX_UPGRADE_COST_ITEMS = 15;
	private static final int MAX_SEARCH_COST_ITEMS = 570;
	
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
		
		collectEnergyCostItems();
		collectRepairCostItems();
		collectUpgradeCostItems();
		collectSearchCostItems();
	}
	
	private static final void collectEnergyCostItems() {
		double min = 0.25;
		double max = 5.10;
		for(int roboFlyCount=1; roboFlyCount <= ROBOFLY_ID_MAX_NUMBER; roboFlyCount++) {
			for(int energyCostCount=0; energyCostCount<=MAX_ENERGY_COST_ITEMS-1; energyCostCount++) {
				double randomEnergyCostAmount = Math.random() * (max - min) + min;
				addCostItem(ROBOFLY_ID_PREFIX + roboFlyCount, CostType.ENERGY, String.valueOf(randomEnergyCostAmount));
			}
		}
	}
	
	private static final void collectRepairCostItems() {
		double min = 5.00;
		double max = 2845.00;
		
		for(int roboFlyCount=1; roboFlyCount <= ROBOFLY_ID_MAX_NUMBER; roboFlyCount++) {
			
			int maxRepairCostItems = 0;
			switch(roboFlyCount) {
				case 1 : case 2 : case 3: maxRepairCostItems = MAX_REPAIRS_TYPE_FLY;
					break;
				case 4 : case 5: maxRepairCostItems = MAX_REPAIRS_TYPE_DRAGONFLY;
					break;
				case 6 : case 7: maxRepairCostItems = MAX_REPAIRS_TYPE_MOSKITO; // highest repair costs
					break;
				case 8 : case 9: maxRepairCostItems = MAX_REPAIRS_TYPE_COPEPOD;
					break;
				default: maxRepairCostItems = 1000;
					break;
			}
			
			for(int repairCostItems=0; repairCostItems<=maxRepairCostItems-1; repairCostItems++) {
				double randomRepairCostAmount = Math.random() * (max - min) + min;
				addCostItem(ROBOFLY_ID_PREFIX + roboFlyCount, CostType.REPAIR, String.valueOf(randomRepairCostAmount));
			}
		}
	}
	
	public static final void collectUpgradeCostItems() {
		double min = 1000.00;
		double max = 12500.00;
		
		for(int roboFlyCount=1; roboFlyCount <= ROBOFLY_ID_MAX_NUMBER; roboFlyCount++) {
			for(int upgradeCostItems=0; upgradeCostItems<=MAX_UPGRADE_COST_ITEMS-1; upgradeCostItems++) {
				double randomUpgradeCostAmount = Math.random() * (max - min) + min;
				addCostItem(ROBOFLY_ID_PREFIX + roboFlyCount, CostType.UPGRADE, String.valueOf(randomUpgradeCostAmount));
			}
		}
	}
	
	public static final void collectSearchCostItems() {
		double min = 1.25;
		double max = 100.00;
		
		for(int roboFlyCount=1; roboFlyCount <= ROBOFLY_ID_MAX_NUMBER; roboFlyCount++) {
			for(int searchCostItems=0; searchCostItems<=MAX_SEARCH_COST_ITEMS-1; searchCostItems++) {
				double randomSearchCostAmount = Math.random() * (max - min) + min;
				addCostItem(ROBOFLY_ID_PREFIX + roboFlyCount, CostType.SEARCH, String.valueOf(randomSearchCostAmount));
			}
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
