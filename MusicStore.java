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

	// **Search song by title**
	public List<song> searchSongByTitle(String title) {
		List<song> matchingSongs = new ArrayList<>();
		for (Album album : albums.values()) {
			for (String songTitle : album.getSongs()) {
				if (songTitle.equalsIgnoreCase(title)) {
					matchingSongs.add(new song(songTitle, album.getArtist(), album.getTitle()));
				}
			}
		}

		return matchingSongs;
	}

	// **Search song by artist**
	public List<song> findSongsByArtist(String artist) {
		List<song> matchingSongs = new ArrayList<>();
		for (Album album : albums.values()) {
			if (album.getArtist().equalsIgnoreCase(artist)) {
				for (String songTitle : album.getSongs()) {
					matchingSongs.add(new song(songTitle, artist, album.getTitle()));
				}
			}
		}

		return matchingSongs;
	}

	// **Search album by artist**
	public List<Album> searchAlbumsByArtist(String artist) {
		List<Album> matchingAlbums = new ArrayList<>();
		for (Album album : albums.values()) {
			if (album.getArtist().equalsIgnoreCase(artist)) {
				matchingAlbums.add(album);
			}
		}

		return matchingAlbums;
	}

	// **Search album by title**
	public Album getAlbum(final String title) {
		Album album = albums.get(title);

		return album;
	}
}
