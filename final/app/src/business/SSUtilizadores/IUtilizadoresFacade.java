package business.SSUtilizadores;

public interface IUtilizadoresFacade {

    /**
     * Importa utilizadores a partir de um arquivo CSV.
     *
     * @param ficheiro Caminho para o arquivo CSV contendo os utilizadores.
     */
    void importarUtilizadores(String ficheiro);

    /**
     * Método de ínicio de sessão.
     *
     * @param id Id de utilizador a procurar
     * @param senha Senha do utilizador a verificar
     *
     * @return String com id do utilizador autenticado ou null se credenciais estão inválidas
     */
    String login(String id, String senha);
}
