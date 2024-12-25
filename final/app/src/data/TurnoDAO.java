package data;

import business.SSHorario.*;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;

/**
 * Classe DAO para gerenciar operações no banco de dados relacionadas à entidade Turno.
 */
public class TurnoDAO {
    private static TurnoDAO singleton = null;

    // Construtor privado para Singleton
    private TurnoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Tabela base 'turnos'
            String sql = "CREATE TABLE IF NOT EXISTS turnos (" +
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
     * Implementação do padrão Singleton
     *
     * @return Instância única de TurnoDAO
     */
    public static TurnoDAO getInstance() {
        if (singleton == null) {
            singleton = new TurnoDAO();
        }
        return singleton;
    }

    /**
     * Obtém um turno pelo código.
     *
     * @param cod Código do turno.
     * @return Objeto Turno correspondente ou null se não encontrado.
     */
    public Turno get(String cod) {
        Turno turno = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM turnos WHERE cod = ?")) {
            pstm.setString(1, cod);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String codUC = rs.getString("codUC");
                    DiaSemana diaSemana = DiaSemana.valueOf(rs.getString("diaSemana"));
                    LocalTime horaInicial = rs.getTime("horaInicial").toLocalTime();
                    LocalTime horaFinal = rs.getTime("horaFinal").toLocalTime();
                    int lotacao = rs.getInt("lotacao");
                    String sala = rs.getString("sala");
                    String tipo = rs.getString("tipo");

                    // Criação do objeto correto com base no tipo
                    if ("TP".equals(tipo)) {
                        turno = new TurnoTP(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    } else if ("T".equals(tipo)) {
                        turno = new TurnoT(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    } else if ("PL".equals(tipo)) {
                        turno = new TurnoPL(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar turno: " + e.getMessage());
        }
        return turno;
    }

    /**
     * Obtém todos os turnos associados a uma UC específica.
     *
     * @param codUC Código da UC.
     * @return Coleção de turnos associados à UC.
     */
    public Collection<Turno> getByUC(String codUC) {
        Collection<Turno> turnos = new ArrayList<>();
        String sql = "SELECT * FROM turnos WHERE codUC = ?";

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, codUC); // Substitui o parâmetro pelo código da UC.

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    String cod = rs.getString("cod");
                    DiaSemana diaSemana = DiaSemana.valueOf(rs.getString("diaSemana"));
                    LocalTime horaInicial = rs.getTime("horaInicial").toLocalTime();
                    LocalTime horaFinal = rs.getTime("horaFinal").toLocalTime();
                    int lotacao = rs.getInt("lotacao");
                    String sala = rs.getString("sala");
                    String tipo = rs.getString("tipo");

                    Turno turno = null;
                    if ("TP".equals(tipo)) {
                        turno = new TurnoTP(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    } else if ("T".equals(tipo)) {
                        turno = new TurnoT(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    } else if ("PL".equals(tipo)) {
                        turno = new TurnoPL(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    }

                    if (turno != null) {
                        turnos.add(turno);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar turnos pela UC: " + e.getMessage());
        }

        return turnos;
    }

    /**
     * Obtém todos os turnos associados a um Aluno específico, divididos por UC.
     *
     * @param numAluno Código do Aluno.
     * @return Map onde a chave é o código da UC e o valor é uma lista de Turnos associados.
     */
    public Map<String, List<Turno>> getByAluno(String numAluno) {
        Map<String, List<Turno>> horario = new HashMap<>();
        String sql = "SELECT t.cod AS turnoCod, t.codUC AS ucCod, t.diaSemana, t.horaInicial, " +
                "t.horaFinal, t.lotacao, t.sala, t.tipo " +
                "FROM turnos t " +
                "JOIN turnosDoAluno ta ON t.cod = ta.codTurno " +
                "WHERE ta.aluno_num = ?";

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, numAluno); // Define o código do aluno no parâmetro.

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    String turnoCod = rs.getString("turnoCod");
                    String ucCod = rs.getString("ucCod");
                    DiaSemana diaSemana = DiaSemana.valueOf(rs.getString("diaSemana"));
                    LocalTime horaInicial = rs.getTime("horaInicial").toLocalTime();
                    LocalTime horaFinal = rs.getTime("horaFinal").toLocalTime();
                    int lotacao = rs.getInt("lotacao");
                    String sala = rs.getString("sala");
                    String tipo = rs.getString("tipo");

                    // Criar instância do turno de acordo com o tipo
                    Turno turno = null;
                    if ("TP".equals(tipo)) {
                        turno = new TurnoTP(turnoCod, ucCod, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    } else if ("T".equals(tipo)) {
                        turno = new TurnoT(turnoCod, ucCod, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    } else if ("PL".equals(tipo)) {
                        turno = new TurnoPL(turnoCod, ucCod, diaSemana, horaInicial, horaFinal, lotacao, sala);
                    }

                    // Adicionar o turno à lista correspondente no Map
                    if (turno != null) {
                        horario.computeIfAbsent(ucCod, k -> new ArrayList<>()).add(turno);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar turnos por aluno: " + e.getMessage());
        }

        return horario;
    }


    /**
     * Obtém todos os turnos do banco de dados.
     *
     * @return Coleção contendo todos os turnos.
     */
    public Collection<Turno> getAll() {
        Collection<Turno> turnos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM turnos")) {
            while (rs.next()) {
                String cod = rs.getString("cod");
                String codUC = rs.getString("codUC");
                DiaSemana diaSemana = DiaSemana.valueOf(rs.getString("diaSemana"));
                LocalTime horaInicial = rs.getTime("horaInicial").toLocalTime();
                LocalTime horaFinal = rs.getTime("horaFinal").toLocalTime();
                int lotacao = rs.getInt("lotacao");
                String sala = rs.getString("sala");
                String tipo = rs.getString("tipo");

                Turno turno = null;
                if ("TP".equals(tipo)) {
                    turno = new TurnoTP(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                } else if ("T".equals(tipo)) {
                    turno = new TurnoT(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                } else if ("PL".equals(tipo)) {
                    turno = new TurnoPL(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                }

                turnos.add(turno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os turnos: " + e.getMessage());
        }
        return turnos;
    }

    /**
     * Obtém todos os turnos do banco de dados como um mapa.
     *
     * @return Mapa contendo todos os turnos, onde a chave é o código do turno.
     */
    public Map<String, Turno> getAllAsMap() {
        Map<String, Turno> turnos = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM turnos")) {
            while (rs.next()) {
                String cod = rs.getString("cod");
                String codUC = rs.getString("codUC");
                DiaSemana diaSemana = DiaSemana.valueOf(rs.getString("diaSemana"));
                LocalTime horaInicial = rs.getTime("horaInicial").toLocalTime();
                LocalTime horaFinal = rs.getTime("horaFinal").toLocalTime();
                int lotacao = rs.getInt("lotacao");
                String sala = rs.getString("sala");
                String tipo = rs.getString("tipo");

                Turno turno = null;
                if ("TP".equals(tipo)) {
                    turno = new TurnoTP(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                } else if ("T".equals(tipo)) {
                    turno = new TurnoT(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                } else if ("PL".equals(tipo)) {
                    turno = new TurnoPL(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
                }

                turnos.put(cod, turno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os turnos: " + e.getMessage());
        }
        return turnos;
    }
}

