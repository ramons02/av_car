package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.clientes.ClienteController;
import br.edu.senai.fatesg.avcar.business.veiculos.VeiculoController;
import br.edu.senai.fatesg.avcar.business.veiculos.VeiculoDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.HashMap;

@org.springframework.stereotype.Component
public class VeiculoPanel extends JPanel {

    @Autowired
    VeiculoController controller;

    @Autowired
    ClienteController clienteController;

    private final JTable tabela;
    private final DefaultTableModel modelo;
    private final JTextField tfBusca = new JTextField(15);
    private List<VeiculoDTO> dadosCarregados;
    private static final HashMap<String, ImageIcon> logoCache = new HashMap<>();

    public VeiculoPanel() {
        this.modelo = new DefaultTableModel(new String[]{
            "Placa", "Chassi", "Ano Fab.", "Ano Mod.", "Cor", "KM", "Marca", "Modelo", "Cliente"
        }, 0);
        this.tabela = new JTable(modelo);
        this.tabela.setRowHeight(40);
    }

    @PostConstruct
    public void init() {
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        JButton btnNovo = new JButton("Novo Veículo");
        JButton btnEditar = new JButton("Editar");
        JButton btnRefresh = new JButton("Atualizar");
        toolbar.add(btnNovo);
        toolbar.add(btnEditar);
        toolbar.add(btnRefresh);
        toolbar.addSeparator();
        toolbar.add(new JLabel("  Buscar placa:"));
        toolbar.add(tfBusca);
        JButton btnBuscar = new JButton("Buscar");
        toolbar.add(btnBuscar);

        btnNovo.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            var dto = VeiculoDialog.showDialog(win, controller, clienteController);
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

        tabela.setRowHeight(40);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(6).setMaxWidth(80);
        tabela.getColumnModel().getColumn(6).setCellRenderer(new LogoTipoRenderer());

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void carregarDados() {
        try {
            dadosCarregados = controller.listar(false).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        String termo = tfBusca.getText().trim();
        try {
            dadosCarregados = controller.buscarPorPlaca(termo).getBody();
            popularTabela(dadosCarregados);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void popularTabela(List<VeiculoDTO> lista) {
        modelo.setRowCount(0);
        for (var v : lista) {
            ImageIcon logo = carregarLogotipo(v.getMarcaLogoUrl(), v.getMarcaNome());
            if (logo != null) {
                logo.setDescription(v.getMarcaNome());
            }
            modelo.addRow(new Object[]{
                v.getPlaca(), v.getChassi(),
                v.getAnoFabricacao(), v.getAnoModelo(),
                v.getCor() != null ? v.getCor() : "",
                v.getQuilometragem(),
                new MarcaComIcone(logo, v.getMarcaNome()),
                v.getModeloNome(),
                v.getClienteNome() != null ? v.getClienteNome() : ""
            });
        }
    }

    private ImageIcon carregarLogotipo(String path, String fallbackNome) {
        if (path == null || path.isBlank()) return null;
        ImageIcon cached = logoCache.get(path);
        if (cached != null) return cached;
        try {
            var is = getClass().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                System.err.println("[AV-CAR] Logo resource not found: " + path);
                return null;
            }
            BufferedImage origem = ImageIO.read(is);
            is.close();
            if (origem == null) {
                System.err.println("[AV-CAR] ImageIO failed to read: " + path);
                return null;
            }
            BufferedImage redim = new BufferedImage(36, 28, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = redim.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(origem, 0, 0, 36, 28, null);
            g.dispose();
            var icon = new ImageIcon(redim);
            logoCache.put(path, icon);
            return icon;
        } catch (Exception e) {
            System.err.println("[AV-CAR] Logo load error for " + path + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void editar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || dadosCarregados == null || linha >= dadosCarregados.size()) return;
        VeiculoDTO v = dadosCarregados.get(linha);

        Window win = SwingUtilities.getWindowAncestor(this);
        var dto = VeiculoDialog.showEditDialog(win, controller, clienteController, v.getId(),
            v.getPlaca(), v.getChassi(), String.valueOf(v.getAnoFabricacao()),
            String.valueOf(v.getAnoModelo()), v.getMarcaNome(), v.getModeloNome(),
            v.getMarcaLogoUrl(), v.getClienteId(), v.getClienteNome(),
            v.getCor(), String.valueOf(v.getQuilometragem()), v.getAcessorios());
        if (dto != null) carregarDados();
    }

    record MarcaComIcone(ImageIcon icon, String nome) {
        public String toString() { return nome; }
    }

    static class LogoTipoRenderer extends DefaultTableCellRenderer {
        LogoTipoRenderer() {
            setHorizontalAlignment(CENTER);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
            if (value instanceof MarcaComIcone m) {
                if (m.icon != null) {
                    setIcon(m.icon);
                    setText("");
                    setToolTipText(m.nome);
                } else {
                    setIcon(null);
                    setText(m.nome);
                    setToolTipText(null);
                }
            } else {
                setIcon(null);
            }
            return this;
        }
    }
}
