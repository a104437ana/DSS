package data;

import business.SSHorarios.Preferência;
import business.SSHorarios.UC;

import java.sql.*;
import java.util.*;

/**
 * Classe DAO para gerenciar operações no banco de dados relacionadas à entidade UC.
 */
public class UCDAO {
    private static UCDAO singleton = null;

    // Construtor privado para Singleton
    private UCDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Criar a tabela 'ucs' se não existir
            String sql = "CREATE TABLE IF NOT EXISTS ucs (" +
                    "codUC VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "semestre INT NOT NULL, " +
                    "opcional BOOLEAN NOT NULL, " +
                    "preferencia VARCHAR(20) NOT NULL)";
            stm.executeUpdate(sql);

            // Tabela 'turnos'
            sql = "CREATE TABLE IF NOT EXISTS turnos (" +
                    "idTurno VARCHAR(10) NOT NULL, " +
                    "codUC VARCHAR(10) NOT NULL, " +
                    "diaSemana VARCHAR(10) NOT NULL, " +
                    "horaInicial TIME NOT NULL, " +
                    "horaFinal TIME NOT NULL, " +
                    "lotacao INT NOT NULL, " +
                    "sala VARCHAR(50) NOT NULL, " +
                    "tipo ENUM('TP', 'T', 'PL') NOT NULL, " +
                    "PRIMARY KEY (idTurno, codUC), " +
                    "FOREIGN KEY (codUC) REFERENCES ucs(codUC), " +
                    "FOREIGN KEY (sala) REFERENCES salas(localizacao))";
            stm.executeUpdate(sql);

            // Tabela 'turnosTP'
            sql = "CREATE TABLE IF NOT EXISTS turnosTP (" +
                    "idTurno VARCHAR(10) NOT NULL, " +
                    "codUC VARCHAR(10) NOT NULL, " +
                    "PRIMARY KEY (idTurno, codUC), " +
                    "FOREIGN KEY (idTurno, codUC) REFERENCES turnos(idTurno, codUC))";
            stm.executeUpdate(sql);

            // Tabela 'turnosT'
            sql = "CREATE TABLE IF NOT EXISTS turnosT (" +
                    "idTurno VARCHAR(10) NOT NULL, " +
                    "codUC VARCHAR(10) NOT NULL, " +
                    "PRIMARY KEY (idTurno, codUC), " +
                    "FOREIGN KEY (idTurno, codUC) REFERENCES turnos(idTurno, codUC))";
            stm.executeUpdate(sql);

            // Tabela 'turnosPL'
            sql = "CREATE TABLE IF NOT EXISTS turnosPL (" +
                    "idTurno VARCHAR(10) NOT NULL, " +
                    "codUC VARCHAR(10) NOT NULL, " +
                    "PRIMARY KEY (idTurno, codUC), " +
                    "FOREIGN KEY (idTurno, codUC) REFERENCES turnos(idTurno, codUC))";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Implementação do padrão Singleton.
     *
     * @return Instância única de UCDAO.
     */
    public static UCDAO getInstance() {
        if (singleton == null) {
            singleton = new UCDAO();
        }
        return singleton;
    }

    /**
     * Inserção de UCs e Turnos iniciais.
     *
     */
    public void inserirUcsETurnosIniciais() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            conn.setAutoCommit(false); // Inicia uma transação

            try (Statement stm = conn.createStatement()) {

                // Inserir UCs
                stm.executeUpdate("INSERT IGNORE INTO ucs (codUC, nome, semestre, opcional, preferencia) VALUES " +
                        "('UC101', 'Cálculo', 1, FALSE, 'MEDIA')," +
                        "('UC102', 'Programação Funcional', 1, FALSE, 'MEDIA')," +
                        "('UC103', 'Álgebra Linear', 1, FALSE, 'INSCRICAO')," +
                        "('UC201', 'Estatística Aplicada', 1, FALSE, 'INSCRICAO')," +
                        "('UC202', 'Arquitetura de Computadores', 1, FALSE, 'MEDIA')," +
                        "('UC203', 'Física Moderna', 1, FALSE, 'NENHUM')");

                // Inserir turnos para Cálculo
                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('T1', 'UC101', 'SEGUNDA', '08:00:00', '10:00:00', 20, 'Ed1-0.02', 'T')," +
                        "('T2', 'UC101', 'QUARTA', '10:00:00', '12:00:00', 20, 'Ed1-0.02', 'T')," +
                        "('TP1', 'UC101', 'TERCA', '08:00:00', '09:30:00', 10, 'Ed1-1.01', 'TP')," +
                        "('TP2', 'UC101', 'QUINTA', '10:30:00', '12:00:00', 10, 'Ed1-1.03', 'TP')," +
                        "('TP3', 'UC101', 'SEXTA', '14:00:00', '16:00:00', 10, 'Ed1-1.02', 'TP')," +
                        "('TP4', 'UC101', 'SEXTA', '14:00:00', '16:00:00', 10, 'Ed1-1.03', 'TP')");
                stm.executeUpdate("INSERT IGNORE INTO turnosT (idTurno, codUC) VALUES ('T1', 'UC101'), ('T2', 'UC101')");
                stm.executeUpdate("INSERT IGNORE INTO turnosTP (idTurno, codUC) VALUES ('TP1', 'UC101'), ('TP2', 'UC101'), ('TP3', 'UC101'), ('TP4', 'UC101')");

