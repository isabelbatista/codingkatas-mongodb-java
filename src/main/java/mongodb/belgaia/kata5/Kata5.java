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
		System.out.println("Add new equipment to the roboflies due to status description.");
		updateRoboFlies();
	}
	
	public void updateRoboFlies() {
		
		String roboFly2 = "RoboFly_ID_2";
		String roboFly4 = "RoboFly_ID_4";
		String roboFly6 = "RoboFly_ID_6";
		
		updater.addMoreInformation(roboFly2, createInfos(2));
		updater.addMoreInformation(roboFly4, createInfos(4));
		updater.addMoreInformation(roboFly6, createInfos(6));
		
		updater.changeStatus(roboFly2, RoboFlyStatus.OUT_OF_SERVICE);
		updater.changeStatus(roboFly4, RoboFlyStatus.OUT_OF_SERVICE);
		updater.changeStatus(roboFly6, RoboFlyStatus.OUT_OF_SERVICE);
		
		updater.setEquipmentAtRoboFly(StatusDescription.EATEN);
		updater.setEquipmentAtRoboFly(StatusDescription.LOST);
	}
	
	private Map<String, String> createInfos(int roboFlyId) {
		
		String commentKey = "comment";
		String descKey = "statusDescription";
		String endOfLifeKey = "endOfLife";
		
		Map<String, String> information = new HashMap<String, String>();
		
		switch(roboFlyId) {
			case 2:
				information.put(descKey, StatusDescription.EATEN.name());
				information.put(commentKey, "Als Beute angesehen");
				information.put(endOfLifeKey, "30.04.14");
				break;
			case 4:
				information.put(descKey, StatusDescription.DAMAGED.name());
				information.put(commentKey, "Zerstoert durch Garten-geraet");
				information.put(endOfLifeKey, "30.04.14");
				break;
			case 6:
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

