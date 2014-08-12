package mongodb.belgaia.kata3;

public class Profile {
	
	String id;
	RoboFly.Type roboflyType;
	Integer size;
	Integer serviceTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public RoboFly.Type getRoboflyType() {
		return roboflyType;
	}
	public void setRoboflyType(RoboFly.Type roboflyType) {
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
