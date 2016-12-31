package data;
import util.Utils;

public enum ShowSourceType {

	ORIGINAL,
	MANGA,
	LIGHT_NOVEL;

	@Override
	public String toString() {
		return Utils.enumToStandardText(name());
	}
	
	public static ShowSourceType forValue(String value) {
		for (ShowSourceType source : values()) {
			if (source.toString().equalsIgnoreCase(value)) {
				return source;
			}
		}
		return null;
	}
}
