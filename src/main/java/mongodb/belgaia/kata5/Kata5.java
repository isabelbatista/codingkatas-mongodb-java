package mongodb.belgaia.kata5;

import java.util.HashMap;
import java.util.Map;


public class Kata5 {

	MongoUpdater updater;
	
	public Kata5() {
		updater = new MongoUpdater();
	}
	
	public void startKata5() {
		System.out.println("Update missed or damaged roboflies with additional information and new status.");
		updateRoboFlies();
	}
	
	public void updateRoboFlies() {
		
		String roboFly2 = "RoboFly_ID_2";
		String roboFly3 = "RoboFly_ID_3";
		String roboFly5 = "RoboFly_ID_5";
		
		updater.addMoreInformation(roboFly2, createInfos(2));
		updater.addMoreInformation(roboFly3, createInfos(3));
		updater.addMoreInformation(roboFly5, createInfos(5));
		
		updater.changeStatus(roboFly2, RoboFlyStatus.OUT_OF_SERVICE);
		updater.changeStatus(roboFly3, RoboFlyStatus.OUT_OF_SERVICE);
		updater.changeStatus(roboFly5, RoboFlyStatus.OUT_OF_SERVICE);
	}
	
	private Map<String, String> createInfos(int roboFlyId) {
		
		String commentKey = "comment";
		String descKey = "statusDescription";
		String endOfLifeKey = "endOfLife";
		
		Map<String, String> information = new HashMap<String, String>();
		
		switch(roboFlyId) {
			case 2:
				information.put(descKey, StatusDescription.DAMAGED.name());
				information.put(commentKey, "Zerstoert durch Garten-geraet");
				information.put(endOfLifeKey, "30.04.14");
				break;
			case 3:
				information.put(descKey, StatusDescription.EATEN.name());
				information.put(commentKey, "Als Beute angesehen");
				information.put(endOfLifeKey, "30.04.14");
				break;
			case 5:
				information.put(descKey, StatusDescription.LOST.name());
				information.put(commentKey, "Nicht wieder aufgefunden");
				information.put(endOfLifeKey, "28.04.14");
				break;
			default:
				break;
		}
		
		return information;		
	}	
}

