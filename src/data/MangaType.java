package data;

import util.Utils;

public enum MangaType {

	MANGA,
	LICENSED_MANGA,
	ONESHOT,
	MANHWA,
	DOUJINSHI,
	NOVEL;
	
	@Override
	public String toString() {
		return Utils.enumToStandardText(name());
	}
	
	public static MangaType forValue(String value) {
		for (MangaType type : values()) {
			if (type.toString().equalsIgnoreCase(value)) {
				return type;
			}
		}
		return null;
	}
}
