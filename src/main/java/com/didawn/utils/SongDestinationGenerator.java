package com.didawn.utils;

import com.didawn.models.Song;
import java.io.File;

public interface SongDestinationGenerator {

    File getDestinationFolder();

    File getDestinationFolder(Song var1);
}
