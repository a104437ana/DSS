
import data.DAOconfig;
import ui.TextUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 *
 */
public class Main {

    /**
     * O método main cria a aplicação e invoca o método run()
     */
    public static void main(String[] args) {
        try {
            DAOconfig.iniciarBD();
            new TextUI().run();
        } catch (Exception e) {
            System.out.println("Erro fatal: " + e.getMessage() + " [" + e.toString() + "]");
        }
    }
}
