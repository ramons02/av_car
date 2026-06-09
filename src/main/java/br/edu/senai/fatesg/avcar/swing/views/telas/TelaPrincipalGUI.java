/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.edu.senai.fatesg.avcar.swing.views.telas;

/**
 *
 * @author lucio-aguiar
 */
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class TelaPrincipalGUI extends javax.swing.JFrame {

    @Autowired
    private ApplicationContext springContext;
    
    @Autowired
    private br.edu.senai.fatesg.avcar.business.clientes.ClienteController clienteController;

    @Autowired
    private br.edu.senai.fatesg.avcar.business.veiculos.VeiculoController veiculoController;

    /**
     * Creates new form TelaPrincipalGUI
     */
    public void carregarTabelaClientes() {
        if (clienteController == null) return; // Evita erro se abrir no modo Design
        
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) jTableClientes.getModel();
        modelo.setNumRows(0);
        
        boolean verInativos = false;
        if (jRadioButtonVisualizarClientesInativos != null) {
            verInativos = jRadioButtonVisualizarClientesInativos.isSelected();
        }
        
        // Verifica se há algo digitado na busca
        String termo = "";
        if (jTextFieldPesquisarCliente != null) {
            termo = jTextFieldPesquisarCliente.getText().trim();
        }
        
        // Puxa do banco de dados respeitando a regra de negócio do Controller
        java.util.List<br.edu.senai.fatesg.avcar.business.clientes.ClienteDTO> lista;
        if (termo.isEmpty()) {
            lista = clienteController.listar(verInativos).getBody();
        } else {
            // Utiliza o método de busca oficial do back-end para preservar a lógica acadêmica (ex: Árvores, SQL puro, Busca Binária)
            lista = clienteController.buscarPorNome(termo).getBody();
        }
        
        for (br.edu.senai.fatesg.avcar.business.clientes.ClienteDTO c : lista) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getTipo(),
                br.edu.senai.fatesg.avcar.swing.views.telas.utils.FormatadorUtil.formatarCpfCnpj(c.getDocumento()),
                br.edu.senai.fatesg.avcar.swing.views.telas.utils.FormatadorUtil.formatarTelefone(c.getTelefone()),
                c.getEmail(),
                c.isAtivo() ? "Ativo" : "Inativo"
            });
        }
        
        // Reaplica o utilitário de cor após popular
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTableClientes);
    }

    public void carregarTabelaVeiculos() {
        if (veiculoController == null) return;
        
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) jTableVeiculos.getModel();
        modelo.setNumRows(0);
        
        String termo = "";
        if (jTextFieldPesquisarVeiculo != null) {
            termo = jTextFieldPesquisarVeiculo.getText().trim();
        }
        
        java.util.List<br.edu.senai.fatesg.avcar.business.veiculos.VeiculoDTO> lista;
        if (termo.isEmpty()) {
            lista = veiculoController.listar(false).getBody();
        } else {
            lista = veiculoController.buscarPorPlaca(termo).getBody();
        }
        
        if (lista != null) {
            for (br.edu.senai.fatesg.avcar.business.veiculos.VeiculoDTO v : lista) {
                modelo.addRow(new Object[]{
                    v.getId(),
                    br.edu.senai.fatesg.avcar.swing.views.telas.utils.FormatadorUtil.formatarPlaca(v.getPlaca()),
                    (v.getChassi() != null ? v.getChassi().toUpperCase() : ""),
                    v.getAnoFabricacao(),
                    v.getAnoModelo(),
                    v.getCor(),
                    br.edu.senai.fatesg.avcar.swing.views.telas.utils.FormatadorUtil.formatarQuilometragem(v.getQuilometragem()),
                    new br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ImagemComTexto(v.getMarcaLogoUrl(), v.getMarcaNome()),
                    v.getModeloNome(),
                    v.getClienteNome()
                });
            }
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            carregarTabelaClientes();
            carregarTabelaVeiculos();
        }
    }

    // --- EVENTOS CUSTOMIZADOS ---
    private void configurarEventosTabelaVeiculos() {
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.adicionarBuscaEmTempoReal(
            jTextFieldPesquisarVeiculo, 
            this::carregarTabelaVeiculos
        );
    }

    private void configurarEventosTabelaClientes() {
        // Busca em tempo real injetada via Utils (Clean Code)
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.adicionarBuscaEmTempoReal(
            jTextFieldPesquisarCliente, 
            this::carregarTabelaClientes
        );
        
        // Ao clicar em uma linha, acende os botões de Editar e Excluir
        jTableClientes.getSelectionModel().addListSelectionListener(e -> {
            boolean linhaSelecionada = jTableClientes.getSelectedRow() != -1;
            jButtonEditarCliente.setEnabled(linhaSelecionada);
            jButtonInativarCliente.setEnabled(linhaSelecionada);
        });

        // Duplo Clique na linha aciona o botão de Editar automaticamente
        jTableClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && jTableClientes.getSelectedRow() != -1) {
                    jButtonEditarCliente.doClick(); // Simula o clique no botão Editar
                }
            }
        });
    }

    public TelaPrincipalGUI() {
        initComponents();
        
        // 1. Aplica padrões UX de tela cheia e centralização
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.JanelaUtil.aplicarPadraoMaximizado(this);
        
        // 2. Oculta a coluna ID (Índice 0) dos bastidores de TODAS as tabelas
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ocultarColuna(jTableClientes, 0);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ocultarColuna(jTableVeiculos, 0);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ocultarColuna(jTableServicos, 0);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ocultarColuna(jTableOS, 0);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ocultarColuna(jTablePecas, 0);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ocultarColuna(jTableFornecedores, 0);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ocultarColuna(jTableColaboradores, 0);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.ocultarColuna(jTableParceiros, 0);
        
        // 3. Aplica cores no Status automaticamente (A função procura a coluna "Status" sozinha)
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTableClientes);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTableVeiculos);
        
        // 4. Configura os eventos de Mouse para a Tabela de Clientes
        configurarEventosTabelaClientes();
        configurarEventosTabelaVeiculos();
        
        // 5. Centraliza as colunas de informações curtas para ficarem esteticamente alinhadas
        // (A TabelaUtil cuida de não quebrar a coluna Status). Índices visuais (sem o ID): 1=Tipo, 2=Doc, 3=Tel
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.centralizarColunas(jTableClientes, 1, 2, 3);
        
        // Centraliza as colunas curtas de Veículos: 0=Placa, 2=Ano Fab, 3=Ano Mod, 5=KM
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.centralizarColunas(jTableVeiculos, 0, 2, 3, 5);
        
        // Aplica o renderizador de logotipo da marca (índice visual 6)
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarRenderizadorDeImagem(jTableVeiculos, 6);
        
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTableServicos);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTableOS);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTablePecas);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTableFornecedores);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTableColaboradores);
        br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.aplicarCorNoStatus(jTableParceiros);
        
        // 3. Define larguras fixas (Você pode descomentar e ajustar as colunas conforme necessário)
        // br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.definirLarguraFixa(jTableClientes, 1, 80); // Coluna Tipo
        // br.edu.senai.fatesg.avcar.swing.views.telas.utils.TabelaUtil.definirLarguraFixa(jTableClientes, 2, 150); // Coluna Documento
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneParceiros = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableClientes = new javax.swing.JTable();
        jLabelPesquisarCliente = new javax.swing.JLabel();
        jTextFieldPesquisarCliente = new javax.swing.JTextField();
        jRadioButtonVisualizarClientesInativos = new javax.swing.JRadioButton();
        jLabelGestaodeClientesCadastrados = new javax.swing.JLabel();
        jButtonNovoCliente = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButtonInativarCliente = new javax.swing.JButton();
        jButtonEditarCliente = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelGestaodeVeiculos = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelPesquisarVeiculo = new javax.swing.JLabel();
        jTextFieldPesquisarVeiculo = new javax.swing.JTextField();
        jButtonNovoVeiculo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableVeiculos = new javax.swing.JTable();
        jButtonEditarVeiculo = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelGestaodeServicos = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabelPesquisarServicos = new javax.swing.JLabel();
        jTextFieldPesquisarServicos = new javax.swing.JTextField();
        jButtonBuscarServicos = new javax.swing.JButton();
        jButtonNovoServico = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableServicos = new javax.swing.JTable();
        jRadioButtonVisualizarServicosInativos = new javax.swing.JRadioButton();
        jButtonEditarServiço = new javax.swing.JButton();
        jButtonInativaServiço = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabelGestaodeOS = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabelPesquisarOS = new javax.swing.JLabel();
        jTextFieldPesquisarOS = new javax.swing.JTextField();
        jButtonBuscarOS = new javax.swing.JButton();
        jButtonNovaOS = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableOS = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonGerenciarItensOS = new javax.swing.JButton();
        jButtonAvancarStatusOS = new javax.swing.JButton();
        jButtonAplicarDesconto = new javax.swing.JButton();
        jButtonFilaAtendimento = new javax.swing.JButton();
        jButtonAplicarGarantia = new javax.swing.JButton();
        jButtonHistoricoOS = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabelGestaodePecas = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabelPesquisarPecas = new javax.swing.JLabel();
        jTextFieldPesquisarPecas = new javax.swing.JTextField();
        jButtonBuscarPecas = new javax.swing.JButton();
        jButtonNovaPeca = new javax.swing.JButton();
        jButtonEditarPeca = new javax.swing.JButton();
        jButtonInativaExcluir = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTablePecas = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabelGestaodeFornecedores = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabelPesquisarFornecedor = new javax.swing.JLabel();
        jTextFieldPesquisarFornecedor = new javax.swing.JTextField();
        jButtonBuscarFornecedor = new javax.swing.JButton();
        jButtonNovoFornecedor = new javax.swing.JButton();
        jButtonInativarFornecedor = new javax.swing.JButton();
        jButtonEditarFornecedor = new javax.swing.JButton();
        jRadioButtonVisualizarFornecedorInativo = new javax.swing.JRadioButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableFornecedores = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabelGestaodeColaboradores = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabelPesquisarColaboradores = new javax.swing.JLabel();
        jTextFieldPesquisarColaboradores = new javax.swing.JTextField();
        jButtonBuscarColaboradores = new javax.swing.JButton();
        jButtonNovoColaborador = new javax.swing.JButton();
        jButtonInativarColaborador = new javax.swing.JButton();
        jButtonEditarColaborador = new javax.swing.JButton();
        jRadioButtonVisualizarColaboradores = new javax.swing.JRadioButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableColaboradores = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabelGestaodeParceiros = new javax.swing.JLabel();
        jLabelPesquisarParceiros = new javax.swing.JLabel();
        jTextFieldPesquisarParceiros = new javax.swing.JTextField();
        jButtonBuscarParceiros = new javax.swing.JButton();
        jButtonNovoParceiro = new javax.swing.JButton();
        jButtonInativarParceiro = new javax.swing.JButton();
        jButtonEditarParceiro = new javax.swing.JButton();
        jRadioButtonVisualizarParceiros = new javax.swing.JRadioButton();
        jSeparator8 = new javax.swing.JSeparator();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableParceiros = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 700));

        jTabbedPaneParceiros.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1907, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 626, Short.MAX_VALUE)
        );

        jTabbedPaneParceiros.addTab("Tela Principal", jPanel9);

        jTableClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Tipo", "CPF / CNPJ", "Telefone", "E-mail", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableClientes);
        if (jTableClientes.getColumnModel().getColumnCount() > 0) {
            jTableClientes.getColumnModel().getColumn(0).setResizable(false);
            jTableClientes.getColumnModel().getColumn(1).setResizable(false);
            jTableClientes.getColumnModel().getColumn(2).setResizable(false);
            jTableClientes.getColumnModel().getColumn(3).setResizable(false);
            jTableClientes.getColumnModel().getColumn(4).setResizable(false);
            jTableClientes.getColumnModel().getColumn(5).setResizable(false);
            jTableClientes.getColumnModel().getColumn(6).setResizable(false);
        }

        jLabelPesquisarCliente.setText("Pesquisar");

        jRadioButtonVisualizarClientesInativos.setText("Visualizar Clientes Inativos");
        jRadioButtonVisualizarClientesInativos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonVisualizarClientesInativosActionPerformed(evt);
            }
        });

        jLabelGestaodeClientesCadastrados.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabelGestaodeClientesCadastrados.setText("Gestão de Clientes Cadastrados");

        jButtonNovoCliente.setText("+ Novo Cliente");
        jButtonNovoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNovoClienteActionPerformed(evt);
            }
        });

        jButtonInativarCliente.setText("Ativar / Inativar Cliente");
        jButtonInativarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInativarClienteActionPerformed(evt);
            }
        });

        jButtonEditarCliente.setText("Editar Cliente");
        jButtonEditarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelGestaodeClientesCadastrados)
                        .addGap(0, 1528, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelPesquisarCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldPesquisarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNovoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonInativarCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEditarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonVisualizarClientesInativos)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabelGestaodeClientesCadastrados)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonVisualizarClientesInativos, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldPesquisarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelPesquisarCliente)
                        .addComponent(jButtonNovoCliente)
                        .addComponent(jButtonInativarCliente)
                        .addComponent(jButtonEditarCliente)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneParceiros.addTab("Clientes", jPanel1);

        jLabelGestaodeVeiculos.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabelGestaodeVeiculos.setText("Gestão de Veículos");

        jLabelPesquisarVeiculo.setText("Pesquisar");

        jButtonNovoVeiculo.setText("+ Novo Veiculo");

        jTableVeiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Placa", "Chassi", "Ano Fabricação", "Ano Modelo", "Cor", "KM", "Marca", "Modelo", "Cliente"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableVeiculos);
        if (jTableVeiculos.getColumnModel().getColumnCount() > 0) {
            jTableVeiculos.getColumnModel().getColumn(0).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(1).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(2).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(3).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(4).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(5).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(6).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(7).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(8).setResizable(false);
            jTableVeiculos.getColumnModel().getColumn(9).setResizable(false);
        }

        jButtonEditarVeiculo.setText("Editar Veículo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelGestaodeVeiculos)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelPesquisarVeiculo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldPesquisarVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonNovoVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonEditarVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 1195, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabelGestaodeVeiculos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNovoVeiculo)
                    .addComponent(jLabelPesquisarVeiculo)
                    .addComponent(jTextFieldPesquisarVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonEditarVeiculo))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneParceiros.addTab("Veículos", jPanel2);

        jLabelGestaodeServicos.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabelGestaodeServicos.setText("Gestão de Serviços");

        jLabelPesquisarServicos.setText("Pesquisar");

        jButtonBuscarServicos.setText("Buscar");

        jButtonNovoServico.setText("+ Novo Serviço");

        jTableServicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Descrição", "Valor", "Garandia (Dias)", "Tempo Estimado", "Status", "Ações"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableServicos);
        if (jTableServicos.getColumnModel().getColumnCount() > 0) {
            jTableServicos.getColumnModel().getColumn(0).setResizable(false);
            jTableServicos.getColumnModel().getColumn(1).setResizable(false);
            jTableServicos.getColumnModel().getColumn(2).setResizable(false);
            jTableServicos.getColumnModel().getColumn(3).setResizable(false);
            jTableServicos.getColumnModel().getColumn(4).setResizable(false);
            jTableServicos.getColumnModel().getColumn(5).setResizable(false);
            jTableServicos.getColumnModel().getColumn(6).setResizable(false);
            jTableServicos.getColumnModel().getColumn(7).setResizable(false);
        }

        jRadioButtonVisualizarServicosInativos.setText("Visualizar Serviços Inativos");

        jButtonEditarServiço.setText("Editar Serviço");

        jButtonInativaServiço.setText("Inativar Serviço");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelGestaodeServicos)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabelPesquisarServicos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldPesquisarServicos, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonBuscarServicos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonNovoServico, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonEditarServiço, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonInativaServiço, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonVisualizarServicosInativos)))
                        .addGap(0, 748, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator3))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabelGestaodeServicos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNovoServico)
                    .addComponent(jLabelPesquisarServicos)
                    .addComponent(jTextFieldPesquisarServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscarServicos)
                    .addComponent(jRadioButtonVisualizarServicosInativos)
                    .addComponent(jButtonEditarServiço)
                    .addComponent(jButtonInativaServiço))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneParceiros.addTab("Serviços", jPanel3);

        jLabelGestaodeOS.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabelGestaodeOS.setText("Gestão de OS (Ordem de Serviços)");

        jLabelPesquisarOS.setText("Pesquisar");

        jButtonBuscarOS.setText("Buscar");

        jButtonNovaOS.setText("+ Nova OS");

        jTableOS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Número OS", "Veículo", "Status da OS", "Data e Hora Abertura", "Valor", "Desconto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTableOS);
        if (jTableOS.getColumnModel().getColumnCount() > 0) {
            jTableOS.getColumnModel().getColumn(0).setResizable(false);
            jTableOS.getColumnModel().getColumn(1).setResizable(false);
            jTableOS.getColumnModel().getColumn(2).setResizable(false);
            jTableOS.getColumnModel().getColumn(3).setResizable(false);
            jTableOS.getColumnModel().getColumn(4).setResizable(false);
            jTableOS.getColumnModel().getColumn(5).setResizable(false);
            jTableOS.getColumnModel().getColumn(6).setResizable(false);
        }

        jButtonGerenciarItensOS.setText("Gerenciar Itens OS");

        jButtonAvancarStatusOS.setText("Avançar Status OS");

        jButtonAplicarDesconto.setText("Aplicar Desconto");

        jButtonFilaAtendimento.setText("Fila de Atendimento");

        jButtonAplicarGarantia.setText("Aplicar Garantia");

        jButtonHistoricoOS.setText("Historico OS");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonGerenciarItensOS, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonAvancarStatusOS, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonAplicarDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonFilaAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonAplicarGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonHistoricoOS, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(791, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonHistoricoOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonFilaAtendimento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAplicarDesconto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jButtonGerenciarItensOS, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                    .addComponent(jButtonAvancarStatusOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAplicarGarantia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane4)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelGestaodeOS)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addComponent(jLabelPesquisarOS)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextFieldPesquisarOS, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jButtonBuscarOS)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButtonNovaOS)))
                                    .addGap(0, 1290, Short.MAX_VALUE)))))
                    .addGap(3, 3, 3)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 556, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jLabelGestaodeOS)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonNovaOS)
                        .addComponent(jLabelPesquisarOS)
                        .addComponent(jTextFieldPesquisarOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonBuscarOS))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGap(76, 76, 76)))
        );

        jTabbedPaneParceiros.addTab("Ordem de Serviços", jPanel4);

        jLabelGestaodePecas.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabelGestaodePecas.setText("Gestão de Peças");

        jLabelPesquisarPecas.setText("Código Nacional");

        jButtonBuscarPecas.setText("Buscar");

        jButtonNovaPeca.setText("+ Novo Peça");

        jButtonEditarPeca.setText("Editar Peça");

        jButtonInativaExcluir.setText("Excluir");

        jTablePecas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Código Nacional", "Código Internacional", "Nome", "Fabricante", "Categoria", "Preço de Custo", "Preço de Venda", "Estoque", "Granatia", "Data Compra", "Fornecedor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTablePecas);
        if (jTablePecas.getColumnModel().getColumnCount() > 0) {
            jTablePecas.getColumnModel().getColumn(0).setResizable(false);
            jTablePecas.getColumnModel().getColumn(1).setResizable(false);
            jTablePecas.getColumnModel().getColumn(2).setResizable(false);
            jTablePecas.getColumnModel().getColumn(3).setResizable(false);
            jTablePecas.getColumnModel().getColumn(4).setResizable(false);
            jTablePecas.getColumnModel().getColumn(5).setResizable(false);
            jTablePecas.getColumnModel().getColumn(6).setResizable(false);
            jTablePecas.getColumnModel().getColumn(7).setResizable(false);
            jTablePecas.getColumnModel().getColumn(8).setResizable(false);
            jTablePecas.getColumnModel().getColumn(9).setResizable(false);
            jTablePecas.getColumnModel().getColumn(10).setResizable(false);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1907, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane5)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelGestaodePecas)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabelPesquisarPecas)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextFieldPesquisarPecas, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jButtonBuscarPecas)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButtonNovaPeca, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButtonEditarPeca, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButtonInativaExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(0, 920, Short.MAX_VALUE))))
                        .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGap(3, 3, 3)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 626, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jLabelGestaodePecas)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonNovaPeca)
                        .addComponent(jLabelPesquisarPecas)
                        .addComponent(jTextFieldPesquisarPecas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonBuscarPecas)
                        .addComponent(jButtonEditarPeca)
                        .addComponent(jButtonInativaExcluir))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                    .addGap(3, 3, 3)))
        );

        jTabbedPaneParceiros.addTab("Peças", jPanel5);

        jLabelGestaodeFornecedores.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabelGestaodeFornecedores.setText("Gestão de Fornecedores");

        jLabelPesquisarFornecedor.setText("Pesquisar");

        jButtonBuscarFornecedor.setText("Buscar");

        jButtonNovoFornecedor.setText("+ Novo Fornecedor");

        jButtonInativarFornecedor.setText("Inativar Fornecedor");

        jButtonEditarFornecedor.setText("Editar Fornecedor");

        jRadioButtonVisualizarFornecedorInativo.setText("Visualizar Fornecedores Inativos");

        jTableFornecedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Razão Social", "CNPJ", "Telefone", "E-mail", "Endereço", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTableFornecedores);
        if (jTableFornecedores.getColumnModel().getColumnCount() > 0) {
            jTableFornecedores.getColumnModel().getColumn(0).setResizable(false);
            jTableFornecedores.getColumnModel().getColumn(1).setResizable(false);
            jTableFornecedores.getColumnModel().getColumn(2).setResizable(false);
            jTableFornecedores.getColumnModel().getColumn(3).setResizable(false);
            jTableFornecedores.getColumnModel().getColumn(4).setResizable(false);
            jTableFornecedores.getColumnModel().getColumn(5).setResizable(false);
            jTableFornecedores.getColumnModel().getColumn(6).setResizable(false);
            jTableFornecedores.getColumnModel().getColumn(6).setHeaderValue("Status");
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabelGestaodeFornecedores)
                        .addGap(0, 1614, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane6)
                        .addContainerGap())))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator6))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelPesquisarFornecedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldPesquisarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonBuscarFornecedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNovoFornecedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonInativarFornecedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEditarFornecedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonVisualizarFornecedorInativo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabelGestaodeFornecedores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonVisualizarFornecedorInativo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldPesquisarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonBuscarFornecedor)
                        .addComponent(jLabelPesquisarFornecedor)
                        .addComponent(jButtonNovoFornecedor)
                        .addComponent(jButtonInativarFornecedor)
                        .addComponent(jButtonEditarFornecedor)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneParceiros.addTab("Fornecedores Peças", jPanel6);

        jLabelGestaodeColaboradores.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabelGestaodeColaboradores.setText("Gestão de Colaboradores");

        jLabelPesquisarColaboradores.setText("Pesquisar");

        jButtonBuscarColaboradores.setText("Buscar");

        jButtonNovoColaborador.setText("+ Novo Colaborador");

        jButtonInativarColaborador.setText("Inativar Colaborador");

        jButtonEditarColaborador.setText("Editar Colaborador");

        jRadioButtonVisualizarColaboradores.setText("Visualizar Colaboradores Inativos");

        jTableColaboradores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "CPF", "Telefone", "E-mail", "Funções"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTableColaboradores);
        if (jTableColaboradores.getColumnModel().getColumnCount() > 0) {
            jTableColaboradores.getColumnModel().getColumn(0).setResizable(false);
            jTableColaboradores.getColumnModel().getColumn(1).setResizable(false);
            jTableColaboradores.getColumnModel().getColumn(2).setResizable(false);
            jTableColaboradores.getColumnModel().getColumn(3).setResizable(false);
            jTableColaboradores.getColumnModel().getColumn(4).setResizable(false);
            jTableColaboradores.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabelGestaodeColaboradores)
                        .addGap(0, 1603, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane7)
                        .addContainerGap())))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator7))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelPesquisarColaboradores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldPesquisarColaboradores, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonBuscarColaboradores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNovoColaborador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonInativarColaborador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEditarColaborador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonVisualizarColaboradores)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabelGestaodeColaboradores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonVisualizarColaboradores, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldPesquisarColaboradores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonBuscarColaboradores)
                        .addComponent(jLabelPesquisarColaboradores)
                        .addComponent(jButtonNovoColaborador)
                        .addComponent(jButtonInativarColaborador)
                        .addComponent(jButtonEditarColaborador)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneParceiros.addTab("Colaboradores", jPanel7);

        jLabelGestaodeParceiros.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabelGestaodeParceiros.setText("Gestão de Parceiros (Serviços)");

        jLabelPesquisarParceiros.setText("Pesquisar");

        jButtonBuscarParceiros.setText("Buscar");

        jButtonNovoParceiro.setText("+ Novo Parceiro");

        jButtonInativarParceiro.setText("Inativar Parceiro");

        jButtonEditarParceiro.setText("Editar Parceiro");

        jRadioButtonVisualizarParceiros.setText("Visualizar Parceiros Inativos");

        jTableParceiros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "CNPJ", "Tipo de Serviço", "Telefone", "E-mail", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTableParceiros);
        if (jTableParceiros.getColumnModel().getColumnCount() > 0) {
            jTableParceiros.getColumnModel().getColumn(0).setResizable(false);
            jTableParceiros.getColumnModel().getColumn(1).setResizable(false);
            jTableParceiros.getColumnModel().getColumn(2).setResizable(false);
            jTableParceiros.getColumnModel().getColumn(3).setResizable(false);
            jTableParceiros.getColumnModel().getColumn(4).setResizable(false);
            jTableParceiros.getColumnModel().getColumn(5).setResizable(false);
            jTableParceiros.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabelGestaodeParceiros)
                        .addGap(0, 1542, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane8)
                        .addContainerGap())))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator8))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelPesquisarParceiros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldPesquisarParceiros, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonBuscarParceiros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNovoParceiro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonInativarParceiro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEditarParceiro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonVisualizarParceiros)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabelGestaodeParceiros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonVisualizarParceiros, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldPesquisarParceiros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonBuscarParceiros)
                        .addComponent(jLabelPesquisarParceiros)
                        .addComponent(jButtonNovoParceiro)
                        .addComponent(jButtonInativarParceiro)
                        .addComponent(jButtonEditarParceiro)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneParceiros.addTab("Parceiros (Serviços)", jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneParceiros)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneParceiros)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNovoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNovoClienteActionPerformed
        if (springContext != null) {
            CadastroClienteGUI cadastroPanel = springContext.getBean(CadastroClienteGUI.class);
            cadastroPanel.prepararParaNovo();
            br.edu.senai.fatesg.avcar.swing.views.telas.utils.JanelaUtil.abrirPainelComoModal(this, "Novo Cliente", cadastroPanel);
        } else {
            br.edu.senai.fatesg.avcar.swing.views.telas.utils.MensagemUtil.exibirAlertaBancoDeDados(this);
        }
    }//GEN-LAST:event_jButtonNovoClienteActionPerformed

    private void jButtonInativarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInativarClienteActionPerformed
        int linha = jTableClientes.getSelectedRow();
        if (linha != -1 && clienteController != null) {
            Long id = (Long) jTableClientes.getModel().getValueAt(linha, 0);
            String nome = (String) jTableClientes.getModel().getValueAt(linha, 1);
            
            if (br.edu.senai.fatesg.avcar.swing.views.telas.utils.MensagemUtil.confirmarInativacao(this, nome)) {
                clienteController.toggleStatus(id);
                carregarTabelaClientes();
            }
        }
    }//GEN-LAST:event_jButtonInativarClienteActionPerformed

    private void jButtonEditarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditarClienteActionPerformed
        int linha = jTableClientes.getSelectedRow();
        if (linha != -1 && springContext != null) {
            Long id = (Long) jTableClientes.getModel().getValueAt(linha, 0);
            
            CadastroClienteGUI cadastroPanel = springContext.getBean(CadastroClienteGUI.class);
            cadastroPanel.preencherParaEdicao(id);
            
            br.edu.senai.fatesg.avcar.swing.views.telas.utils.JanelaUtil.abrirPainelComoModal(this, "Editar Cliente", cadastroPanel);
            
            carregarTabelaClientes();
        }
    }//GEN-LAST:event_jButtonEditarClienteActionPerformed

    private void jRadioButtonVisualizarClientesInativosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonVisualizarClientesInativosActionPerformed
        // Como o método carregarTabelaClientes já lê o estado deste botão, nós só precisamos chamá-lo de novo!
        carregarTabelaClientes();
    }//GEN-LAST:event_jRadioButtonVisualizarClientesInativosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            com.formdev.flatlaf.FlatDarkLaf.setup();
            
            // --- PADRÃO DE FONTE MODERNA (UX/UI) ---
            javax.swing.UIManager.put("defaultFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            
            javax.swing.UIManager.put("TextComponent.arc", 15);
            javax.swing.UIManager.put("Button.arc", 15);
            javax.swing.UIManager.put("Component.arc", 15);
            
            // --- ESTILIZAÇÃO DO MENU LATERAL (TABBED PANE) ---
            javax.swing.UIManager.put("TabbedPane.showTabSeparators", false); // Tira as linhas feias
            javax.swing.UIManager.put("TabbedPane.hasFullBorder", false); // Tira a borda ao redor
            javax.swing.UIManager.put("TabbedPane.tabHeight", 45); // Deixa as guias mais gordinhas
            javax.swing.UIManager.put("TabbedPane.tabInsets", new java.awt.Insets(0, 20, 0, 20)); // Espaçamento do texto
            javax.swing.UIManager.put("TabbedPane.focusColor", new java.awt.Color(0,0,0,0)); // Tira a linha de foco ao clicar
            javax.swing.UIManager.put("TabbedPane.hoverColor", new java.awt.Color(70, 73, 75)); // Efeito Hover super suave
            javax.swing.UIManager.put("TabbedPane.selectedBackground", new java.awt.Color(43, 43, 43)); // Fundo da guia ativa
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TelaPrincipalGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipalGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAplicarDesconto;
    private javax.swing.JButton jButtonAplicarGarantia;
    private javax.swing.JButton jButtonAvancarStatusOS;
    private javax.swing.JButton jButtonBuscarColaboradores;
    private javax.swing.JButton jButtonBuscarFornecedor;
    private javax.swing.JButton jButtonBuscarOS;
    private javax.swing.JButton jButtonBuscarParceiros;
    private javax.swing.JButton jButtonBuscarPecas;
    private javax.swing.JButton jButtonBuscarServicos;
    private javax.swing.JButton jButtonEditarCliente;
    private javax.swing.JButton jButtonEditarColaborador;
    private javax.swing.JButton jButtonEditarFornecedor;
    private javax.swing.JButton jButtonEditarParceiro;
    private javax.swing.JButton jButtonEditarPeca;
    private javax.swing.JButton jButtonEditarServiço;
    private javax.swing.JButton jButtonEditarVeiculo;
    private javax.swing.JButton jButtonFilaAtendimento;
    private javax.swing.JButton jButtonGerenciarItensOS;
    private javax.swing.JButton jButtonHistoricoOS;
    private javax.swing.JButton jButtonInativaExcluir;
    private javax.swing.JButton jButtonInativaServiço;
    private javax.swing.JButton jButtonInativarCliente;
    private javax.swing.JButton jButtonInativarColaborador;
    private javax.swing.JButton jButtonInativarFornecedor;
    private javax.swing.JButton jButtonInativarParceiro;
    private javax.swing.JButton jButtonNovaOS;
    private javax.swing.JButton jButtonNovaPeca;
    private javax.swing.JButton jButtonNovoCliente;
    private javax.swing.JButton jButtonNovoColaborador;
    private javax.swing.JButton jButtonNovoFornecedor;
    private javax.swing.JButton jButtonNovoParceiro;
    private javax.swing.JButton jButtonNovoServico;
    private javax.swing.JButton jButtonNovoVeiculo;
    private javax.swing.JLabel jLabelGestaodeClientesCadastrados;
    private javax.swing.JLabel jLabelGestaodeColaboradores;
    private javax.swing.JLabel jLabelGestaodeFornecedores;
    private javax.swing.JLabel jLabelGestaodeOS;
    private javax.swing.JLabel jLabelGestaodeParceiros;
    private javax.swing.JLabel jLabelGestaodePecas;
    private javax.swing.JLabel jLabelGestaodeServicos;
    private javax.swing.JLabel jLabelGestaodeVeiculos;
    private javax.swing.JLabel jLabelPesquisarCliente;
    private javax.swing.JLabel jLabelPesquisarColaboradores;
    private javax.swing.JLabel jLabelPesquisarFornecedor;
    private javax.swing.JLabel jLabelPesquisarOS;
    private javax.swing.JLabel jLabelPesquisarParceiros;
    private javax.swing.JLabel jLabelPesquisarPecas;
    private javax.swing.JLabel jLabelPesquisarServicos;
    private javax.swing.JLabel jLabelPesquisarVeiculo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButtonVisualizarClientesInativos;
    private javax.swing.JRadioButton jRadioButtonVisualizarColaboradores;
    private javax.swing.JRadioButton jRadioButtonVisualizarFornecedorInativo;
    private javax.swing.JRadioButton jRadioButtonVisualizarParceiros;
    private javax.swing.JRadioButton jRadioButtonVisualizarServicosInativos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPaneParceiros;
    private javax.swing.JTable jTableClientes;
    private javax.swing.JTable jTableColaboradores;
    private javax.swing.JTable jTableFornecedores;
    private javax.swing.JTable jTableOS;
    private javax.swing.JTable jTableParceiros;
    private javax.swing.JTable jTablePecas;
    private javax.swing.JTable jTableServicos;
    private javax.swing.JTable jTableVeiculos;
    private javax.swing.JTextField jTextFieldPesquisarCliente;
    private javax.swing.JTextField jTextFieldPesquisarColaboradores;
    private javax.swing.JTextField jTextFieldPesquisarFornecedor;
    private javax.swing.JTextField jTextFieldPesquisarOS;
    private javax.swing.JTextField jTextFieldPesquisarParceiros;
    private javax.swing.JTextField jTextFieldPesquisarPecas;
    private javax.swing.JTextField jTextFieldPesquisarServicos;
    private javax.swing.JTextField jTextFieldPesquisarVeiculo;
    // End of variables declaration//GEN-END:variables
}
