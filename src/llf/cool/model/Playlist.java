package llf.cool.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private ArrayList<PlaylistEntry> playlist = null;
	
	public Playlist() {
		// TODO Auto-generated constructor stub
		playlist = new ArrayList<PlaylistEntry>();
	}

	public void addPlaylistEntry(PlaylistEntry playlistEntry){
		if(playlistEntry != null){
			playlist.add(playlistEntry);			
		}
	}
	
}
