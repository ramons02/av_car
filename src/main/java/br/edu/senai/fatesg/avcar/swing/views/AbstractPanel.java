package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import br.edu.senai.fatesg.avcar.swing.client.ApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public abstract class AbstractPanel<D extends BaseDTO> extends JPanel {

    protected final ApiClient api;
    protected final JTable tabela;
    protected final DefaultTableModel modelo;
    protected final JTextField tfBusca;
    protected final JCheckBox cbMostrarInativos;
    protected final Class<D> dtoClass;
    protected List<D> dadosCarregados;

    public AbstractPanel(ApiClient api, String[] colunas, Class<D> dtoClass) {
        this.api = api;
        this.dtoClass = dtoClass;
        this.modelo = new DefaultTableModel(colunas, 0);
        this.tabela = new JTable(modelo);
        this.tfBusca = new JTextField(15);
        this.cbMostrarInativos = new JCheckBox("Mostrar inativos");
        setLayout(new BorderLayout());
        initToolbar();
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        tabela.setFillsViewportHeight(true);
        int colAtivo = colunas.length - 1;
        tabela.getColumnModel().getColumn(colAtivo).setCellRenderer(new AtivoCellRenderer());
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabela.getSelectedRow() >= 0) editar();
            }
        });
        carregarDados();
    }

    protected abstract String getRota();
    protected abstract String getBuscaRota();
    protected abstract Object[] toRow(D dto);
    protected abstract void abrirDialogCriar();
    protected abstract void abrirDialogEditar(int linha);

    protected String getParametroBusca() {
        return "nome";
    }

    private void initToolbar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        JButton btnNovo = new JButton("Novo");
        btnNovo.addActionListener(e -> abrirDialogCriar());
        tb.add(btnNovo);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editar());
        tb.add(btnEditar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarDados());
        tb.add(btnAtualizar);

        JButton btnToggle = new JButton("Ativar/Inativar");
        btnToggle.addActionListener(e -> toggleStatus());
        tb.add(btnToggle);

        tb.addSeparator();
        tb.add(new JLabel("Buscar " + getParametroBusca() + ":"));
        tb.add(tfBusca);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        tb.add(btnBuscar);

        tb.addSeparator();
        tb.add(cbMostrarInativos);
        cbMostrarInativos.addActionListener(e -> carregarDados());

        add(tb, BorderLayout.NORTH);
    }

    protected void carregarDados() {
        try {
            String path = getRota() + (cbMostrarInativos.isSelected() ? "?inativos=true" : "");
            dadosCarregados = api.getList(path, dtoClass);
            popularTabela(dadosCarregados);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void buscar() {
        String termo = tfBusca.getText().trim();
        if (termo.isEmpty()) { carregarDados(); return; }
        try {
            String path = getRota() + "/buscar?" + getParametroBusca() + "=" + java.net.URLEncoder.encode(termo, "UTF-8");
            dadosCarregados = api.getList(path, dtoClass);
            popularTabela(dadosCarregados);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void popularTabela(List<D> dados) {
        modelo.setRowCount(0);
        for (D dto : dados) {
            modelo.addRow(toRow(dto));
        }
    }

    protected void toggleStatus() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um registro.");
            return;
        }
        D dto = dadosCarregados.get(linha);
        int conf = JOptionPane.showConfirmDialog(this,
            "Deseja " + (dto.isAtivo() ? "inativar" : "ativar") + " " + getRota() + " #" + dto.getId() + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        try {
            api.patch(getRota() + "/" + dto.getId() + "/toggle-status");
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar status: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void editar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um registro.");
            return;
        }
        abrirDialogEditar(linha);
        carregarDados();
    }
}

