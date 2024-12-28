package data;

import business.SSHorarios.Inscricao;

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
                    "codAluno VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "media DOUBLE NOT NULL, " +
                    "estatuto VARCHAR(20))";
            stm.executeUpdate(sql);

            // Criar a tabela 'inscricoes' se não existir
            sql = "CREATE TABLE IF NOT EXISTS inscricoes (" +
                    "codUC VARCHAR(10) NOT NULL, " +
                    "codAluno VARCHAR(10) NOT NULL, " +
                    "nInscricao INT NOT NULL, " +
                    "PRIMARY KEY (codUC, codAluno), " +
                    "FOREIGN KEY (codUC) REFERENCES ucs(codUC), " +
                    "FOREIGN KEY (codAluno) REFERENCES alunos(codAluno))";
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
                     "INSERT INTO inscricoes (codUC, codAluno, nInscricao) VALUES (?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE nInscricao = VALUES(nInscricao)")
        ) {
            pstm.setString(1, ucCod);
            pstm.setString(2, inscricao.getCodAluno());
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
                    "INSERT INTO inscricoes (codUC, codAluno, nInscricao) VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE nInscricao = VALUES(nInscricao)")
            ) {
                for (Inscricao inscricao : inscricoes) {
                    pstm.setString(1, ucCod);
                    pstm.setString(2, inscricao.getCodAluno());
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
     * Método para remover todas as inscrições de uma dada UC.
     */
    public void removerInscricoesUC(String codUC) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "DELETE FROM inscricoes WHERE codUC = ?")
            ) {

            pstm.setString(1, codUC);
            pstm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar todos as inscricoes da UC: " + e.getMessage());
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
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM inscricoes WHERE codUC = ?")) {
            pstm.setString(1, ucCod);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    String alunoNum = rs.getString("codAluno");
                    int nInscricao = rs.getInt("nInscricao");
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

