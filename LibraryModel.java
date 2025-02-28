package la1;

import java.util.*;

public class LibraryModel {
	private final MusicStore store; // Link to MusicStore
	private final Set<song> library; // Songs user added
	private final Set<Album> albumLibrary; // Albums user added
	private final Map<String, List<song>> playlists; // Playlists
	private final Set<song> favorites; // Favorites
	private final Map<song, Integer> ratings; // Rating

	public LibraryModel(MusicStore store) {
		this.store = store;
		this.library = new HashSet<>();
		this.albumLibrary = new HashSet<>();
		this.playlists = new HashMap<>();
		this.favorites = new HashSet<>();
		this.ratings = new HashMap<>();
	}

	// **Add song to user library**
	public boolean addSongToLibrary(String title) {
		List<song> songs = store.searchSongByTitle(title);
		if (!songs.isEmpty()) {
			library.addAll(songs);
			return true;
		}
		return false;
	}

	// **Add album to user library**
	public boolean addAlbumToLibrary(String albumTitle) {
		Album album = store.getAlbum(albumTitle);
		if (album != null) {
			albumLibrary.add(album);
			library.addAll(album.getSongsAsList(albumTitle, store));
			return true;
		}
		return false;
	}

	// **Create playlist**
	public void createPlaylist(String name) {
		if (!playlists.containsKey(name)) {
			playlists.put(name, new ArrayList<>());
		}
	}

	// **Add song to playlist**
	public boolean addSongToPlaylist(String playlistName, String songTitle) {
		List<song> playlist = playlists.get(playlistName);
		if (playlist != null) {
			List<song> matchingSongs = store.searchSongByTitle(songTitle);
			if (!matchingSongs.isEmpty()) {
				playlist.addAll(matchingSongs);
				return true;
			}
		}
		return false;
	}

	// **Mark as favorite**
	public boolean markAsFavorite(String title) {
		List<song> songs = store.searchSongByTitle(title);
		if (!songs.isEmpty()) {
			favorites.addAll(songs);
			return true;
		}
		return false;
	}

	// **Rating**
	public boolean rateSong(String title, int rating) {
		if (rating < 1 || rating > 5) {
			return false;
		}
		List<song> songs = store.searchSongByTitle(title);
		if (!songs.isEmpty()) {
			for (song song : songs) {
				ratings.put(song, rating);
				if (rating == 5) {
					favorites.add(song); // Rate 5 be favorite
				}
			}
			return true;
		}
		return false;
	}

}
