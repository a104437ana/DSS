package data;

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
}

// Erro do time zone: SET GLOBAL time_zone = '+00:00';