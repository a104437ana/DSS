package data;

import business.SSUtilizadores.Utilizador;

import java.sql.*;
import java.util.*;

public class UtilizadorDAO {

    // Construtor privado para Singleton
    private UtilizadorDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Criar a tabela 'utilizadores' se não existir
            String sql = "CREATE TABLE IF NOT EXISTS utilizadores (" +
                    "codUtilizador VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "senha VARCHAR(45) NOT NULL)";
            stm.executeUpdate(sql);

            // Criar Admin
            sql = "INSERT IGNORE INTO utilizadores (codUtilizador, senha) VALUES ('admin', 'admin')";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private static UtilizadorDAO singleton = null;

    /**
     * Implementação do padrão Singleton
     *
     * @return devolve a instância única desta classe
     */
    public static UtilizadorDAO getInstance() {
        if (UtilizadorDAO.singleton == null) {
            UtilizadorDAO.singleton = new UtilizadorDAO();
        }
        return UtilizadorDAO.singleton;
    }

    /**
     * Método que cerifica se um id de user existe na base de dados
     *
     * @param key id de um user
     * @return true se o user existe
     * @throws NullPointerException Em caso de erro - deveriam ser criadas exepções do projecto
     */

    public boolean existeUtilizador(Object key) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT codUtilizador FROM utilizadores WHERE codUtilizador=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                r = rs.next();  // A chave existe na tabela
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar codUtilizador de utilizador: " + e.getMessage());
        }
        return r;
    }


    /**
     * Obtém a lista de códigos de todos os utilizadores no banco de dados.
     *
     * @return Lista de códigos dos utilizadores.
     */
    public List<String> getAllCods() {
        List<String> cods = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT codUtilizador FROM utilizadores")) {

            while (rs.next()) {
                cods.add(rs.getString("codUtilizador"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os IDs de utilizadores: " + e.getMessage(), e);
        }

        return cods;
    }

    /**
     * Método para obter um user dado o seu ID.
     *
     * @param id ID de Utilizador
     * @return Utilizador ou null se não existir
     */
    public Utilizador get(String id) {
        Utilizador u = null;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM utilizadores WHERE codUtilizador = ?")) {
            pstm.setString(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String senha = rs.getString("senha");

                    u = new Utilizador(id, senha);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return u;
    }

    /**
     * Obtém todos os utilizadores da tabela.
     *
     * @return Coleção contendo todos os users.
     */
    public Collection<Utilizador> getAll() {
        Collection<Utilizador> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM utilizadores")) {
            while (rs.next()) {
                String id = rs.getString("codUtilizador");
                String senha = rs.getString("senha");

                Utilizador user;
                user = new Utilizador(id, senha);

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Obtém todos os utilizadores da tabela como um mapa.
     *
     * @return Um mapa contendo todos os utilizadores, onde a chave é o número do utilizadore.
     */
    public Map<String, Utilizador> getAllAsMap() {
        Map<String, Utilizador> users = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM utilizadores")) {
            while (rs.next()) {
                String id = rs.getString("codUtilizador");
                String senha = rs.getString("senha");

                Utilizador user;
                user = new Utilizador(id, senha);

                users.put(id, user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Insere ou atualiza múltiplos utilizadores no banco de dados.
     *
     * @param utilizadores Coleção de utilizadores a serem adicionados ou atualizados.
     */
    public void putAll(Collection<Utilizador> utilizadores) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO utilizadores (codUtilizador, senha) VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE senha = VALUES(senha)")
            ) {
                for (Utilizador utilizador : utilizadores) {
                    pstm.setString(1, utilizador.getCodUtilizador());
                    pstm.setString(2, utilizador.getSenha());
                    pstm.addBatch(); // Adiciona ao batch para execução em lote
                }
                pstm.executeBatch(); // Executa todos os comandos de uma vez
            }

            conn.commit(); // Confirma a transação
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Reverte a transação em caso de erro
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao inserir ou atualizar utilizadores: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura o modo de auto-commit
                    conn.close(); // Fecha a conexão
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Remove múltiplos utilizadores do banco de dados com base em uma lista de IDs, com suporte a rollback.
     *
     * @param cods Lista de IDs dos utilizadores a serem removidos.
     */
    public void removerLista(List<String> cods) {
        if (cods == null || cods.isEmpty()) return;

        String sql = "DELETE FROM utilizadores WHERE codUtilizador = ?";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            conn.setAutoCommit(false);

            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                for (String id : cods) {
                    pstm.setString(1, id);
                    pstm.addBatch();
                }

                pstm.executeBatch();
                conn.commit();
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                    throw new RuntimeException("Erro ao fazer rollback da remoção: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
            throw new RuntimeException("Erro ao remover lista de utilizadores: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

