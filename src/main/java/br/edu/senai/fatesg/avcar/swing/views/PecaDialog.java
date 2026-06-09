package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorController;
import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorDTO;
import br.edu.senai.fatesg.avcar.business.pecas.PecaController;
import br.edu.senai.fatesg.avcar.business.pecas.PecaDTO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PecaDialog extends JDialog {
    private final PecaController pecaController;
    private final FornecedorController fornecedorController;
    private final boolean editMode;
    private final Long editId;
    private PecaDTO result;

    private final JTextField tfCodigoNacional = new JTextField(20);
    private final JTextField tfCodigoInterno = new JTextField(20);
    private final JTextField tfNome = new JTextField(30);
    private final JTextField tfDescricao = new JTextField(40);
    private final JTextField tfFabricante = new JTextField(30);
    private final JTextField tfCategoria = new JTextField(30);
    private final JTextField tfPrecoCusto = new JTextField(10);
    private final JTextField tfPrecoVenda = new JTextField(10);
    private final JTextField tfEstoque = new JTextField(6);
    private final JTextField tfGarantia = new JTextField(6);
    private final JTextField tfDataCompra = new JTextField(10);
    private final JComboBox<String> cbFornecedor = new JComboBox<>();
    private List<FornecedorDTO> fornecedores;

    public PecaDialog(Window owner, PecaController pecaController, FornecedorController fornecedorController,
                      Long editId, String... valores) {
        super(owner, editId != null ? "Editar Peça" : "Nova Peça", ModalityType.APPLICATION_MODAL);
        this.pecaController = pecaController;
        this.fornecedorController = fornecedorController;
        this.editId = editId;
        this.editMode = editId != null;
        carregarFornecedores();
        initComponents();
        if (editMode && valores.length >= 12) {
            tfCodigoNacional.setText(valores[0]);
            tfCodigoInterno.setText(valores[1]);
            tfNome.setText(valores[2]);
            tfDescricao.setText(valores[3]);
            tfFabricante.setText(valores[4]);
            tfCategoria.setText(valores[5]);
            tfPrecoCusto.setText(valores[6]);
            tfPrecoVenda.setText(valores[7]);
            tfEstoque.setText(valores[8]);
            tfGarantia.setText(valores[9]);
            tfDataCompra.setText(valores[10]);
            if (valores.length >= 12 && valores[11] != null) {
                for (int i = 0; i < cbFornecedor.getItemCount(); i++) {
                    String item = cbFornecedor.getItemAt(i);
                    if (item != null && item.contains(valores[11])) {
                        cbFornecedor.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
        pack();
        setLocationRelativeTo(owner);
    }

    private void carregarFornecedores() {
        try {
            fornecedores = fornecedorController.listar(false).getBody();
            cbFornecedor.addItem("Selecione...");
            if (fornecedores != null) {
                for (FornecedorDTO f : fornecedores) {
                    cbFornecedor.addItem(f.getRazaoSocial() + " (" + f.getId() + ")");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar fornecedores: " + e.getMessage());
        }
    }

    private Long extractId(String comboItem) {
        if (comboItem == null || !comboItem.contains("(")) return null;
        int start = comboItem.lastIndexOf('(') + 1, end = comboItem.lastIndexOf(')');
        if (start >= end) return null;
        return Long.parseLong(comboItem.substring(start, end));
    }

    private void initComponents() {
        AbstractDialog.setApenasDigitos(tfCodigoNacional);
        AbstractDialog.setApenasDecimal(tfPrecoCusto);
        AbstractDialog.setApenasDecimal(tfPrecoVenda);
        AbstractDialog.setApenasDigitos(tfEstoque);
        AbstractDialog.setApenasDigitos(tfGarantia);

        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 8, 4, 8);
        c.anchor = GridBagConstraints.WEST;

        addRow(form, c, 0, "Código Nacional:", tfCodigoNacional);
        addRow(form, c, 1, "Código Interno:", tfCodigoInterno);
        addRow(form, c, 2, "Nome:", tfNome);
        addRow(form, c, 3, "Descrição:", tfDescricao);
        addRow(form, c, 4, "Fabricante:", tfFabricante);
        addRow(form, c, 5, "Categoria:", tfCategoria);
        addRow(form, c, 6, "Preço Custo:", tfPrecoCusto);
        addRow(form, c, 7, "Preço Venda:", tfPrecoVenda);
        addRow(form, c, 8, "Estoque:", tfEstoque);
        addRow(form, c, 9, "Garantia (dias):", tfGarantia);
        addRow(form, c, 10, "Data Compra (yyyy-MM-dd):", tfDataCompra);
        addRow(form, c, 11, "Fornecedor:", cbFornecedor);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton(editMode ? "Atualizar" : "Salvar");
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

    private void salvar() {
        String codNac = tfCodigoNacional.getText().trim();
        String nome = tfNome.getText().trim();
        if (codNac.isEmpty()) { JOptionPane.showMessageDialog(this, "Código nacional é obrigatório."); return; }
        if (nome.isEmpty()) { JOptionPane.showMessageDialog(this, "Nome é obrigatório."); return; }
        Long fornecedorId = extractId((String) cbFornecedor.getSelectedItem());
        if (fornecedorId == null) { JOptionPane.showMessageDialog(this, "Selecione um fornecedor."); return; }

        try {
            long codNacInt = Long.parseLong(codNac);
            String codInt = tfCodigoInterno.getText().trim();
            double pc = Double.parseDouble(tfPrecoCusto.getText().trim().replace(',', '.'));
            double pv = Double.parseDouble(tfPrecoVenda.getText().trim().replace(',', '.'));
            int estoque = Integer.parseInt(tfEstoque.getText().trim());
            int garantia = Integer.parseInt(tfGarantia.getText().trim());
            String dataCompraStr = tfDataCompra.getText().trim();
            LocalDate dataCompra = dataCompraStr.isEmpty() ? null : LocalDate.parse(dataCompraStr);

            PecaController.PecaRequest req = new PecaController.PecaRequest(
                codNacInt, codInt, nome, tfDescricao.getText().trim(),
                tfFabricante.getText().trim(), tfCategoria.getText().trim(),
                pc, pv, estoque, garantia, dataCompra, fornecedorId);

            if (editMode) {
                result = pecaController.atualizar(editId, req).getBody();
            } else {
                result = pecaController.salvar(req).getBody();
            }
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Campos numéricos inválidos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public PecaDTO getResult() { return result; }

    public static PecaDTO showDialog(Window owner, PecaController pecaController) {
        // For PecaDialog, we need FornecedorController to load suppliers.
        // Since PecaPanel only has PecaController, we need to pass FornecedorController too.
        // This method is kept for signature compatibility but uses null fornecedorController.
        // Use showDialog(owner, pecaController, fornecedorController) instead.
        throw new UnsupportedOperationException("Use showDialog(owner, pecaController, fornecedorController)");
    }

    public static PecaDTO showDialog(Window owner, PecaController pecaController,
                                      FornecedorController fornecedorController) {
        PecaDialog d = new PecaDialog(owner, pecaController, fornecedorController, null);
        d.setVisible(true);
        return d.getResult();
    }

    public static PecaDTO showEditDialog(Window owner, PecaController pecaController,
                                          FornecedorController fornecedorController, Long id,
                                          String codigoNacional, String codigoInterno, String nome, String descricao,
                                          String fabricante, String categoria, String precoCusto, String precoVenda,
                                          String estoque, String garantia, String dataCompra, String fornecedorNome) {
        PecaDialog d = new PecaDialog(owner, pecaController, fornecedorController, id,
            codigoNacional, codigoInterno, nome, descricao,
            fabricante, categoria, precoCusto, precoVenda,
            estoque, garantia, dataCompra, fornecedorNome);
        d.setVisible(true);
        return d.getResult();
    }

    // Legacy signature for PecaPanel (passes null fornecedorController - not recommended)
    public static PecaDTO showEditDialog(Window owner, PecaController pecaController, Long id,
            String codigoNacional, String codigoInterno, String nome, String descricao,
            String fabricante, String categoria, String precoCusto, String precoVenda,
            String estoque, String garantia, String dataCompra, String fornecedorNome) {
        throw new UnsupportedOperationException("Use showEditDialog with FornecedorController");
    }
}
