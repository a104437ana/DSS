package business.SSUtilizadores;

import data.UtilizadorDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UtilizadoresFacade implements IUtilizadoresFacade{
    private UtilizadorDAO utilizadoresDAO;

    public UtilizadoresFacade() {
        this.utilizadoresDAO = UtilizadorDAO.getInstance();
    }

    /**
     * Importa utilizadores a partir de um arquivo CSV.
     *
     * @param ficheiro Caminho para o arquivo CSV contendo os utilizadores.
     */
    public void importarUtilizadores(String ficheiro) {
        List<Utilizador> utilizadores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            br.readLine(); // Ignorar a linha de cabeçalho

            String linha;
            while ((linha = br.readLine()) != null) {
                String[] atributos = linha.split(",");
                if (atributos.length < 5) continue; // Garantir que existem ao menos 5 campos

                String id = atributos[0];
                String senha = atributos[4];

                // Criar o utilizador (usamos id e senha apenas)
                Utilizador utilizador = new Utilizador(id, senha);
                utilizadores.add(utilizador);
            }

            // Inserir todos os utilizadores no banco de dados
            utilizadoresDAO.putAll(utilizadores);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao importar utilizadores: " + e.getMessage(), e);
        }
    }


    /**
     * Método de ínicio de sessão.
     *
     * @param id Id de utilizador a procurar
     * @param senha Senha do utilizador a verificar
     *
     * @return String com id do utilizador autenticado ou null se credenciais estão inválidas
     */

    public String login(String id, String senha){
        Utilizador u = this.utilizadoresDAO.get(id);
        if (u!=null && (u.getSenha().equals(senha))) {
            System.out.println("'" + id + "' logado com sucesso!");
            return id;
        } else {
            return null;
        }
    }
}
