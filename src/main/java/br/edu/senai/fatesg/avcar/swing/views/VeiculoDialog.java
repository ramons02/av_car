package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.clientes.ClienteController;
import br.edu.senai.fatesg.avcar.business.clientes.ClienteDTO;
import br.edu.senai.fatesg.avcar.business.veiculos.MarcaDTO;
import br.edu.senai.fatesg.avcar.business.veiculos.ModeloDTO;
import br.edu.senai.fatesg.avcar.business.veiculos.VeiculoController;
import br.edu.senai.fatesg.avcar.business.veiculos.VeiculoDTO;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.HashMap;

public class VeiculoDialog extends JDialog {
    private final VeiculoController controller;
    private final ClienteController clienteController;
    private final boolean editMode;
    private final Long editId;
    private VeiculoDTO result;

    static class MarcaItem {
        final Long id;
        final String nome;
        final String logoUrl;

        MarcaItem(Long id, String nome, String logoUrl) {
            this.id = id;
            this.nome = nome;
            this.logoUrl = logoUrl;
        }

        public String toString() { return nome; }
    }

    static class ClienteItem {
        final Long id;
        final String nome;

        ClienteItem(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public String toString() { return nome; }
    }

    private final JTextField tfPlaca = new JTextField(15);
    private final JTextField tfChassi = new JTextField(20);
    private final JTextField tfAnoFab = new JTextField(6);
    private final JTextField tfAnoMod = new JTextField(6);
    private final JTextField tfCor = new JTextField(10);
    private final JTextField tfQuilometragem = new JTextField(8);
    private final JTextField tfAcessorios = new JTextField(25);
    private final JComboBox<MarcaItem> cbMarca = new JComboBox<>();
    private final JComboBox<String> cbModelo = new JComboBox<>();
    private final JComboBox<ClienteItem> cbCliente = new JComboBox<>();
    private List<ModeloDTO> modelos;

    private static final HashMap<String, ImageIcon> logoCache = new HashMap<>();

