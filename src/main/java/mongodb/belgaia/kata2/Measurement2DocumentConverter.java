package mongodb.belgaia.kata2;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Measurement2DocumentConverter {
	
	public static DBObject convertMeasurement2Document(Measurement measurement) {
		
		DBObject document = new BasicDBObject("roboFlyId",  measurement.getRoboFlyId())
								.append("humidity", measurement.getHumidity())
								.append("barometicPressure", measurement.getBarometicPressure())
								.append("luminosity", measurement.getLuminosity())
								.append("soundIntensity", measurement.getSoundIntensity())
								.append("temperature", measurement.getTemperature())
								.append("co2content", measurement.getCo2content());
		
		return document;
		
	}

}
