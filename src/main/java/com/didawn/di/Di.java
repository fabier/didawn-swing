package com.didawn.di;

import static com.didawn.Constants.dawn;
import static com.didawn.Constants.dawnDotCom;
import static com.didawn.di.Crypter.decryptBlowfish;
import static com.didawn.di.Crypter.getBlowfishKey;
import static com.didawn.di.Utils.closeQuietly;
import static java.lang.Long.parseLong;
import static java.lang.Thread.sleep;
import static java.net.URLEncoder.encode;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.apache.http.impl.client.HttpClientBuilder.create;
import static org.apache.http.util.EntityUtils.consume;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.didawn.json.Alternative;
import com.didawn.json.Data;
import com.didawn.json.Error;
import com.didawn.json.Response;
import com.didawn.json.Results;
import com.didawn.json.SearchResponse;
import com.didawn.models.ArtistList;
import com.didawn.models.Song;
import com.google.gson.Gson;

/**
 *
 * @author fabier
 */
public class Di {

    private static final Logger logger = getLogger(Di.class.getName());
    private static final long WAIT_TIME = 1000L;

    /**
     *
     */
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
    public static final String KEY_USER_AGENT = "User-Agent";
    private static final String UTF8 = "UTF-8";
    private static final String HTTP_API = "http://api.";
    private static final String BASE_API_URL = HTTP_API + dawnDotCom() + "/";
    private static final String TRACK_URL = BASE_API_URL + "search/track?q=%s";

    private static final Header[] browserHeaders = new Header[] { //
	    new BasicHeader("User-Agent", USER_AGENT), //
	    new BasicHeader("Content-Language", "en-US"), //
	    new BasicHeader("Cache-Control", "max-age=0"), //
	    new BasicHeader("Accept", "*/*"), //
	    new BasicHeader("Accept-Charset", "utf-8,ISO-8859-1;q=0.7,*;q=0.3"), //
	    new BasicHeader("Acce" + "pt-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4"), //
	    new BasicHeader("Accept-Encoding", "gzip,deflate,sdch") //
    };
    private static final HttpClient httpClient = create().build();
    private static String oldToken;
    private static String apiToken;

    /**
     *
     * @param url
     * @param headers
     * @return
     */
    public static String get(String url, List<Header> headers) {
	logger.log(INFO, "GET : {0}", new Object[] { url });

	String responseContent = null;
	HttpEntity httpEntity = null;

	try {
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.setHeader(KEY_USER_AGENT, USER_AGENT);
	    if (headers != null) {
		Header[] headersArr = new Header[headers.size()];
		httpGet.setHeaders(headers.toArray(headersArr));
	    }

	    HttpResponse httpResponse = httpClient.execute(httpGet);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    httpEntity = httpResponse.getEntity();
	    StatusLine statusLine = httpResponse.getStatusLine();
	    int statusCode = statusLine.getStatusCode();
	    if (statusCode != 200) {
		throw new IllegalStateException("status code: " + statusLine.getStatusCode());
	    }

	    httpEntity.writeTo(baos);
	    responseContent = baos.toString(UTF8);
	} catch (IOException | RuntimeException var19) {
	    try {
		consume(httpEntity);
	    } catch (IOException var18) {
	    }
	} finally {
	    try {
		consume(httpEntity);
	    } catch (IOException var17) {
	    }
	}

	String newToken = substringBetween(responseContent, "var checkForm", ";");
	if (newToken != null) {
	    while (true) {
		if (!newToken.startsWith("\"") && !newToken.startsWith("'") && !newToken.startsWith("=")
			&& !newToken.startsWith(" ")) {
		    while (newToken.endsWith("\"") || newToken.endsWith("'") || newToken.endsWith("=")
			    || newToken.endsWith(" ")) {
			newToken = newToken.substring(0, newToken.length() - 1);
		    }

		    if (newToken.length() == 32) {
			oldToken = apiToken;
			apiToken = newToken;
		    }
		    break;
		}

		newToken = newToken.substring(1);
	    }
	}

	return responseContent;
    }

