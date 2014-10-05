package mongodb.belgaia.kata5;

public enum Equipment {
	
	ENERGY_SHIELD("energy shield"), GPS("GPS");
	
	String equipment;
	
	private Equipment(String equipment) {
		this.equipment = equipment;
	}
}
