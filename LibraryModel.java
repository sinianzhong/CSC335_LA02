import java.util.*;

public class LibraryModel implements Iterable<song> {
	private final MusicStore store; // Link to MusicStore
	private final Set<song> library; // Songs user added
	private final Set<Album> albumLibrary; // Albums user added
	private final Map<String, List<song>> playlists; // Playlists
	private final Set<song> favorites; // Favorites
	private final Map<song, Integer> ratings; // Rating
	private Map<String, Integer> playCount = new HashMap<>();
	private ArrayList<String> recentPlays = new ArrayList<>();

	public LibraryModel(MusicStore store) {
		this.store = store;
		this.library = new HashSet<>();
		this.albumLibrary = new HashSet<>();
		this.playlists = new HashMap<>();
		this.favorites = new HashSet<>();
		this.ratings = new HashMap<>();
	}

	public boolean containsSong(String title) {
	    for (song s : library) {
	        if (s.getTitle().equals(title)) return true;
	    }
	    return false;
	}

	public List<String> searchSongsByTitle(String title) {
	    List<String> result = new ArrayList<>();
	    for (song s : library) {
	        if (s.getTitle().equalsIgnoreCase(title)) {
	            result.add(s.getTitle() + " - " + s.getArtist());
	        }
	    }
	    return result;
	}

	public List<String> findSongsByArtist(String artist) {
	    List<String> result = new ArrayList<>();
	    for (song s : library) {
	        if (s.getArtist().equalsIgnoreCase(artist)) {
	            result.add(s.getTitle() + " - " + s.getArtist());
	        }
	    }
	    return result;
	}

	public List<String> searchAlbumsByTitle(String albumTitle) {
	    List<String> result = new ArrayList<>();
	    for (Album album : albumLibrary) {
	        if (album.getTitle().equalsIgnoreCase(albumTitle)) {
	            result.add(album.getTitle());
	        }
	    }
	    return result;
	}

	public List<String> findAlbumsByArtist(String artist) {
	    List<String> result = new ArrayList<>();
	    for (Album album : albumLibrary) {
	        if (album.getArtist().equalsIgnoreCase(artist)) {
	            result.add(album.getTitle());
	        }
	    }
	    return result;
	}

	public boolean removeSongFromPlaylist(String playlistName, String songTitle) {
	    List<song> playlist = playlists.get(playlistName);
	    if (playlist != null) {
	        return playlist.removeIf(s -> s.getTitle().equalsIgnoreCase(songTitle));
	    }
	    return false;
	}

	public List<String> viewPlaylist(String playlistName) {
	    List<String> result = new ArrayList<>();
	    List<song> playlist = playlists.get(playlistName);
	    if (playlist != null) {
	        for (song s : playlist) {
	            result.add(s.getTitle() + " - " + s.getArtist());
	        }
	    }
	    return result;
	}

	public Set<String> getAllPlaylistNames() {
	    return playlists.keySet();
	}

