package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.clientes.ClienteController;
import br.edu.senai.fatesg.avcar.business.clientes.ClienteDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class ClientePanel extends JPanel {

    @Autowired
    private ClienteController clienteController;

    @Autowired
    private br.edu.senai.fatesg.avcar.swing.views.telas.CadastroClienteGUI cadastroClienteGUI;

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField tfBusca;
    private JCheckBox cbMostrarInativos;
    private List<ClienteDTO> dadosCarregados;

    public ClientePanel() {
        // construtor vazio — Spring cria o bean
    }

    @PostConstruct
    public void init() {
        this.tfBusca = new JTextField(20);
        this.cbMostrarInativos = new JCheckBox("Mostrar inativos");
        this.modelo = new DefaultTableModel(new String[]{"ID", "Nome", "Tipo", "Documento", "Telefone", "Email", "Ativo"}, 0);
        this.tabela = new JTable(modelo);
        this.tabela.setRowHeight(28);
        this.tabela.removeColumn(this.tabela.getColumnModel().getColumn(0));
        this.tabela.getColumnModel().getColumn(5).setCellRenderer(new AtivoCellRenderer());
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        JButton btnNovo = new JButton("Novo Cliente");
        JButton btnEditar = new JButton("Editar");
        JButton btnRefresh = new JButton("Atualizar");
        JButton btnToggle = new JButton("Ativar/Inativar");
        tfBusca.setMaximumSize(new Dimension(200, 28));
        JButton btnBuscar = new JButton("Buscar");
        toolbar.add(btnNovo);
        toolbar.add(btnEditar);
        toolbar.add(btnRefresh);
        toolbar.add(btnToggle);
        toolbar.addSeparator();
        toolbar.add(new JLabel("  Buscar:"));
        toolbar.add(tfBusca);
        toolbar.add(btnBuscar);

        btnNovo.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(win, "Novo Cliente", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setContentPane(cadastroClienteGUI);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            carregarDados();
        });
        btnEditar.addActionListener(e -> editar());
        btnRefresh.addActionListener(e -> carregarDados());
        btnToggle.addActionListener(e -> toggleStatus());
        btnBuscar.addActionListener(e -> buscar());
        tfBusca.addActionListener(e -> buscar());
        cbMostrarInativos.addActionListener(e -> carregarDados());
        toolbar.add(cbMostrarInativos);

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
            boolean inativos = cbMostrarInativos.isSelected();
            dadosCarregados = clienteController.listar(inativos).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        String termo = tfBusca.getText().trim();
        try {
            dadosCarregados = clienteController.buscarPorNome(termo).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void popularTabela(List<ClienteDTO> clientes) {
        modelo.setRowCount(0);
        clientes.forEach(c -> {
            boolean isPf = "PF".equals(c.getTipo());
            String doc = isPf
                ? ClienteDialog.formatarCPF(c.getDocumento())
                : ClienteDialog.formatarCNPJ(c.getDocumento());
            String tel = ClienteDialog.formatarTelefone(c.getTelefone());
            modelo.addRow(new Object[]{
                c.getId(), c.getNome(), c.getTipo(), doc, tel, c.getEmail(), c.isAtivo()
            });
        });
    }

    private void toggleStatus() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        Long id = ((Number) modelo.getValueAt(linha, 0)).longValue();
        boolean ativo = (boolean) modelo.getValueAt(linha, 6);
        String msg = ativo ? "Inativar este cliente?" : "Ativar este cliente?";
        int confirm = JOptionPane.showConfirmDialog(this, msg, "Confirma",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                clienteController.toggleStatus(id);
                carregarDados();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        }
    }

    private void editar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || dadosCarregados == null || linha >= dadosCarregados.size()) return;
        ClienteDTO c = dadosCarregados.get(linha);

        Window win = SwingUtilities.getWindowAncestor(this);
        ClienteDTO dto = ClienteDialog.showEditDialog(win, clienteController, c.getId(),
            c.getNome(), c.getTipo(), c.getDocumento(), c.getTelefone(), c.getEmail(),
            c.getEndereco() != null ? c.getEndereco() : "",
            c.getBairro() != null ? c.getBairro() : "",
            c.getCidade() != null ? c.getCidade() : "",
            c.getEstado() != null ? c.getEstado() : "",
            c.getCep() != null ? c.getCep() : "",
            null);
        if (dto != null) carregarDados();
    }

    private static class AtivoCellRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private boolean ativo;

        AtivoCellRenderer() {
            setOpaque(true);
            setLayout(new GridBagLayout());
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            ativo = value instanceof Boolean && (Boolean) value;
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            int size = Math.min(getWidth(), getHeight()) / 2;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            g2.setColor(ativo ? new java.awt.Color(76, 175, 80) : new java.awt.Color(244, 67, 54));
            g2.fillOval(x, y, size, size);
            g2.dispose();
        }
    }
}
