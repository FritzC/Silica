package scrapers.mal;
import java.time.LocalDate;
import java.util.List;

import data.PremierSeason;
import data.ShowRating;
import data.ShowSourceType;
import data.ShowStatus;
import data.ShowType;

public class MALAnime {

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
	public ShowType type;
	public Integer episodes;
	public ShowStatus status;
	public LocalDate[] dates;
	public PremierSeason premier;
	public List<String> studios;
	public ShowSourceType sourceType;
	public List<String> genres;
	public Integer duration;
	public ShowRating rating;
	public Double score;
	public Integer scoredBy;
	public Integer ranking;
	public Integer favoriteCount;
	public Integer popularity;
	
	public MALAnime() {}

	public MALAnime(String url, String name, String englishName, String japaneseName, String imageUrl, String synopsis,
			List<String> adaptations, List<String> sideStories, List<String> prequels, List<String> sequels,
			List<String> alternativeVersions, List<String> parentStories, ShowType type, Integer episodes,
			ShowStatus status, LocalDate[] dates, PremierSeason premier, List<String> studios,
			ShowSourceType sourceType, List<String> genres, Integer duration, ShowRating rating, Double score,
			Integer scoredBy, Integer ranking, Integer favoriteCount, Integer popularity) {
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
		this.episodes = episodes;
		this.status = status;
		this.dates = dates;
		this.premier = premier;
		this.studios = studios;
		this.sourceType = sourceType;
		this.genres = genres;
		this.duration = duration;
		this.rating = rating;
		this.score = score;
		this.scoredBy = scoredBy;
		this.ranking = ranking;
		this.favoriteCount = favoriteCount;
		this.popularity = popularity;
	}
	
}
