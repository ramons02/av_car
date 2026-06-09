package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.clientes.ClienteController;
import br.edu.senai.fatesg.avcar.business.clientes.ClienteDTO;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class ClienteDialog extends JDialog {
    private final ClienteController clienteController;
    private final boolean editMode;
    private final Long editId;
    private ClienteDTO result;

    private final JRadioButton rbPF = new JRadioButton("Pessoa Física", true);
    private final JRadioButton rbPJ = new JRadioButton("Pessoa Jurídica");
    private final JTextField tfNome      = new JTextField(30);
    private final JTextField tfEndereco  = new JTextField(30);
    private final JTextField tfBairro    = new JTextField(20);
    private final JTextField tfCidade    = new JTextField(20);
    private final JTextField tfEstado    = new JTextField(5);
    private final JFormattedTextField tfCep;
    private final JFormattedTextField tfTelefone;
    private final JTextField tfEmail     = new JTextField(30);
    private final JFormattedTextField tfDocumento;
    private final JLabel lbDocumento = new JLabel("CPF:");
    private final JTextField tfInscricaoEstadual = new JTextField(20);
    private final JLabel lbIE = new JLabel("Insc. Estadual:");
    private final JPanel pnlExtra = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));

    private static MaskFormatter criarMascara(String pattern) {
        try {
            MaskFormatter mf = new MaskFormatter(pattern);
            mf.setPlaceholderCharacter('_');
            mf.setValidCharacters("0123456789");
            return mf;
        } catch (ParseException e) {
            return null;
        }
    }

    private static final MaskFormatter mascaraCPF  = criarMascara("###.###.###-##");
    private static final MaskFormatter mascaraCNPJ = criarMascara("##.###.###/####-##");
    private static final MaskFormatter mascaraTel  = criarMascara("(##) #####-####");
    private static final MaskFormatter mascaraCEP  = criarMascara("#####-###");

    private ClienteDialog(Window owner, ClienteController clienteController, Long editId, Object... valores) {
        super(owner, valores.length > 0 ? "Editar Cliente" : "Novo Cliente", ModalityType.APPLICATION_MODAL);
        this.clienteController = clienteController;
        this.editId = editId;
        this.editMode = editId != null;
        this.tfDocumento = new JFormattedTextField(mascaraCPF);
        this.tfTelefone  = new JFormattedTextField(mascaraTel);
        this.tfCep       = new JFormattedTextField(mascaraCEP);
        initComponents();
        if (editMode && valores.length >= 5) {
            String tipo = (String) valores[1];
            boolean isPf = "PF".equals(tipo);
            rbPF.setSelected(isPf);
            rbPJ.setSelected(!isPf);
            tfNome.setText((String) valores[0]);
            tfEndereco.setText((String) valores[2]);
            tfTelefone.setText(formatarTelefone((String) valores[3]));
            tfEmail.setText((String) valores[4]);
            tfDocumento.setText(formatarDocumento((String) valores[5], isPf));
            if (valores.length > 6 && valores[6] != null) tfBairro.setText((String) valores[6]);
            if (valores.length > 7 && valores[7] != null) tfCidade.setText((String) valores[7]);
            if (valores.length > 8 && valores[8] != null) tfEstado.setText((String) valores[8]);
            if (valores.length > 9 && valores[9] != null) tfCep.setText((String) valores[9]);
            if (!isPf && valores.length > 10 && valores[10] != null)
                tfInscricaoEstadual.setText((String) valores[10]);
            atualizarCampos();
        }
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 8, 4, 8);
        c.anchor = GridBagConstraints.WEST;

        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbPF);
        grupoTipo.add(rbPJ);
        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        tipoPanel.add(rbPF);
        tipoPanel.add(rbPJ);

        rbPF.addActionListener(e -> atualizarCampos());
        rbPJ.addActionListener(e -> atualizarCampos());

        addRow(form, c, 0, "Tipo:",      tipoPanel);
        addRow(form, c, 1, "Nome:",      tfNome);
        addRow(form, c, 2, "Endereço:",  tfEndereco);
        addRow(form, c, 3, "Bairro:",    tfBairro);
        addRow(form, c, 4, "Cidade:",    tfCidade);
        addRow(form, c, 5, "Estado:",    tfEstado);
        addRow(form, c, 6, "CEP:",       tfCep);
        addRow(form, c, 7, "Telefone:",  tfTelefone);
        addRow(form, c, 8, "E-mail:",    tfEmail);

        c.gridx = 0; c.gridy = 9; c.gridwidth = 2;
        form.add(pnlExtra, c);
        atualizarCampos();

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar   = new JButton(editMode ? "Atualizar" : "Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);

        add(form, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent comp) {
        c.gridx = 0; c.gridy = row; c.gridwidth = 1; c.weightx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.weightx = 1;
        panel.add(comp, c);
    }

    private void atualizarCampos() {
        pnlExtra.removeAll();
        if (rbPF.isSelected()) {
            lbDocumento.setText("CPF:");
            tfDocumento.setFormatterFactory(new javax.swing.JFormattedTextField.AbstractFormatterFactory() {
                public javax.swing.JFormattedTextField.AbstractFormatter getFormatter(javax.swing.JFormattedTextField tf) {
                    return mascaraCPF;
                }
            });
            pnlExtra.add(lbDocumento);
            pnlExtra.add(tfDocumento);
        } else {
            lbDocumento.setText("CNPJ:");
            tfDocumento.setFormatterFactory(new javax.swing.JFormattedTextField.AbstractFormatterFactory() {
                public javax.swing.JFormattedTextField.AbstractFormatter getFormatter(javax.swing.JFormattedTextField tf) {
                    return mascaraCNPJ;
                }
            });
            pnlExtra.add(lbDocumento);
            pnlExtra.add(tfDocumento);
            pnlExtra.add(lbIE);
            pnlExtra.add(tfInscricaoEstadual);
        }
        if (!editMode) {
            rbPF.setEnabled(!editMode);
            rbPJ.setEnabled(!editMode);
        }
        pnlExtra.revalidate();
        pnlExtra.repaint();
        pack();
    }

    private void salvar() {
        String nome      = tfNome.getText().trim();
        String endereco  = tfEndereco.getText().trim();
        String bairro    = tfBairro.getText().trim();
        String cidade    = tfCidade.getText().trim();
        String estado    = tfEstado.getText().trim();
        String cep       = apenasDigitos(tfCep.getText());
        String telefone  = apenasDigitos(tfTelefone.getText());
        String email     = tfEmail.getText().trim();
        String documento = apenasDigitos(tfDocumento.getText());

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
            tfNome.requestFocus();
            return;
        }
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, (rbPF.isSelected() ? "CPF" : "CNPJ") + " é obrigatório.");
            tfDocumento.requestFocus();
            return;
        }

        try {
            if (editMode) {
                String ie = rbPJ.isSelected() ? tfInscricaoEstadual.getText().trim() : null;
                ClienteController.AtualizarClienteRequest req = new ClienteController.AtualizarClienteRequest(
                    nome, endereco, bairro, cidade, estado, cep,
                    telefone, email, documento, null, null, ie, null, null, null);
                result = clienteController.atualizar(editId, req).getBody();
            } else if (rbPF.isSelected()) {
                ClienteController.CriarPFRequest req = new ClienteController.CriarPFRequest(
                    nome, endereco, bairro, cidade, estado, cep,
                    telefone, email, documento, null, null, null);
                result = clienteController.criarPF(req).getBody();
            } else {
                ClienteController.CriarPJRequest req = new ClienteController.CriarPJRequest(
                    nome, endereco, bairro, cidade, estado, cep,
                    telefone, email, documento, tfInscricaoEstadual.getText().trim(), null, null);
                result = clienteController.criarPJ(req).getBody();
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ClienteDTO getResult() { return result; }

    public static ClienteDTO showDialog(Window owner, ClienteController clienteController) {
        ClienteDialog d = new ClienteDialog(owner, clienteController, null);
        d.setVisible(true);
        return d.getResult();
    }

    public static ClienteDTO showEditDialog(Window owner, ClienteController clienteController, Long id,
            String nome, String tipo, String documento, String telefone, String email,
            String endereco, String bairro, String cidade, String estado, String cep,
            String inscricaoEstadual) {
        ClienteDialog d = new ClienteDialog(owner, clienteController, id, nome, tipo, endereco,
            telefone, email, documento, bairro, cidade, estado, cep, inscricaoEstadual);
        d.setVisible(true);
        return d.getResult();
    }

    static String apenasDigitos(String s) {
        return s != null ? s.replaceAll("\\D", "") : "";
    }

    static String formatarCPF(String raw) {
        if (raw == null || raw.length() != 11) return raw;
        return raw.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    static String formatarCNPJ(String raw) {
        if (raw == null || raw.length() != 14) return raw;
        return raw.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    static String formatarTelefone(String raw) {
        if (raw == null || raw.length() < 10) return raw;
        String ddd   = raw.substring(0, 2);
        String parte1 = raw.substring(2, raw.length() - 4);
        String parte2 = raw.substring(raw.length() - 4);
        return "(" + ddd + ") " + parte1 + "-" + parte2;
    }

    static String formatarDocumento(String raw, boolean isPf) {
        return isPf ? formatarCPF(raw) : formatarCNPJ(raw);
    }
}
