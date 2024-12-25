package data;

import business.SSHorario.Sala;

import java.sql.*;
import java.util.*;

/**
 * Classe DAO para gerenciar operações no banco de dados relacionadas à entidade Sala.
 */
public class SalaDAO {
    private static SalaDAO singleton = null;

    // Construtor privado para Singleton
    private SalaDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Criar a tabela 'salas' se não existir
            String sql = "CREATE TABLE IF NOT EXISTS salas (" +
                    "localizacao VARCHAR(100) NOT NULL PRIMARY KEY, " +
                    "capacidade INT NOT NULL)";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Implementação do padrão Singleton.
     *
     * @return Instância única de SalaDAO.
     */
    public static SalaDAO getInstance() {
        if (singleton == null) {
            singleton = new SalaDAO();
        }
        return singleton;
    }

    /**
     * Obtém uma sala pela localização.
     *
     * @param localizacao Localização da sala a ser buscada.
     * @return Objeto Sala correspondente ou null se não encontrado.
     */
    public Sala get(String localizacao) {
        Sala sala = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM salas WHERE localizacao = ?")) {
            pstm.setString(1, localizacao);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    int capacidade = rs.getInt("capacidade");
                    sala = new Sala(capacidade, localizacao);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar sala: " + e.getMessage());
        }
        return sala;
    }

    /**
     * Obtém todas as salas do banco de dados.
     *
     * @return Coleção contendo todas as salas.
     */
    public Collection<Sala> getAll() {
        Collection<Sala> salas = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM salas")) {
            while (rs.next()) {
                String localizacao = rs.getString("localizacao");
                int capacidade = rs.getInt("capacidade");
                Sala sala = new Sala(capacidade, localizacao);
                salas.add(sala);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todas as salas: " + e.getMessage());
        }
        return salas;
    }

    /**
     * Obtém todas as salas do banco de dados como um mapa.
     *
     * @return Mapa contendo todas as salas, onde a chave é a localização da sala.
     */
    public Map<String, Sala> getAllAsMap() {
        Map<String, Sala> salas = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM salas")) {
            while (rs.next()) {
                String localizacao = rs.getString("localizacao");
                int capacidade = rs.getInt("capacidade");
                Sala sala = new Sala(capacidade, localizacao);
                salas.put(localizacao, sala);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todas as salas: " + e.getMessage());
        }
        return salas;
    }
}
