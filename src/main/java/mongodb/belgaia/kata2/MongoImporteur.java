package mongodb.belgaia.kata2;

import java.io.IOException;

/**
 * Class for the usage of mongoimport as part of the solution for Kata 2 of the book
 * "Coding Katas for MongoDB".
 * 
 * The importeur sends a command to the mongoimport.exe that is
 * part of the mongodb installation.
 * 
 * @author isabel.batista
 *
 */
public class MongoImporteur {
	
	private String databaseName;
	
	public MongoImporteur() {
		this.databaseName = "mobilerobotics";
	}
	
	public MongoImporteur(String databaseName) {
		
		this.databaseName = databaseName;
	}
	
	public void importData2MongoDb(String collectionName, String exportType, String fileName) {
				
		String mongoimportCommand = "mongoimport -d " + databaseName
											 + " -c " + collectionName
											 + " --type " + exportType
											 + " --file " + fileName
											 + " --headerline";
		
		try {
			Runtime.getRuntime().exec(mongoimportCommand);
		} catch (IOException e) {
			System.err.println("Importing data with mongoimport threw an error: " + e.getMessage());
		}
	}
	
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
		
		MongoImporteur importeur = new MongoImporteur();
		importeur.importData2MongoDb(collectionName, exportType, fileName);
	}
}
