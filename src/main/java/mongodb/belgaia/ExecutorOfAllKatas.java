package mongodb.belgaia;

import mongodb.belgaia.kata1.RoboFliesPersistence;
import mongodb.belgaia.kata2.MeasurementPersistence;

public class ExecutorOfAllKatas {
	
	public static final void main (String[] args) {
		
		System.out.println("Executing Kata 1");
		RoboFliesPersistence.main(null);
		System.out.println("Executing Kata 2");
		MeasurementPersistence.main(null);
		
	}

}
