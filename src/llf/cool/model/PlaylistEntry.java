package llf.cool.model;

import java.io.Serializable;

public class PlaylistEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Album album;
	private Track track;
	
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}


	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}
}
