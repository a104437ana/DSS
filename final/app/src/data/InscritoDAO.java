package data;

import business.SSHorario.Inscricao;

import java.sql.*;
import java.util.*;

/**
 * Classe DAO para gerenciar inscrições de alunos em UCs no banco de dados.
 */
public class InscritoDAO {
    private static InscritoDAO singleton = null;

    // Construtor privado para Singleton
    private InscritoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Criar a tabela 'alunos' se não existir
            String sql = "CREATE TABLE IF NOT EXISTS alunos (" +
                    "num VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "media DOUBLE NOT NULL, " +
                    "estatuto VARCHAR(20))";
            stm.executeUpdate(sql);

            // Criar a tabela 'inscricoes' se não existir
            sql = "CREATE TABLE IF NOT EXISTS inscricoes (" +
                    "uc_cod VARCHAR(10) NOT NULL, " +
                    "aluno_num VARCHAR(10) NOT NULL, " +
                    "n_inscricao INT NOT NULL, " +
                    "PRIMARY KEY (uc_cod, aluno_num), " +
                    "FOREIGN KEY (uc_cod) REFERENCES ucs(cod), " +
                    "FOREIGN KEY (aluno_num) REFERENCES alunos(num))";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Implementação do padrão Singleton.
     *
     * @return Instância única de InscritoDAO.
     */
    public static InscritoDAO getInstance() {
        if (singleton == null) {
            singleton = new InscritoDAO();
        }
        return singleton;
    }

    /**
     * Adiciona uma inscrição ao banco de dados.
     *
     * @param ucCod      Código da UC.
     * @param inscricao  Objeto Inscricao.
     */
    public void put(String ucCod, Inscricao inscricao) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "INSERT INTO inscricoes (uc_cod, aluno_num, n_inscricao) VALUES (?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE n_inscricao = VALUES(n_inscricao)")
        ) {
            pstm.setString(1, ucCod);
            pstm.setString(2, inscricao.getAlunoId());
            pstm.setInt(3, inscricao.getNInscricao());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir ou atualizar inscrição: " + e.getMessage());
        }
    }

    /**
     * Adiciona ou atualiza uma coleção de inscrições no banco de dados.
     *
     * @param ucCod Código da UC.
     * @param inscricoes Coleção de objetos Inscricao a serem adicionados ou atualizados.
     */
    public void putAll(String ucCod, Collection<Inscricao> inscricoes) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            conn.setAutoCommit(false); // Iniciar transação

            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO inscricoes (uc_cod, aluno_num, n_inscricao) VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE n_inscricao = VALUES(n_inscricao)")
            ) {
                for (Inscricao inscricao : inscricoes) {
                    pstm.setString(1, ucCod);
                    pstm.setString(2, inscricao.getAlunoId());
                    pstm.setInt(3, inscricao.getNInscricao());
                    pstm.addBatch();
                }
                pstm.executeBatch();
            }

            conn.commit(); // Confirmar transação
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Reverter em caso de erro
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao inserir ou atualizar coleção de inscrições: " + e.getMessage());
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

    /**
     * Obtém todas as inscrições de uma UC.
     *
     * @param ucCod Código da UC.
     * @return Lista de inscrições.
     */
    public List<Inscricao> getByUC(String ucCod) {
        List<Inscricao> inscricoes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM inscricoes WHERE uc_cod = ?")) {
            pstm.setString(1, ucCod);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    String alunoNum = rs.getString("aluno_num");
                    int nInscricao = rs.getInt("n_inscricao");
                    inscricoes.add(new Inscricao(alunoNum, nInscricao));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar inscrições por UC: " + e.getMessage());
        }
        return inscricoes;
    }
}

