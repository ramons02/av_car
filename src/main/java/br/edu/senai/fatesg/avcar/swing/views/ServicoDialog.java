package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.servicos.ServicoController;
import br.edu.senai.fatesg.avcar.business.servicos.ServicoDTO;

import javax.swing.*;
import java.awt.*;

public class ServicoDialog extends AbstractDialog<ServicoDTO> {

    private final ServicoController servicoController;
    private JTextField tfNome;
    private JTextField tfDescricao;
    private JTextField tfValor;
    private JTextField tfGarantia;
    private JTextField tfTempo;

    private ServicoDialog(Window owner, ServicoController servicoController, Long editId, String... valores) {
        super(owner, editId == null ? "Novo Serviço" : "Editar Serviço", editId);
        this.servicoController = servicoController;
        tfNome = new JTextField(30);
        tfDescricao = new JTextField(40);
        tfValor = new JTextField(10);
        tfGarantia = new JTextField(6);
        tfTempo = new JTextField(10);
        if (editId != null && valores.length >= 5) {
            tfNome.setText(valores[0]);
            tfDescricao.setText(valores[1]);
            tfValor.setText(valores[2]);
            tfGarantia.setText(valores[3]);
            tfTempo.setText(valores[4]);
        }
        initComponents();
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initComponents() {
        setApenasDecimal(tfValor);
        setApenasDigitos(tfGarantia);

        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        addRow(form, gbc, 0, "Nome:", tfNome);
        addRow(form, gbc, 1, "Descrição:", tfDescricao);
        addRow(form, gbc, 2, "Valor:", tfValor);
        addRow(form, gbc, 3, "Garantia (dias):", tfGarantia);
        addRow(form, gbc, 4, "Tempo Estimado:", tfTempo);
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
        if (nome.isEmpty()) { JOptionPane.showMessageDialog(this, "Nome é obrigatório."); return; }
        double valor;
        int garantia;
        try {
            valor = Double.parseDouble(tfValor.getText().trim().replace(',', '.'));
            garantia = Integer.parseInt(tfGarantia.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor e Garantia devem ser números.");
            return;
        }
        ServicoController.ServicoRequest req = new ServicoController.ServicoRequest(
            nome, tfDescricao.getText().trim(), valor, garantia, tfTempo.getText().trim());
        try {
            if (editMode) {
                resultado = servicoController.atualizar(editId, req).getBody();
            } else {
                resultado = servicoController.salvar(req).getBody();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ServicoDTO showDialog(Window owner, ServicoController servicoController) {
        return new ServicoDialog(owner, servicoController, null).getResultado();
    }

    public static ServicoDTO showEditDialog(Window owner, ServicoController servicoController, Long id,
                                             String nome, String descricao, String valor,
                                             String garantia, String tempo) {
        return new ServicoDialog(owner, servicoController, id, nome, descricao, valor, garantia, tempo).getResultado();
    }
}