    /**
     *
     * @param url
     * @param data
     * @param headers
     * @return
     */
    public static String post(String url, String data, List<Header> headers) {
	logger.log(INFO, "POST : {0}", new Object[] { url });

	String responseContent = null;
	HttpEntity httpEntity = null;

	try {
	    HttpPost httpPost = new HttpPost(url);
	    httpPost.setHeader(KEY_USER_AGENT, USER_AGENT);
	    if (headers != null) {
		httpPost.setHeaders(headers.toArray(new Header[headers.size()]));
	    }

	    httpPost.setEntity(new StringEntity(data));
	    HttpResponse httpResponse = httpClient.execute(httpPost);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    httpEntity = httpResponse.getEntity();
	    StatusLine statusLine = httpResponse.getStatusLine();
	    int statusCode = statusLine.getStatusCode();
	    if (statusCode != 200) {
		logger.log(INFO, "{0}", statusCode);
		throw new IllegalStateException("status code: " + statusLine.getStatusCode());
	    }

	    httpEntity.writeTo(baos);
	    responseContent = baos.toString(UTF8);
	} catch (IOException var18) {
	} finally {
	    try {
		consume(httpEntity);
	    } catch (IOException var17) {
	    }

	}

	if (responseContent != null && responseContent.contains("VALID_TOKEN_REQUIRED")) {
	    get(BASE_API_URL, asList(browserHeaders));
	    return post(url.replace(oldToken, apiToken), data, headers);
	} else {
	    return responseContent;
	}
    }

    /**
     *
     * @param track
     * @param outputStream
     * @param downloadListener
     * @return
     */
    public static boolean download(Song track, OutputStream outputStream, DownloadProgressListener downloadListener) {
	HttpGet httpGet = new HttpGet(track.getDownloadURL());

	try {
	    httpGet.setHeaders(browserHeaders);
	    HttpResponse httpResponse = httpClient.execute(httpGet);
	    HttpEntity httpEntity = httpResponse.getEntity();
	    StatusLine statusLine = httpResponse.getStatusLine();
	    int statusCode = statusLine.getStatusCode();
	    if (statusCode != 200) {
		return false;
	    } else {
		int chunkSize = 2_048;
		int intervalChunk = 3;
		InputStream inputStream = httpEntity.getContent();
		long contentLength = httpEntity.getContentLength();
		byte[] chunk = new byte[chunkSize];
		int chunks = (int) (contentLength / chunkSize);
		int readTotal = 0;
		int i = 0;
		byte[] blowfishKey = getBlowfishKey(parseLong(track.getId()));

		int read;
		while ((read = inputStream.read(chunk, 0, chunkSize)) != -1) {
		    if (read < chunkSize && i < chunks - 1) {
			ByteBuffer buffer = allocate(chunkSize);
			buffer.put(wrap(chunk, 0, read));

			while (buffer.hasRemaining()) {
			    byte[] temp = new byte[buffer.remaining()];
			    int tempRead = inputStream.read(temp, 0, buffer.remaining());
			    read += tempRead;
			    buffer.put(temp, 0, tempRead);
			}

			chunk = buffer.array();
		    }

		    if (i % intervalChunk == 0 && read == chunkSize) {
			chunk = decryptBlowfish(chunk, blowfishKey);
		    }

		    outputStream.write(chunk, 0, read);
		    ++i;
		    readTotal += read;
		    downloadListener.onProgress(track, readTotal, contentLength);
		}

		outputStream.close();
		closeQuietly(outputStream, track.getTitle());
		consume(httpEntity);
		return true;
	    }
	} catch (IOException var22) {
	    return false;
	}
    }

    /**
     *
     */
    public Di() {
	get(BASE_API_URL, asList(browserHeaders));
    }

    /**
     *
     * @param songID
     * @return
     */
    public Song getSong(String songID) {
	String trackResponse = post(
		"http://www." + dawnDotCom() + "/ajax/gw-light.php?api_version=1.0&api_token=" + apiToken + "&input=3",
		"[{\"method\":\"song.getListData\",\"params\":{\"sng_ids\":[" + songID + "]}}]",
		asList(browserHeaders));
	if (trackResponse == null
		|| trackResponse.equals("[{\"error\":{\"REQUEST_ERROR\":\"Wrong parameters\"},\"results\":{}}]")) {
	    return null;
	} else {
	    logger.log(INFO, trackResponse);
	    Response response = new Gson().fromJson(trackResponse, Response.class);
	    Results result = response.getResults();
	    if (result.getCount() == 0) {
		return null;
	    } else {
		return result.getTracks().get(0).toSong();
	    }
	}
    }

