package com.didawn.models;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.unmodifiableList;
import static java.util.ResourceBundle.getBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import com.didawn.views.SongTable;

/**
 *
 * @author fabier
 */
public class SongTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 9_122_544_141_146_723_736L;

    private static final ResourceBundle RES = getBundle("songtable");
    private final List<SongTable.SongSelectionListener> listeners = new ArrayList<>();
    private final Map<Song, Boolean> selections = new HashMap<>();
    private final List<Song> songs = new ArrayList<>();

    @Override
    public int getRowCount() {
	return this.songs.size();
    }

    @Override
    public int getColumnCount() {
	return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
	Song songToDisplay = this.songs.get(rowIndex);
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
	    value = songToDisplay.getId();
	    break;
	default:
	    value = null;
	}

	return value;
    }

    @Override
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
	    return RES.getString("songtable.columnname.id");
	default:
	    return "";
	}
    }

    @Override
    public boolean isCellEditable(int row, int col) {
	return col == 0;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
	switch (columnIndex) {
	case 0:
	    return Boolean.class;
	case 1:
	case 2:
	case 3:
	case 4:
	    return String.class;
	default:
	    return super.getColumnClass(columnIndex);
	}
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
	if (col == 0 && value instanceof Boolean) {
	    Song song = this.songs.get(row);
	    Boolean selected = (Boolean) value;
	    this.selections.put(song, selected);
	    this.informSelectionListeners();
	}
    }

    /**
     *
     * @param songsToAdd
     */
    public void addSongs(List<Song> songsToAdd) {
	this.songs.addAll(songsToAdd);
    }

    /**
     *
     * @param songToAdd
     */
    public void addSong(Song songToAdd) {
	this.songs.add(songToAdd);
    }

    /**
     *
     * @param songToRemove
     */
    public void removeSong(Song songToRemove) {
	this.songs.remove(songToRemove);
	this.selections.remove(songToRemove);
    }

    /**
     *
     */
    public void clear() {
	this.songs.clear();
	this.selections.clear();
    }

    private boolean isSongSelected(Song song) {
	return Objects.equals(this.selections.get(song), TRUE);
    }

    /**
     *
     * @param listener
     */
    public void addSongSelectionListener(SongTable.SongSelectionListener listener) {
	this.listeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeSongSelectionListener(SongTable.SongSelectionListener listener) {
	this.listeners.remove(listener);
    }

    private void informSelectionListeners() {
	List<Song> selectedSongs = this.getSelectedSongs();
	this.listeners.forEach(listener -> listener.onSelectionChanged(selectedSongs));
    }

    /**
     *
     * @return
     */
    public List<Song> getSelectedSongs() {
	ArrayList<Song> selectedSongs = new ArrayList<>();
	this.selections.entrySet().stream().filter(entry -> (Objects.equals(entry.getValue(), TRUE)))
		.forEachOrdered(entry -> selectedSongs.add(entry.getKey()));
	return selectedSongs;
    }

    /**
     *
     * @return
     */
    public List<Song> getSongs() {
	return unmodifiableList(this.songs);
    }
}
