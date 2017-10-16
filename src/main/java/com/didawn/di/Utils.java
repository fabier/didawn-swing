package com.didawn.di;

import com.didawn.models.Song;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;

public class Utils {

    private static final Logger log = Logger.getLogger(Utils.class.getName());

    private static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static void closeQuietly(Closeable closeable, String streamDescription) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var3) {
                if (!isEmpty(streamDescription)) {
                    var3.printStackTrace();
                }
            }
        }

    }

    public static void writeTrackInfo(Song track, File f_in) throws IOException {
        try {
            MP3File f = (MP3File) AudioFileIO.read(f_in);
            ID3v23Tag tag = new ID3v23Tag();
            Iterator var4 = track.getArtists().iterator();

            while (var4.hasNext()) {
                String artist = (String) var4.next();
                tag.addField(FieldKey.ARTIST, artist);
            }

            tag.addField(FieldKey.ALBUM_ARTIST, track.getAlbumArtist());
            tag.addField(FieldKey.TITLE, track.getTitle());
            tag.addField(FieldKey.TRACK, String.valueOf(track.getTrackNumber()));
            tag.addField(FieldKey.DISC_NO, String.valueOf(track.getDiskNumber()));
            tag.addField(FieldKey.ALBUM, track.getAlbum());
            tag.addField(FieldKey.YEAR, track.getYear());
            tag.addField(FieldKey.GENRE, track.getGenre());
            tag.addField(FieldKey.ISRC, track.getISRC());
            tag.addField(FieldKey.COMPOSER, track.getComposer());
            tag.addField(FieldKey.BPM, track.getBPM());
            tag.addField(FieldKey.TRACK_TOTAL, track.getAlbumTrackCount());
            tag.addField(FieldKey.RECORD_LABEL, track.getLabel());
            tag.addField(getAlbumArtwork(track.getCoverURL()));
            f.setTag(tag);
            f.commit();
        } catch (CannotWriteException | InvalidAudioFrameException | ReadOnlyFileException | TagException | CannotReadException var6) {
            var6.printStackTrace();
        }
    }

    private static Artwork getAlbumArtwork(String coverURL) {
        StandardArtwork a = new StandardArtwork();
        if (coverURL == null) {
            return a;
        } else {
            try {
                File f = File.createTempFile("tmp", ".jpg");
                FileUtils.copyURLToFile(new URL(coverURL), f);
                a.setFromFile(f);
                deleteFile(f);
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            a.setImageUrl(coverURL);
            return a;
        }
    }

    public static void deleteFile(File f) {
        if (!f.delete()) {
            log.info("Cannot delete file: " + f.getAbsolutePath());
        }
    }
}
