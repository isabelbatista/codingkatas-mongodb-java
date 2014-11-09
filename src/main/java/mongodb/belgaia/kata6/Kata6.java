package mongodb.belgaia.kata6;


public class Kata6 {
	
	MongoConnector mongoConnector = new MongoConnector();
	
	public double calculateAverageLoadingTime(RoboFlyType roboflyType) {
		return mongoConnector.calculateAverageLoadingTime(roboflyType);
	}
}
