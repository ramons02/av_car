package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.colaboradores.ColaboradorController;
import br.edu.senai.fatesg.avcar.business.colaboradores.ColaboradorDTO;
import br.edu.senai.fatesg.avcar.business.colaboradores.FuncaoDTO;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ColaboradorDialog extends JDialog {
    private final ColaboradorController colaboradorController;
    private final boolean editMode;
    private final Long editId;
    private ColaboradorDTO result;

    private final JTextField tfNome = new JTextField(30);
    private final JFormattedTextField tfCpf;
    private final JFormattedTextField tfTelefone;
    private final JTextField tfEmail = new JTextField(30);
    private final JList<String> listaFuncoes = new JList<>();
    private final DefaultListModel<String> funcoesModelo = new DefaultListModel<>();
    private List<FuncaoDTO> todasFuncoes;

    private static MaskFormatter mf(String p) {
        try { return new MaskFormatter(p); } catch (ParseException e) { return null; }
    }

    public ColaboradorDialog(Window owner, ColaboradorController colaboradorController, Long editId, String... valores) {
        super(owner, editId != null ? "Editar Colaborador" : "Novo Colaborador", ModalityType.APPLICATION_MODAL);
        this.colaboradorController = colaboradorController;
        this.editId = editId;
        this.editMode = editId != null;
        this.tfCpf = new JFormattedTextField(mf("###.###.###-##"));
        this.tfCpf.setColumns(18);
        this.tfTelefone = new JFormattedTextField(mf("(##) #####-####"));
        this.tfTelefone.setColumns(18);
        carregarFuncoes();
        initComponents();
        if (editMode && valores.length >= 5) {
            tfNome.setText(valores[0]);
            tfCpf.setText(ClienteDialog.formatarCPF(valores[1]));
            tfTelefone.setText(ClienteDialog.formatarTelefone(valores[2]));
            tfEmail.setText(valores[3]);
            if (valores.length >= 5 && valores[4] != null && !valores[4].isEmpty()) {
                String[] funcs = valores[4].split(", ");
                for (int i = 0; i < funcoesModelo.size(); i++) {
                    for (String f : funcs) {
                        if (funcoesModelo.get(i).startsWith(f + " (")) {
                            listaFuncoes.addSelectionInterval(i, i);
                        }
                    }
                }
            }
        }
        pack();
        setLocationRelativeTo(owner);
    }

    private void carregarFuncoes() {
        try {
            todasFuncoes = colaboradorController.listarFuncoes().getBody();
            if (todasFuncoes != null) {
                for (FuncaoDTO f : todasFuncoes) {
                    funcoesModelo.addElement(f.getFuncaoColaborador() + " (" + f.getIdFuncao() + ")");
                }
            }
            listaFuncoes.setModel(funcoesModelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar funções: " + e.getMessage());
        }
    }

    private List<Long> getFuncoesSelecionadas() {
        List<Long> ids = new ArrayList<>();
        for (int idx : listaFuncoes.getSelectedIndices()) {
            String item = funcoesModelo.get(idx);
            if (item.contains("(")) {
                int start = item.lastIndexOf('(') + 1, end = item.lastIndexOf(')');
                ids.add(Long.parseLong(item.substring(start, end)));
            }
        }
        return ids;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 8, 4, 8);
        c.anchor = GridBagConstraints.WEST;

        addRow(form, c, 0, "Nome:", tfNome);
        addRow(form, c, 1, "CPF:", tfCpf);
        addRow(form, c, 2, "Telefone:", tfTelefone);
        addRow(form, c, 3, "E-mail:", tfEmail);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2; c.weightx = 1; c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        JPanel funcoesPanel = new JPanel(new BorderLayout(5, 5));
        funcoesPanel.setBorder(BorderFactory.createTitledBorder("Funções (clique para selecionar)"));
        listaFuncoes.setVisibleRowCount(5);
        listaFuncoes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        funcoesPanel.add(new JScrollPane(listaFuncoes), BorderLayout.CENTER);
        form.add(funcoesPanel, c);

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
        c.gridx = 0; c.gridy = row; c.gridwidth = 1; c.weightx = 0; c.weighty = 0; c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.weightx = 1;
        panel.add(comp, c);
    }

    private void salvar() {
        String nome = tfNome.getText().trim();
        String cpf = ClienteDialog.apenasDigitos(tfCpf.getText());
        String telefone = ClienteDialog.apenasDigitos(tfTelefone.getText());
        String email = tfEmail.getText().trim();

        if (nome.isEmpty()) { JOptionPane.showMessageDialog(this, "Nome é obrigatório."); return; }
        if (cpf.isEmpty()) { JOptionPane.showMessageDialog(this, "CPF é obrigatório."); return; }

        String ddi1 = "55";
        String ddd1 = telefone.length() >= 2 ? telefone.substring(0, 2) : "";
        String numerotelefone1 = telefone.length() > 2 ? telefone.substring(2) : "";

        try {
            List<Long> funcaoIds = getFuncoesSelecionadas();
            ColaboradorController.ColaboradorRequest req = new ColaboradorController.ColaboradorRequest(
                nome, cpf, ddi1, ddd1, numerotelefone1, email, funcaoIds);

            if (editMode) {
                result = colaboradorController.atualizar(editId, req).getBody();
            } else {
                result = colaboradorController.salvar(req).getBody();
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ColaboradorDTO getResult() { return result; }

    public static ColaboradorDTO showDialog(Window owner, ColaboradorController colaboradorController) {
        ColaboradorDialog d = new ColaboradorDialog(owner, colaboradorController, null);
        d.setVisible(true);
        return d.getResult();
    }

    public static ColaboradorDTO showEditDialog(Window owner, ColaboradorController colaboradorController, Long id,
            String nome, String cpf, String telefone, String email, String funcoesStr) {
        ColaboradorDialog d = new ColaboradorDialog(owner, colaboradorController, id,
            nome, cpf, telefone, email, funcoesStr);
        d.setVisible(true);
        return d.getResult();
    }
}
