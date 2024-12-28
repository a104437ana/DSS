package business.SSUtilizadores;

import java.util.Map;

public interface ISSUtilizadores {

    /**
     * Importa utilizadores a partir de um arquivo CSV.
     *
     * @param mapAlunosImportados Caminho para o arquivo CSV contendo os utilizadores.
     */
    void atualizarUtilizadores(Map<String, String> mapAlunosImportados);

    /**
     * Método de ínicio de sessão.
     *
     * @param codUtilizador Id de utilizador a procurar
     * @param senha Senha do utilizador a verificar
     *
     * @return String com id do utilizador autenticado ou null se credenciais estão inválidas
     */
    String iniciarSessao(String codUtilizador, String senha);
}
