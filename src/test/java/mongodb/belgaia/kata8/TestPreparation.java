package mongodb.belgaia.kata8;

class TestPreparation {
	
	private static final String DATABASE_NAME = "kataTest";
	private MongoConnector connector;
	
	public TestPreparation(String databaseName) {
		connector = new MongoConnector(DATABASE_NAME);
	}
	
	public void prepareDatabase() {
		
		importDataFiles();
		
		System.out.println("--- Waiting for some seconds ---");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	private void importDataFiles() {
		String robofliesFile = "src/test/resources/kata8/roboflies.csv";
		
		connector.importData2MongoDb(robofliesFile, "roboflies");		
	}
}
