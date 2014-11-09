package mongodb.belgaia.kata3;

import com.mongodb.DBObject;

public class Robofly2ProfileReference {
	
	// The collection name of the referenced document
	private String referenceCollection;
	
	// The collection name of the main document where the reference must be set
	private String documentCollection;
	
	// The id of the referenced document
	private String referenceId;
	
	// The name of the field/key that contains the reference
	private String referenceField;
	
	// The document where the reference must be set
	private DBObject document;
	
	public Robofly2ProfileReference(String referenceCollection, String documentCollection, String referenceField, String referenceId, DBObject document) {
		
		this.referenceCollection = referenceCollection;
		this.documentCollection = documentCollection;
		this.referenceField = referenceField;
		this.referenceId = referenceId;
		this.document = document;
	}
	
	public String getReferenceCollection() {
		return referenceCollection;
	}

	public void setReferenceCollection(String referenceCollection) {
		this.referenceCollection = referenceCollection;
	}

	public String getDocumentCollection() {
		return documentCollection;
	}

	public void setDocumentCollection(String documentCollection) {
		this.documentCollection = documentCollection;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	
	public String getReferenceField() {
		return referenceField;
	}

	public void setReferenceField(String referenceField) {
		this.referenceField = referenceField;
	}

	public DBObject getDocument() {
		return document;
	}

	public void setDocument(DBObject document) {
		this.document = document;
	}
}
