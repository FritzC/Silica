
public enum ShowType {

	TV,
	MOVIE,
	OVA,
	ONA,
	SPECIAL,
	MUSIC;
	
	public static ShowType forValue(String value) {
		for (ShowType type : values()) {
			if (type.toString().equalsIgnoreCase(value)) {
				return type;
			}
		}
		return null;
	}
}
