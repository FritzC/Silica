import java.time.LocalDate;
import java.util.List;

public class Anime {

	public String url;
	public String name;
	public String englishName;
	public String japaneseName;
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
	
	public Anime() {}
	
	public Anime(String url, String name, String englishName, String japaneseName, ShowType type, Integer episodes,
			ShowStatus status, LocalDate[] dates, PremierSeason premier, List<String> studios, ShowSourceType sourceType,
			List<String> genres, Integer duration, ShowRating rating, Double score, Integer scoredBy, Integer ranking,
			Integer favoriteCount) {
		this.url = url;
		this.name = name;
		this.englishName = englishName;
		this.japaneseName = japaneseName;
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
	}
	
}
