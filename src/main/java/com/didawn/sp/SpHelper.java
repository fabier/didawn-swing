package com.didawn.sp;

import static com.wrapper.spotify.Api.builder;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.User;

/**
 *
 * @author fabier
 */
public class SpHelper {

    private static final Logger logger = Logger.getLogger(SpHelper.class.getName());
    private User me;
    private final Api api;

    /**
     *
     * @param token
     */
    public SpHelper(String token) {
	this.api = builder().accessToken(token).build();
    }

    /**
     *
     * @return
     */
    public Api getApi() {
	return api;
    }

    /**
     *
     * @return @throws IOException
     * @throws WebApiException
     */
    public User getMe() throws IOException, WebApiException {
	if (this.me == null) {
	    this.me = getApi().getMe().build().get();
	}
	return this.me;
    }

    /**
     *
     * @throws IOException
     * @throws WebApiException
     */
    public void getInfoAboutMe() throws IOException, WebApiException {
	User user = getMe();
	logger.log(Level.INFO, "me = {0}", user.getDisplayName());
	logger.log(Level.INFO, "email = {0}", user.getEmail());
	logger.log(Level.INFO, "country = {0}", user.getCountry());
    }

    /**
     *
     * @return @throws IOException
     * @throws WebApiException
     */
    public String getMyId() throws IOException, WebApiException {
	return getMe().getId();
    }

    /**
     *
     * @return @throws IOException
     * @throws WebApiException
     */
    public String getMyIdBase62() throws IOException, WebApiException {
	return getMe().getId();
    }

    /**
     *
     * @throws IOException
     * @throws WebApiException
     */
    public void getInfoAboutMyPlaylists() throws IOException, WebApiException {
	Page<SimplePlaylist> playlists = getApi().getPlaylistsForUser(getMyId()).limit(50).build().get();
	List<SimplePlaylist> items = playlists.getItems();
	for (SimplePlaylist item : items) {
	    Page<PlaylistTrack> playlistTracks = getApi().getPlaylistTracks(item.getOwner().getId(), item.getId())
		    .build().get();
	    List<PlaylistTrack> tracks = playlistTracks.getItems();
	    tracks.forEach(track -> {
		String artistName = track.getTrack().getArtists().get(0).getName();
		String trackName = track.getTrack().getName();
		logger.log(Level.INFO, "track = {0} - {1}", new String[] { artistName, trackName });
	    });
	}
    }
}
