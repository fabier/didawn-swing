package com.didawn.views;

import static com.didawn.di.DiDawnManager.State.IDLE;
import static java.awt.Color.GRAY;
import static java.awt.Cursor.getPredefinedCursor;
import static java.util.ResourceBundle.getBundle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.didawn.di.DiDawnManager;
import com.didawn.utils.EnumTranslator;

/**
 *
 * @author fabier
 */
public class DownloadStatusBar extends JPanel {

    private static final long serialVersionUID = 1_917_613_736_464_098_083L;
    private static final ResourceBundle RES = getBundle("downloadstatusbar");
    private static final EnumTranslator ENUM_TRANSLATOR = new EnumTranslator("downloaderstate");
    private static final Format DOWNLOAD_SPEED_FORMATTER = new DecimalFormat("###0.00");
    private final JLabel statusLabel;
    private final JProgressBar progressBar;
    private final JLabel progressLabel;
    private final JLabel speedLabel;
    private final JLabel abortButton;
    private int currentlyDownloadedSongIndex;
    private int numberOfSongsToDownload;

    /**
     *
     */
    public DownloadStatusBar() {
	this.setLayout(new BorderLayout());
	this.setBorder(new LineBorder(GRAY));
	this.setPreferredSize(new Dimension(0, 26));
	JPanel statusPanel = new JPanel(new FlowLayout(0));
	statusPanel.setPreferredSize(new Dimension(200, 26));
	statusPanel.add(new JLabel(RES.getString("downloadstatusbar.label.status")));
	this.statusLabel = new JLabel();
	this.setDownloaderState(IDLE);
	statusPanel.add(this.statusLabel);
	this.abortButton = new JLabel(new ImageIcon(this.getClass().getResource("/abort.png")));
	this.abortButton.setCursor(getPredefinedCursor(12));
	this.abortButton.setVisible(false);
	statusPanel.add(this.abortButton);
	JPanel progressPanel = new JPanel();
	progressPanel.setLayout(new BorderLayout());
	progressPanel.add(new JLabel(RES.getString("downloadstatusbar.label.progress")), "West");
	this.progressBar = new JProgressBar(0, 100);
	this.progressBar.setBorder(new EmptyBorder(5, 5, 5, 5));
	progressPanel.add(this.progressBar, "Center");
	this.progressLabel = new JLabel("0/0");
	progressPanel.add(this.progressLabel, "East");
	JPanel speedPanel = new JPanel(new FlowLayout(2));
	speedPanel.setPreferredSize(new Dimension(200, 26));
	speedPanel.add(new JLabel(RES.getString("downloadstatusbar.label.speed")));
	this.speedLabel = new JLabel("0kBit/s");
	speedPanel.add(this.speedLabel);
	this.add(statusPanel, "West");
	this.add(progressPanel, "Center");
	this.add(speedPanel, "East");
    }

    /**
     *
     * @param currentlyDownloadedSongIndex
     */
    public void setCurrentlyDownloadedSongIndex(int currentlyDownloadedSongIndex) {
	this.currentlyDownloadedSongIndex = currentlyDownloadedSongIndex;
	this.progressLabel.setText(Integer.toString(currentlyDownloadedSongIndex) + "/" + this.numberOfSongsToDownload);
    }

    /**
     *
     * @param numberOfSongsToDownload
     */
    public void setNumberOfSongsToDownload(int numberOfSongsToDownload) {
	this.numberOfSongsToDownload = numberOfSongsToDownload;
	this.progressLabel.setText(Integer.toString(this.currentlyDownloadedSongIndex) + "/" + numberOfSongsToDownload);
    }

    /**
     *
     * @param state
     */
    public void setDownloaderState(DiDawnManager.State state) {
	this.statusLabel.setText(ENUM_TRANSLATOR.translate(state));
    }

    /**
     *
     * @param visible
     */
    public void setAbortDownloadButtonVisible(boolean visible) {
	this.abortButton.setVisible(visible);
    }

    /**
     *
     * @param listener
     */
    public void addAbortDownloadButtonListener(MouseListener listener) {
	this.abortButton.addMouseListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeAbortDownloadButtonListener(MouseListener listener) {
	this.abortButton.removeMouseListener(listener);
    }

    /**
     *
     * @param speedInBitPerSecond
     */
    public void setDownloadSpeed(int speedInBitPerSecond) {
	double speed = speedInBitPerSecond;
	String speedUnit = "Bit/s";
	if (speed > 1024.0D) {
	    speed /= 1024.0D;
	    speedUnit = "KBit/s";
	}

	if (speed > 1024.0D) {
	    speed /= 1024.0D;
	    speedUnit = "MBit/s";
	}

	this.speedLabel.setText(DOWNLOAD_SPEED_FORMATTER.format(speed) + speedUnit);
    }

    /**
     *
     * @param maximalProgress
     */
    public void setProgressMax(int maximalProgress) {
	this.progressBar.setMaximum(maximalProgress);
    }

    /**
     *
     * @param progress
     */
    public void setProgress(int progress) {
	this.progressBar.setValue(progress);
    }
}
