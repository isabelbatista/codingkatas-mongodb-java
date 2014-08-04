package mongodb.belgaia.kata1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mongodb.belgaia.shared.RoboFly;

/**
 * Solution for Kata 1 from the Book
 * "Coding Katas for MongoDB"
 * 
 * Inserting an initial set of robo flies to mongodb.
 * @param args
 */
public class Kata1 {
	
	public void startKata1() {
		
		System.out.println("Solution for Kata 1");
		System.out.println("###################");
		System.out.println("");

		RoboFliesPersistence persistence = new RoboFliesPersistence();
		List<String> savedRoboFlyIds = persistence.saveRoboFlies(getInitialSetOfFlies());
		
		for (String roboFlyId : savedRoboFlyIds) {
			
			System.out.println("Saved robo fly with id: " + roboFlyId);
		}
		
	}
	
	private Map<String, RoboFly> getInitialSetOfFlies() {

		Map<String, RoboFly> roboFlyMap = new HashMap<String, RoboFly>();

		RoboFly roboFly1 = new RoboFly("Calliphora", 2014, 2, 60, Status.OK);
		roboFly1.setId("RoboFly_ID_1");
		roboFlyMap.put(roboFly1.getId(), roboFly1);

		RoboFly roboFly2 = new RoboFly("Lucilia", 2014, 2, 60, Status.OK);
		roboFly2.setId("RoboFly_ID_2");
		roboFlyMap.put(roboFly2.getId(), roboFly2);

		RoboFly roboFly3 = new RoboFly("Onesia", 2014, 2, 60, Status.OK);
		roboFly3.setId("RoboFly_ID_3");
		roboFlyMap.put(roboFly3.getId(), roboFly3);

		return roboFlyMap;
	}
	
}
