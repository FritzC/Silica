public class PremierSeason {
	
	private Season season;
	private int year;
	
	public PremierSeason(Season season, int year) {
		this.season = season;
		this.year = year;
	}
	
	public Season getSeason() {
		return season;
	}
	
	public int getYear() {
		return year;
	}
	
	public String toString() {
		return Utils.enumToStandardText(season.toString()) + " " + year;
	}

	public enum Season {

		SPRING, 
		SUMMER, 
		FALL, 
		WINTER;

		public static Season forValue(String value) {
			for (Season season : values()) {
				if (season.toString().equalsIgnoreCase(value)) {
					return season;
				}
			}
			return null;
		}
	}
}
