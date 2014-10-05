package mongodb.belgaia.kata4;

public class Kata4 {

	private MongoAggregator aggregator;
	
	public Kata4() {
		aggregator = new MongoAggregator();
	}
	
	public void startKata4() {
		
		System.out.println("Calculate averages of measurements.");
		calculateAverages();
		
		System.out.println("Mark all roboflies with status 'To be calibrated' that measure wrong values.");
		markRoboFliesToBeCalibrated();
	}
	
	private void calculateAverages() {
		
		Double soundIntensityAvg = aggregator.calculateAverage("soundIntensity");
		Double co2contentAvg = aggregator.calculateAverage("co2content");
		Double humidityAvg = aggregator.calculateAverage("humidity");
		Double airPressureAvg = aggregator.calculateAverage("airPressure");
		Double luminosityAvg = aggregator.calculateAverage("luminosity");
		Double temperatureAvg = aggregator.calculateAverage("temperature");
		
		System.out.println("Sound Intensity: " + soundIntensityAvg);
		System.out.println("Co2 Content: " + co2contentAvg);
		System.out.println("Humidity: " + humidityAvg);
		System.out.println("Air Pressure: " + airPressureAvg);
		System.out.println("Luminosity: " + luminosityAvg);
		System.out.println("Temperature: " + temperatureAvg);
	}
	
	private void markRoboFliesToBeCalibrated() {
		aggregator.markRoboFliesToBeCalibrated(RoboFlyStatus.TO_BE_CALIBRATED);
	}
}
