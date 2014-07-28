package mongodb.belgaia.kata2;

public class Measurement {
	
	private String roboFlyId;
	public String getRoboFlyId() {
		return roboFlyId;
	}

	private float humidity;	// in percent
	private float barometicPressure; // in hecto pascal
	private int luminosity; // in lux
	private int soundIntensity; // in decibel
	private float temperature; // in celsius
	private float co2content; // in percent
	
	public Measurement(String roboFlyId) {
		
		this.roboFlyId = roboFlyId;
		
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getBarometicPressure() {
		return barometicPressure;
	}

	public void setBarometicPressure(float barometicPressure) {
		this.barometicPressure = barometicPressure;
	}

	public int getLuminosity() {
		return luminosity;
	}

	public void setLuminosity(int luminosity) {
		this.luminosity = luminosity;
	}

	public int getSoundIntensity() {
		return soundIntensity;
	}

	public void setSoundIntensity(int soundIntensity) {
		this.soundIntensity = soundIntensity;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getCo2content() {
		return co2content;
	}

	public void setCo2content(float co2content) {
		this.co2content = co2content;
	}

}