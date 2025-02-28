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

	private void rateSong() {
		System.out.print("Please enter the name of the song you want to rate: ");
		String title = scanner.nextLine();
		System.out.print("Please enter a rating (1-5): ");
		int rating = scanner.nextInt();
		scanner.nextLine(); // Clear line break

		if (rating < 1 || rating > 5) {
			System.out.println("❌ The rating must be between 1 and 5!");
			return;
		}

		if (library.rateSong(title, rating)) {
			System.out.println("Rateing Success: " + title + "(" + rating + ")");
			if (rating == 5) {
				System.out.println("❤️ This song has been added to the favorites!");
			}
		} else {
			System.out.println("Rating fails, and the song '" + title + "' may not exist");
		}
	}

	private void showPlaylists() {
		List<String> playlists = library.getAllPlaylists();
		if (playlists.isEmpty()) {
			System.out.println("There are no Playlists.");
		} else {
			System.out.println("🎼 Playlists:");
			playlists.forEach(System.out::println);
		}
	}

	private void showFavorites() {
		List<song> favorites = library.getFavoriteSongs();
		if (favorites.isEmpty()) {
			System.out.println("Favorites is empty.");
		} else {
			System.out.println("❤️ Favorites:");
			favorites.forEach(System.out::println);
		}
	}

	private void listAllLibrarySongs() {
		List<String> songs = library.getAllSongTitles();
		if (songs.isEmpty()) {
			System.out.println("You don't have any songs in your Library");
		} else {
			System.out.println("Songs in your Library:");
			for (String song : songs) {
				System.out.println(" - " + song);
			}
		}
	}

	private void listAllLibraryAlbums() {
		List<String> albums = library.getAllAlbums();
		if (albums.isEmpty()) {
			System.out.println("You don't have any albums in your Library");
		} else {
			System.out.println("Albums in your Library:");
			for (String album : albums) {
				System.out.println(" - " + album);
			}
		}
	}

	private void listAllLibraryArtists() {
		List<String> artists = library.getAllArtists();
		if (artists.isEmpty()) {
			System.out.println("You don't have any artists in your Library");
		} else {
			System.out.println("🎤 Artists in your Library:");
			for (String artist : artists) {
				System.out.println(" - " + artist);
			}
		}
	}

	private void addLibrarySongToPlaylist() {
		List<String> songs = library.getAllSongTitles();

		// 🔹 **Check if library has songs**
		if (songs.isEmpty()) {
			System.out.println("You don't have any songs in your Library and nothing to add to a Playlist!");
			return;
		}

		// 🔹 **Displays songs from the user's library**
		System.out.println("🎶 Songs in your Library:");
		for (int i = 0; i < songs.size(); i++) {
			System.out.println((i + 1) + ". " + songs.get(i));
		}

		// 🔹 **User select a song**
		System.out.print("Please enter the number of songs you want to add to the Playlist (0 to cancel): ");
		int songIndex = scanner.nextInt();
		scanner.nextLine(); // Clear line break

		if (songIndex <= 0 || songIndex > songs.size()) {
			System.out.println("Cancel");
			return;
		}

		String selectedSong = songs.get(songIndex - 1);

		// 🔹 **Display all playlists**
		List<String> playlists = library.getAllPlaylists();

		if (!playlists.isEmpty()) {
			System.out.println("Your Playlists:");
			for (int i = 0; i < playlists.size(); i++) {
				System.out.println((i + 1) + ". " + playlists.get(i));
			}
		}

		// 🔹 **User enters a playlist name**
		System.out.print("Please enter a Playlist name (it will be created automatically if it doesn't exist): ");
		String playlistName = scanner.nextLine().trim();

		// 🔹 **created automatically if playlist doesn't exist**
		if (!playlists.contains(playlistName)) {
			library.createPlaylist(playlistName);
			System.out.println("📁 Automatically created Playlist '" + playlistName + "'!");
		}

		// 🔹 **Execute the add**
		if (library.addSongToPlaylist(playlistName, selectedSong)) {
			System.out.println("✅ Added song '" + selectedSong + "' to Playlist '" + playlistName + "'!");
		} else {
			System.out.println("❌ Adding fails!");
		}
	}

	public static void main(String[] args) {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		MusicLibraryCLI cli = new MusicLibraryCLI(library, store);
		cli.start();
	}
}
