/*
 *  DISCLAIMER: Este código foi criado para discussão e edição durante as aulas práticas de DSS, representando
 *  uma solução em construção. Como tal, não deverá ser visto como uma solução canónica, ou mesmo acabada.
 *  É disponibilizado para auxiliar o processo de estudo. Os alunos são encorajados a testar adequadamente o
 *  código fornecido e a procurar soluções alternativas, à medida que forem adquirindo mais conhecimentos.
 */
package projetodss.data;

/**
 * Classe que representa a configuração da base de dados.
 *
 * @author DSS
 * @version 20230915
 */

public class DAOconfig {
    static final String USERNAME = "root";                        // Actualizar
    static final String PASSWORD = "edgarsql";                    // Actualizar
    private static final String DATABASE = "projetodss";          // Actualizar
    //private static final String DRIVER = "jdbc:mariadb";        // Usar para MariaDB
    private static final String DRIVER = "jdbc:mysql";        // Usar para MySQL
    static final String URL = DRIVER + "://localhost:3306/" + DATABASE;
}
