import java.util.Objects;

public class song {
	private final String title;
	private final String artist;
	private final String album;
	private String genre;

	public song(String title, String artist, String album) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		
	}
	public song(String title, String artist, String album, String genre) {
	    this.title = title;
	    this.artist = artist;
	    this.album = album;
	    this.genre = genre;
	}

	public String getGenre() {
		return genre;
	}
	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public String toString() {
		return title + " - " + artist + " (Album: " + album + ")";
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    song other = (song) o;
	    return title.equalsIgnoreCase(other.title) && artist.equalsIgnoreCase(other.artist);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(title.toLowerCase(), artist.toLowerCase());
	}


}
