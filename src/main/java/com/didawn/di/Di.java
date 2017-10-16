package com.didawn.di;

import com.didawn.Constants;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.didawn.models.ArtistList;
import com.didawn.models.Song;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class Di {

    private static final Logger log = Logger.getLogger(Di.class.getName());
    public static Header[] browserHeaders;
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    private static HttpClient httpClient;
    private static String oldToken;
    private static String apiToken;

    public Di() {
        ArrayList browserHeadersList = new ArrayList();
        browserHeadersList.add(new BasicHeader("User-Agent", userAgent));
        browserHeadersList.add(new BasicHeader("Content-Language", "en-US"));
        browserHeadersList.add(new BasicHeader("Cache-Control", "max-age=0"));
        browserHeadersList.add(new BasicHeader("Accept", "*/*"));
        browserHeadersList.add(new BasicHeader("Accept-Charset", "utf-8,ISO-8859-1;q=0.7,*;q=0.3"));
        browserHeadersList.add(new BasicHeader("Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4"));
        browserHeadersList.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"));
        browserHeaders = new Header[browserHeadersList.size()];
        browserHeaders = (Header[]) browserHeadersList.toArray(browserHeaders);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClient = httpClientBuilder.build();
        get("http://www." + Constants.dawnDotCom() + "/", Arrays.asList(browserHeaders));
    }

    public static String get(String url, List headers) {
        String responseContent = null;
        HttpEntity httpEntity = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", userAgent);
            if (headers != null) {
                Header[] headersArr = new Header[headers.size()];
                httpGet.setHeaders((Header[]) headers.toArray(headersArr));
            }

            HttpResponse httpResponse = httpClient.execute(httpGet);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            httpEntity = httpResponse.getEntity();
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException("status code: " + statusLine.getStatusCode());
            }

            httpEntity.writeTo(baos);
            responseContent = baos.toString("UTF-8");
        } catch (Exception var19) {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException var18) {
                var18.printStackTrace();
            }
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException var17) {
                var17.printStackTrace();
            }

        }

        String newToken = StringUtils.substringBetween(responseContent, "var checkForm", ";");
        if (newToken != null) {
            while (true) {
                if (!newToken.startsWith("\"") && !newToken.startsWith("'") && !newToken.startsWith("=") && !newToken.startsWith(" ")) {
                    while (newToken.endsWith("\"") || newToken.endsWith("'") || newToken.endsWith("=") || newToken.endsWith(" ")) {
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

    public static String post(String url, String data, List headers) {
        String responseContent = null;
        HttpEntity httpEntity = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("User-Agent", userAgent);
            if (headers != null) {
                Header[] headersArr = new Header[headers.size()];
                httpPost.setHeaders((Header[]) headers.toArray(headersArr));
            }

            httpPost.setEntity(new StringEntity(data));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            httpEntity = httpResponse.getEntity();
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode != 200) {
                System.out.println(statusCode);
                System.out.println(statusLine);
                throw new RuntimeException("status code: " + statusLine.getStatusCode());
            }

            httpEntity.writeTo(baos);
            responseContent = baos.toString("UTF-8");
        } catch (IOException var18) {
            var18.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException var17) {
                ;
            }

        }

        if (responseContent != null && responseContent.contains("VALID_TOKEN_REQUIRED")) {
            get("http://www." + Constants.dawnDotCom() + "/", Arrays.asList(browserHeaders));
            return post(url.replace(oldToken, apiToken), data, headers);
        } else {
            return responseContent;
        }
    }

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
                int chunkSize = 2048;
                int intervalChunk = 3;
                InputStream inputStream = httpEntity.getContent();
                long contentLength = httpEntity.getContentLength();
                byte[] chunk = new byte[chunkSize];
                int chunks = (int) Math.ceil((double) (contentLength / (long) chunkSize));
                int readTotal = 0;
                int i = 0;
                byte[] blowfishKey = Crypter.getBlowfishKey(Long.parseLong(track.getId()));

                int read;
                while ((read = inputStream.read(chunk, 0, chunkSize)) != -1) {
                    if (read < chunkSize && i < chunks - 1) {
                        ByteBuffer buffer = ByteBuffer.allocate(chunkSize);
                        buffer.put(ByteBuffer.wrap(chunk, 0, read));

                        while (buffer.hasRemaining()) {
                            byte[] temp = new byte[buffer.remaining()];
                            int tempRead = inputStream.read(temp, 0, buffer.remaining());
                            read += tempRead;
                            buffer.put(temp, 0, tempRead);
                        }

                        chunk = buffer.array();
                    }

                    if (i % intervalChunk == 0 && read == chunkSize) {
                        chunk = Crypter.decryptBlowfish(chunk, blowfishKey);
                    }

                    outputStream.write(chunk, 0, read);
                    ++i;
                    readTotal += read;
                    downloadListener.onProgress(track, (long) readTotal, contentLength);
                }

                outputStream.close();
                Utils.closeQuietly(outputStream, track.getTitle());
                EntityUtils.consume(httpEntity);
                return true;
            }
        } catch (IOException var22) {
            var22.printStackTrace();
            return false;
        }
    }

    private static String getString(JsonValue badString) {
        try {
            return badString.asString();
        } catch (Exception var2) {
            return badString.toString().replace("\"\"", "\"");
        }
    }

    public Song getSong(String songID) {
        String trackResponse = post("http://www." + Constants.dawnDotCom() + "/ajax/gw-light.php?api_version=1.0&api_token=" + apiToken + "&input=3", "[{\"method\":\"song.getListData\",\"params\":{\"sng_ids\":[" + songID + "]}}]", Arrays.asList(browserHeaders));
        if (trackResponse.equals("[{\"error\":{\"REQUEST_ERROR\":\"Wrong parameters\"},\"results\":{}}]")) {
            return null;
        } else {
            JsonObject results = Json.parse(trackResponse).asArray().get(0).asObject().get("results").asObject();
            if (results.getInt("count", 0) == 0) {
                return null;
            } else {
                JsonObject trackJson = results.get("data").asArray().get(0).asObject();
                return songID.startsWith("-") ? this.parseUserTrack(trackJson) : this.parseTrack(trackJson);
            }
        }
    }

    private Song parseTrack(JsonObject trackJson) {
        String songID = getString(trackJson.get("SNG_ID"));
        String puid = getString(trackJson.get("MD5_ORIGIN"));
        int format = Long.valueOf(getString(trackJson.get("FILESIZE_MP3_320"))) > 0L ? 3 : (Long.valueOf(getString(trackJson.get("FILESIZE_MP3_256"))) > 0L ? 5 : 1);
        int mediaVersion = Integer.valueOf(getString(trackJson.get("MEDIA_VERSION")));
        String mp3Url = Crypter.getDownloadURL(puid, format, songID, mediaVersion);
        String title = trackJson.get("SNG_TITLE").asString();
        String version = trackJson.getString("VERSION", "");
        if (!version.equals("")) {
            title = title + " " + version;
        }

        String album = trackJson.get("ALB_TITLE").asString();
        ArtistList artists = new ArtistList();
        artists.add(trackJson.get("ART_NAME").asString());
        JsonArray js_artists = trackJson.get("ARTISTS").asArray();

        int diskNumber;
        String tmpArtist;
        for (int i = 0; i < js_artists.size(); ++i) {
            String artist = getString(js_artists.get(i).asObject().get("ART_NAME"));
            if (artist.contains(" feat. ")) {
                String[] var15 = artist.split(" feat. ");
                int var16 = var15.length;

                for (diskNumber = 0; diskNumber < var16; ++diskNumber) {
                    tmpArtist = var15[diskNumber];
                    if (!artists.contains(tmpArtist)) {
                        artists.add(tmpArtist);
                    }
                }
            } else if (!artists.contains(artist)) {
                artists.add(artist);
            }
        }

        String year = trackJson.getString("DIGITAL_RELEASE_DATE", "");
        long duration = Long.parseLong(trackJson.getString("DURATION", "0"));
        long trackNumber = Long.parseLong(trackJson.getString("TRACK_NUMBER", "0"));
        diskNumber = Integer.parseInt(trackJson.getString("DISK_NUMBER", "0"));
        tmpArtist = getString(trackJson.get("ISRC"));
        String composer = getString(trackJson.get("COMPOSER"));
        String bpm = getString(trackJson.get("BPM"));
        Song s = new Song(songID, title, artists, album, duration, trackNumber, year);
        s.setDownloadURL(mp3Url);
        s.setDiskNumber(diskNumber);
        s.setAlbumArtist((String) artists.get(0));
        s.setISRC(tmpArtist);
        s.setComposer(composer);
        s.setBPM(bpm);
        String coverURL = String.format("http://cdn-images." + Constants.dawnDotCom() + "/images/cover/%s/500x500-000000-80-0-0.jpg", trackJson.getString("ALB_PICTURE", ""));
        s.setCoverURL(coverURL);
        return s;
    }

    private Song parseUserTrack(JsonObject trackJson) {
        String songID = getString(trackJson.get("SNG_ID"));
        String puid = getString(trackJson.get("MD5_ORIGIN"));
        int format = 0;
        int mediaVersion = Integer.valueOf(getString(trackJson.get("MEDIA_VERSION")));
        String mp3Url = Crypter.getDownloadURL(puid, format, songID.substring(1), mediaVersion);
        String title = trackJson.get("SNG_TITLE").asString();
        String album = trackJson.get("ALB_TITLE").asString();
        ArtistList artists = new ArtistList();
        artists.add(trackJson.get("ART_NAME").asString());
        String year = "";
        long duration = Long.parseLong(trackJson.getString("DURATION", "0"));
        long trackNumber = Long.parseLong(trackJson.getString("TRACK_NUMBER", "0"));
        int diskNumber = Integer.parseInt(trackJson.getString("DISK_NUMBER", "0"));
        Song s = new Song(songID, title, artists, album, duration, trackNumber, year);
        s.setDownloadURL(mp3Url);
        s.setDiskNumber(diskNumber);
        s.setAlbumArtist((String) artists.get(0));
        String coverURL = String.format("http://cdn-images." + Constants.dawnDotCom() + "/images/cover/%s/500x500-000000-80-0-0.jpg", trackJson.getString("ALB_PICTURE", ""));
        s.setCoverURL(coverURL);
        return s;
    }

    public HashMap getPlaylist(String pl) {
        HashMap songList = new HashMap();
        String searchResponse = get(pl, Arrays.asList(browserHeaders));
        if (searchResponse != null) {
            JsonObject result = Json.parse(searchResponse).asObject();
            JsonValue error = result.get("error");
            if (error != null) {
                if (error.asObject().getInt("code", -1) == 4) {
                    try {
                        Thread.sleep(5000L);
                        return this.getPlaylist(pl);
                    } catch (InterruptedException var18) {
                        var18.printStackTrace();
                    }
                }

                return songList;
            }

            JsonArray tracks = result.get("tracks").asObject().get("data").asArray();
            if (tracks.isEmpty()) {
                return songList;
            }

            for (int i = 0; i < tracks.size(); ++i) {
                JsonObject track = tracks.get(i).asObject();
                String id = track.get("id").toString();
                String title = track.get("title").asString();
                String album = track.get("album").asObject().get("title").asString();
                ArtistList artists = new ArtistList();
                artists.add(track.get("artist").asObject().get("name").asString());
                long duration = track.getLong("duration", 0L);
                String altSID = "";
                if (track.get("alternative") != null) {
                    altSID = getString(track.get("alternative").asObject().get("id"));
                }

                Song s = new Song(id, title, artists, album, duration, 0L, "");
                s.setAlbumArtist((String) artists.get(0));
                s.setAlternativeID(altSID);
                String coverURL;
                if (!track.get("album").asObject().get("cover_xl").isNull()) {
                    coverURL = track.get("album").asObject().getString("cover_xl", "");
                    s.setCoverURL(coverURL);
                } else if (!track.get("album").asObject().get("cover_big").isNull()) {
                    coverURL = track.get("album").asObject().getString("cover_big", "");
                    s.setCoverURL(coverURL);
                }

                songList.put(id, s);
            }

            if ((songList = this.getExtraInfo(songList)).size() == 400) {
                songList.putAll(this.getPlaylistTracks(pl, 400));
            }
        }

        return songList;
    }

    public HashMap getArtist(String artistURL) {
        HashMap songList = new HashMap();
        String searchResponse = get(artistURL, Arrays.asList(browserHeaders));
        if (searchResponse != null) {
            JsonObject result = Json.parse(searchResponse).asObject();
            JsonValue error = result.get("error");
            if (error != null) {
                if (error.asObject().getInt("code", -1) == 4) {
                    try {
                        Thread.sleep(5000L);
                        return this.getArtist(artistURL);
                    } catch (InterruptedException var10) {
                        var10.printStackTrace();
                    }
                }

                return songList;
            }

            JsonArray albums = result.get("data").asArray();
            if (albums.isEmpty()) {
                return songList;
            }

            for (int i = 0; i < albums.size(); ++i) {
                JsonObject album = albums.get(i).asObject();
                String link = album.getString("link", "");
                if (!link.equals("")) {
                    songList.putAll(this.getAlbum(link.replace("www.", "api.")));
                }
            }

            songList = this.getExtraInfo(songList);
            String next = result.getString("next", "");
            if (!next.equals("")) {
                songList.putAll(this.getArtist(next));
            }
        }

        return songList;
    }

    private Map getPlaylistTracks(String pl, int index) {
        HashMap songList = new HashMap();
        String searchResponse = get(pl + "/tracks?index=" + index, Arrays.asList(browserHeaders));
        if (searchResponse != null) {
            JsonObject result = Json.parse(searchResponse).asObject();
            JsonValue error = result.get("error");
            if (error != null) {
                if (error.asObject().getInt("code", -1) == 4) {
                    try {
                        Thread.sleep(5000L);
                        return this.getPlaylistTracks(pl, index);
                    } catch (InterruptedException var19) {
                        var19.printStackTrace();
                    }
                }

                return songList;
            }

            JsonArray tracks = result.get("data").asArray();
            if (tracks.isEmpty()) {
                return songList;
            }

            for (int i = 0; i < tracks.size(); ++i) {
                JsonObject track = tracks.get(i).asObject();
                String id = track.get("id").toString();
                String title = track.get("title").asString();
                String album = track.get("album").asObject().get("title").asString();
                ArtistList artists = new ArtistList();
                artists.add(track.get("artist").asObject().get("name").asString());
                long duration = track.getLong("duration", 0L);
                String altSID = "";
                if (track.get("alternative") != null) {
                    altSID = getString(track.get("alternative").asObject().get("id"));
                }

                Song s = new Song(id, title, artists, album, duration, 0L, "");
                s.setAlternativeID(altSID);
                s.setAlbumArtist((String) artists.get(0));
                String coverURL;
                if (!track.get("album").asObject().get("cover_xl").isNull()) {
                    coverURL = track.get("album").asObject().getString("cover_xl", "");
                    s.setCoverURL(coverURL);
                } else if (!track.get("album").asObject().get("cover_big").isNull()) {
                    coverURL = track.get("album").asObject().getString("cover_big", "");
                    s.setCoverURL(coverURL);
                }

                songList.put(id, s);
            }

            songList = this.getExtraInfo(songList);
            if (result.get("next") != null) {
                songList.putAll(this.getPlaylistTracks(pl, index + 25));
            }
        }

        return songList;
    }

    private HashMap getExtraInfo(HashMap songList) {
        String IDs = "[{\"method\":\"song.getListData\",\"params\":{\"sng_ids\":[";

        Song s;
        for (Iterator var3 = songList.values().iterator(); var3.hasNext(); IDs = IDs + s.getId() + ",") {
            s = (Song) var3.next();
        }

        IDs = IDs + "]}}]";
        IDs = IDs.replace(",]", "]");
        String trackResponse = post("http://www." + Constants.dawnDotCom() + "/ajax/gw-light.php?api_version=1.0&api_token=" + apiToken + "&input=3&cid=" + RandomStringUtils.randomAlphanumeric(18).toLowerCase(), IDs, Arrays.asList(browserHeaders));
        JsonArray tracks = Json.parse(trackResponse).asArray().get(0).asObject().get("results").asObject().get("data").asArray();
        Iterator var5 = tracks.iterator();

        while (var5.hasNext()) {
            JsonValue trackJsonTmp = (JsonValue) var5.next();
            JsonObject trackJson = trackJsonTmp.asObject();
            String tmpID = getString(trackJson.get("SNG_ID"));
            Song s2 = (Song) songList.get(tmpID);
            Song tmp;
            if (tmpID.startsWith("-")) {
                tmp = this.parseUserTrack(trackJson);
            } else {
                tmp = this.parseTrack(trackJson);
                s2.setArtists(tmp.getArtists());
            }

            s2.setTrackNum(tmp.getTrackNumber());
            s2.setYear(tmp.getYear());
            s2.setDiskNumber(tmp.getDiskNumber());
            s2.setISRC(tmp.getISRC());
            s2.setComposer(tmp.getComposer());
            s2.setBPM(tmp.getBPM());
            s2.setDownloadURL(tmp.getDownloadURL());
        }

        return songList;
    }

    public HashMap getAlbum(String albumID) {
        HashMap songList = new HashMap();
        String searchResponse = get(albumID, Arrays.asList(browserHeaders));

        try {
            if (searchResponse != null) {
                JsonObject result = Json.parse(searchResponse).asObject();
                JsonValue error = result.get("error");
                if (error != null) {
                    if (error.asObject().getInt("code", -1) == 4) {
                        try {
                            Thread.sleep(5000L);
                            return this.getAlbum(albumID);
                        } catch (InterruptedException var21) {
                            var21.printStackTrace();
                        }
                    }

                    return songList;
                }

                String album = result.get("title").asString();
                String genre = "";
                if (result.get("genres").asObject().get("data").asArray().size() > 0) {
                    genre = getString(result.get("genres").asObject().get("data").asArray().get(0).asObject().get("name"));
                }

                String label = result.getString("label", "");
                String trackCount = getString(result.get("nb_tracks"));
                String albumArtist = result.get("artist").asObject().get("name").asString();
                JsonArray tracks = result.get("tracks").asObject().get("data").asArray();
                if (tracks.isEmpty()) {
                    return songList;
                }

                for (int i = 0; i < tracks.size(); ++i) {
                    JsonObject track = tracks.get(i).asObject();
                    String id = track.get("id").toString();
                    String title = track.get("title").asString();
                    ArtistList artists = new ArtistList();
                    artists.add(track.get("artist").asObject().get("name").asString());
                    long duration = track.getLong("duration", 0L);
                    Song s = new Song(id, title, artists, album, duration, 0L, "");
                    String coverURL;
                    if (track.get("alternative") != null) {
                        coverURL = getString(track.get("alternative").asObject().get("id"));
                        s.setAlternativeID(coverURL);
                    }

                    s.setGenre(genre);
                    s.setLabel(label);
                    s.setAlbumTrackCount(trackCount);
                    s.setAlbumArtist(albumArtist);
                    if (!result.get("cover_xl").isNull()) {
                        coverURL = result.getString("cover_xl", "");
                        s.setCoverURL(coverURL);
                    } else if (!result.get("cover_big").isNull()) {
                        coverURL = result.getString("cover_big", "");
                        s.setCoverURL(coverURL);
                    }

                    songList.put(id, s);
                }
            }
        } catch (NullPointerException var22) {
            var22.printStackTrace();
        }

        return this.getExtraInfo(songList);
    }

    public boolean downloadHelper(Song s, OutputStream out, DownloadProgressListener downloadListener) {
        if (download(s, out, downloadListener)) {
            return true;
        } else {
            return (s.getAlternativeID() != null || !s.getAlternativeID().equals(s.getId())) && (s = this.getSong(s.getAlternativeID())) != null ? download(s, out, downloadListener) : false;
        }
    }

    public HashMap search(String query) {
        HashMap songList = this.searchAlbum(query);
        songList.putAll(this.searchTrack(query));
        return songList;
    }

    private Map searchTrack(String query) {
        HashMap songList = new HashMap();

        try {
            String searchResponse = get("http://api." + Constants.dawnDotCom() + "/search/track?q=" + URLEncoder.encode(query, "UTF-8"), Arrays.asList(browserHeaders));
            if (searchResponse != null) {
                JsonObject result = Json.parse(searchResponse).asObject();
                JsonValue error = result.get("error");
                if (error != null) {
                    if (error.asObject().getInt("code", -1) == 4) {
                        try {
                            Thread.sleep(5000L);
                            return this.searchTrack(query);
                        } catch (InterruptedException var17) {
                            var17.printStackTrace();
                        }
                    }

                    return songList;
                }

                JsonArray tracks = result.get("data").asArray();
                if (tracks.isEmpty()) {
                    return songList;
                }

                for (int i = 0; i < tracks.size(); ++i) {
                    JsonObject track = tracks.get(i).asObject();
                    String id = track.get("id").toString();
                    String title = track.get("title").asString();
                    String album = track.get("album").asObject().get("title").asString();
                    ArtistList artists = new ArtistList();
                    artists.add(track.get("artist").asObject().get("name").asString());
                    long duration = track.getLong("duration", 0L);
                    Song s = new Song(id, title, artists, album, duration, 0L, "");
                    String coverURL;
                    if (track.get("alternative") != null) {
                        coverURL = getString(track.get("alternative").asObject().get("id"));
                        s.setAlternativeID(coverURL);
                    }

                    s.setAlbumArtist((String) artists.get(0));
                    coverURL = track.get("album").asObject().getString("cover_big", "");
                    s.setCoverURL(coverURL);
                    songList.put(id, s);
                }
            }
        } catch (UnsupportedEncodingException var18) {
            var18.printStackTrace();
        }

        return this.getExtraInfo(songList);
    }

    private HashMap searchAlbum(String query) {
        HashMap songList = new HashMap();

        try {
            String searchResponse = get("http://api." + Constants.dawnDotCom() + "/search/album?q=" + URLEncoder.encode(query, "UTF-8"), Arrays.asList(browserHeaders));
            if (searchResponse != null) {
                JsonObject result = Json.parse(searchResponse).asObject();
                JsonValue error = result.get("error");
                if (error != null) {
                    if (error.asObject().getInt("code", -1) == 4) {
                        try {
                            Thread.sleep(5000L);
                            return this.searchAlbum(query);
                        } catch (InterruptedException var10) {
                            var10.printStackTrace();
                        }
                    }

                    return songList;
                }

                JsonArray tracks = result.get("data").asArray();
                if (tracks.isEmpty()) {
                    return songList;
                }

                for (int i = 0; i < tracks.size(); ++i) {
                    JsonObject track = tracks.get(i).asObject();
                    String id = track.get("id").toString();
                    songList.putAll(this.getAlbum("http://api." + Constants.dawnDotCom() + "/album/" + id));
                }
            }
        } catch (UnsupportedEncodingException var11) {
            var11.printStackTrace();
        }

        return songList;
    }

    public List query(String searchTerm) {
        HashMap trackList = new HashMap();
        String re1 = ".*?";
        String re2 = "(www|api)";
        String re3 = "(\\." + Constants.dawn() + "\\.com/)";
        String re5 = "(playlist)";
        String re6 = "(/)";
        String re7 = "(\\d+)";
        Pattern p = Pattern.compile(re1 + re2 + re3 + re5 + re6 + re7, 34);
        Matcher m = p.matcher(searchTerm);
        if (m.find()) {
            trackList = this.getPlaylist("http://api." + Constants.dawnDotCom() + "/playlist/" + m.group(5));
        } else {
            re5 = "(album)";
            p = Pattern.compile(re1 + re2 + re3 + re5 + re6 + re7, 34);
            m = p.matcher(searchTerm);
            if (m.find()) {
                trackList = this.getAlbum("http://api." + Constants.dawnDotCom() + "/album/" + m.group(5));
            } else {
                re5 = "(artist)";
                p = Pattern.compile(re1 + re2 + re3 + re5 + re6 + re7, 34);
                m = p.matcher(searchTerm);
                if (m.find()) {
                    trackList = this.getArtist("http://api." + Constants.dawnDotCom() + "/artist/" + m.group(5) + "/albums");
                } else {
                    re5 = "(track)";
                    re7 = "((-?)\\d+)";
                    p = Pattern.compile(re1 + re2 + re3 + re5 + re6 + re7, 34);
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

        ArrayList sortedTrackList = new ArrayList(trackList.values());
        Collections.sort(sortedTrackList, new Comparator<Song>() {
            public int compare(Song s1, Song s2) {
                if (s1.getAlbum().equals(s2.getAlbum())) {
                    return s1.getDiskNumber() == s2.getDiskNumber() ? s1.getTrackNumber().compareTo(s2.getTrackNumber()) : s1.getDiskNumber() - s2.getDiskNumber();
                } else {
                    return s1.getAlbum().compareTo(s2.getAlbum());
                }
            }
        });
        return sortedTrackList;
    }
}
