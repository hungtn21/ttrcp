package models.places;

public class Port {
	private String code; // Mã cảng
	private String locationCode; // Mã địa điểm của cảng, được sử dụng để liên kết với các điểm trong route
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public Port(String code, String locationCode) {
		super();
		this.code = code;
		this.locationCode = locationCode;
	}
	public Port() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