    /**
     *
     * @param pl
     * @return
     * @throws InterruptedException
     */
    public Map<String, Song> getPlaylist(String pl) throws InterruptedException {
	Map<String, Song> songList = new HashMap<>();
	String searchResponse = get(pl, asList(browserHeaders));
	if (searchResponse != null) {
	    logger.log(INFO, searchResponse);
	    SearchResponse response = new Gson().fromJson(searchResponse, SearchResponse.class);
	    com.didawn.json.Error error = response.getError();
	    if (error != null) {
		if (error.getCode() == 4) {
		    sleep(WAIT_TIME);
		    return this.getPlaylist(pl);
		} else {
		    return songList;
		}
	    }

	    List<Data> tracks = response.getTracks().getData();

	    if (tracks.isEmpty()) {
		return songList;
	    }

	    for (Data track : tracks) {
		String id = track.getId();
		String title = track.getTitle();
		String album = track.getAlbum().getTitle();
		ArtistList artists = new ArtistList();
		artists.add(track.getArtist().getName());
		long duration = track.getDuration();
		String altSID = track.getAlternative() == null ? "" : track.getAlternative().getID();
		Song s = new Song(id, title, artists, album, duration, 0L, "");
		s.setAlbumArtist(artists.get(0));
		s.setAlternativeID(altSID);

		String coverXL = track.getAlbum().getCoverXL();
		if (coverXL != null) {
		    s.setCoverURL(coverXL);
		} else {
		    String coverBig = track.getAlbum().getCoverBig();
		    if (coverBig != null) {
			s.setCoverURL(coverBig);
		    }
		}
		songList.put(id, s);
	    }

	    if ((songList = this.getExtraInfo(songList)).size() == 400) {
		songList.putAll(this.getPlaylistTracks(pl, 400));
	    }
	}

	return songList;
    }

    /**
     *
     * @param artistURL
     * @return
     * @throws InterruptedException
     */
    public Map<String, Song> getArtist(String artistURL) throws InterruptedException {
	Map<String, Song> songList = new HashMap<>();
	String searchResponse = get(artistURL, asList(browserHeaders));
	if (searchResponse != null) {
	    logger.log(INFO, searchResponse);
	    SearchResponse response = new Gson().fromJson(searchResponse, SearchResponse.class);
	    com.didawn.json.Error error = response.getError();
	    if (error != null) {
		if (error.getCode() == 4) {
		    sleep(WAIT_TIME);
		    return this.getArtist(artistURL);
		}

		return songList;
	    }

	    List<Data> albums = response.getData();
	    if (albums.isEmpty()) {
		return songList;
	    }

	    for (Data album : albums) {
		String link = album.getLink();
		if (!link.isEmpty()) {
		    songList.putAll(this.getAlbum(link.replace("www.", "api.")));
		}
	    }

	    songList = this.getExtraInfo(songList);
	    String next = response.getNext();
	    if (!next.isEmpty()) {
		songList.putAll(this.getArtist(next));
	    }
	}

	return songList;
    }

    private Map<String, Song> getPlaylistTracks(String pl, int index) throws InterruptedException {
	Map<String, Song> songList = new HashMap<>();
	String searchResponse = get(pl + "/tracks?index=" + index, asList(browserHeaders));
	if (searchResponse != null) {
	    logger.log(INFO, searchResponse);
	    SearchResponse response = new Gson().fromJson(searchResponse, SearchResponse.class);
	    com.didawn.json.Error error = response.getError();
	    if (error != null) {
		if (error.getCode() == 4) {
		    sleep(WAIT_TIME);
		    return this.getPlaylistTracks(pl, index);
		} else {
		    return songList;
		}
	    }

	    List<Data> tracks = response.getData();
	    if (tracks.isEmpty()) {
		return songList;
	    }

	    for (Data track : tracks) {
		String id = track.getId();
		String title = track.getTitle();
		String album = track.getAlbum().getTitle();
		ArtistList artists = new ArtistList();
		artists.add(track.getArtist().getName());
		long duration = track.getDuration();
		String altSID = track.getAlternative() == null ? "" : track.getAlternative().getID();

		Song s = new Song(id, title, artists, album, duration, 0L, "");
		s.setAlternativeID(altSID);
		s.setAlbumArtist(artists.get(0));
		String coverXL = track.getAlbum().getCoverXL();
		if (coverXL != null) {
		    s.setCoverURL(coverXL);
		} else {
		    String coverBig = track.getAlbum().getCoverBig();
		    if (coverBig != null) {
			s.setCoverURL(coverBig);
		    }
		}

		songList.put(id, s);
	    }

	    songList = this.getExtraInfo(songList);
	    if (response.getNext() != null) {
		songList.putAll(this.getPlaylistTracks(pl, index + 25));
	    }
	}

	return songList;
    }

