package mongodb.belgaia.kata7;

enum RoboFlyType {

	FLY("FLY"), MOSKITO("MOSKITO"), DRAGONFLY("DRAGONFLY"), COPEPOD("COPEPOD");

	String name;

	RoboFlyType(String name) {
		this.name = name;
	}
}
