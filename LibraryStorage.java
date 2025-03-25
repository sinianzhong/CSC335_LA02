import java.io.*;
import java.util.*;

public class LibraryStorage {

    // Load the user's library
    public static LibraryModel loadLibrary(String username, MusicStore store) {
        LibraryModel library = new LibraryModel(store);
        File file = new File("library_" + username + ".txt");
        if (!file.exists()) return library;  // Return the empty library if no such file

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("SONG:")) {
                    String title = line.substring(5);
                    library.addSongToLibrary(title);
                } else if (line.startsWith("ALBUM:")) {
                    String albumTitle = line.substring(6);
                    library.addAlbumToLibrary(albumTitle);
                } else if (line.startsWith("FAVORITE:")) {
                    String favTitle = line.substring(9);
                    library.markAsFavorite(favTitle);
                } else if (line.startsWith("PLAYLIST:")) {
                    String playlistName = line.substring("PLAYLIST:".length());
                    List<song> playlistSongs = new ArrayList<>();
                    while ((line = br.readLine()) != null && !line.startsWith("PLAYLIST:") && !line.startsWith("FAVORITE:")) {
                        String[] parts = line.split("\\|");
                        song s = new song(parts[0], parts[1], parts[2], parts[3]);
                        playlistSongs.add(s);
                    }
                    library.getPlaylists().put(playlistName, playlistSongs);
                    if (line == null) break;
                } else if (line.startsWith("RECENT_PLAYS:")) {
                    while ((line = br.readLine()) != null && !line.startsWith("MOST_PLAYED:") && !line.startsWith("PLAYLIST:") && !line.startsWith("FAVORITE:")) {
                        library.getRecentPlays().add(line);
                    }
                    if (line == null) break;
                } else if (line.startsWith("MOST_PLAYED:")) {
                    while ((line = br.readLine()) != null && !line.startsWith("PLAYLIST:") && !line.startsWith("FAVORITE:") && !line.startsWith("RECENT_PLAYS:")) {
                        library.getMostPlayed().add(line);
                    }
                    if (line == null) break;
                }            
            }
        } catch (IOException e) {
            System.out.println("Failed to load library for " + username);
        }
        return library;
    }

}

