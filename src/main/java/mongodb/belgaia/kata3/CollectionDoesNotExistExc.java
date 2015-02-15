package mongodb.belgaia.kata3;

public class CollectionDoesNotExistExc extends Exception {

	private static final long serialVersionUID = -2153841197096770423L;

	public CollectionDoesNotExistExc() {
	}
	
	public CollectionDoesNotExistExc(String message) {
		super(message);
	}
}
