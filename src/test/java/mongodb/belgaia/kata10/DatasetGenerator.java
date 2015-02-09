package mongodb.belgaia.kata10;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class DatasetGenerator {
	
	private static final String ROBOFLY_ID_PREFIX = "RoboFly_ID_";
	private static final String SEPARATOR = ";";
	
	private static final String COSTS_FIELD_NAME = "costs_in_euro";
	private static final String COST_ID_FIELD_NAME = "_id";
	private static final String ROBOFLY_ID_FIELD_NAME = "robofly_id";
	private static final String COST_TYPE_FIELD_NAME = "cost_type";
	
	private static final String FILEPATH = "src/test/resources/kata10/";
	private static final String FILENAME = "costs.csv";
	
	private static void writeFile() throws FileNotFoundException, UnsupportedEncodingException {
		
		String header = generateHeader();
		
		PrintWriter writer = new PrintWriter(FILEPATH + FILENAME, "UTF-8");
		writer.println(header);
		addInvestmentCostsToWriter(writer);
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
	
	private static void addInvestmentCostsToWriter(PrintWriter writer) {
		String investmentRoboFlyOne = generateCostsLine(ROBOFLY_ID_PREFIX + "1", CostType.INVESTMENT.name(), "25000,00");
		String investmentRoboFlyTwo = generateCostsLine(ROBOFLY_ID_PREFIX + "2", CostType.INVESTMENT.name(), "25000,00");
		String investmentRoboFlyThree = generateCostsLine(ROBOFLY_ID_PREFIX + "3", CostType.INVESTMENT.name(), "25000,00");
		String investmentRoboFlyFour = generateCostsLine(ROBOFLY_ID_PREFIX + "4", CostType.INVESTMENT.name(), "32000,00");
		String investmentRoboFlyFive = generateCostsLine(ROBOFLY_ID_PREFIX + "5", CostType.INVESTMENT.name(), "32000,00");
		String investmentRoboFlySix = generateCostsLine(ROBOFLY_ID_PREFIX + "6", CostType.INVESTMENT.name(), "18300,00");
		String investmentRoboFlySeven = generateCostsLine(ROBOFLY_ID_PREFIX + "7", CostType.INVESTMENT.name(), "18300,00");
		String investmentRoboFlyEight = generateCostsLine(ROBOFLY_ID_PREFIX + "8", CostType.INVESTMENT.name(), "8000,00");
		String investmentRoboFlyNine = generateCostsLine(ROBOFLY_ID_PREFIX + "9", CostType.INVESTMENT.name(), "8000,00");
		
		writer.println(investmentRoboFlyOne);
		writer.println(investmentRoboFlyTwo);
		writer.println(investmentRoboFlyThree);
		writer.println(investmentRoboFlyFour);
		writer.println(investmentRoboFlyFive);
		writer.println(investmentRoboFlySix);
		writer.println(investmentRoboFlySeven);
		writer.println(investmentRoboFlyEight);
		writer.println(investmentRoboFlyNine);
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
