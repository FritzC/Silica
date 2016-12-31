package util;
import org.apache.commons.lang3.text.WordUtils;

public class Utils {

	public static String enumToStandardText(String enumName) {
		return WordUtils.capitalizeFully(enumName.replace("_", " "));
	}
}
