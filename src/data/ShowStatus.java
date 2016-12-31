package data;
import org.apache.commons.lang3.text.WordUtils;

import util.Utils;

public enum ShowStatus {

	NOT_YET_AIRING,
	CURRENTLY_AIRING,
	FINISHED_AIRING;
	
	@Override
	public String toString() {
		return Utils.enumToStandardText(name());
	}

	public static ShowStatus forValue(String value) {
		for (ShowStatus status : values()) {
			if (status.toString().equalsIgnoreCase(value)) {
				return status;
			}
		}
		return null;
	}
}
