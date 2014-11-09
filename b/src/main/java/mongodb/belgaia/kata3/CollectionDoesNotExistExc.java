package mongodb.belgaia.kata3;

public class CollectionDoesNotExistExc extends Exception {
	
	public CollectionDoesNotExistExc() {
	}
	
	public CollectionDoesNotExistExc(String message) {
		super(message);
	}
}
