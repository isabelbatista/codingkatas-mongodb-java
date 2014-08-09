package mongodb.belgaia.kata3;


class RoboFly {
		
	private final String id;
	private final String name;
	
	private final int constructionYear;
	private final int size;				// in millimeters
	private final int serviceTime;		// maximum in minutes
	private final Status status;
	private final Type type;
	
	private RoboFly(Builder builder) {
		
		this.id = builder.id;
		this.name = builder.name;
		this.constructionYear = builder.constructionYear;
		this.size = builder.size;				
		this.serviceTime = builder.serviceTime;
		this.status = builder.status;
		this.type = builder.type;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public int getConstructionYear() {
		return constructionYear;
	}

	public int getSize() {
		return size;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public Status getStatus() {
		return status;
	}

	public Type getType() {
		return type;
	}

	public static class Builder {
		
		private final String id;
		private final String name;
		
		private int constructionYear;
		private int size;
		private int serviceTime;
		private Status status;
		private Type type;		
		
		public Builder(String id, String name) {
			
			this.id = id;
			this.name = name;
		}
		
		public Builder constructionYear(int constructionYear) {
			this.constructionYear = constructionYear;
			return this;
		}
		
		public Builder size(int size) {
			this.size = size;
			return this;
		}
		
		public Builder serviceTime(int serviceTime) {
			this.serviceTime = serviceTime;
			return this;
		}
		
		public Builder status (Status status) {
			this.status = status;
			return this;
		}
		
		public Builder type(Type type) {
			this.type = type;
			return this;
		}
		
		public RoboFly build() {
			return new RoboFly(this);
		}
	}
	
	enum Status {
		OK("ok");
		
		String name;
		
		Status(String name) {
			this.name = name;
		}
	}
	
	enum Type {

		FLY, MOSKITO, DRAGONFLY, RUDERFUSSKREBS
	}
}
