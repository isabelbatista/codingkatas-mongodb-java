package mongodb.belgaia.kata5;

class Profile {
	
	String id;
	RoboFlyType roboflyType;
	Integer size;
	Integer serviceTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public RoboFlyType getRoboflyType() {
		return roboflyType;
	}
	public void setRoboflyType(RoboFlyType roboflyType) {
		this.roboflyType = roboflyType;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(Integer serviceTime) {
		this.serviceTime = serviceTime;
	}
}
