package scrapers.mal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.NotFound;
import com.jaunt.UserAgent;

import data.PremierSeason;
import data.ShowRating;
import data.ShowSourceType;
import data.ShowStatus;
import data.ShowType;
import util.Logger;

public class MALAnimeScraper {
	
	private UserAgent userAgent;

	/**
	 * Creates a scraper for the specified MAL anime url
	 * 
	 * @param url - URL to scrape
	 */
	public MALAnimeScraper(String url) {
		try {
			userAgent = new UserAgent();
			userAgent.visit(url);
		} catch (Exception e) {
			Logger.error("Error fetching page (" + url + ")");
		}
	}
	
	/**
	 * Gets and populates an Anime object for the page
	 * 
	 * @return - Anime object
	 */
	public MALAnime scrape() {
		try {
			return new MALAnime(userAgent.getLocation(), getName(), getEnglishName(), getJapaneseName(), getImageUrl(),
					getSynopsis(), getAdaptations(), getSideStories(), getPrequels(), getSequels(),
					getAlternativeVersions(), getParentStories(), getType(), getEpisodeCount(), getStatus(), getDates(),
					getPremieredSeason(), getStudios(), getSourceType(), getGenres(), getDuration(), getRating(),
					getScore(), getScoreCount(), getRanking(), getFavoritedCount(), getPopularity());
		} catch (Exception e) {
			Logger.error("Failed to scrape data for: " + userAgent.getLocation());
			return null;
		}
	}
	
