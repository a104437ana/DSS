package data;

import business.SSHorario.Pref;
import business.SSHorario.UC;

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
                    "cod VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "semestre INT NOT NULL, " +
                    "opcional BOOLEAN NOT NULL, " +
                    "preferencia VARCHAR(20) NOT NULL)";
            stm.executeUpdate(sql);

            // Tabela base 'turnos'
            sql = "CREATE TABLE IF NOT EXISTS turnos (" +
                    "cod VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "codUC VARCHAR(10) NOT NULL, " +
                    "diaSemana VARCHAR(10) NOT NULL, " +
                    "horaInicial TIME NOT NULL, " +
                    "horaFinal TIME NOT NULL, " +
                    "lotacao INT NOT NULL, " +
                    "sala VARCHAR(50) NOT NULL, " +
                    "tipo ENUM('TP', 'T', 'PL') NOT NULL, " +
                    "FOREIGN KEY (codUC) REFERENCES ucs(cod), " +
                    "FOREIGN KEY (sala) REFERENCES salas(localizacao))";
            stm.executeUpdate(sql);

            // Tabela 'turnosTP'
            sql = "CREATE TABLE IF NOT EXISTS turnosTP (" +
                    "cod VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "FOREIGN KEY (cod) REFERENCES turnos(cod))";
            stm.executeUpdate(sql);

            // Tabela 'turnosT'
            sql = "CREATE TABLE IF NOT EXISTS turnosT (" +
                    "cod VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "FOREIGN KEY (cod) REFERENCES turnos(cod))";
            stm.executeUpdate(sql);

            // Tabela 'turnosPL'
            sql = "CREATE TABLE IF NOT EXISTS turnosPL (" +
                    "cod VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "FOREIGN KEY (cod) REFERENCES turnos(cod))";
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
     * Obtém uma UC pelo código.
     *
     * @param cod Código da UC a ser buscada.
     * @return Objeto UC correspondente ou null se não encontrado.
     */
    public UC get(String cod) {
        UC uc = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM ucs WHERE cod = ?")) {
            pstm.setString(1, cod);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    int semestre = rs.getInt("semestre");
                    boolean opcional = rs.getBoolean("opcional");
                    Pref preferencia = Pref.valueOf(rs.getString("preferencia"));

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
                String cod = rs.getString("cod");
                String nome = rs.getString("nome");
                int semestre = rs.getInt("semestre");
                boolean opcional = rs.getBoolean("opcional");
                Pref preferencia = Pref.valueOf(rs.getString("preferencia"));

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
                String cod = rs.getString("cod");
                String nome = rs.getString("nome");
                int semestre = rs.getInt("semestre");
                boolean opcional = rs.getBoolean("opcional");
                Pref preferencia = Pref.valueOf(rs.getString("preferencia"));

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
