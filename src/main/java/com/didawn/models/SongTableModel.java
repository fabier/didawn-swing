package com.didawn.models;

import com.didawn.views.SongTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

public class SongTableModel extends AbstractTableModel {

    private static final ResourceBundle RES = ResourceBundle.getBundle("songtable");
    private List listeners = new ArrayList();
    private Map selections = new HashMap();
    private List songs = new ArrayList();

    public int getRowCount() {
        return this.songs.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Song songToDisplay = (Song) this.songs.get(rowIndex);
        Object value;
        switch (columnIndex) {
        case 0:
            value = this.isSongSelected(songToDisplay);
            break;
        case 1:
            value = songToDisplay.getTitle();
            break;
        case 2:
            value = songToDisplay.getArtists();
            break;
        case 3:
            value = songToDisplay.getAlbum();
            break;
        case 4:
            value = (long) (songToDisplay.getDiskNumber() * 100) + songToDisplay.getTrackNumber();
            break;
        default:
            value = null;
        }

        return value;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return RES.getString("songtable.columnname.download");
        case 1:
            return RES.getString("songtable.columnname.title");
        case 2:
            return RES.getString("songtable.columnname.artist");
        case 3:
            return RES.getString("songtable.columnname.album");
        case 4:
            return RES.getString("songtable.columnname.track");
        default:
            return "";
        }
    }

    public boolean isCellEditable(int row, int col) {
        return col == 0;
    }

    public Class getColumnClass(int c) {
        return this.getValueAt(0, c).getClass();
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 0 && value instanceof Boolean) {
            Song song = (Song) this.songs.get(row);
            Boolean selected = (Boolean) value;
            this.selections.put(song, selected);
            this.informSelectionListeners();
        }

    }

    public void addSongs(List songsToAdd) {
        this.songs.addAll(songsToAdd);
    }

    public void addSong(Song songToAdd) {
        this.songs.add(songToAdd);
    }

    public void removeSong(Song songToRemove) {
        this.songs.remove(songToRemove);
        this.selections.remove(songToRemove);
    }

    public void clear() {
        this.songs.clear();
        this.selections.clear();
    }

    private boolean isSongSelected(Song song) {
        Boolean isSelected = (Boolean) this.selections.get(song);
        if (isSelected == null) {
            isSelected = Boolean.FALSE;
        }

        return isSelected;
    }

    public void addSongSelectionListener(SongTable.SongSelectionListener listener) {
        this.listeners.add(listener);
    }

    public void removeSongSelectionListener(SongTable.SongSelectionListener listener) {
        this.listeners.remove(listener);
    }

    private void informSelectionListeners() {
        List selectedSongs = this.getSelectedSongs();
        Iterator var2 = this.listeners.iterator();

        while (var2.hasNext()) {
            SongTable.SongSelectionListener listener = (SongTable.SongSelectionListener) var2.next();
            listener.onSelectionChanged(selectedSongs);
        }

    }

    public List getSelectedSongs() {
        ArrayList selectedSongs = new ArrayList();
        Iterator var2 = this.selections.keySet().iterator();

        while (var2.hasNext()) {
            Song song = (Song) var2.next();
            Boolean isSelected = (Boolean) this.selections.get(song);
            if (isSelected) {
                selectedSongs.add(song);
            }
        }

        return selectedSongs;
    }

    public List getSongs() {
        return this.songs;
    }
}
