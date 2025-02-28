package la1;

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
			System.out.println("10. Quit");
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
				case 10 -> {
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
						System.out.println("❤️ Favorite Song:" + selectedSong.getTitle());
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

				// 🔹 **New Add: ask if want to favorite the songs**
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
