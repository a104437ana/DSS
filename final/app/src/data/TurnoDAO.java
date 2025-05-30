package data;

import business.SSHorarios.Turno;
import business.SSHorarios.TurnoT;
import business.SSHorarios.TurnoTP;
import business.SSHorarios.TurnoPL;
import business.SSHorarios.DiaSemana;

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

            // Tabela 'turnos'
            String sql = "CREATE TABLE IF NOT EXISTS turnos (" +
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

    public boolean existeTurno(String idTurno, String codUC) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT idTurno FROM turnos WHERE idTurno=? AND codUC=?")) {
            pstm.setString(1, idTurno);
            pstm.setString(2, codUC);
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
     * Obtém um turno pelo código.
     *
     * @param cod Código do turno.
     * @param codUC Código da UC do turno.
     * @return Objeto Turno correspondente ou null se não encontrado.
     */
    public Turno get(String cod, String codUC) {
        Turno turno = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM turnos WHERE idTurno = ? AND codUC = ?")) {
            pstm.setString(1, cod);
            pstm.setString(2, codUC);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
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
                    String cod = rs.getString("idTurno");
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
                String cod = rs.getString("idTurno");
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
                String cod = rs.getString("idTurno");
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


    /* --------------------- TABELA TURNOSDOALUNO -----------------------*/
    /**
     * Obtém todos os turnos associados a um Aluno específico, divididos por UC.
     *
     * @param numAluno Código do Aluno.
     * @return Map onde a chave é o código da UC e o valor é uma lista de Turnos associados.
     */
    public Map<String, List<Turno>> getByAluno(String numAluno) {
        Map<String, List<Turno>> horario = new HashMap<>();
        String sql = "SELECT t.idTurno AS turnoCod, t.codUC AS ucCod, t.diaSemana, t.horaInicial, " +
                "t.horaFinal, t.lotacao, t.sala, t.tipo " +
                "FROM turnos t " +
                "JOIN turnosDoAluno ta ON t.idTurno = ta.idTurno AND t.codUC = ta.codUC " +
                "WHERE ta.codAluno = ?";

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, numAluno);

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
     * Adiciona um aluno a um turno específico de uma UC.
     *
     * @param codAluno Código do aluno.
     * @param idTurno  Código do turno.
     * @param codUC    Código da UC.
     */
    public void putAlunoTurno(String codAluno, String codUC, String idTurno) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "INSERT INTO turnosdoaluno (codAluno, idTurno, codUC) VALUES (?, ?, ?)")
        ) {
            pstm.setString(1, codAluno);
            pstm.setString(2, idTurno);
            pstm.setString(3, codUC);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir aluno no turno: " + e.getMessage());
        }
    }

    /**
     * Remove um aluno de um turno específico de uma UC.
     *
     * @param codAluno Código do aluno.
     * @param idTurno  Código do turno.
     * @param codUC    Código da UC.
     */
    public void removeAlunoTurno(String codAluno, String codUC, String idTurno) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "DELETE FROM turnosdoaluno WHERE codAluno = ? AND idTurno = ? AND codUC = ?")
        ) {
            pstm.setString(1, codAluno);
            pstm.setString(2, idTurno);
            pstm.setString(3, codUC);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover aluno do turno: " + e.getMessage());
        }
    }

    /**
     * Verifica se um aluno está associado a um turno específico de uma uc.
     *
     * @param codAluno Código do aluno.
     * @param idTurno  Código do turno.
     * @param codUC   Código da UC.
     * @return True se o aluno está associado ao turno, se não False.
     */
    public boolean existeAlunoTurno(String codAluno, String codUC, String idTurno) {
        boolean existe = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "SELECT codAluno FROM turnosdoaluno WHERE codAluno = ? AND idTurno = ? AND codUC = ?")
        ) {
            pstm.setString(1, codAluno);
            pstm.setString(2, idTurno);
            pstm.setString(3, codUC);
            try (ResultSet rs = pstm.executeQuery()) {
                existe = rs.next(); // A chave existe na tabela
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar se aluno está no turno: " + e.getMessage());
        }
        return existe;
    }

}