                // Inserir turnos para Programação Funcional
                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('T1', 'UC102', 'SEGUNDA', '10:00:00', '12:00:00', 20, 'Ed1-0.02', 'T')," +
                        "('T2', 'UC102', 'TERCA', '14:00:00', '16:00:00', 20, 'Ed1-0.02', 'T')," +
                        "('TP1', 'UC102', 'QUARTA', '08:00:00', '09:30:00', 10, 'Ed1-1.01', 'TP')," +
                        "('TP2', 'UC102', 'QUINTA', '09:30:00', '11:00:00', 10, 'Ed2-1.03', 'TP')," +
                        "('TP3', 'UC102', 'SEXTA', '11:00:00', '12:30:00', 10, 'Ed1-1.02', 'TP')," +
                        "('TP4', 'UC102', 'SEXTA', '14:00:00', '15:30:00', 10, 'Ed1-1.01', 'TP')");
                stm.executeUpdate("INSERT IGNORE INTO turnosT (idTurno, codUC) VALUES ('T1', 'UC102'), ('T2', 'UC102')");
                stm.executeUpdate("INSERT IGNORE INTO turnosTP (idTurno, codUC) VALUES ('TP1', 'UC102'), ('TP2', 'UC102'), ('TP3', 'UC102'), ('TP4', 'UC102')");

                // Inserir turnos para Álgebra Linear
                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('T1', 'UC103', 'SEGUNDA', '10:00:00', '12:00:00', 20, 'Ed3-0.06', 'T')," +
                        "('T2', 'UC103', 'QUINTA', '11:00:00', '13:00:00', 20, 'Ed1-0.08', 'T')," +
                        "('TP1', 'UC103', 'QUARTA', '11:00:00', '13:00:00', 10, 'Ed3-2.01', 'TP')," +
                        "('TP2', 'UC103', 'QUARTA', '14:00:00', '16:00:00', 10, 'Ed2-2.06', 'TP')," +
                        "('TP3', 'UC103', 'QUINTA', '09:00:00', '11:00:00', 10, 'Ed2-1.15', 'TP')");
                stm.executeUpdate("INSERT IGNORE INTO turnosT (idTurno, codUC) VALUES ('T1', 'UC103'), ('T2', 'UC103')");
                stm.executeUpdate("INSERT IGNORE INTO turnosTP (idTurno, codUC) VALUES ('TP1', 'UC103'), ('TP2', 'UC103'), ('TP3', 'UC103')");

                // Inserir turnos para Estatística Aplicada
                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('T1', 'UC201', 'SEGUNDA', '14:00:00', '16:00:00', 20, 'Ed3-0.06', 'T')," +
                        "('T2', 'UC201', 'QUARTA', '16:00:00', '18:00:00', 20, 'Ed3-0.06', 'T')");
                stm.executeUpdate("INSERT IGNORE INTO turnosT (idTurno, codUC) VALUES ('T1', 'UC201'), ('T2', 'UC201')");

                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('TP1', 'UC201', 'SEGUNDA', '11:00:00', '13:00:00', 10, 'Ed3-1.05', 'TP')," +
                        "('TP2', 'UC201', 'QUARTA', '09:00:00', '11:00:00', 10, 'Ed1-2.14', 'TP')," +
                        "('TP3', 'UC201', 'QUARTA', '11:00:00', '13:00:00', 10, 'Ed1-2.17', 'TP')");
                stm.executeUpdate("INSERT IGNORE INTO turnosTP (idTurno, codUC) VALUES ('TP1', 'UC201'), ('TP2', 'UC201'), ('TP3', 'UC201')");

                // Inserir turnos para Arquitetura de Computadores
                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('T1', 'UC202', 'TERCA', '13:00:00', '15:00:00', 20, 'Ed3-0.06', 'T')," +
                        "('T2', 'UC202', 'SEXTA', '14:00:00', '16:00:00', 20, 'Ed2-0.05', 'T')");
                stm.executeUpdate("INSERT IGNORE INTO turnosT (idTurno, codUC) VALUES ('T1', 'UC202'), ('T2', 'UC202')");

                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('TP1', 'UC202', 'QUARTA', '14:00:00', '16:00:00', 10, 'Ed7-1.10', 'TP')," +
                        "('TP2', 'UC202', 'QUINTA', '13:30:00', '15:30:00', 10, 'Ed7-1.10', 'TP')," +
                        "('TP3', 'UC202', 'QUINTA', '13:30:00', '15:30:00', 10, 'Ed7-0.08', 'TP')");
                stm.executeUpdate("INSERT IGNORE INTO turnosTP (idTurno, codUC) VALUES ('TP1', 'UC202'), ('TP2', 'UC202'), ('TP3', 'UC202')");

                // Inserir turnos para Física Moderna
                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('T1', 'UC203', 'TERCA', '15:00:00', '17:00:00', 20, 'Ed3-0.06', 'T')," +
                        "('T2', 'UC203', 'QUINTA', '18:00:00', '20:00:00', 20, 'Ed1-0.08', 'T')");
                stm.executeUpdate("INSERT IGNORE INTO turnosT (idTurno, codUC) VALUES ('T1', 'UC203'), ('T2', 'UC203')");

                stm.executeUpdate("INSERT IGNORE INTO turnos (idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala, tipo) VALUES " +
                        "('TP1', 'UC203', 'SEGUNDA', '11:00:00', '13:00:00', 10, 'Ed2-2.09', 'TP')," +
                        "('TP2', 'UC203', 'QUARTA', '11:00:00', '13:00:00', 10, 'Ed2-2.09', 'TP')," +
                        "('TP3', 'UC203', 'QUINTA', '09:00:00', '11:00:00', 10, 'Ed2-0.20', 'TP')");
                stm.executeUpdate("INSERT IGNORE INTO turnosTP (idTurno, codUC) VALUES ('TP1', 'UC203'), ('TP2', 'UC203'), ('TP3', 'UC203')");

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                throw new RuntimeException("Erro ao inserir UCs e turnos: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true); // Restaura o estado padrão do autocommit
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }


    /**
     * Método que cerifica se um código de UC existe na base de dados
     *
     * @param key codigo de uma UC
     * @return true se a UC existe, false se não existe
     * @throws NullPointerException Em caso de erro
     */

    public boolean existeUC(Object key) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT codUC FROM ucs WHERE codUC=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                r = rs.next();  // A chave existe na tabela
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar código do aluno: " + e.getMessage());
        }
        return r;
    }

    /**
     * Remoção de UCs e Turnos.
     *
     */
    public void removerTodosTurnosEUCs() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            conn.setAutoCommit(false); // Inicia uma transação

            try (Statement stm = conn.createStatement()) {

                // Desabilitar constraints temporariamente para garantir a exclusão
                stm.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");

                // Remover todos os turnos dependentes primeiro
                stm.executeUpdate("DELETE FROM turnosTP");
                stm.executeUpdate("DELETE FROM turnosT");
                stm.executeUpdate("DELETE FROM turnosPL");
                stm.executeUpdate("DELETE FROM turnos");

                // Remover todas as UCs
                stm.executeUpdate("DELETE FROM ucs");

                // Reativar constraints após exclusão
                stm.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                throw new RuntimeException("Erro ao inserir UCs e turnos: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true); // Restaura o estado padrão do autocommit
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Obtém uma UC pelo código.
     *
     * @param cod Código da UC a ser buscada.
     * @return Objeto UC correspondente ou null se não encontrado.
     */
    public UC get(String cod) {
        UC uc = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM ucs WHERE codUC = ?")) {
            pstm.setString(1, cod);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    int semestre = rs.getInt("semestre");
                    boolean opcional = rs.getBoolean("opcional");
                    Preferência preferencia = Preferência.valueOf(rs.getString("preferencia"));

                    uc = new UC(cod, nome, semestre, opcional, preferencia);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar UC: " + e.getMessage());
        }
        return uc;
    }

    /**
     * Obtém todas as UCs do banco de dados.
     *
     * @return Coleção contendo todas as UCs.
     */
    public Collection<UC> getAll() {
        Collection<UC> ucs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM ucs")) {
            while (rs.next()) {
                String cod = rs.getString("codUC");
                String nome = rs.getString("nome");
                int semestre = rs.getInt("semestre");
                boolean opcional = rs.getBoolean("opcional");
                Preferência preferencia = Preferência.valueOf(rs.getString("preferencia"));

                UC uc = new UC(cod, nome, semestre, opcional, preferencia);
                ucs.add(uc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todas as UCs: " + e.getMessage());
        }
        return ucs;
    }

    /**
     * Obtém todas as UCs do banco de dados como um mapa.
     *
     * @return Mapa contendo todas as UCs, onde a chave é o código da UC.
     */
    public Map<String, UC> getAllAsMap() {
        Map<String, UC> ucs = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM ucs")) {
            while (rs.next()) {
                String cod = rs.getString("codUC");
                String nome = rs.getString("nome");
                int semestre = rs.getInt("semestre");
                boolean opcional = rs.getBoolean("opcional");
                Preferência preferencia = Preferência.valueOf(rs.getString("preferencia"));

                UC uc = new UC(cod, nome, semestre, opcional, preferencia);
                ucs.put(cod, uc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todas as UCs: " + e.getMessage());
        }
        return ucs;
    }
}
