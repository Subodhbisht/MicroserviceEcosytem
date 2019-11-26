package io.javabrains.moviecatalogservice.resources;

public class EventResponse {

	private String id;
	private String evnetId;
	private String sku;
	private String quantityConsumed;
	private String type;
	private String dateTimestamp;
	private String value;

	public EventResponse() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvnetId() {
		return evnetId;
	}

	public void setEvnetId(String evnetId) {
		this.evnetId = evnetId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getQuantityConsumed() {
		return quantityConsumed;
	}

	public void setQuantityConsumed(String quantityConsumed) {
		this.quantityConsumed = quantityConsumed;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDateTimestamp() {
		return dateTimestamp;
	}

	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
