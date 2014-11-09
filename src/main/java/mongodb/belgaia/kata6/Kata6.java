package mongodb.belgaia.kata6;


public class Kata6 {
	
	MongoConnector mongoConnector = new MongoConnector();
	
	public double calculateAverageLoadingTime(RoboFlyType roboflyType) {
		mongoConnector.calculateAverageLoadingTime(roboflyType);
		return 0.0;
	}
}
