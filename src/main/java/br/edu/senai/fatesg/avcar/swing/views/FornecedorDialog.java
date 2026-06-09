package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorController;
import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorDTO;

import javax.swing.*;
import java.awt.*;

public class FornecedorDialog extends AbstractDialog<FornecedorDTO> {

    private final FornecedorController fornecedorController;
    private JTextField tfRazaoSocial;
    private JFormattedTextField tfCnpj;
    private JTextField tfDdi;
    private JTextField tfDdd;
    private JFormattedTextField tfNumeroFornecedor;
    private JTextField tfEmail;
    private JTextField tfEnderecoFornecedor;
    private JTextField tfBairroFornecedor;
    private JTextField tfCidadeFornecedor;
    private JTextField tfEstadoFornecedor;
    private JTextField tfCepFornecedor;

    private FornecedorDialog(Window owner, FornecedorController fornecedorController, Long editId, String... valores) {
        super(owner, editId == null ? "Novo Fornecedor" : "Editar Fornecedor", editId);
        this.fornecedorController = fornecedorController;
        tfRazaoSocial = new JTextField(30);
        tfCnpj = new JFormattedTextField(mf("##.###.###/####-##"));
        tfDdi = new JTextField(5);
        tfDdd = new JTextField(5);
        tfNumeroFornecedor = new JFormattedTextField(mf("#####-####"));
        tfEmail = new JTextField(30);
        tfEnderecoFornecedor = new JTextField(40);
        tfBairroFornecedor = new JTextField(30);
        tfCidadeFornecedor = new JTextField(30);
        tfEstadoFornecedor = new JTextField(20);
        tfCepFornecedor = new JTextField(10);
        if (editId != null && valores.length >= 11) {
            tfRazaoSocial.setText(valores[0]);
            tfCnpj.setText(ClienteDialog.apenasDigitos(valores[1]));
            tfDdi.setText(valores[2]);
            tfDdd.setText(valores[3]);
            tfNumeroFornecedor.setText(ClienteDialog.apenasDigitos(valores[4]));
            tfEmail.setText(valores[5]);
            tfEnderecoFornecedor.setText(valores[6]);
            tfBairroFornecedor.setText(valores[7]);
            tfCidadeFornecedor.setText(valores[8]);
            tfEstadoFornecedor.setText(valores[9]);
            tfCepFornecedor.setText(valores[10]);
        }
        initComponents();
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initComponents() {
        setApenasDigitos(tfDdi);
        setApenasDigitos(tfDdd);
        setApenasDigitos(tfCepFornecedor);

        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        addRow(form, gbc, 0, "Razão Social:", tfRazaoSocial);
        addRow(form, gbc, 1, "CNPJ:", tfCnpj);
        addRow(form, gbc, 2, "DDI:", tfDdi);
        addRow(form, gbc, 3, "DDD:", tfDdd);
        addRow(form, gbc, 4, "Número:", tfNumeroFornecedor);
        addRow(form, gbc, 5, "Email:", tfEmail);
        addRow(form, gbc, 6, "Endereço:", tfEnderecoFornecedor);
        addRow(form, gbc, 7, "Bairro:", tfBairroFornecedor);
        addRow(form, gbc, 8, "Cidade:", tfCidadeFornecedor);
        addRow(form, gbc, 9, "Estado:", tfEstadoFornecedor);
        addRow(form, gbc, 10, "CEP:", tfCepFornecedor);
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
        String razaoSocial = tfRazaoSocial.getText().trim();
        String cnpj = ClienteDialog.apenasDigitos(tfCnpj.getText());
        String ddi = tfDdi.getText().trim();
        String ddd = tfDdd.getText().trim();
        String numeroFornecedor = ClienteDialog.apenasDigitos(tfNumeroFornecedor.getText());
        String email = tfEmail.getText().trim();
        String enderecoFornecedor = tfEnderecoFornecedor.getText().trim();
        String bairroFornecedor = tfBairroFornecedor.getText().trim();
        String cidadeFornecedor = tfCidadeFornecedor.getText().trim();
        String estadoFornecedor = tfEstadoFornecedor.getText().trim();
        int cepFornecedor = 0;
        try {
            cepFornecedor = Integer.parseInt(ClienteDialog.apenasDigitos(tfCepFornecedor.getText()));
        } catch (NumberFormatException ignored) {}
        if (razaoSocial.isEmpty() || cnpj.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Razão Social e CNPJ são obrigatórios.");
            return;
        }
        FornecedorController.FornecedorRequest req = new FornecedorController.FornecedorRequest(
            razaoSocial, cnpj, ddi, ddd, numeroFornecedor, email,
            enderecoFornecedor, bairroFornecedor, cidadeFornecedor, estadoFornecedor, cepFornecedor);
        try {
            if (editMode) {
                resultado = fornecedorController.atualizar(editId, req).getBody();
            } else {
                resultado = fornecedorController.salvar(req).getBody();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static FornecedorDTO showDialog(Window owner, FornecedorController fornecedorController) {
        return new FornecedorDialog(owner, fornecedorController, null).getResultado();
    }

    public static FornecedorDTO showEditDialog(Window owner, FornecedorController fornecedorController, Long id,
                                                String razaoSocial, String cnpj, String ddi, String ddd,
                                                String numeroFornecedor, String email,
                                                String enderecoFornecedor, String bairroFornecedor,
                                                String cidadeFornecedor, String estadoFornecedor,
                                                String cepFornecedor) {
        return new FornecedorDialog(owner, fornecedorController, id, razaoSocial, cnpj, ddi, ddd,
            numeroFornecedor, email, enderecoFornecedor, bairroFornecedor,
            cidadeFornecedor, estadoFornecedor, cepFornecedor).getResultado();
    }
}
