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

	// **Get playlist**
	public List<song> getPlaylist(String name) {
		if (playlists.containsKey(name)) {
			return playlists.get(name);
		}
		return new ArrayList<>(); // Return an empty ArrayList
	}

	// **Get favorites**
	public Set<song> getFavorites() {
		return new HashSet<>(favorites);
	}

	// **Print songs in library**
	public void printLibrary() {
		for (song song : library) {
			System.out.println(song);
		}
	}

	// **Get all song titles**
	public List<String> getAllSongTitles() {
		List<String> songTitles = new ArrayList<>();
		for (song song : library) {
			if (!songTitles.contains(song.getTitle())) {
				songTitles.add(song.getTitle());
			}
		}
		return songTitles;
	}

	// **Get all artists**
	public List<String> getAllArtists() {
		List<String> artists = new ArrayList<>();
		for (song song : library) {
			if (!artists.contains(song.getArtist())) {
				artists.add(song.getArtist());
			}
		}
		return artists;
	}

	// **Get all albums**
	public List<String> getAllAlbums() {
		List<String> albumTitles = new ArrayList<>();
		for (Album album : albumLibrary) {
			if (!albumTitles.contains(album.getTitle())) {
				albumTitles.add(album.getTitle());
			}
		}
		return albumTitles;
	}

	// **Get all playlists**
	public List<String> getAllPlaylists() {
		return new ArrayList<>(playlists.keySet());
	}

	// **Get all favorites**
	public List<song> getFavoriteSongs() {
		return new ArrayList<>(favorites);
	}
}
