package mongodb.belgaia.kata5;

/**
 * Enumeration for the RoboFly status description as part of the solution for
 * Kata 5 of the book "Coding Katas for MongoDB".
 * 
 * Enumeration to define additional status description for the RoboFly object.
 * It is the reason for the RoboFlyStatus.
 * 
 * @author isabel.batista
 *
 */
public enum StatusDescription {
	DAMAGED("damaged"), EATEN("eaten"), LOST("lost");
	
	String description;
	
	StatusDescription(String description) {
		this.description = description;
	}
}
