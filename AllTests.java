package la1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AllTests {

	// **Test MusicStore**
	@Test
	void testSearchSongByTitle_Found() {
		MusicStore store = new MusicStore();

		List<song> songs = store.searchSongByTitle("Lullaby");
		assertFalse(songs.isEmpty());
		assertEquals("Lullaby", songs.get(0).getTitle());
	}

	// **Test toString**
	@Test
	void testtoString() {
		song song = new song("Uptown Funk", "Mark Ronson", "Uptown Special");
		Album album = new Album("Uptown Special", "Mark Ronson", "Funk", 2015, List.of("Uptown Funk"));
		System.out.print(album.toString());
		assertEquals("Uptown Funk - Mark Ronson (Album: Uptown Special)", song.toString());
		assertEquals("Uptown Special - Mark Ronson (2015) [Funk]", album.toString());
	}

	@Test
	void testSearchSongByTitle_NotFound() {
		MusicStore store = new MusicStore();
		List<song> songs = store.searchSongByTitle("Nonexistent Song");
		assertTrue(songs.isEmpty());
	}

	@Test
	public void testSongWithGenre() {

		song s = new song("Yellow", "Coldplay", "Parachutes", "Alternative");

		assertEquals("Yellow", s.getTitle());
		assertEquals("Coldplay", s.getArtist());
		assertEquals("Parachutes", s.getAlbum());
		assertEquals("Alternative", s.getGenre());
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

		assertTrue(library.getPlaylist("Chill Vibes").isEmpty());

		library.addSongToPlaylist("Chill Vibes", "Lullaby");
		List<song> playlist = library.getPlaylist("Chill Vibes");
		assertFalse(playlist.isEmpty());
		assertEquals("Lullaby", playlist.get(0).getTitle());

		assertTrue(library.getPlaylist("Unknown Playlist").isEmpty());
	}

	@Test
	void testMarkAsFavorite() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("Lullaby");
		library.addSongToLibrary("Going Home");

		assertTrue(library.getFavoriteSongs().isEmpty());

		assertTrue(library.markAsFavorite("Lullaby"));
		List<song> favorites = library.getFavoriteSongs();
		assertEquals(2, favorites.size());
		assertEquals("Lullaby", favorites.get(0).getTitle());

		assertFalse(library.markAsFavorite("Unknown Song"));
	}

	@Test
	void testAlbumGetGenreAndYear() {
		Album album = new Album("Old Ideas", "Leonard Cohen", "Folk", 2012, List.of("Lullaby", "Going Home"));
		assertEquals("Folk", album.getGenre());

		assertEquals(2012, album.getYear());
	}

	@Test
	public void testGenerateFavoritePlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.rateSong("Clocks", 5);
		assertTrue(library.generateFavoritePlaylist());
		assertFalse(library.getPlaylist("Favorites").isEmpty());
	}

	@Test
	public void testGenerateTopRatedPlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		assertTrue(library.addSongToLibrary("Lullaby"));
		library.rateSong("Lullaby", 5);
		assertTrue(library.generateTopRatedPlaylist());
		assertFalse(library.getPlaylist("TopRated").isEmpty());
	}

	@Test
	public void testGenerateGenrePlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		assertFalse(library.generateGenrePlaylist("Rock"));
	}

	@Test
	public void testPlayRandomSong() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		String playedSong = library.playRandomSong();
		assertNotNull(playedSong);
	}

	@Test
	public void testLoginFail() {
		UserManager userManager = new UserManager();
		userManager.registerUser("testUser", "password123");
		assertNull(userManager.login("testUser", "wrongPassword"));
	}

	@Test
	public void testShuffleLibrary() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		library.addSongToLibrary("rolling in the deep");
		library.addSongToLibrary("Tired");
		library.addSongToLibrary("My Same");

		List<String> originalOrder = new ArrayList<>();
		for (song s : library) {
			originalOrder.add(s.getTitle());
		}

		library.shuffleLibrary();

		List<String> shuffledOrder = new ArrayList<>();
		for (song s : library) {
			shuffledOrder.add(s.getTitle());
		}

		assertEquals(3, shuffledOrder.size());
		assertTrue(originalOrder.containsAll(shuffledOrder));
	}

	@Test
	public void testShufflePlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);

		List<song> playlist = new ArrayList<>();
		playlist.add(new song("Daydreamer", "Adele", "19"));
		playlist.add(new song("Tired", "Adele", "19"));
		playlist.add(new song("My Same", "Adele", "19"));
		library.getPlaylists().put("TestPlaylist", playlist);

		List<String> originalOrder = new ArrayList<>();
		for (song s : library.getPlaylist("TestPlaylist")) {
			originalOrder.add(s.getTitle());
		}

		library.shufflePlaylist("TestPlaylist");

		List<String> shuffledOrder = new ArrayList<>();
		for (song s : library.getPlaylist("TestPlaylist")) {
			shuffledOrder.add(s.getTitle());
		}

		assertEquals(3, shuffledOrder.size());
		assertTrue(originalOrder.containsAll(shuffledOrder));
	}

	@Test
	public void testContainsSong() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		assertTrue(library.containsSong("Clocks"));
		assertFalse(library.containsSong("Viva La Vida"));
	}

	@Test
	public void testSearchSongsByTitle() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		List<String> result = library.searchSongsByTitle("Clocks");
		assertFalse(result.isEmpty());
	}

	@Test
	public void testFindSongsByArtist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		List<String> result = library.findSongsByArtist("Coldplay");
		assertFalse(result.isEmpty());
	}

	@Test
	public void testSearchAlbumsByTitle() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addAlbumToLibrary("A Rush of Blood to the Head");
		List<String> result = library.searchAlbumsByTitle("A Rush of Blood to the Head");
		assertFalse(result.isEmpty());
	}

	@Test
	public void testFindAlbumsByArtist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addAlbumToLibrary("A Rush of Blood to the Head");
		List<String> result = library.findAlbumsByArtist("Coldplay");
		assertFalse(result.isEmpty());
	}

	@Test
	public void testRemoveSongFromPlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		library.createPlaylist("MyPlaylist");
		library.addSongToPlaylist("MyPlaylist", "Clocks");
		assertTrue(library.removeSongFromPlaylist("MyPlaylist", "Clocks"));
	}

	@Test
	public void testViewPlaylist() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		library.createPlaylist("MyPlaylist");
		library.addSongToPlaylist("MyPlaylist", "Clocks");
		List<String> playlist = library.viewPlaylist("MyPlaylist");
		assertEquals(1, playlist.size());
	}

	@Test
	public void testPlaySong() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		assertTrue(library.playSong("Clocks"));
	}

	@Test
	public void testRemoveSong() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		assertTrue(library.removeSong("Clocks"));
		assertFalse(library.containsSong("Clocks"));
	}

	@Test
	public void testGetRecentPlays() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		library.playSong("Clocks");
		List<String> recent = library.getRecentPlays();
		assertFalse(recent.isEmpty());
		assertEquals("Clocks", recent.get(0));
	}

	@Test
	public void testGetMostPlayed() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		library.playSong("Clocks");
		library.playSong("Clocks");
		List<String> mostPlayed = library.getMostPlayed();
		assertEquals("Clocks", mostPlayed.get(0));
	}

	@Test
	public void testSearchSongsByGenre() {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		library.addSongToLibrary("Clocks");
		List<song> result = library.searchSongsByGenre("Alternative Rock");
		assertTrue(result.isEmpty());
	}

	@Test
	public void testRegisterUserSuccess() {
		UserManager userManager = new UserManager();
		boolean result = userManager.registerUser("testUser11", "password123");
		assertTrue(result);
	}

	@Test
	public void testSaveUsersFileCreated() {
		UserManager userManager = new UserManager();

		userManager.registerUser("testUser6", "password456");

		File file = new File("users.txt");
		assertTrue(file.exists());
	}
}
