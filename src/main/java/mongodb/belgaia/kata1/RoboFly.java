package mongodb.belgaia.kata1;

public class RoboFly {
	
	String id;
	String name;
	int constructionYear;
	int size;				// in millimeters
	int serviceTime;		// maximum in minutes
	Status status;
	
	public RoboFly(String name, int constructionYear, int size, int serviceTime, Status status) {
		
		this.name = name;
		this.constructionYear = constructionYear;
		this.size = size;
		this.serviceTime = serviceTime;
		this.status = status;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}