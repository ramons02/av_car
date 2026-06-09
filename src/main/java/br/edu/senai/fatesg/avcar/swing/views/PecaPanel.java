package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorController;
import br.edu.senai.fatesg.avcar.business.pecas.PecaController;
import br.edu.senai.fatesg.avcar.business.pecas.PecaDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class PecaPanel extends JPanel {

    @Autowired
    private PecaController pecaController;

    @Autowired
    private FornecedorController fornecedorController;

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField tfBusca;

    public PecaPanel() {
        // construtor vazio — Spring cria o bean
    }

    @PostConstruct
    public void init() {
        this.tfBusca = new JTextField(15);
        this.modelo = new DefaultTableModel(new String[]{
            "ID", "Código Nacional", "Código Interno", "Nome", "Fabricante",
            "Categoria", "Preço Custo", "Preço Venda", "Estoque", "Garantia",
            "Data Compra", "Fornecedor"
        }, 0);
        this.tabela = new JTable(modelo);
        this.tabela.setRowHeight(28);
        this.tabela.removeColumn(this.tabela.getColumnModel().getColumn(0));
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        JButton btnNovo = new JButton("Nova Peça");
        JButton btnEditar = new JButton("Editar");
        JButton btnRefresh = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnEstoqueBaixo = new JButton("Estoque Baixo");
        toolbar.add(btnNovo);
        toolbar.add(btnEditar);
        toolbar.add(btnRefresh);
        toolbar.add(btnExcluir);
        toolbar.add(btnEstoqueBaixo);
        toolbar.addSeparator();
        toolbar.add(new JLabel("  Código Nacional:"));
        toolbar.add(tfBusca);
        JButton btnBuscar = new JButton("Buscar");
        toolbar.add(btnBuscar);

        btnNovo.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            var dto = PecaDialog.showDialog(win, pecaController, fornecedorController);
            if (dto != null) carregarDados();
        });
        btnEditar.addActionListener(e -> editar());
        btnRefresh.addActionListener(e -> carregarDados());
        btnExcluir.addActionListener(e -> excluir());
        btnEstoqueBaixo.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this,
                "Quantidade mínima em estoque:", "Estoque Baixo", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;
            try {
                int min = Integer.parseInt(input.trim());
                var lista = pecaController.estoqueBaixo(min).getBody();
                popularTabela(lista);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Informe um número inteiro.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });
        btnBuscar.addActionListener(e -> buscar());
        tfBusca.addActionListener(e -> buscar());

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) editar();
            }
        });

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    void carregarDados() {
        try {
            var lista = pecaController.listar(false).getBody();
            popularTabela(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        String termo = tfBusca.getText().trim();
        try {
            long codigo = Long.parseLong(termo);
            var lista = pecaController.buscarPorCodigo(codigo).getBody();
            popularTabela(lista);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código Nacional deve ser numérico.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void popularTabela(List<PecaDTO> lista) {
        modelo.setRowCount(0);
        if (lista == null) return;
        lista.forEach(p -> modelo.addRow(new Object[]{
            p.getId(), p.getCodigoNacional(), p.getCodigoInterno(), p.getNome(),
            p.getFabricante(), p.getCategoria(),
            String.format("%.2f", p.getPrecoCusto()),
            String.format("%.2f", p.getPrecoVenda()),
            p.getQuantidadeEstoque(), p.getGarantiaPeca(),
            p.getDataCompraPeca(), p.getFornecedorNome()
        }));
    }

    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        Long id = ((Number) modelo.getValueAt(linha, 0)).longValue();
        String nome = (String) modelo.getValueAt(linha, 3);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Excluir a peça \"" + nome + "\"?", "Confirma exclusão",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pecaController.deletar(id);
                carregarDados();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        }
    }

    private void editar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        Long id = ((Number) modelo.getValueAt(linha, 0)).longValue();
        String codigoNacional = String.valueOf(modelo.getValueAt(linha, 1));
        String codigoInterno = String.valueOf(modelo.getValueAt(linha, 2));
        String nome = (String) modelo.getValueAt(linha, 3);
        String fabricante = (String) modelo.getValueAt(linha, 4);
        String categoria = (String) modelo.getValueAt(linha, 5);
        String precoCusto = (String) modelo.getValueAt(linha, 6);
        String precoVenda = (String) modelo.getValueAt(linha, 7);
        String estoque = String.valueOf(modelo.getValueAt(linha, 8));
        String garantia = String.valueOf(modelo.getValueAt(linha, 9));
        String dataCompra = modelo.getValueAt(linha, 10) != null ? modelo.getValueAt(linha, 10).toString() : "";
        String fornecedor = (String) modelo.getValueAt(linha, 11);
        String descricao = "";

        Window win = SwingUtilities.getWindowAncestor(this);
        var dto = PecaDialog.showEditDialog(win, pecaController, fornecedorController, id,
            codigoNacional, codigoInterno, nome, descricao,
            fabricante, categoria, precoCusto, precoVenda,
            estoque, garantia, dataCompra, fornecedor);
        if (dto != null) carregarDados();
    }
}
