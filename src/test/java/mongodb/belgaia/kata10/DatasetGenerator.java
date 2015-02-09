package mongodb.belgaia.kata10;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class DatasetGenerator {
	
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
		writer.close();
	}
	
	private static String generateHeader() {
		return COST_ID_FIELD_NAME + "," + ROBOFLY_ID_FIELD_NAME + "," + COST_TYPE_FIELD_NAME + "," + COSTS_FIELD_NAME;
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
