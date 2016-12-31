package scrapers.mal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import util.Logger;

public class MAL {
	
	public List<MALAnime> animeCache = new ArrayList<>();
	public List<MALManga> mangaCache = new ArrayList<>();
	
	public void updateAnimeCache() {
		try {
			Logger.log("Updating anime-000.xml...");
			File malAnimeIndex = new File("./cache/mal/anime-000.xml");
			malAnimeIndex.delete();
			malAnimeIndex.createNewFile();
			URL website = new URL("https://myanimelist.net/sitemap/anime-000.xml");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(malAnimeIndex);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			Logger.log("Finished updating anime-000.xml!");
			
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
			for (int i = 0; i < urls.size(); i++) {
				MALAnimeScraper scraper = new MALAnimeScraper(urls.get(i));
				MALAnime anime = scraper.scrape();
				if (anime == null) {
					Logger.error("\tFAILED [" + (i + 1) + "/" + urls.size() + "] ~ '" + urls.get(i) + "'");
				} else {
					animeCache.add(anime);
					Logger.log("\t[" + (i + 1) + "/" + urls.size() + "] ~ '" + anime.name + "' successfully scraped");
				}
			}
			Logger.log("Finished scraping!");
			
			Logger.log("Storing cache...");
			File localCache = new File("./cache/mal/anime.json");
			localCache.delete();
			Writer writer = new FileWriter(localCache);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			writer.write(gson.toJson(animeCache));
			writer.close();
			Logger.log("Finished storing cache!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateMangaCache() {
		try {
			Logger.log("Updating manga-000.xml...");
			File malAnimeIndex = new File("./cache/mal/manga-000.xml");
			malAnimeIndex.delete();
			malAnimeIndex.createNewFile();
			URL website = new URL("https://myanimelist.net/sitemap/manga-000.xml");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(malAnimeIndex);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			Logger.log("Finished updating manga-000.xml!");
			
			Logger.log("Parsing manga-000.xml...");
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
			for (int i = 0; i < 2; i++) {
				MALMangaScraper scraper = new MALMangaScraper(urls.get(i));
				MALManga manga = scraper.scrape();
				if (manga == null) {
					Logger.error("\tFAILED [" + (i + 1) + "/" + urls.size() + "] ~ '" + urls.get(i) + "'");
				} else {
					mangaCache.add(manga);
					Logger.log("\t[" + (i + 1) + "/" + urls.size() + "] ~ '" + manga.name + "' successfully scraped");
				}
			}
			Logger.log("Finished scraping!");
			
			Logger.log("Storing cache...");
			File localCache = new File("./cache/mal/manga.json");
			localCache.delete();
			Writer writer = new FileWriter(localCache);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			writer.write(gson.toJson(mangaCache));
			writer.close();
			Logger.log("Finished storing cache!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
