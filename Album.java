import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Album {
	private final String title;
	private final String artist;
	private final String genre;
	private final int year;
	private final List<String> songs;

	public Album(String title, String artist, String genre, int year, List<String> songs) {
		this.title = title;
		this.artist = artist;
		this.genre = genre;
		this.year = year;
		this.songs = songs; // Insure list immutable
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getGenre() {
		return genre;
	}

	public int getYear() {
		return year;
	}

	public List<String> getSongs() {
		return songs;
	} // Only return immutable list

	public List<song> getSongsAsList(String albumTitle, MusicStore store) {
		List<song> songObjects = new ArrayList<>();
		for (String songTitle : songs) {
			songObjects.add(new song(songTitle, this.artist, albumTitle));
		}
		return songObjects;
	}

	public String toString() {
		return title + " - " + artist + " (" + year + ") [" + genre + "]";
	}

}
