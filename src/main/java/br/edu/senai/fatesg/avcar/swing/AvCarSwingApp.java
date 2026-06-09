package br.edu.senai.fatesg.avcar.swing;

import br.edu.senai.fatesg.avcar.AvCarApplication;
import org.springframework.boot.SpringApplication;

/**
 * Legacy standalone entry-point — replaced by AvCarApplication (Spring context).
 * This class is kept for reference only; use AvCarApplication.main() to run the app.
 */
public class AvCarSwingApp {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        try {
            com.formdev.flatlaf.FlatDarkLaf.setup();
            
            // --- PADRÃO DE FONTE MODERNA (UX/UI) ---
            javax.swing.UIManager.put("defaultFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            
            javax.swing.UIManager.put("TextComponent.arc", 15);
            javax.swing.UIManager.put("Button.arc", 15);
            javax.swing.UIManager.put("Component.arc", 15);
            
            // --- ESTILIZAÇÃO DO MENU LATERAL (TABBED PANE) ---
            javax.swing.UIManager.put("TabbedPane.showTabSeparators", false);
            javax.swing.UIManager.put("TabbedPane.hasFullBorder", false);
            javax.swing.UIManager.put("TabbedPane.tabHeight", 45);
            javax.swing.UIManager.put("TabbedPane.tabInsets", new java.awt.Insets(0, 20, 0, 20));
            javax.swing.UIManager.put("TabbedPane.focusColor", new java.awt.Color(0,0,0,0));
            javax.swing.UIManager.put("TabbedPane.hoverColor", new java.awt.Color(70, 73, 75));
            javax.swing.UIManager.put("TabbedPane.selectedBackground", new java.awt.Color(43, 43, 43));
        } catch (Exception ignored) {}
        SpringApplication.run(AvCarApplication.class, args);
    }
}
