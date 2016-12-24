import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	public List<Anime> animeCache = new ArrayList<>();

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
			System.out.println("Updating anime-000.xml...");
			File malAnimeIndex = new File("./cache/anime-000.xml");
/*			malAnimeIndex.delete();
			malAnimeIndex.createNewFile();
			URL website = new URL("https://myanimelist.net/sitemap/anime-000.xml");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(malAnimeIndex);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			System.out.println("Finished updating anime-000.xml!");
			*/
			System.out.println("Parsing anime-000.xml...");
			List<String> urls = new ArrayList<>();
			try(BufferedReader br = new BufferedReader(new FileReader(malAnimeIndex))) {
			    for(String line; (line = br.readLine()) != null;) {
			        if (line.contains("<loc>")) {
			        	urls.add(line.split("<loc>")[1].split("</loc>")[0]);
			        }
			    }
			}
			System.out.println("Finished parsing " + urls.size() + " urls!");
			MALScraper test = new MALScraper(urls.get(0));
			
		}
	}
}
