package com.didawn.views;

import static java.awt.event.KeyEvent.VK_SPACE;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JTable;

import com.didawn.models.Song;
import com.didawn.models.SongTableModel;

/**
 *
 * @author fabier
 */
public class SongTable extends JTable {

    private static final long serialVersionUID = 1_034_209_976_546_454_131L;
    private SongTableModel model;

    /**
     *
     */
    public SongTable() {
	this.initializeTable();
	this.initializeDataModel();
	this.initializeColumnModel();
    }

    private void initializeTable() {
	this.setAutoCreateRowSorter(true);
	this.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == VK_SPACE) {
		    Boolean value = null;
		    for (int row : getSelectedRows()) {
			int modelRow = convertRowIndexToModel(row);
			if (value == null) {
			    value = (Boolean) model.getValueAt(modelRow, 0);
			    value = !value;
			}
			model.setValueAt(value, modelRow, 0);
			model.fireTableCellUpdated(modelRow, 0);
		    }
		}
	    }
	});
    }

    private void initializeDataModel() {
	this.model = new SongTableModel();
	this.setModel(this.model);
    }

    private void initializeColumnModel() {
	this.getColumnModel().getColumn(0).setMinWidth(75);
	this.getColumnModel().getColumn(0).setMaxWidth(75);
	this.getColumnModel().getColumn(4).setMinWidth(75);
	this.getColumnModel().getColumn(4).setMaxWidth(75);
    }

    /**
     *
     * @param songs
     */
    public void addSongs(List<Song> songs) {
	this.model.addSongs(songs);
	this.model.fireTableDataChanged();
    }

    /**
     *
     * @param listener
     */
    public void addSongSelectionListener(SongTable.SongSelectionListener listener) {
	this.model.addSongSelectionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeSongSelectionListener(SongTable.SongSelectionListener listener) {
	this.model.removeSongSelectionListener(listener);
    }

    /**
     *
     * @return
     */
    public List<Song> getSelectedSongs() {
	return this.model.getSelectedSongs();
    }

    /**
     *
     * @return
     */
    public List<Song> getSongs() {
	return this.model.getSongs();
    }

    /**
     *
     * @param songs
     */
    public void setSongs(List<Song> songs) {
	this.model.clear();
	this.addSongs(songs);
    }

    /**
     *
     */
    public interface SongSelectionListener {

	/**
	 *
	 * @param songs
	 */
	void onSelectionChanged(List<Song> songs);
    }
}
