package la1;

import java.io.*;
import java.util.*;

public class MusicStore {
	private final Map<String, Album> albums; // Use final to insure Map can't be reassigned

	public MusicStore() {
		albums = new HashMap<>();
		loadMusic(); // Load data
	}

	// Read albums.txt and load all albums
	private void loadMusic() {
		try (BufferedReader reader = new BufferedReader(new FileReader("albums.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				String albumTitle = parts[0].trim();
				String artist = parts[1].trim();
				loadAlbum(albumTitle, artist);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Read a single album file
	private void loadAlbum(String title, String artist) {
		String filename = title + "_" + artist + ".txt";
		List<String> songs = new ArrayList<>();
		String genre = "";
		int year = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String firstLine = reader.readLine();
			if (firstLine != null) {
				String[] info = firstLine.split(",");
				if (info.length >= 4) {
					genre = info[2].trim();
					year = Integer.parseInt(info[3].trim());
				}
			}
			String songTitle;
			while ((songTitle = reader.readLine()) != null) {
				songs.add(songTitle.trim());
			}
			albums.put(title, new Album(title, artist, genre, year, songs));
		} catch (IOException e) {
			System.out.println("File not found: " + filename);
		}
	}


}
