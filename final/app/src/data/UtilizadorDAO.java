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
                    "id VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "senha VARCHAR(45) NOT NULL)";
            stm.executeUpdate(sql);

            // Criar Admin
            sql = "INSERT IGNORE INTO utilizadores (id, senha) VALUES ('admin', 'admin')";
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

    public boolean existeIdUtilizador(Object key) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM utilizadores WHERE id=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                r = rs.next();  // A chave existe na tabela
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar id de utilizador: " + e.getMessage());
        }
        return r;
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
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM utilizadores WHERE id = ?")) {
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
                String id = rs.getString("id");
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
                String id = rs.getString("id");
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
                    "INSERT INTO utilizadores (id, senha) VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE senha = VALUES(senha)")
            ) {
                for (Utilizador utilizador : utilizadores) {
                    pstm.setString(1, utilizador.getId());
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
}

