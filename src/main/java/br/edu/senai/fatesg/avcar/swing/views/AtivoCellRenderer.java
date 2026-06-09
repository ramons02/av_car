package br.edu.senai.fatesg.avcar.swing.views;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class AtivoCellRenderer extends JPanel implements TableCellRenderer {
    private boolean ativo;

    public AtivoCellRenderer() {
        setOpaque(true);
        setLayout(new GridBagLayout());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        ativo = value instanceof Boolean && (Boolean) value;
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = Math.min(getWidth(), getHeight()) / 2;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;
        g2.setColor(ativo ? new Color(76, 175, 80) : new Color(244, 67, 54));
        g2.fillOval(x, y, size, size);
        g2.dispose();
    }
}
