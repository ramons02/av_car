package br.edu.senai.fatesg.avcar.swing.views;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Lazy
@Component
public class MainView extends JFrame {

    public MainView(ClientePanel clientePanel, VeiculoPanel veiculoPanel,
                    ServicoPanel servicoPanel, OrdemServicoPanel ordemServicoPanel,
                    PecaPanel pecaPanel, FornecedorPanel fornecedorPanel,
                    ColaboradorPanel colaboradorPanel, ParceiroPanel parceiroPanel) {
        initComponents(clientePanel, veiculoPanel, servicoPanel, ordemServicoPanel,
                pecaPanel, fornecedorPanel, colaboradorPanel, parceiroPanel);
    }

    private void initComponents(ClientePanel clientePanel, VeiculoPanel veiculoPanel,
                                 ServicoPanel servicoPanel, OrdemServicoPanel ordemServicoPanel,
                                 PecaPanel pecaPanel, FornecedorPanel fornecedorPanel,
                                 ColaboradorPanel colaboradorPanel, ParceiroPanel parceiroPanel) {
        setTitle("AV-CAR Auto Center - Sistema de Gestão");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        abas.addTab("Clientes", clientePanel);
        abas.addTab("Veículos", veiculoPanel);
        abas.addTab("Serviços", servicoPanel);
        abas.addTab("Ordens de Serviço", ordemServicoPanel);
        abas.addTab("Peças", pecaPanel);
        abas.addTab("Fornecedores", fornecedorPanel);
        abas.addTab("Colaboradores", colaboradorPanel);
        abas.addTab("Parceiros", parceiroPanel);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(240, 240, 240));
        JLabel statusLabel = new JLabel("  AV-CAR Auto Center - Sistema Integrado");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(Color.GRAY);
        statusBar.add(statusLabel);

        add(abas, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }
}
