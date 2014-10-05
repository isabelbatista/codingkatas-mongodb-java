package mongodb.belgaia.kata5;

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
enum RoboFlyStatus {
	OK("OK"), TO_BE_CALIBRATED("To be calibrated"), OUT_OF_SERVICE("Out of service");
	
	String name;
	
	RoboFlyStatus(String name) {
		this.name = name;
	}
}
