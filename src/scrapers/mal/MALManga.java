package scrapers.mal;

import java.time.LocalDate;
import java.util.List;

import data.MangaStatus;
import data.MangaType;

public class MALManga {

	public String url;
	public String name;
	public String englishName;
	public String japaneseName;
	public String imageUrl;
	public String synopsis;
	public List<String> adaptations;
	public List<String> sideStories;
	public List<String> prequels;
	public List<String> sequels;
	public List<String> alternativeVersions;
	public List<String> parentStories;
	public MangaType type;
	public Integer volumes;
	public Integer chapters;
	public MangaStatus status;
	public LocalDate[] dates;
	public List<String> authors;
	public List<String> genres;
	public String serialization;
	public Double score;
	public Integer scoredBy;
	public Integer ranking;
	public Integer favoriteCount;
	public Integer popularity;

	public MALManga() { }

	public MALManga(String url, String name, String englishName, String japaneseName, String imageUrl, String synopsis,
			List<String> adaptations, List<String> sideStories, List<String> prequels, List<String> sequels,
			List<String> alternativeVersions, List<String> parentStories, MangaType type, Integer volumes,
			Integer chapters, MangaStatus status, LocalDate[] dates, List<String> authors, List<String> genres, 
			String serialization, Double score, Integer scoredBy, Integer ranking, Integer favoriteCount, 
			Integer popularity) {
		this.url = url;
		this.name = name;
		this.englishName = englishName;
		this.japaneseName = japaneseName;
		this.imageUrl = imageUrl;
		this.synopsis = synopsis;
		this.adaptations = adaptations;
		this.sideStories = sideStories;
		this.prequels = prequels;
		this.sequels = sequels;
		this.alternativeVersions = alternativeVersions;
		this.parentStories = parentStories;
		this.type = type;
		this.volumes = volumes;
		this.chapters = chapters;
		this.status = status;
		this.dates = dates;
		this.authors = authors;
		this.genres = genres;
		this.serialization = serialization;
		this.score = score;
		this.scoredBy = scoredBy;
		this.ranking = ranking;
		this.favoriteCount = favoriteCount;
		this.popularity = popularity;
	}
	
}
