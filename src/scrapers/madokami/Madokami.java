package scrapers.madokami;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import util.Logger;

public class Madokami {

	public void updateMUIDs() {
		try {
			Logger.log("Updating muids.dat...");
			File idList = new File("./cache/madokami/muids.dat");
			idList.delete();
			idList.createNewFile();
			URL website = new URL("https://manga.madokami.al/stupidapi/muids");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(idList);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			Logger.log("Finished updating muids.dat!");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMUIDs() {
		
	}
}
