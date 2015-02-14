package mongodb.belgaia.kata10;


public class CostItem {

	private final String costItemId;
	private final String roboFlyId;
	private final CostType type;
	private final String currency;
	private final String amount;
	
	public String getCostItemId() {
		return costItemId;
	}
	public String getRoboFlyId() {
		return roboFlyId;
	}

	public CostType getType() {
		return type;
	}

	public String getCurrency() {
		return currency;
	}

	public String getAmount() {
		return amount;
	}

	private CostItem(Builder builder) {
		this.costItemId = builder.costItemId;
		this.roboFlyId = builder.roboFlyId;
		this.type = builder.type;
		this.currency = builder.currency;				
		this.amount = builder.amount;
	}
	
	public static class Builder {
		
		private String costItemId;
		private final String roboFlyId;
		private CostType type;
		private String currency;
		private String amount;
		
		public Builder(String roboFlyId) {
			this.roboFlyId = roboFlyId;
		}
		
		public Builder costItemId(String costItemId) {
			this.costItemId = costItemId;
			return this;
		}
		
		public Builder type(CostType type) {
			this.type = type;
			return this;
		}
		
		public Builder currency(String currency) {
			this.currency = currency;
			return this;
		}
		
		public Builder amount(String amount) {
			this.amount = amount;
			return this;
		}
		
		public CostItem build() {
			return new CostItem(this);
		}
	}
}
