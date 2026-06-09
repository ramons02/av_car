package br.edu.senai.fatesg.avcar.swing.views.telas.utils;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class JanelaUtil {
    
    public static void aplicarPadraoMaximizado(JFrame janela) {
        janela.setLocationRelativeTo(null); // Centro exato do monitor
        
        // Fila de eventos para garantir que o SO grave a posição base corretamente
        EventQueue.invokeLater(() -> {
            janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
    }

    public static void abrirPainelComoModal(java.awt.Window parent, String title, javax.swing.JPanel panel) {
        javax.swing.JDialog dialog = new javax.swing.JDialog(parent, title, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
