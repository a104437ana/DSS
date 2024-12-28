package business.SSUtilizadores;

import data.UtilizadorDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSUtilizadores implements ISSUtilizadores {
    private UtilizadorDAO utilizadoresDAO;

    public SSUtilizadores() {
        this.utilizadoresDAO = UtilizadorDAO.getInstance();
    }

    /**
     * Atualiza os utilizadores no banco de dados com base em um mapa fornecido.
     *
     * @param mapAlunosImportados Mapa contendo o código do aluno como chave e a senha como valor.
     */
    public void atualizarUtilizadores(Map<String, String> mapAlunosImportados) {
        List<Utilizador> novosUtilizadores = new ArrayList<>();

        try {
            // Obter a lista de todos os utilizadores existentes
            List<String> utilizadoresExistentes = this.utilizadoresDAO.getAllCods();

            // Verificar e adicionar utilizadores do mapa
            for (Map.Entry<String, String> entry : mapAlunosImportados.entrySet()) {
                String codUtilizador = entry.getKey();
                String senha = entry.getValue();

                // Adicionar apenas se o utilizador ainda não existir
                if (!utilizadoresExistentes.contains(codUtilizador)) {
                    Utilizador utilizador = new Utilizador(codUtilizador, senha);
                    novosUtilizadores.add(utilizador);
                }

                // Remover da lista existente para sabermos que foi atualizado
                utilizadoresExistentes.remove(codUtilizador);
            }

            // Adicionar novos utilizadores
            if (!novosUtilizadores.isEmpty()) {
                this.utilizadoresDAO.putAll(novosUtilizadores);
            }

            // Remover os utilizadores que não estão mais no mapa, ou seja aqueles que não foram atualziados
            if (!utilizadoresExistentes.isEmpty()) {
                utilizadoresDAO.removerLista(utilizadoresExistentes);
            }

        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar utilizadores: " + e.getMessage(), e);
        }
    }




    /**
     * Método de ínicio de sessão.
     *
     * @param codUtilizador Id de utilizador a procurar
     * @param senha Senha do utilizador a verificar
     *
     * @return String com id do utilizador autenticado ou null se credenciais estão inválidas
     */

    public String iniciarSessao(String codUtilizador, String senha){
        Utilizador u = this.utilizadoresDAO.get(codUtilizador);
        if (u!=null && (u.getSenha().equals(senha))) {
            System.out.println("'" + codUtilizador + "' logado com sucesso!");
            return codUtilizador;
        } else {
            return null;
        }
    }
}
