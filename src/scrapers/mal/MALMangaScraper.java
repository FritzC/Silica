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

import data.MangaStatus;
import data.MangaType;
import data.PremierSeason;
import util.Logger;

public class MALMangaScraper {
	
	private UserAgent userAgent;

	/**
	 * Creates a scraper for the specified MAL anime url
	 * 
	 * @param url - URL to scrape
	 */
	public MALMangaScraper(String url) {
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
	public MALManga scrape() {
		try {
			return new MALManga(userAgent.getLocation(), getName(), getEnglishName(), getJapaneseName(), getImageUrl(),
					getSynopsis(), getAdaptations(), getSideStories(), getPrequels(), getSequels(),
					getAlternativeVersions(), getParentStories(), getType(), getVolumes(), getChapters(), getStatus(),
					getDates(), getAuthors(), getGenres(), getSerialization(), getScore(), getScoreCount(), getRanking(),
					getFavoritedCount(), getPopularity());
		} catch (Exception e) {
			Logger.error("Failed to scrape data for: " + userAgent.getLocation());
			return null;
		}
	}
	
	/**
	 * Scrapes the manga's name
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
	 * Scrapes the manga's alternative english name
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
	 * Scrapes the manga's alternative japanese name
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
	 * Scrapes the manga's cover image url
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
	 * Scrapes the manga's type of media
	 * 
	 * @return - Media type
	 */
	public MangaType getType() {
		MangaType value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Type:</span>");
			value = MangaType.forValue(e.getParent().getFirst("<a href>").getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding type");
		}
		return value;
	}
	
	/**
	 * Scrapes manga's volume count
	 * 
	 * @return - Number of volumes
	 */
	private Integer getVolumes() {
		Integer value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Volumes:</span>");
			value = Integer.parseInt(e.getParent().getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding episode count");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes manga's chapter count
	 * 
	 * @return - Number of chapter
	 */
	private Integer getChapters() {
		Integer value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Chapters:</span>");
			value = Integer.parseInt(e.getParent().getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding episode count");
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}
	
	/**
	 * Scrapes the manga's current status
	 * 
	 * @return - Status
	 */
	public MangaStatus getStatus() {
		MangaStatus value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Status:</span>");
			value = MangaStatus.forValue(e.getParent().getText().trim());
		} catch (NotFound e) {
			Logger.warning("Error finding status");
		}
		return value;
	}
	
	/**
	 * Scrapes manga's start and end dates
	 * 
	 * @return - Start and end dates
	 */
	public LocalDate[] getDates() {
		LocalDate[] value = new LocalDate[2];
		String check = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
			Element e = userAgent.doc.findFirst("<span class=dark_text>Published:</span>");
			String[] text = e.getParent().getText().trim().split(" to ");
			for (int i = 0; i < text.length; i++) {
				if (!text[i].equals("?")) {
					value[i] = LocalDate.parse(text[i].replaceAll("  ", " "), formatter);
				}
			}
		} catch (NotFound e) {
			Logger.warning("Error finding start date");
		} catch (DateTimeParseException e) {
			Logger.warning("Error parsing start date (" + check +")");
		}
		return value;
	}
	
	/**
	 * Scrapes the season the manga premiered
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
	 * Scrapes the list of studios who created the manga
	 * 
	 * @return - Studios who created the manga
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
	 * Scrapes a list of the manga's authors
	 * 
	 * @return - Authors
	 */
	public List<String> getAuthors() {
		List<String> value = new ArrayList<>();
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Authors:</span>");
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
	 * Scrapes a list of the manga's genres
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
	 * Scrapes the manga's serialization
	 * 
	 * @return - Serialization
	 */
	private String getSerialization() {
		String value = null;
		try {
			Element e = userAgent.doc.findFirst("<span class=dark_text>Serialization:</span>");
			value = e.getParent().findFirst("<a href>").getText().trim();
		} catch (NotFound e) {
			Logger.warning("Error finding duration");
		}
		return value;
	}
	
	/**
	 * Scrapes the manga's score
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
	 * Scrapes number of people who have scored the manga
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
	 * Scrapes the manga's ranking
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
	 * Scrapes the manga's popularity
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
	 * Scrapes the manga's number of favorites
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
	 * Scrapes the manga's list of adaptations
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
	 * Scrapes the manga's list of side stories
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
	 * Scrapes the manga's list of sequels
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
	 * Scrapes the manga's list of prequels
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
	 * Scrapes the manga's list of alternative versions
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
	 * Scrapes the manga's list of parent stories
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
	 * Scrapes the manga's synopsis
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
