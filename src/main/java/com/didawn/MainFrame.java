package com.didawn;

import static java.awt.SystemTray.getSystemTray;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.System.exit;
import static java.util.ResourceBundle.getBundle;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.didawn.controller.MainController;
import com.didawn.views.DownloadArea;
import com.didawn.views.DownloadStatusBar;
import com.didawn.views.SearchArea;
import com.didawn.views.SettingsPanel;
import com.didawn.views.SongTable;
import com.didawn.views.VersionInformationDialog;

/**
 *
 * @author fabier
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 925_253_616_232_685_487L;
    private static final ResourceBundle RES = getBundle("mainframe");
    private static final String FRAME_TITLE = "frame.title";
    private static final int COMPONENT_SPACING = 10;
    private static final Dimension SCREEN_SIZE = getDefaultToolkit().getScreenSize();
    private static final Dimension FRAME_SIZE = new Dimension(800, 600);
    private SearchArea searchArea;
    private SettingsPanel settingsPanel;
    private SongTable songTable;
    private DownloadArea downloadArea;
    private DownloadStatusBar downloadDownloadStatusBar;

    /**
     *
     */
    public MainFrame() {
	this.initializeSubComponents();
	this.initializeToolbar();
	this.initializeLayout();
	ImageIcon icon = new ImageIcon(this.getClass().getResource("/icon.png"));
	this.setIconImage(icon.getImage());
    }

    @SuppressWarnings("unused")
    private void initializeTray() throws AWTException {
	SystemTray tray = getSystemTray();
	PopupMenu popup = new PopupMenu();
	ImageIcon icon = new ImageIcon(this.getClass().getResource("/icon.png"));
	Image image = icon.getImage();
	TrayIcon trayIcon = new TrayIcon(image, RES.getString(FRAME_TITLE));
	trayIcon.setImageAutoSize(true);
	trayIcon.setToolTip(RES.getString(FRAME_TITLE));
	tray.add(trayIcon);
	MenuItem versionInfo = new MenuItem(RES.getString("frame.toolbar.help.versionInfo"));
	versionInfo.addActionListener(e -> (new VersionInformationDialog(MainFrame.this)).setVisible(true));
	MenuItem displayFrame = new MenuItem(RES.getString("frame.show"));
	displayFrame.addActionListener(e -> {
	    MainFrame.this.setExtendedState(NORMAL);
	    MainFrame.this.setVisible(true);
	    MainFrame.this.toFront();
	});
	MenuItem exitItem = new MenuItem(RES.getString("frame.toolbar.file.close"));
	exitItem.addActionListener(e -> exit(0));
	popup.add(displayFrame);
	popup.addSeparator();
	popup.add(versionInfo);
	popup.addSeparator();
	popup.add(exitItem);
	trayIcon.setPopupMenu(popup);
	trayIcon.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
		    MainFrame.this.setExtendedState(NORMAL);
		    MainFrame.this.setVisible(true);
		    MainFrame.this.toFront();
		}
	    }
	});
	this.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowIconified(WindowEvent e) {
		MainFrame.this.dispose();
	    }
	});
    }

    private void initializeSubComponents() {
	this.searchArea = new SearchArea();
	this.searchArea
		.setBorder(new EmptyBorder(COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING));
	this.settingsPanel = new SettingsPanel();
	this.settingsPanel.setBorder(new EmptyBorder(0, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING));
	this.songTable = new SongTable();
	this.downloadArea = new DownloadArea();
	this.downloadDownloadStatusBar = new DownloadStatusBar();
	new MainController(this, this.searchArea, this.settingsPanel, this.songTable, this.downloadArea,
		this.downloadDownloadStatusBar);
    }

    private void initializeLayout() {
	this.setTitle(RES.getString(FRAME_TITLE));
	this.setLayout(new BorderLayout(COMPONENT_SPACING, COMPONENT_SPACING));
	JPanel searchPanel = new JPanel();
	searchPanel.setLayout(new BorderLayout());
	searchPanel.add(this.searchArea, BorderLayout.NORTH);
	searchPanel.add(this.settingsPanel, BorderLayout.CENTER);
	JPanel mainArea = new JPanel();
	mainArea.setLayout(new BorderLayout());
	mainArea.add(searchPanel, "North");
	mainArea.add(new JScrollPane(this.songTable), "Center");
	mainArea.add(this.downloadArea, "South");
	this.add(mainArea, "Center");
	this.add(this.downloadDownloadStatusBar, "South");
	int xPosition = (SCREEN_SIZE.width - FRAME_SIZE.width) / 2;
	int yPosition = (SCREEN_SIZE.height - FRAME_SIZE.height) / 2;
	this.setLocation(xPosition, yPosition);
	this.setSize(FRAME_SIZE);
	this.setMinimumSize(FRAME_SIZE);
	this.setDefaultCloseOperation(3);
    }

    private void initializeToolbar() {
	JMenuItem close = new JMenuItem(RES.getString("frame.toolbar.file.close"));
	close.addActionListener(e -> exit(0));
	JMenu file = new JMenu(RES.getString("frame.toolbar.file"));
	file.add(close);
	JMenuItem versionInfo = new JMenuItem(RES.getString("frame.toolbar.help.versionInfo"));
	versionInfo.addActionListener(e -> new VersionInformationDialog(MainFrame.this).setVisible(true));
	JMenu help = new JMenu(RES.getString("frame.toolbar.help"));
	help.add(versionInfo);
	JMenuBar menu = new JMenuBar();
	menu.add(file);
	menu.add(help);
	this.setJMenuBar(menu);
    }
}
