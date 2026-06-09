package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public abstract class AbstractDialog<D extends BaseDTO> extends JDialog {

    protected final boolean editMode;
    protected final Long editId;
    protected D resultado;

    public AbstractDialog(Window owner, String titulo, Long editId) {
        super(owner, titulo, ModalityType.APPLICATION_MODAL);
        this.editMode = editId != null;
        this.editId = editId;
    }

    protected void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(label, SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(comp, gbc);
    }

    protected static MaskFormatter mf(String pattern) {
        try {
            MaskFormatter mf = new MaskFormatter(pattern);
            mf.setPlaceholderCharacter('_');
            return mf;
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void setApenasDigitos(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {
                if (text != null && text.matches("\\d*"))
                    super.insertString(fb, offset, text, attr);
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr)
                    throws BadLocationException {
                if (text != null && text.matches("\\d*"))
                    super.replace(fb, offset, length, text, attr);
            }
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    protected static void setApenasDecimal(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {
                if (text != null && text.matches("[\\d,.]*")) {
                    String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newVal = new StringBuilder(current).insert(offset, text).toString();
                    if (newVal.matches("\\d*([.,]\\d*)?"))
                        super.insertString(fb, offset, text, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr)
                    throws BadLocationException {
                if (text != null && text.matches("[\\d,.]*")) {
                    String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String before = current.substring(0, offset);
                    String after = current.substring(offset + length);
                    String newVal = before + text + after;
                    if (newVal.matches("\\d*([.,]\\d*)?"))
                        super.replace(fb, offset, length, text, attr);
                }
            }
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    public D getResultado() { return resultado; }
}
