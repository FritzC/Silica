import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import com.jaunt.Element;
import com.jaunt.NotFound;
import com.jaunt.UserAgent;

public class MALScraper {
	
	private UserAgent userAgent;

	/**
	 * Creates a scraper for the specified MAL url
	 * 
	 * @param url - URL of a show to scrape
	 */
	public MALScraper(String url) {
		try {
			userAgent = new UserAgent();
			userAgent.visit(url);
			System.out.println(url);
			     
			System.out.println("Type: " + getType());
			System.out.println("Episodes: " + getEpisodeCount());
			System.out.println("Status: " + getStatus());
			System.out.println("Start Date: " + getDates()[0]);
			System.out.println("End Date: " + getDates()[1]);
			System.out.println("Premier: " + getPremieredSeason());
		} catch (Exception e) {}
	}
	
	/**
	 * Scrapes the type of media of the show
	 * 
	 * @return - Media type
	 */
	public ShowType getType() {
		ShowType value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Type:</span>");
			value = ShowType.forValue(e.getParent().getFirst("<a href>").getText().trim());
		} catch (NotFound e) {
			System.out.println("Error finding type");
		}
		return value;
	}
	
	/**
	 * Scrapes the number of episodes of the show
	 * 
	 * @return - Number of episodes
	 */
	private String getEpisodeCount() {
		String value = "Unknown";
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Episodes:</span>");
			value = e.getParent().getText().trim();
		} catch (NotFound e) {
			System.out.println("Error finding episode count");
		}
		return value;
	}
	
	/**
	 * Scrapes the current status of the show
	 * 
	 * @return - Status
	 */
	public ShowStatus getStatus() {
		ShowStatus value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Status:</span>");
			value = ShowStatus.forValue(e.getParent().getText().trim());
		} catch (NotFound e) {
			System.out.println("Error finding status");
		}
		return value;
	}
	
	/**
	 * Scrapes the start and end dates of the show
	 * 
	 * @return - Start and end dates
	 */
	public LocalDate[] getDates() {
		LocalDate[] value = new LocalDate[2];
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
			Element e = userAgent.doc.findFirst("<span class=dark_text>Aired:</span>");
			String[] text = e.getParent().getText().trim().split(" to ");
			for (int i = 0; i < value.length; i++) {
				if (!text[i].equals("?")) {
					value[i] = LocalDate.parse(text[i], formatter);
				}
			}
		} catch (NotFound e) {
			System.out.println("Error finding start date");
		} catch (DateTimeParseException e) {
			System.out.println("Error parsing start date");
		}
		return value;
	}
	
	/**
	 * Scrapes the season the show premiered
	 * 
	 * @return - Premiered season
	 */
	public PremieredSeason getPremieredSeason() {
		PremieredSeason value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Premiered:</span>");
			String[] text = e.getParent().findFirst("<a href>").getText().trim().split(" ");
			value = new PremieredSeason(PremieredSeason.Season.forValue(text[0]), Integer.parseInt(text[1]));
		} catch (NotFound e) {
			System.out.println("Error finding premiered season");
		}
		return value;
	}
}
