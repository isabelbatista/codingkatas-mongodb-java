package mongodb.belgaia.kata7;

/**
 * Enumeration for the RoboFly status as part of the solution for Kata 7 of the book
 * "Coding Katas for MongoDB".
 * 
 * Enumeration to define status values for the RoboFly object.
 * 
 * @author isabel.batista
 *
 */
enum RoboFlyStatus {
	OK("OK"), TO_BE_CALIBRATED("To be calibrated");
	
	String name;
	
	RoboFlyStatus(String name) {
		this.name = name;
	}
}
