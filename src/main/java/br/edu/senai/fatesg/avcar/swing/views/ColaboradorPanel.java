package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.colaboradores.ColaboradorController;
import br.edu.senai.fatesg.avcar.business.colaboradores.ColaboradorDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class ColaboradorPanel extends JPanel {

    @Autowired
    private ColaboradorController colaboradorController;

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField tfBusca;
    private List<ColaboradorDTO> dadosCarregados;

    public ColaboradorPanel() {
        // construtor vazio — Spring cria o bean
    }

    @PostConstruct
    public void init() {
        this.tfBusca = new JTextField(15);
        this.modelo = new DefaultTableModel(
            new String[]{"Nome", "CPF", "Telefone", "Email", "Funções"}, 0);
        this.tabela = new JTable(modelo);
        this.tabela.setRowHeight(28);
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        JButton btnNovo = new JButton("Novo Colaborador");
        JButton btnEditar = new JButton("Editar");
        JButton btnRefresh = new JButton("Atualizar");
        toolbar.add(btnNovo);
        toolbar.add(btnEditar);
        toolbar.add(btnRefresh);
        toolbar.addSeparator();
        toolbar.add(new JLabel("  Buscar:"));
        toolbar.add(tfBusca);
        JButton btnBuscar = new JButton("Buscar");
        toolbar.add(btnBuscar);

        btnNovo.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            var dto = ColaboradorDialog.showDialog(win, colaboradorController);
            if (dto != null) carregarDados();
        });
        btnEditar.addActionListener(e -> editar());
        btnRefresh.addActionListener(e -> carregarDados());
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
            dadosCarregados = colaboradorController.listar(false).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        String termo = tfBusca.getText().trim();
        try {
            dadosCarregados = colaboradorController.buscarPorNome(termo).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void popularTabela(List<ColaboradorDTO> lista) {
        modelo.setRowCount(0);
        if (lista == null) return;
        lista.forEach(c -> modelo.addRow(new Object[]{
            c.getNome(),
            ClienteDialog.formatarCPF(c.getCpf()),
            ClienteDialog.formatarTelefone(c.getTelefone()),
            c.getEmail(),
            String.join(", ", c.getFuncoes())
        }));
    }

    private void editar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || dadosCarregados == null || linha >= dadosCarregados.size()) return;
        var c = dadosCarregados.get(linha);
        Window win = SwingUtilities.getWindowAncestor(this);
        var dto = ColaboradorDialog.showEditDialog(win, colaboradorController, c.getId(),
            c.getNome(), c.getCpf(), c.getTelefone(), c.getEmail(),
            String.join(", ", c.getFuncoes()));
        if (dto != null) carregarDados();
    }
}
