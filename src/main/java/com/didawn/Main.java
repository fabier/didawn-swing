package com.didawn;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main {

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
            LookAndFeelInfo[] var0 = UIManager.getInstalledLookAndFeels();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                LookAndFeelInfo info = var0[var2];
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
