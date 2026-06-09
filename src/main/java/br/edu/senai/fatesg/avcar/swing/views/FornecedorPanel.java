package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorController;
import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class FornecedorPanel extends JPanel {

    @Autowired
    private FornecedorController fornecedorController;

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField tfBusca;
    private JCheckBox cbMostrarInativos;
    private List<FornecedorDTO> dadosCarregados;

    public FornecedorPanel() {
        // construtor vazio — Spring cria o bean
    }

    @PostConstruct
    public void init() {
        this.tfBusca = new JTextField(15);
        this.cbMostrarInativos = new JCheckBox("Mostrar inativos");
        this.modelo = new DefaultTableModel(
            new String[]{"Razão Social", "CNPJ", "Telefone", "Email", "Endereço", "Ativo"}, 0);
        this.tabela = new JTable(modelo);
        this.tabela.setRowHeight(28);
        this.tabela.getColumnModel().getColumn(5).setCellRenderer(new AtivoCellRenderer());
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        JButton btnNovo = new JButton("Novo Fornecedor");
        JButton btnEditar = new JButton("Editar");
        JButton btnRefresh = new JButton("Atualizar");
        JButton btnBuscar = new JButton("Buscar");

        toolbar.add(btnNovo);
        toolbar.add(btnEditar);
        toolbar.add(btnRefresh);
        toolbar.addSeparator();
        toolbar.add(new JLabel("  Buscar nome:"));
        toolbar.add(tfBusca);
        toolbar.add(btnBuscar);
        toolbar.addSeparator();
        toolbar.add(cbMostrarInativos);

        btnNovo.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            FornecedorDTO result = FornecedorDialog.showDialog(win, fornecedorController);
            if (result != null) carregarDados();
        });
        btnEditar.addActionListener(e -> editar());
        btnRefresh.addActionListener(e -> carregarDados());
        btnBuscar.addActionListener(e -> buscar());
        tfBusca.addActionListener(e -> buscar());
        cbMostrarInativos.addActionListener(e -> carregarDados());

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
            dadosCarregados = fornecedorController.listar(cbMostrarInativos.isSelected()).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        String termo = tfBusca.getText().trim();
        if (termo.isEmpty()) { carregarDados(); return; }
        try {
            dadosCarregados = fornecedorController.buscarPorNome(termo).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void popularTabela(List<FornecedorDTO> lista) {
        modelo.setRowCount(0);
        if (lista == null) return;
        lista.forEach(dto -> {
            String telefone = "+" + dto.getDdi() + " (" + dto.getDdd() + ") " + dto.getNumeroFornecedor();
            modelo.addRow(new Object[]{
                dto.getRazaoSocial(),
                ClienteDialog.formatarCNPJ(dto.getCnpj()),
                telefone,
                dto.getEmail(), dto.getEnderecoFornecedor(), dto.isAtivo()
            });
        });
    }

    private void editar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(this, "Selecione um registro."); return; }
        FornecedorDTO dto = dadosCarregados.get(linha);
        Window win = SwingUtilities.getWindowAncestor(this);
        FornecedorDTO result = FornecedorDialog.showEditDialog(win, fornecedorController, dto.getId(),
            dto.getRazaoSocial(), dto.getCnpj(), dto.getDdi(), dto.getDdd(),
            dto.getNumeroFornecedor(), dto.getEmail(), dto.getEnderecoFornecedor(),
            dto.getBairroFornecedor(), dto.getCidadeFornecedor(), dto.getEstadoFornecedor(),
            String.valueOf(dto.getCepFornecedor()));
        if (result != null) carregarDados();
    }
}
