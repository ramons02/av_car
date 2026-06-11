package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServicoController;
import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServicoDTO;
import br.edu.senai.fatesg.avcar.datastructures.FilaEsperaOS;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FilaEsperaDialog extends JDialog {

    private static final FilaEsperaOS<OrdemServicoDTO> FILA = new FilaEsperaOS<>();

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> jList = new JList<>(listModel);
    private final JComboBox<OrdemServicoDTO> comboOS = new JComboBox<>();
    private final OrdemServicoController controller;

    public static void showDialog(Window owner, OrdemServicoController controller) {
        FilaEsperaDialog d = new FilaEsperaDialog(owner, controller);
        d.setVisible(true);
    }

    public static FilaEsperaOS<OrdemServicoDTO> getFila() {
        return FILA;
    }

    private FilaEsperaDialog(Window owner, OrdemServicoController controller) {
        super(owner, "Fila de Espera — Ordens de Serviço", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        initComponents();
        carregarCombo();
        atualizarLista();
        pack();
        setSize(600, 450);
        setLocationRelativeTo(owner);
    }

    private void carregarCombo() {
        comboOS.removeAllItems();
        try {
            List<OrdemServicoDTO> lista = controller.listar().getBody();
            if (lista != null) {
                for (OrdemServicoDTO os : lista) {
                    comboOS.addItem(os);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar OS: " + e.getMessage());
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        comboOS.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof OrdemServicoDTO os) {
                    setText(String.format("#%d | %s | %s", os.getNumeroOs(), os.getVeiculo(), os.getStatus()));
                }
                return c;
            }
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Selecionar OS:"));
        topo.add(comboOS);
        JButton btnAdicionar = new JButton("Adicionar (Enqueue)");
        topo.add(btnAdicionar);

        JScrollPane scroll = new JScrollPane(jList);
        jList.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnRemover = new JButton("Remover (Dequeue)");
        JButton btnProximo = new JButton("Próximo (Peek)");
        JButton btnLimpar = new JButton("Limpar Tudo");
        JButton btnFechar = new JButton("Fechar");
        botoes.add(btnRemover);
        botoes.add(btnProximo);
        botoes.add(btnLimpar);
        botoes.add(btnFechar);

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> {
            OrdemServicoDTO sel = (OrdemServicoDTO) comboOS.getSelectedItem();
            if (sel == null) return;
            FILA.enqueue(sel);
            atualizarLista();
        });

        btnRemover.addActionListener(e -> {
            if (FILA.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fila vazia.");
                return;
            }
            OrdemServicoDTO removido = FILA.dequeue();
            JOptionPane.showMessageDialog(this,
                String.format("Removido: #%d | %s | %s",
                    removido.getNumeroOs(), removido.getVeiculo(), removido.getStatus()));
            atualizarLista();
        });

        btnProximo.addActionListener(e -> {
            if (FILA.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fila vazia.");
                return;
            }
            OrdemServicoDTO prox = FILA.peek();
            JOptionPane.showMessageDialog(this,
                String.format("Próximo: #%d | %s | %s",
                    prox.getNumeroOs(), prox.getVeiculo(), prox.getStatus()));
        });

        btnLimpar.addActionListener(e -> {
            while (!FILA.isEmpty()) FILA.dequeue();
            atualizarLista();
        });

        btnFechar.addActionListener(e -> dispose());
    }

    private void atualizarLista() {
        listModel.clear();
        List<OrdemServicoDTO> itens = FILA.listar();
        for (OrdemServicoDTO os : itens) {
            listModel.addElement(String.format("#%d | %s | %s",
                os.getNumeroOs(), os.getVeiculo(), os.getStatus()));
        }
    }
}
