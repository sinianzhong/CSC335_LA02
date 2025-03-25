import java.util.List;
import java.util.Scanner;

public class MusicLibraryCLI {
	private final LibraryModel library;
	private final MusicStore store;
	private final Scanner scanner;

	public MusicLibraryCLI(LibraryModel library, MusicStore store) {
		this.library = library;
		this.store = store;
		this.scanner = new Scanner(System.in);
	}

	public void start() {
		System.out.println("Welcome to the Music Library Management System");
		while (true) {
			System.out.println("\nPlease select an action:");
			System.out.println("1. Search for songs (by title or artist, and can be added)");
			System.out.println("2. Search for albums (by title or artist, and can be added)");
			System.out.println("3. Rate");
			System.out.println("4. Show all Playlists");
			System.out.println("5. Display all Favorite Songs");
			System.out.println("6. Displays all songs from Library");
			System.out.println("7. Displays all albums from Library");
			System.out.println("8. Displays all artists from Library");
			System.out.println("9. Add song in Library to Playlist");
			System.out.println("10. Play Songs (Stats)");
			System.out.println("11. View the most recent played songs");
			System.out.println("12. view the most frequently played songs");
			System.out.println("13. Create a favorites leaderboard");
			System.out.println("14. Create a TopRated leaderboard");
			System.out.println("15. Create Genre playlist");
			System.out.println("16. Search for songs by title");
			System.out.println("17. Search for songs by artist");
			System.out.println("18. Search for albums by title");
			System.out.println("19. Search for albums by artist");
			System.out.println("20. Search for Playlists");
			System.out.println("21. Remove songs from the Playlist");
			System.out.println("22. View Playlist content");
			System.out.println("23. Show all Playlists");
			System.out.println("24. Play a random song");
			System.out.println("25. Shuffle song");
			System.out.println("26. Shuffle Playlists");
			System.out.println("27. Search for songs by genre");
			System.out.println("28. Quit");
			System.out.print("Please enter an option: ");
			try {
				int choice = Integer.parseInt(scanner.nextLine().trim()); // Use 'parseInt()' to handle exceptions
				switch (choice) {
				case 1 -> searchAndAddSong();
				case 2 -> searchAndAddAlbum();
				case 3 -> rateSong();
				case 4 -> showPlaylists();
				case 5 -> showFavorites();
				case 6 -> listAllLibrarySongs();
				case 7 -> listAllLibraryAlbums();
				case 8 -> listAllLibraryArtists();
				case 9 -> addLibrarySongToPlaylist();
				case 10 -> playSong();
				case 11 -> showRecentPlays();
				case 12 -> showMostPlayed();
				case 13 -> showFavoritePlaylist();
				case 14 -> showTopRatedPlaylist();
				case 15 -> showGenrePlaylist();
				case 16 -> searchSongByTitle();
				case 17 -> searchSongByArtist();
				case 18 -> searchAlbumByTitle();
				case 19 -> searchAlbumByArtist();
			    	case 20 -> searchPlaylistByName();
				case 21 -> removeSongFromPlaylist();
			    	case 22 -> viewPlaylist();
			    	case 23 -> viewAllPlaylists();
			    	case 24 -> playRandomSong();
			    	case 25 -> shuffleAndPlayLibrary();
			    	case 26 -> shuffleAndPlayPlaylist();
			    	case 27 -> searchSongsByGenre();
			    	case 28 ->
				{
					System.out.println("Good bye! Thanks for using the Music Library");
					return;
				}
				default -> System.out.println("Invalid option, please enter a number between 1-10!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid option number!");
			}
		}
	}

	private void searchAndAddSong() {
		while (true) {
			System.out.print("Please enter the name of the song or the artist (0 to exit): ");
			String query = scanner.nextLine().trim();

			if (query.equals("0")) {
				System.out.println("Canceled the search for songs.");
				return;
			}

			// **Step 1: Find it in the library first**
			List<String> userSongs = library.getAllSongTitles();
			if (userSongs.contains(query)) {
				System.out.println("This song is already in your Library!");
				return;
			}

			// **Step 2: Search in MusicStore**
			List<song> storeSongs = store.searchSongByTitle(query);

			// **If can't find by title, search by artist**
			if (storeSongs.isEmpty()) {
				storeSongs = store.findSongsByArtist(query);
			}

			// **If found, ask if you want to add it**
			if (!storeSongs.isEmpty()) {
				System.out.println("Find the following songs, would you like to add to Library?");
				for (int i = 0; i < storeSongs.size(); i++) {
					System.out.println((i + 1) + ". " + storeSongs.get(i));
				}

				System.out.print("Enter the number you want to add (0 to cancel): ");
				int selection = scanner.nextInt();
				scanner.nextLine(); // Clear line break

				if (selection > 0 && selection <= storeSongs.size()) {
					song selectedSong = storeSongs.get(selection - 1);
					library.addSongToLibrary(selectedSong.getTitle());
					System.out.println("Added " + selectedSong.getTitle() + " to your Library!");

					// **Ask if add to favorite**
					System.out.print("Do you want to add to Favorite?(y/n): ");
					String favoriteChoice = scanner.nextLine().trim().toLowerCase();
					if (favoriteChoice.equals("y")) {
						library.rateSong(selectedSong.getTitle(), 5);
						System.out.println("Favorite Song:" + selectedSong.getTitle());
					}
					return; // Exit the loop after adding successfully
				} else {
					System.out.println("Cancel.");
					return;
				}
			} else {
				System.out.println("No songs related to '" + query + "' were found, please re-enter.");
			}
		}
	}

	private void searchAndAddAlbum() {
		System.out.print("Please enter the name of album or artist:");
		String query = scanner.nextLine();

		// **Step 1: Find it in the library first**
		List<String> userAlbums = library.getAllAlbums();
		if (userAlbums.contains(query)) {
			System.out.println("This album is already in your Library!");
			return;
		}

		// **Step 2: Search in MusicStore**
		Album album = store.getAlbum(query);

		// **If can't find it by title, search by artist**
		if (album == null) {
			List<Album> artistAlbums = store.searchAlbumsByArtist(query);
			if (!artistAlbums.isEmpty()) {
				System.out.println("Find the following albums, would you like to add to Library?");
				for (int i = 0; i < artistAlbums.size(); i++) {
					System.out.println((i + 1) + ". " + artistAlbums.get(i));
				}

				System.out.print("Enter the number you want to add (0 to cancel): ");
				int selection = scanner.nextInt();
				scanner.nextLine(); // Clear line break

				if (selection > 0 && selection <= artistAlbums.size()) {
					Album selectedAlbum = artistAlbums.get(selection - 1);
					library.addAlbumToLibrary(selectedAlbum.getTitle());
					System.out.println("Added " + selectedAlbum.getTitle() + " to your Library!");

					// **New Add: ask if want to favorite the songs**
					System.out.print("Do you want to add the songs to Favorite? (y/n): ");
					String favoriteChoice = scanner.nextLine().trim().toLowerCase();
					if (favoriteChoice.equals("y")) {
						for (String songTitle : selectedAlbum.getSongs()) {
							library.rateSong(songTitle, 5);
						}
						System.out.println("All the songs in album are added to Favorite!");
					}
				} else {
					System.out.println("Cancel.");
				}
				return;
			}
		}

		// **If found a single album**
		if (album != null) {
			System.out.println("Find album:" + album);
			System.out.print("Do you want to add to Library? (y/n): ");
			String choice = scanner.nextLine().trim().toLowerCase();

			if (choice.equals("y")) {
				library.addAlbumToLibrary(album.getTitle());
				System.out.println("Added album " + album.getTitle() + " to your Library!");

				// **New Add: ask if want to favorite the songs**
				System.out.print("Do you want to add the songs to Favorite? (y/n):");
				String favoriteChoice = scanner.nextLine().trim().toLowerCase();
				if (favoriteChoice.equals("y")) {
					for (String songTitle : album.getSongs()) {
						library.rateSong(songTitle, 5);
					}
					System.out.println("All the songs in album are added to Favorite!");
				}
			} else {
				System.out.println("Cancel.");
			}
		} else {
			System.out.println("No albums related to '" + query + "' were found, please re-enter.");
		}
	}

	private void rateSong() {
		System.out.print("Please enter the name of the song you want to rate: ");
		String title = scanner.nextLine();
		System.out.print("Please enter a rating (1-5): ");
		int rating = scanner.nextInt();
		scanner.nextLine(); // Clear line break

		if (rating < 1 || rating > 5) {
			System.out.println("The rating must be between 1 and 5!");
			return;
		}

		if (library.rateSong(title, rating)) {
			System.out.println("Rateing Success: " + title + "(" + rating + ")");
			if (rating == 5) {
				System.out.println("This song has been added to the favorites!");
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
			System.out.println("Playlists:");
			playlists.forEach(System.out::println);
		}
	}

	private void showFavorites() {
		List<song> favorites = library.getFavoriteSongs();
		if (favorites.isEmpty()) {
			System.out.println("Favorites is empty.");
		} else {
			System.out.println("Favorites:");
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
			System.out.println("Artists in your Library:");
			for (String artist : artists) {
				System.out.println(" - " + artist);
			}
		}
	}

	private void addLibrarySongToPlaylist() {
		List<String> songs = library.getAllSongTitles();

		// **Check if library has songs**
		if (songs.isEmpty()) {
			System.out.println("You don't have any songs in your Library and nothing to add to a Playlist!");
			return;
		}

		// **Displays songs from the user's library**
		System.out.println("Songs in your Library:");
		for (int i = 0; i < songs.size(); i++) {
			System.out.println((i + 1) + ". " + songs.get(i));
		}

		// **User select a song**
		System.out.print("Please enter the number of songs you want to add to the Playlist (0 to cancel): ");
		int songIndex = scanner.nextInt();
		scanner.nextLine(); // Clear line break

		if (songIndex <= 0 || songIndex > songs.size()) {
			System.out.println("Cancel");
			return;
		}

		String selectedSong = songs.get(songIndex - 1);

		// **Display all playlists**
		List<String> playlists = library.getAllPlaylists();

		if (!playlists.isEmpty()) {
			System.out.println("Your Playlists:");
			for (int i = 0; i < playlists.size(); i++) {
				System.out.println((i + 1) + ". " + playlists.get(i));
			}
		}

		// **User enters a playlist name**
		System.out.print("Please enter a Playlist name (it will be created automatically if it doesn't exist): ");
		String playlistName = scanner.nextLine().trim();

		// **created automatically if playlist doesn't exist**
		if (!playlists.contains(playlistName)) {
			library.createPlaylist(playlistName);
			System.out.println("Automatically created Playlist '" + playlistName + "'!");
		}

		// **Execute the add**
		if (library.addSongToPlaylist(playlistName, selectedSong)) {
			System.out.println("Added song '" + selectedSong + "' to Playlist '" + playlistName + "'!");
		} else {
			System.out.println("Adding fails!");
		}
	}

/* LA_02 New Methods */

		// Play and count the number of plays
	private void playSong() {
	    System.out.print("Please enter the title of the song you want to play:");
	    String songTitle = scanner.nextLine();
	    if (library.playSong(songTitle)) {
	        System.out.println("Successfully played the song!");
	    } else {
	        System.out.println("The song doesn't exist and can't be played!");
	    }
	}

	// Show recent plays
	private void showRecentPlays() {
		library.generateRecentPlaysPlaylist();
		List<song> recentList = library.getPlaylist("RecentPlays");
		if (recentList == null || recentList.isEmpty()) {
			System.out.println("Have no play history");
		} else {
			System.out.println("Recent played history:");
			for (song s : recentList) {
				System.out.println(s.getTitle());
			}
		}
	}
   
	// Show most played
	private void showMostPlayed() {
		library.generateMostPlayedPlaylist();
		List<song> mostPlayedList = library.getPlaylist("MostPlayed");
		if (mostPlayedList == null || mostPlayedList.isEmpty()) {
			System.out.println("Have no play history");
		} else {
			System.out.println("Most frequently played Top 10：");
			for (song s : mostPlayedList) {
				System.out.println(s.getTitle());
			}
		}
	}

	private void showFavoritePlaylist() {
	    if (library.generateFavoritePlaylist()) {
	        List<song> favList = library.getPlaylist("Favorites");
	        System.out.println("Favorites (Favorite Songs with 4 or 5 Rating):");
	        for (song s : favList) {
	            System.out.println(s.getTitle());
	        }

	    } else {
	        System.out.println("The Favorites list is empty and cannot be generated！");
	    }
	}

	private void showTopRatedPlaylist() {
	    if (library.generateTopRatedPlaylist()) {
	        List<song> topRated = library.getPlaylist("TopRated");
	        System.out.println("Top Rated Rankings (rating 4 and above):");
	        for (song s : topRated) {
	            System.out.println(s.getTitle());
	        }

	    } else {
	        System.out.println("Top Rated playlist is empty and cannot be generated!");
	    }
	}

	private void showGenrePlaylist() {
	    System.out.print("Please enter the Genre：");
	    String genre = scanner.nextLine();
	    if (library.generateGenrePlaylist(genre)) {
	        List<song> genreList = library.getPlaylist(genre);
	        System.out.println("Genre Playlist【" + genre + "】:");
	        for (song s : genreList) {
	            System.out.println(s.getTitle());
	        }

	    } else {
	        System.out.println("There are less than 10 songs in this genre, and cannot generate playlist!");
	    }
	}

	public static void main(String[] args) {
		MusicStore store = new MusicStore();
		LibraryModel library = new LibraryModel(store);
		MusicLibraryCLI cli = new MusicLibraryCLI(library, store);
		cli.start();
	}
}
