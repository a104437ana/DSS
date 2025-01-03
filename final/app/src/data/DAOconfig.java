package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe que representa a configuração da base de dados.
 */

public class DAOconfig {
    static final String USERNAME = "root";
    static final String PASSWORD = "edgarsql";
    private static final String DATABASE = "dss";
    //private static final String DRIVER = "jdbc:mariadb";              // Usar para MariaDB
    private static final String DRIVER = "jdbc:mysql";                  // Usar para MySQL
    static final String URL = DRIVER + "://localhost:3306/" + DATABASE;

    public static void iniciarBD() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Criar a tabela 'alunos' se não existir
            String sql = "CREATE DATABASE IF NOT EXISTS dss;";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}

// Erro do time zone: SET GLOBAL time_zone = '+00:00';