	public boolean playSong(String songTitle) {
	    // Traverse the library to find if there is a match (ignore case)
	    for (song s : library) {
	        if (s.getTitle().equalsIgnoreCase(songTitle)) {
	            String matchedTitle = s.getTitle();  // Keep the original format

	            playCount.put(matchedTitle, playCount.getOrDefault(matchedTitle, 0) + 1);
	            recentPlays.remove(matchedTitle); // If it exists, delete it first
	            recentPlays.add(0, matchedTitle); // Add to the top

	            if (recentPlays.size() > 10) {
	                recentPlays.remove(recentPlays.size() - 1); // More than 10 songs, delete the last one
	            }
	            return true;  // Successfully played
	        }
	    }
	    return false; // Not found
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

    public boolean removeSong(String title) {
        song target = null;
        for (song s : library) {
            if (s.getTitle().equals(title)) {
                target = s;
                break;
            }
        }
        if (target != null) {
            library.remove(target);
            favorites.remove(target);
            ratings.remove(target);
            playCount.remove(title);
            recentPlays.remove(title);
            return true;
        }
        return false;
    }

	// Generate recent played Playlist
	public void generateRecentPlaysPlaylist() {
	List<song> result = new ArrayList<>();
	for (String title : recentPlays) {
		for (song s : library) {
			if (s.getTitle().equalsIgnoreCase(title)) {
				result.add(s);
				break;
			}
		}
	}
	playlists.put("RecentPlays", result);
	}

    public List<String> getRecentPlays() {
        return new ArrayList<>(recentPlays);
    }

	// Generate most played Playlist
	public void generateMostPlayedPlaylist() {
		List<song> result = new ArrayList<>();
		List<Map.Entry<String, Integer>> sorted = new ArrayList<>(playCount.entrySet());
		sorted.sort((a, b) -> b.getValue() - a.getValue());
		int count = 0;
		for (Map.Entry<String, Integer> entry : sorted) {
			if (count >= 10) break;
			for (song s : library) {
				if (s.getTitle().equalsIgnoreCase(entry.getKey())) {
					result.add(s);
					count++;
					break;
				}
			}
		}
		playlists.put("MostPlayed", result);
	}

    public List<String> getMostPlayed() {
        List<song> resultList = new ArrayList<>(library);

        resultList.sort((s1, s2) -> 
            playCount.getOrDefault(s2.getTitle(), 0) - playCount.getOrDefault(s1.getTitle(), 0)
        );

        List<String> topPlayed = new ArrayList<>();
        int count = 0;
        for (song s : resultList) {
            if (count >= 10) break;
            topPlayed.add(s.getTitle());
            count++;
        }
        return topPlayed;
    }

    public int getRating(String title) {
        for (Map.Entry<song, Integer> entry : ratings.entrySet()) {
            if (entry.getKey().getTitle().equalsIgnoreCase(title)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public boolean generateFavoritePlaylist() {
        if (favorites.isEmpty()) return false;
        playlists.put("Favorites", new ArrayList<>(favorites)); // Directly refer to the same song
        return true;
    }

    public boolean generateTopRatedPlaylist() {
        List<song> result = new ArrayList<>();
        for (song s : library) {
            if (getRating(s.getTitle()) >= 4) {
                result.add(s);
            }
        }
        if (!result.isEmpty()) {
            playlists.put("TopRated", result);
            return true;
        }
        return false;
    }

    public boolean generateGenrePlaylist(String genre) {
        List<song> result = new ArrayList<>();
        for (song s : library) {
            if (s.getGenre() != null && s.getGenre().equalsIgnoreCase(genre)) {
                result.add(s);
            }
        }
        if (result.size() >= 10) {
            playlists.put(genre, result);
            return true;
        }
        return false;
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
	
	public String playRandomSong() {
	    if (library.isEmpty()) {
	        return null;
	    }
	    List<song> songList = new ArrayList<>(library);
	    Random random = new Random();
	    song randomSong = songList.get(random.nextInt(songList.size()));

	    // Normal playback logic (counting and joining recent plays)
	    playCount.put(randomSong.getTitle(), playCount.getOrDefault(randomSong.getTitle(), 0) + 1);
	    recentPlays.remove(randomSong.getTitle());
	    recentPlays.add(0, randomSong.getTitle());
	    if (recentPlays.size() > 10) {
	        recentPlays.remove(recentPlays.size() - 1);
	    }
	    return randomSong.getTitle();
	}
	
	public List<song> searchSongsByGenre(String genre) {
	    List<song> result = new ArrayList<>();
	    for (song s : library) {
	        if (s.getGenre() != null && s.getGenre().equalsIgnoreCase(genre)) {
	            result.add(s);
	        }
	    }
	    return result;
	}

	public void shuffleLibrary() {
	    List<song> songList = new ArrayList<>(library);
	    Collections.shuffle(songList);
	    library.clear();
	    library.addAll(songList);  // Empty it directly and add it back in order
	}
	
	public void shufflePlaylist(String playlistName) {
	    List<song> playlist = playlists.get(playlistName);
	    if (playlist != null) {
	        Collections.shuffle(playlist); // Randomly shuffle
	    }
	}

	@Override
	public Iterator<song> iterator() {
	    return library.iterator();  // Support for-each
	}
	public Map<String, List<song>> getPlaylists() {
	    return playlists;
	}

}
