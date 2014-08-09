package mongodb.belgaia.kata4;


/**
 * Type RoboFly as part of the solution for Kata 1 of the book
 * "Coding Katas for MongoDB".
 * 
 * This type describes a robo fly.
 * 
 * The constructor contains all necessary information about the roboFly,
 * instead of the _id that can be set separately with the setter method
 * or will be set automatically by mongodb. 
 * 
 * @author isabel.batista
 *
 */
class RoboFly {
	
	String id;
	String name;
	int constructionYear;
	int size;				// in millimeters
	int serviceTime;		// maximum in minutes
	RoboFlyStatus status;
	RoboFlyType type;
	
	public RoboFly(String name, int constructionYear, int size, int serviceTime, RoboFlyStatus status) {
		
		this.name = name;
		this.constructionYear = constructionYear;
		this.size = size;
		this.serviceTime = serviceTime;
		this.status = status;
		
	}

	public int getConstructionYear() {
		return constructionYear;
	}

	public int getSize() {
		return size;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public RoboFlyStatus getStatus() {
		return status;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

}