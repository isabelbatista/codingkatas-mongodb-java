package mongodb.belgaia.kata2;

import java.io.IOException;

public class MongoImporteur {
	
	private static final String DATABASE_NAME = "mobilerobotics";
	
	/**
	 * Mongoimport needs arguments for:
	 * 1. collectionname (target for importing the data)
	 * 2. file extension without dot (type of the file that contains the data to import)
	 * 3. file name with extension (that contains the data to import)
	 * @param args
	 */
	public static void main(String[] args) {
		
		String collectionName = args[0];
		String exportType = args[1];
		String fileName = args[2];
		
		String mongoimportCommand = "mongoimport -d " + DATABASE_NAME
											 + " -c " + collectionName
											 + " --type " + exportType
											 + " --file " + fileName
											 + " --headerline";
		
		try {
			Runtime.getRuntime().exec(mongoimportCommand);
		} catch (IOException e) {
			System.err.println("Importing data with mongoimport threw an error: " + e.getMessage());
			System.err.println("Please check the file '" + fileName + "'");
		}
	}

}
