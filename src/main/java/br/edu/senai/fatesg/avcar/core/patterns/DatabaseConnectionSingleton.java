package br.edu.senai.fatesg.avcar.core.patterns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// PADRÃO SINGLETON: Garante que uma classe tenha apenas uma única instância
// global, com um ponto centralizado de acesso a ela. Aplicado aqui para
// gerenciar uma única conexão JDBC PostgreSQL reutilizável pelo módulo Swing,
// evitando múltiplas conexões abertas e potenciais vazamentos de recurso.
public final class DatabaseConnectionSingleton {

    private static DatabaseConnectionSingleton instancia;
    private Connection conexao;

    private static final String URL = "jdbc:postgresql://localhost:5432/avcar";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    private DatabaseConnectionSingleton() {
    }

    public static synchronized DatabaseConnectionSingleton getInstance() {
        if (instancia == null) {
            instancia = new DatabaseConnectionSingleton();
        }
        return instancia;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return conexao;
    }

    public synchronized void closeConnection() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public synchronized boolean isConnected() {
        try {
            return conexao != null && !conexao.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
