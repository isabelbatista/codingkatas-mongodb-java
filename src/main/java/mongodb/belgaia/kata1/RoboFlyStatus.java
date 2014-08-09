package mongodb.belgaia.kata1;

/**
 * Enumeration for the RoboFly status as part of the solution for Kata 1 of the book
 * "Coding Katas for MongoDB".
 * 
 * Enumeration to define status values for the RoboFly object. In this case just one
 * possible status.
 * 
 * @author isabel.batista
 *
 */
public enum RoboFlyStatus {
	OK("ok");
	
	String name;
	
	RoboFlyStatus(String name) {
		this.name = name;
	}
}
