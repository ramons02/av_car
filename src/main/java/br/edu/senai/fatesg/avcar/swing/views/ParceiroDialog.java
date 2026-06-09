package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.parceiros.ParceiroDTO;
import br.edu.senai.fatesg.avcar.business.parceiros.ParceiroExternoController;

import javax.swing.*;
import java.awt.*;

public class ParceiroDialog extends AbstractDialog<ParceiroDTO> {

    private final ParceiroExternoController parceiroController;
    private JTextField tfNome;
    private JFormattedTextField tfCnpj;
    private JTextField tfTipoServico;
    private JFormattedTextField tfTelefone;
    private JTextField tfEmail;
    private JCheckBox cbAtivo;

    private ParceiroDialog(Window owner, ParceiroExternoController parceiroController, Long editId, String... valores) {
        super(owner, editId == null ? "Novo Parceiro" : "Editar Parceiro", editId);
        this.parceiroController = parceiroController;
        tfNome = new JTextField(30);
        tfCnpj = new JFormattedTextField(mf("##.###.###/####-##"));
        tfTipoServico = new JTextField(25);
        tfTelefone = new JFormattedTextField(mf("(##) #####-####"));
        tfEmail = new JTextField(30);
        cbAtivo = new JCheckBox("Ativo", true);
        if (editId != null && valores.length >= 6) {
            tfNome.setText(valores[0]);
            tfCnpj.setText(ClienteDialog.apenasDigitos(valores[1]));
            tfTipoServico.setText(valores[2]);
            tfTelefone.setText(ClienteDialog.apenasDigitos(valores[3]));
            tfEmail.setText(valores[4]);
            cbAtivo.setSelected(Boolean.parseBoolean(valores[5]));
        }
        initComponents();
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        addRow(form, gbc, 0, "Nome:", tfNome);
        addRow(form, gbc, 1, "CNPJ:", tfCnpj);
        addRow(form, gbc, 2, "Tipo Serviço:", tfTipoServico);
        addRow(form, gbc, 3, "Telefone:", tfTelefone);
        addRow(form, gbc, 4, "Email:", tfEmail);
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.LINE_START;
        form.add(cbAtivo, gbc);
        add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);
        add(botoes, BorderLayout.SOUTH);
    }

    private void salvar() {
        String nome = tfNome.getText().trim();
        String cnpj = ClienteDialog.apenasDigitos(tfCnpj.getText());
        if (nome.isEmpty() || cnpj.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e CNPJ são obrigatórios.");
            return;
        }
        ParceiroExternoController.ParceiroRequest req = new ParceiroExternoController.ParceiroRequest(
            nome, cnpj, tfTipoServico.getText().trim(),
            ClienteDialog.apenasDigitos(tfTelefone.getText()),
            tfEmail.getText().trim(), cbAtivo.isSelected());
        try {
            if (editMode) {
                resultado = parceiroController.atualizar(editId, req).getBody();
            } else {
                resultado = parceiroController.salvar(req).getBody();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ParceiroDTO showDialog(Window owner, ParceiroExternoController parceiroController) {
        return new ParceiroDialog(owner, parceiroController, null).getResultado();
    }

    public static ParceiroDTO showEditDialog(Window owner, ParceiroExternoController parceiroController, Long id,
                                              String nome, String cnpj, String tipoServico,
                                              String telefone, String email, String ativo) {
        return new ParceiroDialog(owner, parceiroController, id, nome, cnpj, tipoServico, telefone, email, ativo).getResultado();
    }
}
