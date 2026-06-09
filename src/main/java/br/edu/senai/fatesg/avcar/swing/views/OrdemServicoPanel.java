package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.datastructures.BuscaOS;
import br.edu.senai.fatesg.avcar.datastructures.OrdenacaoOS;
import br.edu.senai.fatesg.avcar.business.colaboradores.ColaboradorController;
import br.edu.senai.fatesg.avcar.business.fornecedores.FornecedorController;
import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServicoController;
import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServicoDTO;
import br.edu.senai.fatesg.avcar.business.ordemservico.GarantiaDTO;
import br.edu.senai.fatesg.avcar.business.pecas.PecaController;
import br.edu.senai.fatesg.avcar.business.servicos.ServicoController;
import br.edu.senai.fatesg.avcar.business.veiculos.VeiculoController;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class OrdemServicoPanel extends JPanel {

    @Autowired
    private OrdemServicoController ordemServicoController;

    @Autowired
    private VeiculoController veiculoController;

    @Autowired
    private ServicoController servicoController;

    @Autowired
    private PecaController pecaController;

    @Autowired
    private ColaboradorController colaboradorController;

    @Autowired
    private FornecedorController fornecedorController;

    private JTable tabela;
    private DefaultTableModel modelo;
    private List<OrdemServicoDTO> dadosCarregados = new ArrayList<>();
    private int sortColumn = -1;
    private boolean sortAsc = true;

    public OrdemServicoPanel() {
        // construtor vazio — Spring cria o bean
    }

    @PostConstruct
    public void init() {
        this.modelo = new DefaultTableModel(new String[]{
            "ID", "Número OS", "Veículo", "Status", "Abertura", "Entrada", "Valor", "Desc."
        }, 0);
        this.tabela = new JTable(modelo);
        tabela.removeColumn(tabela.getColumnModel().getColumn(0));
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        JButton btnNovo = new JButton("Nova OS");
        JButton btnRefresh = new JButton("Atualizar");
        JButton btnItens = new JButton("Gerenciar Itens");
        JButton btnAvancar = new JButton("Avançar Status");
        JButton btnGarantia = new JButton("Garantia Estendida");
        JButton btnVerGarantia = new JButton("Ver Garantia");
        JButton btnDesconto = new JButton("Aplicar Desconto");
        JButton btnFila = new JButton("Fila de Espera");
        toolbar.add(btnNovo);
        toolbar.add(btnRefresh);
        toolbar.add(btnItens);
        toolbar.add(btnAvancar);
        toolbar.add(btnGarantia);
        toolbar.add(btnVerGarantia);
        toolbar.add(btnDesconto);
        toolbar.add(btnFila);
        toolbar.addSeparator();
        JTextField tfBusca = new JTextField(12);
        JButton btnBuscar = new JButton("Buscar");
        toolbar.add(new JLabel("Buscar:"));
        toolbar.add(tfBusca);
        toolbar.add(btnBuscar);

        btnNovo.addActionListener(e -> novaOS());
        btnRefresh.addActionListener(e -> carregarDados());
        btnAvancar.addActionListener(e -> avancarStatus());
        btnItens.addActionListener(e -> gerenciarItens());
        btnGarantia.addActionListener(e -> aplicarGarantia());
        btnVerGarantia.addActionListener(e -> verGarantia());
        btnDesconto.addActionListener(e -> aplicarDesconto());
        btnFila.addActionListener(e -> FilaEsperaDialog.showDialog(SwingUtilities.getWindowAncestor(this)));
        btnBuscar.addActionListener(e -> buscarOS(tfBusca.getText().trim()));
        tfBusca.addActionListener(e -> buscarOS(tfBusca.getText().trim()));

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        tabela.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                if (col == sortColumn) {
                    sortAsc = !sortAsc;
                } else {
                    sortColumn = col;
                    sortAsc = true;
                }
                ordenarTabela(col);
            }
        });
    }

    private void ordenarTabela(int col) {
        if (col < 0 || col > 6) return;
        Comparator<OrdemServicoDTO> comp = switch (col) {
            case 0 -> Comparator.comparing(OrdemServicoDTO::getNumeroOs, Comparator.nullsLast(Comparator.naturalOrder()));
            case 1 -> Comparator.comparing(OrdemServicoDTO::getVeiculo, Comparator.nullsLast(String::compareTo));
            case 2 -> Comparator.comparing(OrdemServicoDTO::getStatus, Comparator.nullsLast(String::compareTo));
            case 3 -> Comparator.comparing(OrdemServicoDTO::getDataAberturaLegado, Comparator.nullsLast(Comparator.naturalOrder()));
            case 4 -> Comparator.comparing(OrdemServicoDTO::getEntradaVeiculo, Comparator.nullsLast(Comparator.naturalOrder()));
            case 5 -> Comparator.comparing(OrdemServicoDTO::getValorTotal, Comparator.nullsLast(Comparator.naturalOrder()));
            case 6 -> Comparator.comparing(OrdemServicoDTO::getValorDesconto, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> throw new IllegalArgumentException();
        };
        if (!sortAsc) comp = comp.reversed();

        OrdenacaoOS.quickSort(dadosCarregados, comp);

        modelo.setRowCount(0);
        for (var os : dadosCarregados) {
            modelo.addRow(linha(os));
        }
    }

    private void novaOS() {
        Window win = SwingUtilities.getWindowAncestor(this);
        var dto = OrdemServicoDialog.showDialog(win, ordemServicoController, veiculoController);
        if (dto != null) carregarDados();
    }

    private void gerenciarItens() {
        Long id = getSelectedId();
        if (id == null) return;
        int linha = tabela.getSelectedRow();
        String numero = linha >= 0 ? String.valueOf(modelo.getValueAt(linha, 1)) : "";
        boolean modified = ItensOSDialog.showDialog(SwingUtilities.getWindowAncestor(this),
            ordemServicoController, servicoController, pecaController,
            colaboradorController, fornecedorController, id, numero);
        if (modified) carregarDados();
    }

    void carregarDados() {
        try {
            dadosCarregados = ordemServicoController.listar().getBody();
            if (dadosCarregados == null) dadosCarregados = new ArrayList<>();
            sortColumn = -1;
            sortAsc = true;
            modelo.setRowCount(0);
            for (var os : dadosCarregados) {
                modelo.addRow(linha(os));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Object[] linha(OrdemServicoDTO os) {
        return new Object[]{
            os.getId(), os.getNumeroOs(), os.getVeiculo(),
            os.getStatus(), os.getDataAberturaLegado(),
            os.getEntradaVeiculo() != null ? os.getEntradaVeiculo() : "",
            String.format("R$ %.2f", os.getValorTotal()),
            os.getValorDesconto() > 0 ? String.format("R$ %.2f", os.getValorDesconto()) : ""
        };
    }

    private Long getSelectedId() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma ordem de serviço.");
            return null;
        }
        Object val = modelo.getValueAt(linha, 0);
        if (val instanceof Number n) return n.longValue();
        return null;
    }

    private String getSelectedStatus() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return null;
        Object val = modelo.getValueAt(linha, 3);
        return val != null ? val.toString() : null;
    }

    private void verGarantia() {
        Long id = getSelectedId();
        if (id == null) return;
        try {
            var osDTO = ordemServicoController.buscarPorId(id).getBody();
            var garantias = ordemServicoController.calcularGarantia(id).getBody();
            if (garantias == null || garantias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum item na OS para calcular garantia.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            if (osDTO != null && osDTO.getColaboradorNome() != null) {
                sb.append("Responsável: ").append(osDTO.getColaboradorNome()).append("\n\n");
            }
            sb.append("Garantia dos Itens:\n\n");
            for (GarantiaDTO g : garantias) {
                String status = g.isVencida() ? "VENCIDA" : "Vigente";
                sb.append(String.format("[%s] %s\n", g.getTipo(), g.getItem()));
                if (g.getColaboradorNome() != null) {
                    sb.append(String.format("  Responsável: %s\n", g.getColaboradorNome()));
                }
                if (g.getDataFinalizacao() != null) {
                    sb.append(String.format("  Finalização: %s\n", g.getDataFinalizacao()));
                    sb.append(String.format("  Vencimento: %s (%s)\n", g.getDataVencimento(), status));
                    sb.append(String.format("  Dias restantes: %d\n", g.getDiasRestantes()));
                } else {
                    sb.append("  OS não finalizada — garantia não iniciada.\n");
                }
                sb.append("\n");
            }
            JTextArea ta = new JTextArea(sb.toString());
            ta.setEditable(false);
            ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Garantia da OS",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void avancarStatus() {
        Long id = getSelectedId();
        if (id == null) return;
        String status = getSelectedStatus();
        if (status == null) return;

        String label = null;
        java.util.function.Supplier<org.springframework.http.ResponseEntity<OrdemServicoDTO>> action = null;
        switch (status) {
            case "Aberta" -> { action = () -> ordemServicoController.avancarOrcamento(id); label = "Em orçamento"; }
            case "Em orçamento", "Aguardando peça" -> { action = () -> ordemServicoController.avancarExecucao(id); label = "Em execução"; }
            case "Em execução" -> { action = () -> ordemServicoController.avancarPagamento(id); label = "Finalizada"; }
            default -> {
                JOptionPane.showMessageDialog(this, "Status \"" + status + "\" não pode avançar.");
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Avançar para \"" + label + "\"?", "Avançar Status", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            action.get();
            carregarDados();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao avançar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarGarantia() {
        Long id = getSelectedId();
        if (id == null) return;

        String input = JOptionPane.showInputDialog(this,
            "Dias adicionais de garantia:", "Garantia Estendida", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            int dias = Integer.parseInt(input.trim());
            if (dias <= 0) throw new NumberFormatException();
            ordemServicoController.aplicarGarantia(id, dias);
            carregarDados();
            JOptionPane.showMessageDialog(this, "Garantia estendida em " + dias + " dias.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um número inteiro positivo.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarDesconto() {
        Long id = getSelectedId();
        if (id == null) return;

        String input = JOptionPane.showInputDialog(this,
            "Percentual de desconto:", "Aplicar Desconto", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            double percentual = Double.parseDouble(input.trim().replace(',', '.'));
            if (percentual <= 0 || percentual > 100) throw new NumberFormatException();
            ordemServicoController.aplicarDesconto(id, percentual);
            carregarDados();
            JOptionPane.showMessageDialog(this, "Desconto de " + percentual + "% aplicado.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um percentual válido (1-100).");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarOS(String termo) {
        if (termo.isEmpty()) { carregarDados(); return; }
        int idx = BuscaOS.buscaLinear(dadosCarregados,
            os -> os.getVeiculo() + " " + os.getNumeroOs() + " " + os.getStatus(),
            termo);
        if (idx >= 0) {
            tabela.setRowSelectionInterval(idx, idx);
            tabela.scrollRectToVisible(tabela.getCellRect(idx, 0, true));
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma OS encontrada para \"" + termo + "\".");
        }
    }
}
