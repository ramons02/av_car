package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.colaboradores.ColaboradorController;
import br.edu.senai.fatesg.avcar.business.colaboradores.ColaboradorDTO;
import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorController;
import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorDTO;
import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServicoController;
import br.edu.senai.fatesg.avcar.business.pecas.PecaController;
import br.edu.senai.fatesg.avcar.business.pecas.PecaDTO;
import br.edu.senai.fatesg.avcar.business.servicos.ItemServicoDTO;
import br.edu.senai.fatesg.avcar.business.pecas.ItemPecaDTO;
import br.edu.senai.fatesg.avcar.business.servicos.ServicoController;
import br.edu.senai.fatesg.avcar.business.servicos.ServicoDTO;
import br.edu.senai.fatesg.avcar.business.servicos.ServicoExternoDTO;
import br.edu.senai.fatesg.avcar.datastructures.CalculoOS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItensOSDialog extends JDialog {
    private final OrdemServicoController ordemServicoController;
    private final ServicoController servicoController;
    private final PecaController pecaController;
    private final ColaboradorController colaboradorController;
    private final FornecedorController fornecedorController;
    private final Long osId;
    private boolean modified;

    private final DefaultTableModel modelServicos;
    private final DefaultTableModel modelPecas;
    private final DefaultTableModel modelExternos;
    private final JTable tabServicos = new JTable();
    private final JTable tabPecas = new JTable();
    private final JTable tabExternos = new JTable();
    private final JLabel lblTotal = new JLabel(" ");

    public ItensOSDialog(Window owner, OrdemServicoController ordemServicoController,
                         ServicoController servicoController, PecaController pecaController,
                         ColaboradorController colaboradorController, FornecedorController fornecedorController,
                         Long osId, String osNumero) {
        super(owner, "Itens da OS " + osNumero, ModalityType.APPLICATION_MODAL);
        this.ordemServicoController = ordemServicoController;
        this.servicoController = servicoController;
        this.pecaController = pecaController;
        this.colaboradorController = colaboradorController;
        this.fornecedorController = fornecedorController;
        this.osId = osId;

        modelServicos = new DefaultTableModel(new String[]{"ID", "Serviço", "Qtd", "Valor Unit.", "Subtotal"}, 0);
        modelPecas = new DefaultTableModel(new String[]{"ID", "Peça", "Qtd", "Valor Unit.", "Subtotal"}, 0);
        modelExternos = new DefaultTableModel(new String[]{"ID", "Fornecedor", "Descrição", "Valor", "Garantia (dias)"}, 0);
        tabServicos.setModel(modelServicos);
        tabPecas.setModel(modelPecas);
        tabExternos.setModel(modelExternos);

        initComponents();
        carregarDados();
        pack();
        setSize(750, 500);
        setLocationRelativeTo(owner);
    }

    private JPanel criarAbaServicos() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(tabServicos), BorderLayout.CENTER);

        JToolBar tb = new JToolBar();
        JButton btnAdd = new JButton("Adicionar Serviço");
        JButton btnDel = new JButton("Remover");
        tb.add(btnAdd);
        tb.add(btnDel);
        p.add(tb, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> adicionarServico());
        btnDel.addActionListener(e -> removerItemServico());
        return p;
    }

    private JPanel criarAbaPecas() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(tabPecas), BorderLayout.CENTER);

        JToolBar tb = new JToolBar();
        JButton btnAdd = new JButton("Adicionar Peça");
        JButton btnDel = new JButton("Remover");
        tb.add(btnAdd);
        tb.add(btnDel);
        p.add(tb, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> adicionarPeca());
        btnDel.addActionListener(e -> removerItemPeca());
        return p;
    }

    private JPanel criarAbaExternos() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(tabExternos), BorderLayout.CENTER);

        JToolBar tb = new JToolBar();
        JButton btnAdd = new JButton("Adicionar Serv. Externo");
        JButton btnDel = new JButton("Remover");
        tb.add(btnAdd);
        tb.add(btnDel);
        p.add(tb, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> adicionarServicoExterno());
        btnDel.addActionListener(e -> removerItemExterno());
        return p;
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Serviços", criarAbaServicos());
        abas.addTab("Peças", criarAbaPecas());
        abas.addTab("Serv. Externos", criarAbaExternos());
        add(abas, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        lblTotal.setFont(new Font("Monospaced", Font.BOLD, 13));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        south.add(lblTotal, BorderLayout.WEST);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        botoes.add(btnFechar);
        south.add(botoes, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);
    }

    private void carregarDados() {
        carregarServicos();
        carregarPecas();
        carregarExternos();
    }

    private void carregarServicos() {
        try {
            var lista = ordemServicoController.listarItensServico(osId).getBody();
            modelServicos.setRowCount(0);
            if (lista != null) {
                for (ItemServicoDTO is : lista) {
                    modelServicos.addRow(new Object[]{
                        is.getId(), is.getServicoNome(), is.getQuantidade(),
                        String.format("R$ %.2f", is.getValorUnitario()),
                        String.format("R$ %.2f", is.getSubtotal())
                    });
                }
            }
            atualizarTotal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar serviços: " + e.getMessage());
        }
    }

    private void carregarPecas() {
        try {
            var lista = ordemServicoController.listarItensPeca(osId).getBody();
            modelPecas.setRowCount(0);
            if (lista != null) {
                for (ItemPecaDTO ip : lista) {
                    modelPecas.addRow(new Object[]{
                        ip.getId(), ip.getPecaNome(), ip.getQuantidade(),
                        String.format("R$ %.2f", ip.getValorUnitario()),
                        String.format("R$ %.2f", ip.getSubtotal())
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar peças: " + e.getMessage());
        }
        atualizarTotal();
    }

    private void carregarExternos() {
        try {
            var lista = ordemServicoController.listarServicosExternos(osId).getBody();
            modelExternos.setRowCount(0);
            if (lista != null) {
                for (ServicoExternoDTO se : lista) {
                    modelExternos.addRow(new Object[]{
                        se.getId(), se.getFornecedorNome(), se.getDescricao(),
                        String.format("R$ %.2f", se.getValor()), se.getGarantiaDias()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar serv. externos: " + e.getMessage());
        }
        atualizarTotal();
    }

    private double parseValor(Object obj) {
        if (obj == null) return 0;
        try {
            String s = obj.toString().replace("R$ ", "").replace(",", ".").trim();
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void atualizarTotal() {
        List<Double> valsServicos = new ArrayList<>();
        for (int i = 0; i < modelServicos.getRowCount(); i++) {
            valsServicos.add(parseValor(modelServicos.getValueAt(i, 4)));
        }
        List<Double> valsPecas = new ArrayList<>();
        for (int i = 0; i < modelPecas.getRowCount(); i++) {
            valsPecas.add(parseValor(modelPecas.getValueAt(i, 4)));
        }
        List<Double> valsExternos = new ArrayList<>();
        for (int i = 0; i < modelExternos.getRowCount(); i++) {
            valsExternos.add(parseValor(modelExternos.getValueAt(i, 3)));
        }
        double total = CalculoOS.calcularValorTotal(valsServicos, valsPecas, valsExternos, 0);
        lblTotal.setText(String.format(
            "Serviços: R$ %.2f  |  Peças: R$ %.2f  |  Externos: R$ %.2f  |  TOTAL: R$ %.2f",
            CalculoOS.somarValores(valsServicos, 0),
            CalculoOS.somarValores(valsPecas, 0),
            CalculoOS.somarValores(valsExternos, 0),
            total));
    }

    private void adicionarServico() {
        try {
            List<ServicoDTO> servicos = servicoController.listar(false).getBody();
            if (servicos == null || servicos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum serviço cadastrado.");
                return;
            }
            var nomes = servicos.stream().map(ServicoDTO::getNomeServico).toArray(String[]::new);
            String escolha = (String) JOptionPane.showInputDialog(this,
                "Selecione o serviço:", "Adicionar Serviço",
                JOptionPane.QUESTION_MESSAGE, null, nomes, nomes[0]);
            if (escolha == null) return;

            ServicoDTO sel = servicos.stream()
                .filter(s -> escolha.equals(s.getNomeServico())).findFirst().orElse(null);
            if (sel == null) return;
            double valor = sel.getValorServico();

            JTextField tfQtd = new JTextField("1", 10);
            AbstractDialog.setApenasDigitos(tfQtd);
            int opt = JOptionPane.showConfirmDialog(this, new Object[]{"Quantidade:", tfQtd},
                "Quantidade", JOptionPane.OK_CANCEL_OPTION);
            if (opt != JOptionPane.OK_OPTION) return;
            String qtdStr = tfQtd.getText().trim();
            if (qtdStr.isEmpty()) return;
            int qtd = Integer.parseInt(qtdStr);

            List<ColaboradorDTO> colaboradores = colaboradorController.listar(false).getBody();
            if (colaboradores == null || colaboradores.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum colaborador cadastrado.");
                return;
            }
            var nomesColab = colaboradores.stream().map(ColaboradorDTO::getNome).toArray(String[]::new);
            String escolhaColab = (String) JOptionPane.showInputDialog(this,
                "Selecione o responsável:", "Colaborador",
                JOptionPane.QUESTION_MESSAGE, null, nomesColab, nomesColab[0]);
            if (escolhaColab == null) return;
            ColaboradorDTO selColab = colaboradores.stream()
                .filter(c -> escolhaColab.equals(c.getNome())).findFirst().orElse(null);
            if (selColab == null) return;

            OrdemServicoController.ItemServicoRequest req = new OrdemServicoController.ItemServicoRequest(
                sel.getId(), qtd, valor, null, null, null, selColab.getId());
            ordemServicoController.adicionarItemServico(osId, req);
            modified = true;
            carregarServicos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void adicionarPeca() {
        try {
            List<PecaDTO> pecas = pecaController.listar(false).getBody();
            if (pecas == null || pecas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma peça cadastrada.");
                return;
            }
            var nomes = pecas.stream().map(PecaDTO::getNome).toArray(String[]::new);
            String escolha = (String) JOptionPane.showInputDialog(this,
                "Selecione a peça:", "Adicionar Peça",
                JOptionPane.QUESTION_MESSAGE, null, nomes, nomes[0]);
            if (escolha == null) return;

            PecaDTO sel = pecas.stream()
                .filter(s -> escolha.equals(s.getNome())).findFirst().orElse(null);
            if (sel == null) return;
            double preco = sel.getPrecoVenda();

            JTextField tfQtd = new JTextField("1", 10);
            AbstractDialog.setApenasDigitos(tfQtd);
            int opt = JOptionPane.showConfirmDialog(this, new Object[]{"Quantidade:", tfQtd},
                "Quantidade", JOptionPane.OK_CANCEL_OPTION);
            if (opt != JOptionPane.OK_OPTION) return;
            String qtdStr = tfQtd.getText().trim();
            if (qtdStr.isEmpty()) return;
            int qtd = Integer.parseInt(qtdStr);

            OrdemServicoController.ItemPecaRequest req = new OrdemServicoController.ItemPecaRequest(
                sel.getId(), qtd, preco);
            ordemServicoController.adicionarItemPeca(osId, req);
            modified = true;
            carregarPecas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void adicionarServicoExterno() {
        try {
            List<FornecedorDTO> fornecedores = fornecedorController.listar(false).getBody();
            if (fornecedores == null || fornecedores.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum fornecedor cadastrado.");
                return;
            }
            var nomes = fornecedores.stream().map(FornecedorDTO::getRazaoSocial).toArray(String[]::new);
            String escolha = (String) JOptionPane.showInputDialog(this,
                "Selecione o fornecedor:", "Adicionar Serv. Externo",
                JOptionPane.QUESTION_MESSAGE, null, nomes, nomes[0]);
            if (escolha == null) return;

            FornecedorDTO sel = fornecedores.stream()
                .filter(s -> escolha.equals(s.getRazaoSocial())).findFirst().orElse(null);
            if (sel == null) return;

            String desc = JOptionPane.showInputDialog(this, "Descrição do serviço:");
            if (desc == null || desc.isBlank()) return;

            JTextField tfValor = new JTextField(10);
            AbstractDialog.setApenasDecimal(tfValor);
            int optValor = JOptionPane.showConfirmDialog(this, new Object[]{"Valor:", tfValor},
                "Valor", JOptionPane.OK_CANCEL_OPTION);
            if (optValor != JOptionPane.OK_OPTION) return;
            String valorStr = tfValor.getText().trim();
            if (valorStr.isEmpty()) return;
            double valor = Double.parseDouble(valorStr.replace(',', '.'));

            JTextField tfDias = new JTextField("90", 10);
            AbstractDialog.setApenasDigitos(tfDias);
            int optDias = JOptionPane.showConfirmDialog(this, new Object[]{"Prazo de garantia (dias):", tfDias},
                "Garantia", JOptionPane.OK_CANCEL_OPTION);
            if (optDias != JOptionPane.OK_OPTION) return;
            String diasStr = tfDias.getText().trim();
            if (diasStr.isEmpty()) return;
            int dias = Integer.parseInt(diasStr);

            OrdemServicoController.ServicoExternoRequest req = new OrdemServicoController.ServicoExternoRequest(
                sel.getId(), desc, valor, dias);
            ordemServicoController.adicionarServicoExterno(osId, req);
            modified = true;
            carregarExternos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void removerItemServico() {
        int linha = tabServicos.getSelectedRow();
        if (linha < 0) return;
        Object val = modelServicos.getValueAt(linha, 0);
        if (!(val instanceof Number)) return;
        Long itemId = ((Number) val).longValue();
        int confirm = JOptionPane.showConfirmDialog(this, "Remover este item?", "Confirmar",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            ordemServicoController.removerItemServico(osId, itemId);
            modified = true;
            modelServicos.removeRow(linha);
            atualizarTotal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover: " + e.getMessage());
        }
    }

    private void removerItemPeca() {
        int linha = tabPecas.getSelectedRow();
        if (linha < 0) return;
        Object val = modelPecas.getValueAt(linha, 0);
        if (!(val instanceof Number)) return;
        Long itemId = ((Number) val).longValue();
        int confirm = JOptionPane.showConfirmDialog(this, "Remover este item?", "Confirmar",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            ordemServicoController.removerItemPeca(osId, itemId);
            modified = true;
            modelPecas.removeRow(linha);
            atualizarTotal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover: " + e.getMessage());
        }
    }

    private void removerItemExterno() {
        int linha = tabExternos.getSelectedRow();
        if (linha < 0) return;
        Object val = modelExternos.getValueAt(linha, 0);
        if (!(val instanceof Number)) return;
        Long itemId = ((Number) val).longValue();
        int confirm = JOptionPane.showConfirmDialog(this, "Remover este item?", "Confirmar",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            ordemServicoController.removerServicoExterno(osId, itemId);
            modified = true;
            modelExternos.removeRow(linha);
            atualizarTotal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover: " + e.getMessage());
        }
    }

    public boolean isModified() { return modified; }

    public static boolean showDialog(Window owner, OrdemServicoController ordemServicoController,
                                     ServicoController servicoController, PecaController pecaController,
                                     ColaboradorController colaboradorController,
                                     FornecedorController fornecedorController,
                                     Long osId, String osNumero) {
        ItensOSDialog d = new ItensOSDialog(owner, ordemServicoController, servicoController,
            pecaController, colaboradorController, fornecedorController, osId, osNumero);
        d.setVisible(true);
        return d.isModified();
    }
}
