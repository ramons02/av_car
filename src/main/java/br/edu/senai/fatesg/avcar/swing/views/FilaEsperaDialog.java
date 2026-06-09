package br.edu.senai.fatesg.avcar.swing.views;

import br.edu.senai.fatesg.avcar.datastructures.FilaEsperaOS;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FilaEsperaDialog extends JDialog {

    private static final FilaEsperaOS<String> FILA = new FilaEsperaOS<>();

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> jList = new JList<>(listModel);
    private final JTextField tfNumero = new JTextField(15);

    public static void showDialog(Window owner) {
        FilaEsperaDialog d = new FilaEsperaDialog(owner);
        d.setVisible(true);
    }

    public static FilaEsperaOS<String> getFila() {
        return FILA;
    }

    private FilaEsperaDialog(Window owner) {
        super(owner, "Fila de Espera — Ordens de Serviço", ModalityType.APPLICATION_MODAL);
        initComponents();
        atualizarLista();
        pack();
        setSize(500, 400);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        AbstractDialog.setApenasDigitos(tfNumero);

        setLayout(new BorderLayout());

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Número da OS:"));
        topo.add(tfNumero);
        JButton btnAdicionar = new JButton("Adicionar (Enqueue)");
        topo.add(btnAdicionar);

        JLabel lblInfo = new JLabel(" ");

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
            String num = tfNumero.getText().trim();
            if (num.isEmpty()) return;
            FILA.enqueue(num);
            tfNumero.setText("");
            atualizarLista();
        });

        btnRemover.addActionListener(e -> {
            if (FILA.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fila vazia.");
                return;
            }
            String removido = FILA.dequeue();
            JOptionPane.showMessageDialog(this, "Removido: " + removido);
            atualizarLista();
        });

        btnProximo.addActionListener(e -> {
            if (FILA.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fila vazia.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Próximo: " + FILA.peek());
        });

        btnLimpar.addActionListener(e -> {
            while (!FILA.isEmpty()) FILA.dequeue();
            atualizarLista();
        });

        btnFechar.addActionListener(e -> dispose());
    }

    private void atualizarLista() {
        listModel.clear();
        List<String> itens = FILA.listar();
        for (String s : itens) listModel.addElement(s);
    }
}
