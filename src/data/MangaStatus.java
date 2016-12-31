package data;
import org.apache.commons.lang3.text.WordUtils;

import util.Utils;

public enum MangaStatus {

	PUBLISHING,
	COMPLETED;
	
	@Override
	public String toString() {
		return Utils.enumToStandardText(name());
	}

	public static MangaStatus forValue(String value) {
		for (MangaStatus status : values()) {
			if (status.toString().equalsIgnoreCase(value)) {
				return status;
			}
		}
		return null;
	}
}