	/**
	 * Scrapes the show's name
	 * 
	 * @return - Name
	 */
	public String getName() {
		String value = null;
		try {
			Element e = userAgent.doc.findFirst("<span itemprop=name>");
			value = e.getText().trim();
		} catch (NotFound e) {
			Logger.warning("Error finding name");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's alternative english name
	 * 
	 * @return - English alternative name
	 */
	public String getEnglishName() {
		String value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>English:</span>");
			value = e.getParent().getText().trim();
		} catch (NotFound e) {
			Logger.warning("Error finding english name");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's alternative japanese name
	 * 
	 * @return - Japanese alternative name
	 */
	public String getJapaneseName() {
		String value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Japanese:</span>");
			value = e.getParent().getText().trim();
		} catch (NotFound e) {
			Logger.warning("Error finding japanese name");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's cover image url
	 * 
	 * @return - Image url
	 */
	public String getImageUrl() {
		String value = null;
		try {
			Element e = userAgent.doc.findFirst("<meta property=og:image");
			value = e.getAt("content");
		} catch (NotFound e) {
			Logger.warning("Error finding image url");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's type of media
	 * 
	 * @return - Media type
	 */
	public ShowType getType() {
		ShowType value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Type:</span>");
			value = ShowType.forValue(e.getParent().getFirst("<a href>").getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding type");
		}
		return value;
	}
	
	/**
	 * Scrapes show's episode count
	 * 
	 * @return - Number of episodes
	 */
	private Integer getEpisodeCount() {
		Integer value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Episodes:</span>");
			value = Integer.parseInt(e.getParent().getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding episode count");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes the show's current status
	 * 
	 * @return - Status
	 */
	public ShowStatus getStatus() {
		ShowStatus value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Status:</span>");
			value = ShowStatus.forValue(e.getParent().getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding status");
		}
		return value;
	}
	
	/**
	 * Scrapes show's start and end dates
	 * 
	 * @return - Start and end dates
	 */
	public LocalDate[] getDates() {
		LocalDate[] value = new LocalDate[2];
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
			Element e = userAgent.doc.findFirst("<span class=dark_text>Aired:</span>");
			String[] text = e.getParent().getText().trim().split(" to ");
			for (int i = 0; i < text.length; i++) {
				if (!text[i].equals("?")) {
					value[i] = LocalDate.parse(text[i], formatter);
				}
			}
		} catch (NotFound e) {
			Logger.warning("Error finding start date");
		} catch (DateTimeParseException e) {
			Logger.warning("Error parsing start date");
		}
		return value;
	}
	
	/**
	 * Scrapes the season the show premiered
	 * 
	 * @return - Premiered season
	 */
	public PremierSeason getPremieredSeason() {
		PremierSeason value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Premiered:</span>");
			String[] text = e.getParent().findFirst("<a href>").getText().trim().split(" ");
			value = new PremierSeason(PremierSeason.Season.forValue(text[0]), Integer.parseInt(text[1]));
		} catch (NotFound e) {
			Logger.warning("Error finding premiered season");
		}
		return value;
	}
	
	/**
	 * Scrapes the list of studios who created the show
	 * 
	 * @return - Studios who created the show
	 */
	public List<String> getStudios() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Studios:</span>");
			Elements studios = e.getParent().findEach("<a href>");
			for (Element studio : studios) {
				value.add(studio.getText());
			}
		} catch (NotFound e) {
			Logger.warning("Error finding studios");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's source material type
	 * 
	 * @return - Source material type
	 */
	public ShowSourceType getSourceType() {
		ShowSourceType value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Source:</span>");
			value = ShowSourceType.forValue(e.getParent().getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding source type");
		}
		return value;
	}
	
	/**
	 * Scrapes a list of the show's genres
	 * 
	 * @return - Genres
	 */
	public List<String> getGenres() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Genres:</span>");
			Elements genres = e.getParent().findEach("<a href>");
			for (Element genre : genres) {
				value.add(genre.getText());
			}
		} catch (NotFound e) {
			Logger.warning("Error finding genres");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's duration
	 * 
	 * @return - Duration
	 */
	private Integer getDuration() {
		Integer value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Duration:</span>");
			String text = e.getParent().getText().trim();
			value = 0;
			if (text.contains("hr.")) {
				value = Integer.parseInt(text.split(" hr.")[0]) * 60;
			}
			if (text.contains("min.")) {
				String[] subText = text.split(" min.")[0].split(" ");
				value += Integer.parseInt(subText[subText.length - 1]);
			}
		} catch (NotFound e) {
			Logger.warning("Error finding duration");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes the show's rating
	 * 
	 * @return - Rating
	 */
	public ShowRating getRating() {
		ShowRating value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Rating:</span>");
			value = ShowRating.forValue(e.getParent().getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding rating");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's score
	 * 
	 * @return - Score
	 */
	private Double getScore() {
		Double value = null;
		try {
			Element e = userAgent.doc.findFirst("<span itemprop=ratingValue>");
			value = Double.parseDouble(e.getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding score");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes number of people who have scored the show
	 * 
	 * @return - Number of scorers
	 */
	private Integer getScoreCount() {
		Integer value = null;
		try {
			Element e = userAgent.doc.findFirst("<span itemprop=ratingCount>");
			value = Integer.parseInt(e.getText().replaceAll(",", "").trim());
		} catch (NotFound e) {
			Logger.warning("Error finding score count");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes the show's ranking
	 * 
	 * @return - Ranking
	 */
	private Integer getRanking() {
		Integer value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Ranked:</span>");
			value = Integer.parseInt(e.getParent().getText().replace("#", "").trim());
		} catch (NotFound e) {
			Logger.warning("Error finding ranking");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes the show's popularity
	 * 
	 * @return - Popularity
	 */
	private Integer getPopularity() {
		Integer value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Popularity:</span>");
			value = Integer.parseInt(e.getParent().getText().replace("#", "").trim());
		} catch (NotFound e) {
			Logger.warning("Error finding popularity");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes the show's number of favorites
	 * 
	 * @return - Number of times favorited
	 */
	private Integer getFavoritedCount() {
		Integer value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Favorites:</span>");
			value = Integer.parseInt(e.getParent().getText().replaceAll(",", "").trim());
		} catch (NotFound e) {
			Logger.warning("Error finding number of favorites");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes the show's list of adaptations
	 * 
	 * @return - List of adaptations
	 */
	public List<String> getAdaptations() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<table class=anime_detail_related_anime");
			List<Element> values = e.findFirst("<td>Adaptation").nextSiblingElement().getChildElements();
			for (Element story : values) {
				value.add(story.getText());
			}
		} catch (NotFound e) {
			Logger.warning("Error finding adaptations");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's list of side stories
	 * 
	 * @return - List of side stories
	 */
	public List<String> getSideStories() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<table class=anime_detail_related_anime");
			List<Element> values = e.findFirst("<td>Side story").nextSiblingElement().getChildElements();
			for (Element story : values) {
				value.add(story.getText());
			}
		} catch (NotFound e) {
			Logger.warning("Error finding side stories");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's list of sequels
	 * 
	 * @return - List of sequels
	 */
	public List<String> getSequels() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<table class=anime_detail_related_anime");
			List<Element> values = e.findFirst("<td>Sequel").nextSiblingElement().getChildElements();
			for (Element story : values) {
				value.add(story.getText());
			}
		} catch (NotFound e) {
			Logger.warning("Error finding sequels");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's list of prequels
	 * 
	 * @return - List of prequels
	 */
	public List<String> getPrequels() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<table class=anime_detail_related_anime");
			List<Element> values = e.findFirst("<td>Prequel").nextSiblingElement().getChildElements();
			for (Element story : values) {
				value.add(story.getText());
			}
		} catch (NotFound e) {
			Logger.warning("Error finding prequels");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's list of alternative versions
	 * 
	 * @return - List of alternative versions
	 */
	public List<String> getAlternativeVersions() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<table class=anime_detail_related_anime");
			List<Element> values = e.findFirst("<td>Alternative").nextSiblingElement().getChildElements();
			for (Element story : values) {
				value.add(story.getText());
			}
		} catch (NotFound e) {
			Logger.warning("Error finding alternative versions");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's list of parent stories
	 * 
	 * @return - List of parent stories
	 */
	public List<String> getParentStories() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<table class=anime_detail_related_anime");
			List<Element> values = e.findFirst("<td>Parent Story").nextSiblingElement().getChildElements();
			for (Element story : values) {
				value.add(story.getText());
			}
		} catch (NotFound e) {
			Logger.warning("Error finding parent stories");
		}
		return value;
	}
	
	/**
	 * Scrapes the show's synopsis
	 * 
	 * @return - Synopsis
	 */
	public String getSynopsis() {
		String value = null;
		try {
			Element e = userAgent.doc.findFirst("<span itemprop=description>");
			value = e.getText();
		} catch (NotFound e) {
			Logger.warning("Error finding synopsis");
		}
		return value;
	}
}
