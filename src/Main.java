import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
	
	public static List<Anime> animeCache = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(System.in);
		System.out.print("Update Cache? (Y/N): ");
		boolean update = false;
		while (true) {
			String in = input.nextLine();
			if (in.equalsIgnoreCase("Y") || in.equalsIgnoreCase("N")) {
				update = in.equalsIgnoreCase("Y");
				break;
			}
		}
		if (update) {
			Logger.log("Updating anime-000.xml...");
			File malAnimeIndex = new File("./cache/anime-000.xml");
/*			malAnimeIndex.delete();
			malAnimeIndex.createNewFile();
			URL website = new URL("https://myanimelist.net/sitemap/anime-000.xml");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(malAnimeIndex);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			Logger.log("Finished updating anime-000.xml!");
			*/
			Logger.log("Parsing anime-000.xml...");
			List<String> urls = new ArrayList<>();
			try(BufferedReader br = new BufferedReader(new FileReader(malAnimeIndex))) {
			    for(String line; (line = br.readLine()) != null;) {
			        if (line.contains("<loc>")) {
			        	urls.add(line.split("<loc>")[1].split("</loc>")[0]);
			        }
			    }
			}
			Logger.log("Finished parsing " + urls.size() + " urls!");
			
			Logger.log("Scraping data...");
			for (int i = 0; i < 3; i++) {
				MALScraper scraper = new MALScraper(urls.get(i));
				Anime anime = scraper.scrape();
				if (anime == null) {
					Logger.error("\tFAILED [" + (i + 1) + "/" + urls.size() + "] ~ '" + urls.get(i) + "'");
				} else {
					animeCache.add(anime);
					Logger.log("\t[" + (i + 1) + "/" + urls.size() + "] ~ '" + anime.name + "' successfully scraped");
				}
			}
			Logger.log("Finished scraping!");
			
			Logger.log("Storing cache...");
			File localCache = new File("./cache/anime.json");
			localCache.delete();
			Writer writer = new FileWriter(localCache);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			writer.write(gson.toJson(animeCache));
			writer.close();
			Logger.log("Finished storing cache!");
		}
	}
}
