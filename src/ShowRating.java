
public enum ShowRating {

	G("G - All Ages"),
	PG("PG - Children"),
	PG_13("PG-13 - Teens 13 and older"),
	R_17("R - 17+ (violence & profanity)"),
	R_PLUS("R+ - Mild Nudity"),
	RX("Rx - Hentai");
	
	private String name;
	
	private ShowRating(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static ShowRating forValue(String value) {
		value = value.replace("&amp;", "&");
		for (ShowRating rating : values()) {
			if (rating.toString().equalsIgnoreCase(value)) {
				return rating;
			}
		}
		return null;
	}
	
}
