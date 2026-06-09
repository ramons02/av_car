package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.parceiros.ParceiroDTO;
import br.edu.senai.fatesg.avcar.business.parceiros.ParceiroExternoController;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class ParceiroPanel extends JPanel {

    @Autowired
    private ParceiroExternoController parceiroController;

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField tfBusca;
    private JCheckBox cbMostrarInativos;
    private List<ParceiroDTO> dadosCarregados;

    public ParceiroPanel() {
        // construtor vazio — Spring cria o bean
    }

    @PostConstruct
    public void init() {
        this.tfBusca = new JTextField(15);
        this.cbMostrarInativos = new JCheckBox("Mostrar inativos");
        this.modelo = new DefaultTableModel(
            new String[]{"Nome", "CNPJ", "Tipo Serviço", "Telefone", "Email", "Ativo"}, 0);
        this.tabela = new JTable(modelo);
        this.tabela.setRowHeight(28);
        this.tabela.getColumnModel().getColumn(5).setCellRenderer(new AtivoCellRenderer());
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        JButton btnNovo = new JButton("Novo Parceiro");
        JButton btnEditar = new JButton("Editar");
        JButton btnRefresh = new JButton("Atualizar");
        JButton btnToggle = new JButton("Ativar/Inativar");
        JButton btnBuscar = new JButton("Buscar");

        toolbar.add(btnNovo);
        toolbar.add(btnEditar);
        toolbar.add(btnRefresh);
        toolbar.add(btnToggle);
        toolbar.addSeparator();
        toolbar.add(new JLabel("  Buscar nome:"));
        toolbar.add(tfBusca);
        toolbar.add(btnBuscar);
        toolbar.addSeparator();
        toolbar.add(cbMostrarInativos);

        btnNovo.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            ParceiroDTO result = ParceiroDialog.showDialog(win, parceiroController);
            if (result != null) carregarDados();
        });
        btnEditar.addActionListener(e -> editar());
        btnRefresh.addActionListener(e -> carregarDados());
        btnToggle.addActionListener(e -> toggleStatus());
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
            dadosCarregados = parceiroController.listar(cbMostrarInativos.isSelected()).getBody();
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
            dadosCarregados = parceiroController.buscarPorNome(termo).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void popularTabela(List<ParceiroDTO> lista) {
        modelo.setRowCount(0);
        if (lista == null) return;
        lista.forEach(dto -> modelo.addRow(new Object[]{
            dto.getNome(),
            ClienteDialog.formatarCNPJ(dto.getCnpj()),
            dto.getTipoServico(),
            ClienteDialog.formatarTelefone(dto.getTelefone()),
            dto.getEmail(), dto.isAtivo()
        }));
    }

    private void toggleStatus() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(this, "Selecione um registro."); return; }
        ParceiroDTO dto = dadosCarregados.get(linha);
        int conf = JOptionPane.showConfirmDialog(this,
            "Deseja " + (dto.isAtivo() ? "inativar" : "ativar") + " o parceiro #" + dto.getId() + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        try {
            parceiroController.toggleStatus(dto.getId());
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar status: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(this, "Selecione um registro."); return; }
        ParceiroDTO dto = dadosCarregados.get(linha);
        Window win = SwingUtilities.getWindowAncestor(this);
        ParceiroDTO result = ParceiroDialog.showEditDialog(win, parceiroController, dto.getId(),
            dto.getNome(), dto.getCnpj(), dto.getTipoServico(),
            dto.getTelefone(), dto.getEmail(), String.valueOf(dto.isAtivo()));
        if (result != null) carregarDados();
    }
}
