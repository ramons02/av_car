package br.edu.senai.fatesg.avcar.swing.views.telas.utils;

import javax.swing.JTable;

public class TabelaUtil {

    public static void ocultarColuna(JTable tabela, int indice) {
        if (tabela != null && tabela.getColumnCount() > indice) {
            tabela.getColumnModel().removeColumn(tabela.getColumnModel().getColumn(indice));
        }
    }

    public static void definirLarguraFixa(JTable tabela, int indice, int larguraPixels) {
        if (tabela != null && tabela.getColumnCount() > indice) {
            tabela.getColumnModel().getColumn(indice).setMinWidth(larguraPixels);
            tabela.getColumnModel().getColumn(indice).setMaxWidth(larguraPixels);
            tabela.getColumnModel().getColumn(indice).setPreferredWidth(larguraPixels);
        }
    }

    public static void aplicarCorNoStatus(JTable tabela) {
        if (tabela != null) {
            for (int i = 0; i < tabela.getColumnCount(); i++) {
                if (tabela.getColumnName(i).equalsIgnoreCase("Status")) {
                    tabela.getColumnModel().getColumn(i).setCellRenderer(new StatusRenderer());
                    break;
                }
            }
        }
    }

    public record ImagemComTexto(String urlLocal, String texto) {
        @Override
        public String toString() { return texto; }
    }

    private static final java.util.HashMap<String, javax.swing.ImageIcon> logoCache = new java.util.HashMap<>();

    public static void aplicarRenderizadorDeImagem(javax.swing.JTable tabela, int colunaIndex) {
        if (tabela == null || colunaIndex < 0 || colunaIndex >= tabela.getColumnCount()) return;
        
        // Dá um "respiro" maior para as linhas da tabela
        if (tabela.getRowHeight() < 36) {
            tabela.setRowHeight(36);
        }
        
        tabela.getColumnModel().getColumn(colunaIndex).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                // Margem invisível para não colar nas bordas
                setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5));
                
                if (value instanceof ImagemComTexto iv) {
                    if (iv.urlLocal() != null && !iv.urlLocal().isBlank()) {
                        javax.swing.ImageIcon icon = logoCache.get(iv.urlLocal());
                        if (icon == null) {
                            try {
                                java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(iv.urlLocal());
                                if (is != null) {
                                    java.awt.image.BufferedImage origem = javax.imageio.ImageIO.read(is);
                                    is.close();
                                    if (origem != null) {
                                        // Cálculo de Aspect Ratio
                                        int max = 28;
                                        int w = origem.getWidth();
                                        int h = origem.getHeight();
                                        double ratio = (double) w / h;
                                        if (w > h) {
                                            w = max;
                                            h = (int) (max / ratio);
                                        } else {
                                            h = max;
                                            w = (int) (max * ratio);
                                        }
                                        
                                        java.awt.image.BufferedImage redim = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                                        java.awt.Graphics2D g = redim.createGraphics();
                                        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                                        g.drawImage(origem, 0, 0, w, h, null);
                                        g.dispose();
                                        icon = new javax.swing.ImageIcon(redim);
                                        logoCache.put(iv.urlLocal(), icon);
                                    }
                                }
                            } catch (Exception e) {}
                        }
                        if (icon != null) {
                            setIcon(icon);
                            setText("");
                            setToolTipText(iv.texto());
                            return this;
                        }
                    }
                    setIcon(null);
                    setText(iv.texto());
                    setToolTipText(null);
                } else if (value != null) {
                    setIcon(null);
                    setText(value.toString());
                }
                return this;
            }
        });
    }

    public static void adicionarBuscaEmTempoReal(javax.swing.JTextField campo, Runnable acaoBusca) {
        if (campo != null && acaoBusca != null) {
            campo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { acaoBusca.run(); }
                @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { acaoBusca.run(); }
                @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { acaoBusca.run(); }
            });
        }
    }

    public static void centralizarColunas(JTable tabela, int... indices) {
        if (tabela != null) {
            javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            
            for (int indice : indices) {
                // Previne NullPointer e ArrayOutOfBounds, e não sobrescreve a coluna Status
                if (indice < tabela.getColumnCount() && !tabela.getColumnName(indice).equalsIgnoreCase("Status")) {
                    tabela.getColumnModel().getColumn(indice).setCellRenderer(centerRenderer);
                }
            }
        }
    }
}
