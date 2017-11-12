package com.didawn.di;

import static java.io.File.createTempFile;
import static java.lang.String.valueOf;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.apache.commons.io.FileUtils.copyURLToFile;
import static org.jaudiotagger.audio.AudioFileIO.read;
import static org.jaudiotagger.tag.FieldKey.ALBUM;
import static org.jaudiotagger.tag.FieldKey.ALBUM_ARTIST;
import static org.jaudiotagger.tag.FieldKey.ARTIST;
import static org.jaudiotagger.tag.FieldKey.BPM;
import static org.jaudiotagger.tag.FieldKey.COMPOSER;
import static org.jaudiotagger.tag.FieldKey.DISC_NO;
import static org.jaudiotagger.tag.FieldKey.GENRE;
import static org.jaudiotagger.tag.FieldKey.ISRC;
import static org.jaudiotagger.tag.FieldKey.RECORD_LABEL;
import static org.jaudiotagger.tag.FieldKey.TITLE;
import static org.jaudiotagger.tag.FieldKey.TRACK;
import static org.jaudiotagger.tag.FieldKey.TRACK_TOTAL;
import static org.jaudiotagger.tag.FieldKey.YEAR;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;

import com.didawn.models.Song;

/**
 *
 * @author fabier
 */
public class Utils {

    private static final Logger logger = getLogger(Utils.class.getName());

    private static boolean isEmpty(String s) {
	return s == null || s.isEmpty();
    }

    /**
     *
     * @param closeable
     * @param streamDescription
     */
    public static void closeQuietly(Closeable closeable, String streamDescription) {
	if (closeable != null) {
	    try {
		closeable.close();
	    } catch (IOException var3) {
		if (!isEmpty(streamDescription)) {
		}
	    }
	}
    }

    /**
     *
     * @param track
     * @param fileIn
     * @throws IOException
     */
    public static void writeTrackInfo(Song track, File fileIn) throws IOException {
	try {
	    MP3File f = (MP3File) read(fileIn);
	    ID3v23Tag tag = new ID3v23Tag();

	    for (String artist : track.getArtists()) {
		tag.addField(ARTIST, artist);
	    }

	    tag.addField(ALBUM_ARTIST, track.getAlbumArtist());
	    tag.addField(TITLE, track.getTitle());
	    tag.addField(TRACK, valueOf(track.getTrackNumber()));
	    tag.addField(DISC_NO, valueOf(track.getDiskNumber()));
	    tag.addField(ALBUM, track.getAlbum());
	    tag.addField(YEAR, track.getYear());
	    tag.addField(GENRE, track.getGenre());
	    tag.addField(ISRC, track.getIsrc());
	    tag.addField(COMPOSER, track.getComposer());
	    tag.addField(BPM, track.getBpm());
	    tag.addField(TRACK_TOTAL, track.getAlbumTrackCount());
	    tag.addField(RECORD_LABEL, track.getLabel());
	    tag.addField(getAlbumArtwork(track.getCoverURL()));
	    f.setTag(tag);
	    f.commit();
	} catch (CannotWriteException | InvalidAudioFrameException | ReadOnlyFileException | TagException
		| CannotReadException var6) {
	}
    }

    private static Artwork getAlbumArtwork(String coverURL) throws IOException {
	StandardArtwork a = new StandardArtwork();
	if (coverURL == null) {
	    return a;
	} else {
	    File f = createTempFile("tmp", ".jpg");
	    copyURLToFile(new URL(coverURL), f);
	    a.setFromFile(f);
	    deleteFile(f);
	    a.setImageUrl(coverURL);
	    return a;
	}
    }

    /**
     *
     * @param f
     */
    public static void deleteFile(File f) {
	if (!f.delete()) {
	    logger.log(INFO, "Cannot delete file: {0}", f.getAbsolutePath());
	}
    }

    private Utils() {
    }
}
