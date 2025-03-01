import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class AllTests {

	// **Test MusicStore**
	@Test
	void testSearchSongByTitle_Found() {
		MusicStore store = new MusicStore();

		List<song> songs = store.searchSongByTitle("Lullaby");
		assertFalse(songs.isEmpty());
		assertEquals("Lullaby", songs.get(0).getTitle());
	}

	@Test
	void testSearchSongByTitle_NotFound() {
		MusicStore store = new MusicStore();
		List<song> songs = store.searchSongByTitle("Nonexistent Song");
		assertTrue(songs.isEmpty());
	}

	@Test
	void testFindSongsByArtist_Found() {
		MusicStore store = new MusicStore();

		List<song> songs = store.findSongsByArtist("Leonard Cohen");
		assertFalse(songs.isEmpty());
	}

	@Test
	void testFindSongsByArtist_NotFound() {
		MusicStore store = new MusicStore();
		List<song> songs = store.findSongsByArtist("Unknown Artist");
		assertTrue(songs.isEmpty());
	}

	@Test
	void testSearchAlbumsByArtist_Found() {
		MusicStore store = new MusicStore();

		List<Album> albums = store.searchAlbumsByArtist("Leonard Cohen");
		assertFalse(albums.isEmpty());
	}

	@Test
	void testSearchAlbumsByArtist_NotFound() {
		MusicStore store = new MusicStore();
		List<Album> albums = store.searchAlbumsByArtist("Unknown Artist");
		assertTrue(albums.isEmpty());
	}

	@Test
	void testGetAlbum_Found() {
		MusicStore store = new MusicStore();

		Album album = store.getAlbum("Old Ideas");
		assertNotNull(album);
	}

	@Test
	void testGetAlbum_NotFound() {
		MusicStore store = new MusicStore();
		Album album = store.getAlbum("Fake Album");
		assertNull(album);
	}

	// **Test LibraryModel**
	@Test
	void testAddSongToLibrary() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		assertTrue(library.addSongToLibrary("Lullaby"));
	}

	@Test
	void testAddAlbumToLibrary() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		assertTrue(library.addAlbumToLibrary("Old Ideas"));
	}

	@Test
	void testCreatePlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.createPlaylist("Relaxing");
		assertTrue(library.getAllPlaylists().contains("Relaxing"));
	}

	@Test
	void testAddSongToPlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");
		library.createPlaylist("Chill Vibes");

		assertTrue(library.addSongToPlaylist("Chill Vibes", "Lullaby"));
	}

	@Test
	void testRateSong() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");

		assertTrue(library.rateSong("Lullaby", 5));
	}

	@Test
	void testGetAllSongTitles() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");
		library.addSongToLibrary("Going Home");

		List<String> songTitles = library.getAllSongTitles();
		assertTrue(songTitles.contains("Lullaby"));
		assertTrue(songTitles.contains("Going Home"));
	}

	@Test
	void testGetAllArtists() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addAlbumToLibrary("Old Ideas");

		List<String> artists = library.getAllArtists();
		assertTrue(artists.contains("Leonard Cohen"));
	}

	@Test
	void testGetAllAlbums() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addAlbumToLibrary("Old Ideas");

		List<String> albums = library.getAllAlbums();
		assertTrue(albums.contains("Old Ideas"));
	}

	@Test
	void testGetAllPlaylists() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.createPlaylist("Chill Vibes");

		List<String> playlists = library.getAllPlaylists();
		assertTrue(playlists.contains("Chill Vibes"));
	}

	@Test
	void testGetFavoriteSongs() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");
		library.rateSong("Lullaby", 5);

		List<song> favoriteSongs = library.getFavoriteSongs();
		assertFalse(favoriteSongs.isEmpty());
		assertEquals("Lullaby", favoriteSongs.get(0).getTitle());
	}

	@Test
	void testPrintLibrary() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");
		library.addSongToLibrary("Going Home");

		library.printLibrary();
	}

	@Test
	void testGetFavorites() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");
		library.addSongToLibrary("Going Home");

		assertTrue(library.getFavoriteSongs().isEmpty());

		library.rateSong("Lullaby", 5);
		List<song> favorites = library.getFavoriteSongs();
		assertEquals(2, favorites.size());
		assertEquals("Lullaby", favorites.get(0).getTitle());

		library.rateSong("Going Home", 4);
		assertEquals(2, library.getFavoriteSongs().size());
	}

	@Test
	void testGetPlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");
		library.createPlaylist("Chill Vibes");

		// **The initial playlist should be empty**
		assertTrue(library.getPlaylist("Chill Vibes").isEmpty());

		// **Add song**
		library.addSongToPlaylist("Chill Vibes", "Lullaby");
		List<song> playlist = library.getPlaylist("Chill Vibes");
		assertFalse(playlist.isEmpty());
		assertEquals("Lullaby", playlist.get(0).getTitle());

		// **Search for non-exist playlist**
		assertTrue(library.getPlaylist("Unknown Playlist").isEmpty());
	}

	@Test
	void testMarkAsFavorite() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");
		library.addSongToLibrary("Going Home");

		// **The initial favorite should be empty**
		assertTrue(library.getFavoriteSongs().isEmpty());

		// **Add favorite**
		assertTrue(library.markAsFavorite("Lullaby"));
		List<song> favorites = library.getFavoriteSongs();
		assertEquals(2, favorites.size());
		assertEquals("Lullaby", favorites.get(0).getTitle());

		// **Add favorite that doesn't exist should fail**
		assertFalse(library.markAsFavorite("Unknown Song"));
	}

	@Test
	void testAlbumGetGenreAndYear() {
		Album album = new Album("Old Ideas", "Leonard Cohen", "Folk", 2012, List.of("Lullaby", "Going Home"));

		// **Test getGenre()**
		assertEquals("Folk", album.getGenre());

		// **Test getYear()**
		assertEquals(2012, album.getYear());
	}

}