    public VeiculoDialog(Window owner, VeiculoController controller, ClienteController clienteController,
                         Long editId, String... valores) {
        super(owner, editId != null ? "Editar Veículo" : "Novo Veículo", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        this.clienteController = clienteController;
        this.editId = editId;
        this.editMode = editId != null;
        initComponents();
        carregarMarcas();
        carregarClientes();
        if (editMode && valores.length >= 5) {
            tfPlaca.setText(valores[0]);
            tfChassi.setText(valores[1]);
            tfAnoFab.setText(valores[2]);
            tfAnoMod.setText(valores[3]);
            String logoUrl = valores.length >= 7 ? valores[6] : null;
            selecionarMarcaModelo(valores[4], valores[5], logoUrl);
            if (valores.length >= 9) {
                String clienteNome = valores[8];
                SwingUtilities.invokeLater(() -> {
                    for (int i = 0; i < cbCliente.getItemCount(); i++) {
                        ClienteItem item = cbCliente.getItemAt(i);
                        if (item != null && item.nome.equals(clienteNome)) {
                            cbCliente.setSelectedIndex(i);
                            break;
                        }
                    }
                });
            }
            if (valores.length >= 10) tfCor.setText(valores[9]);
            if (valores.length >= 11) tfQuilometragem.setText(valores[10]);
            if (valores.length >= 12) tfAcessorios.setText(valores[11]);
        }
        pack();
        setLocationRelativeTo(owner);
    }

    private void carregarClientes() {
        try {
            List<ClienteDTO> clientes = clienteController.listar(true).getBody();
            if (clientes != null) {
                for (ClienteDTO c : clientes) {
                    cbCliente.addItem(new ClienteItem(c.getId(), c.getNome()));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void carregarMarcas() {
        try {
            List<MarcaDTO> marcas = controller.listarMarcas().getBody();
            cbMarca.addItem(new MarcaItem(null, "Selecione...", null));
            if (marcas != null) {
                for (MarcaDTO m : marcas) {
                    cbMarca.addItem(new MarcaItem(m.getIdMarca(), m.getNomeMarca(), m.getLogoUrl()));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar marcas: " + e.getMessage());
        }
    }

    private void carregarModelos(Long marcaId) {
        try {
            modelos = controller.listarModelos(marcaId).getBody();
            cbModelo.removeAllItems();
            cbModelo.addItem("Selecione...");
            if (modelos != null) {
                for (ModeloDTO m : modelos) {
                    cbModelo.addItem(m.getNomeModelo() + " (" + m.getIdModelo() + ")");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar modelos: " + e.getMessage());
        }
    }

    private void selecionarMarcaModelo(String marcaNome, String modeloNome, String logoUrl) {
        for (int i = 0; i < cbMarca.getItemCount(); i++) {
            MarcaItem item = cbMarca.getItemAt(i);
            if (item != null && item.nome.equals(marcaNome)) {
                cbMarca.setSelectedIndex(i);
                break;
            }
        }
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < cbModelo.getItemCount(); i++) {
                String item = cbModelo.getItemAt(i);
                if (item != null && item.startsWith(modeloNome + " (")) {
                    cbModelo.setSelectedIndex(i);
                    break;
                }
            }
        });
    }

    private Long extractId(String comboItem) {
        if (comboItem == null || !comboItem.contains("(")) return null;
        int start = comboItem.lastIndexOf('(') + 1;
        int end = comboItem.lastIndexOf(')');
        if (start >= end) return null;
        return Long.parseLong(comboItem.substring(start, end));
    }

    private void initComponents() {
        AbstractDialog.setApenasDigitos(tfAnoFab);
        AbstractDialog.setApenasDigitos(tfAnoMod);
        AbstractDialog.setApenasDigitos(tfQuilometragem);

        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 8, 4, 8);
        c.anchor = GridBagConstraints.WEST;

        cbMarca.setRenderer(new MarcaListRenderer());
        cbMarca.addActionListener(e -> {
            MarcaItem selected = (MarcaItem) cbMarca.getSelectedItem();
            if (selected != null && selected.id != null) carregarModelos(selected.id);
            else { cbModelo.removeAllItems(); cbModelo.addItem("Selecione..."); }
        });

        addRow(form, c, 0, "Placa:", tfPlaca);
        addRow(form, c, 1, "Chassi:", tfChassi);
        addRow(form, c, 2, "Ano Fabricação:", tfAnoFab);
        addRow(form, c, 3, "Ano Modelo:", tfAnoMod);
        addRow(form, c, 4, "Cor:", tfCor);
        addRow(form, c, 5, "Quilometragem:", tfQuilometragem);
        addRow(form, c, 6, "Acessórios:", tfAcessorios);
        addRow(form, c, 7, "Marca:", cbMarca);
        addRow(form, c, 8, "Modelo:", cbModelo);
        addRow(form, c, 9, "Cliente:", cbCliente);

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
        String placa = tfPlaca.getText().trim().toUpperCase();
        String chassi = tfChassi.getText().trim();
        String anoFab = tfAnoFab.getText().trim();
        String anoMod = tfAnoMod.getText().trim();
        String cor = tfCor.getText().trim();
        int quilometragem = 0;
        try {
            quilometragem = Integer.parseInt(tfQuilometragem.getText().trim());
        } catch (NumberFormatException ignored) {}
        String acessorios = tfAcessorios.getText().trim();
        MarcaItem marcaItem = (MarcaItem) cbMarca.getSelectedItem();
        Long marcaId = marcaItem != null ? marcaItem.id : null;
        Long modeloId = extractId((String) cbModelo.getSelectedItem());
        ClienteItem clienteItem = (ClienteItem) cbCliente.getSelectedItem();
        Long clienteId = clienteItem != null ? clienteItem.id : null;

        if (placa.isEmpty()) { JOptionPane.showMessageDialog(this, "Placa é obrigatória."); return; }
        if (anoFab.isEmpty()) { JOptionPane.showMessageDialog(this, "Ano fabricação é obrigatório."); return; }
        if (anoMod.isEmpty()) { JOptionPane.showMessageDialog(this, "Ano modelo é obrigatório."); return; }
        if (marcaId == null) { JOptionPane.showMessageDialog(this, "Selecione marca e modelo."); return; }
        if (modeloId == null) { JOptionPane.showMessageDialog(this, "Selecione modelo."); return; }
        if (clienteId == null) { JOptionPane.showMessageDialog(this, "Selecione um cliente."); return; }

        try {
            int af = Integer.parseInt(anoFab);
            int am = Integer.parseInt(anoMod);
            VeiculoController.VeiculoRequest req = new VeiculoController.VeiculoRequest(
                placa, chassi, af, am, cor, quilometragem, acessorios, modeloId, clienteId);

            if (editMode) {
                result = controller.atualizar(editId, req).getBody();
            } else {
                result = controller.salvar(req).getBody();
            }
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Anos devem ser números inteiros.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public VeiculoDTO getResult() { return result; }

    public static VeiculoDTO showDialog(Window owner, VeiculoController controller,
                                         ClienteController clienteController) {
        VeiculoDialog d = new VeiculoDialog(owner, controller, clienteController, null);
        d.setVisible(true);
        return d.getResult();
    }

    public static VeiculoDTO showEditDialog(Window owner, VeiculoController controller,
            ClienteController clienteController, Long id,
            String placa, String chassi, String anoFab, String anoMod,
            String marcaNome, String modeloNome, String marcaLogoUrl,
            Long clienteId, String clienteNome, String cor,
            String quilometragem, String acessorios) {
        VeiculoDialog d = new VeiculoDialog(owner, controller, clienteController, id,
            placa, chassi, anoFab, anoMod, marcaNome, modeloNome, marcaLogoUrl,
            clienteId != null ? String.valueOf(clienteId) : null, clienteNome,
            cor, quilometragem, acessorios);
        d.setVisible(true);
        return d.getResult();
    }

    static class MarcaListRenderer extends JLabel implements ListCellRenderer<MarcaItem> {
        MarcaListRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList<? extends MarcaItem> list, MarcaItem value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setFont(list.getFont());
            if (value == null || value.id == null) {
                setIcon(null);
                setText(value != null ? value.nome : "");
                setToolTipText(null);
                return this;
            }
            setToolTipText(value.nome);
            ImageIcon icon = null;
            if (value.logoUrl != null && !value.logoUrl.isBlank()) {
                icon = logoCache.get(value.logoUrl);
                if (icon == null) {
                    try {
                        var is = getClass().getClassLoader().getResourceAsStream(value.logoUrl);
                        if (is != null) {
                            BufferedImage origem = ImageIO.read(is);
                            is.close();
                            if (origem != null) {
                                BufferedImage redim = new BufferedImage(36, 28, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g = redim.createGraphics();
                                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g.drawImage(origem, 0, 0, 36, 28, null);
                                g.dispose();
                                icon = new ImageIcon(redim);
                                logoCache.put(value.logoUrl, icon);
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
            if (icon != null) {
                setIcon(icon);
                setText("");
            } else {
                setIcon(null);
                setText(value.nome);
            }
            return this;
        }
    }
}
