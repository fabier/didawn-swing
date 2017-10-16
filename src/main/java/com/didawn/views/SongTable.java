package com.didawn.views;

import com.didawn.models.SongTableModel;
import java.util.List;
import javax.swing.JTable;

public class SongTable extends JTable {

    private SongTableModel model;

    public SongTable() {
        this.initializeTable();
        this.initializeDataModel();
        this.initializeColumnModel();
    }

    private void initializeTable() {
        this.setAutoCreateRowSorter(true);
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

    public void addSongs(List songs) {
        this.model.addSongs(songs);
        this.model.fireTableDataChanged();
    }

    public void addSongSelectionListener(SongTable.SongSelectionListener listener) {
        this.model.addSongSelectionListener(listener);
    }

    public void removeSongSelectionListener(SongTable.SongSelectionListener listener) {
        this.model.removeSongSelectionListener(listener);
    }

    public List getSelectedSongs() {
        return this.model.getSelectedSongs();
    }

    public List getSongs() {
        return this.model.getSongs();
    }

    public void setSongs(List songs) {
        this.model.clear();
        this.addSongs(songs);
    }

    public interface SongSelectionListener {

        void onSelectionChanged(List var1);
    }
}
