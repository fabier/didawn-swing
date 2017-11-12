package com.didawn;

import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author fabier
 */
public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
	setNimbusLookAndFeel();
	createMainFrame();
    }

    private static void createMainFrame() {
	MainFrame mainFrame = new MainFrame();
	mainFrame.setVisible(true);
    }

    private static void setNimbusLookAndFeel() {
	try {
	    LookAndFeelInfo[] lookAndFeelInfos = getInstalledLookAndFeels();
	    for (LookAndFeelInfo lookAndFeelInfo : lookAndFeelInfos) {
		if ("Nimbus".equals(lookAndFeelInfo.getName())) {
		    setLookAndFeel(lookAndFeelInfo.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException | IllegalAccessException | InstantiationException
		| UnsupportedLookAndFeelException e) {
	}
    }
}
