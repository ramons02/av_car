package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServicoController;
import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServicoDTO;
import br.edu.senai.fatesg.avcar.business.veiculos.VeiculoController;
import br.edu.senai.fatesg.avcar.business.veiculos.VeiculoDTO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrdemServicoDialog extends JDialog {
    private final OrdemServicoController ordemServicoController;
    private OrdemServicoDTO result;

    static class ItemCombo {
        final Long id;
        final String nome;
        ItemCombo(Long id, String nome) { this.id = id; this.nome = nome; }
        public String toString() { return nome; }
    }

    private final JComboBox<ItemCombo> cbVeiculo = new JComboBox<>();
    private final JTextField tfEntradaVeiculo = new JTextField(10);
    private final JTextArea taDefeitoRelatado = new JTextArea(5, 40);

    public OrdemServicoDialog(Window owner, OrdemServicoController ordemServicoController,
                               VeiculoController veiculoController) {
        super(owner, "Nova Ordem de Serviço", ModalityType.APPLICATION_MODAL);
        this.ordemServicoController = ordemServicoController;
        initComponents();
        carregarVeiculos(veiculoController);
        tfEntradaVeiculo.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        pack();
        setSize(500, 350);
        setLocationRelativeTo(owner);
    }

    private void carregarVeiculos(VeiculoController veiculoController) {
        try {
            List<VeiculoDTO> veiculos = veiculoController.listar(true).getBody();
            if (veiculos != null) {
                for (VeiculoDTO v : veiculos) {
                    cbVeiculo.addItem(new ItemCombo(v.getId(),
                        v.getPlaca() + " - " + v.getModeloNome()));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar veículos: " + e.getMessage());
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel topo = new JPanel(new GridLayout(2, 2, 4, 4));
        topo.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        topo.add(new JLabel("Veículo:"));
        topo.add(cbVeiculo);
        topo.add(new JLabel("Entrada Veículo (AAAA-MM-DD):"));
        topo.add(tfEntradaVeiculo);

        taDefeitoRelatado.setLineWrap(true);
        taDefeitoRelatado.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(taDefeitoRelatado);
        scroll.setBorder(BorderFactory.createTitledBorder("Defeito Relatado"));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Criar OS");
        JButton btnCancelar = new JButton("Cancelar");
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
    }

    private void salvar() {
        ItemCombo veiculoItem = (ItemCombo) cbVeiculo.getSelectedItem();

        if (veiculoItem == null || veiculoItem.id == null) {
            JOptionPane.showMessageDialog(this, "Selecione um veículo.");
            return;
        }

        try {
            OrdemServicoController.CriarOSRequest req = new OrdemServicoController.CriarOSRequest(
                veiculoItem.id,
                tfEntradaVeiculo.getText().trim(),
                taDefeitoRelatado.getText().trim(),
                null
            );
            result = ordemServicoController.criar(req).getBody();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar OS: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public OrdemServicoDTO getResult() { return result; }

    public static OrdemServicoDTO showDialog(Window owner, OrdemServicoController ordemServicoController,
                                              VeiculoController veiculoController) {
        OrdemServicoDialog d = new OrdemServicoDialog(owner, ordemServicoController, veiculoController);
        d.setVisible(true);
        return d.getResult();
    }
}