    private Map<String, Song> getExtraInfo(Map<String, Song> songList) {
	StringBuilder sb = new StringBuilder("[{\"method\":\"song.getListData\",\"params\":{\"sng_ids\":[");

	boolean first = true;
	for (Song song : songList.values()) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",");
	    }
	    sb.append(song.getId());
	}

	sb.append("]}}]");
	String ids = sb.toString();
	String trackResponse = post("http://www." + dawnDotCom() + "/ajax/gw-light.php?api_version=1.0&api_token="
		+ apiToken + "&input=3&cid=" + randomAlphanumeric(18).toLowerCase(), ids, asList(browserHeaders));

	logger.log(INFO, trackResponse);
	Response[] response = new Gson().fromJson(trackResponse, Response[].class);
	List<Data> tracks = response[0].getResults().getData();

	tracks.forEach(track -> {
	    String tmpID = track.getSongID();
	    Song s2 = songList.get(tmpID);
	    Song tmp;
	    if (tmpID.startsWith("-")) {
		tmp = track.toSong();
	    } else {
		tmp = track.toSong();
		s2.setArtists(tmp.getArtists());
	    }
	    s2.setTrackNum(tmp.getTrackNumber());
	    s2.setYear(tmp.getYear());
	    s2.setDiskNumber(tmp.getDiskNumber());
	    s2.setIsrc(tmp.getIsrc());
	    s2.setComposer(tmp.getComposer());
	    s2.setBpm(tmp.getBpm());
	    s2.setDownloadURL(tmp.getDownloadURL());
	});

	return songList;
    }

    /**
     *
     * @param albumID
     * @return
     * @throws InterruptedException
     */
    public Map<String, Song> getAlbum(String albumID) throws InterruptedException {
	Map<String, Song> songList = new HashMap<>();
	String searchResponse = get(albumID, asList(browserHeaders));

	try {
	    if (searchResponse != null) {
		logger.log(INFO, searchResponse);

		SearchResponse response = new Gson().fromJson(searchResponse, SearchResponse.class);
		com.didawn.json.Error error = response.getError();
		if (error != null) {
		    if (error.getCode() == 4) {
			sleep(WAIT_TIME);
			return this.getAlbum(albumID);
		    } else {
			return songList;
		    }
		}

		String album = response.getTitle();
		String genre = "";
		List<Data> data = response.getGenres().getData();
		if (data != null && !data.isEmpty()) {
		    genre = data.get(0).getName();
		}
		String label = response.getLabel();
		String trackCount = response.getNbTracks();

		String albumArtist = response.getArtist().getName();

		List<Data> tracks = response.getTracks().getData();
		if (tracks.isEmpty()) {
		    return songList;
		}
		for (Data track : tracks) {
		    String id = track.getId();
		    String title = track.getTitle();
		    ArtistList artists = new ArtistList();
		    artists.add(track.getArtist().getName());
		    long duration = track.getDuration();
		    Alternative alternative = track.getAlternative();

		    Song s = new Song(id, title, artists, album, duration, 0L, "");

		    if (alternative != null) {
			s.setAlternativeID(alternative.getID());
		    }

		    s.setGenre(genre);
		    s.setLabel(label);
		    s.setAlbumTrackCount(trackCount);
		    s.setAlbumArtist(albumArtist);
		    String coverXL = response.getCoverXL();
		    String coverBig = response.getCoverBig();
		    if (coverXL != null) {
			s.setCoverURL(coverXL);
		    } else if (coverBig != null) {
			s.setCoverURL(coverBig);
		    }

		    songList.put(id, s);
		}
	    }
	} catch (NullPointerException var22) {
	}

	return this.getExtraInfo(songList);
    }

    /**
     *
     * @param song
     * @param out
     * @param downloadListener
     * @return
     */
    public boolean downloadHelper(Song song, OutputStream out, DownloadProgressListener downloadListener) {
	if (download(song, out, downloadListener)) {
	    return true;
	} else {
	    return (song.getAlternativeID() != null || !song.getAlternativeID().equals(song.getId()))
		    && (song = this.getSong(song.getAlternativeID())) != null ? download(song, out, downloadListener)
			    : false;
	}
    }

    /**
     *
     * @param query
     * @return
     * @throws InterruptedException
     */
    public Map<String, Song> search(String query) throws InterruptedException {
	Map<String, Song> songList = this.searchAlbum(query);
	songList.putAll(this.searchTrack(query));
	return songList;
    }

    private Map<String, Song> searchTrack(String query) throws InterruptedException {
	Map<String, Song> songList = new HashMap<>();

	try {
	    String url = String.format(TRACK_URL, encode(query, UTF8));
	    String searchResponse = get(url, asList(browserHeaders));
	    if (searchResponse != null) {
		logger.log(INFO, searchResponse);
		SearchResponse sr = new Gson().fromJson(searchResponse, SearchResponse.class);
		com.didawn.json.Error error = sr.getError();
		if (error != null) {
		    if (error.getCode() == 4) {
			sleep(WAIT_TIME);
			return this.searchTrack(query);
		    } else {
			return songList;
		    }
		}

		List<Data> tracks = sr.getData();
		if (tracks.isEmpty()) {
		    return songList;
		}

		tracks.forEach(track -> {
		    String id = track.getId();
		    String title = track.getTitle();
		    String album = track.getAlbum().getTitle();
		    ArtistList artists = new ArtistList();
		    artists.add(track.getArtist().getName());
		    long duration = track.getDuration();
		    Song s = new Song(id, title, artists, album, duration, 0L, "");
		    String coverURL;
		    Alternative alternative = track.getAlternative();
		    if (alternative != null) {
			coverURL = alternative.getID();
			s.setAlternativeID(coverURL);
		    }
		    s.setAlbumArtist(artists.get(0));
		    com.didawn.json.Album albumJson = track.getAlbum();
		    coverURL = albumJson.getCoverBig();
		    s.setCoverURL(coverURL);
		    songList.put(id, s);
		});
	    }
	} catch (UnsupportedEncodingException e) {
	}

	return this.getExtraInfo(songList);
    }

    private Map<String, Song> searchAlbum(String query) throws InterruptedException {
	Map<String, Song> songList = new HashMap<>();

	try {
	    String url = BASE_API_URL + "search/album?q=" + encode(query, UTF8);
	    String searchResponse = get(url, asList(browserHeaders));
	    if (searchResponse != null) {
		logger.log(INFO, searchResponse);
		SearchResponse sr = new Gson().fromJson(searchResponse, SearchResponse.class);
		Error error = sr.getError();
		if (error != null) {
		    if (error.getCode() == 4) {
			sleep(WAIT_TIME);
			return this.searchAlbum(query);
		    } else {
			return songList;
		    }
		}

		List<Data> tracks = sr.getData();
		if (tracks.isEmpty()) {
		    return songList;
		}

		for (Data data : tracks) {
		    songList.putAll(this.getAlbum(BASE_API_URL + "album/" + data.getId()));
		}
	    }
	} catch (UnsupportedEncodingException e) {
	    logger.log(INFO, "UTF-8 not supported", e);
	}

	return songList;
    }

    /**
     *
     * @param searchTerm
     * @return
     * @throws InterruptedException
     */
    public List<Song> query(String searchTerm) throws InterruptedException {
	Map<String, Song> trackList = new HashMap<>();
	String re1 = ".*?";
	String re2 = "(www|api)";
	String re3 = "(\\." + dawn() + "\\.com/)";
	String re5 = "(playlist)";
	String re6 = "(/)";
	String re7 = "(\\d+)";
	Pattern p = compile(re1 + re2 + re3 + re5 + re6 + re7, 34);
	Matcher m = p.matcher(searchTerm);
	if (m.find()) {
	    trackList = this.getPlaylist(BASE_API_URL + "playlist/" + m.group(5));
	} else {
	    re5 = "(album)";
	    p = compile(re1 + re2 + re3 + re5 + re6 + re7, 34);
	    m = p.matcher(searchTerm);
	    if (m.find()) {
		trackList = this.getAlbum(BASE_API_URL + "album/" + m.group(5));
	    } else {
		re5 = "(artist)";
		p = compile(re1 + re2 + re3 + re5 + re6 + re7, 34);
		m = p.matcher(searchTerm);
		if (m.find()) {
		    trackList = this.getArtist(BASE_API_URL + "artist/" + m.group(5) + "/albums");
		} else {
		    re5 = "(track)";
		    re7 = "((-?)\\d+)";
		    p = compile(re1 + re2 + re3 + re5 + re6 + re7, 34);
		    m = p.matcher(searchTerm);
		    if (m.find()) {
			Song s = this.getSong(m.group(5));
			if (s != null) {
			    trackList.put(s.getId(), s);
			}
		    } else {
			trackList = this.search(searchTerm);
		    }
		}
	    }
	}

	List<Song> sortedTrackList = new ArrayList<>(trackList.values());
	sort(sortedTrackList, (Song s1, Song s2) -> {
	    if (s1.getAlbum().equals(s2.getAlbum())) {
		return s1.getDiskNumber() == s2.getDiskNumber() ? s1.getTrackNumber().compareTo(s2.getTrackNumber())
			: s1.getDiskNumber() - s2.getDiskNumber();
	    } else {
		return s1.getAlbum().compareTo(s2.getAlbum());
	    }
	});
	return sortedTrackList;
    }
}
