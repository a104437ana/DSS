package projetodss.business;

import java.util.Collection;

public interface IGestorHorariosFacade {

    /**
     * Método que obtém todos os alunos atualmente registados no sistema.
     *
     * @return Coleção de objetos Aluno contendo todos os alunos registados.
     */
    Collection<Aluno> getAlunos();

    /**
     * Método que verifica se existem alunos registados no sistema.
     *
     * @return true se houverem alunos registados; caso contrário, false.
     */
    boolean existemAlunos();

    /**
     * Método que obtém o número total de alunos registados no sistema.
     *
     * @return Número de alunos registados.
     */
    int numAlunos();

    /**
     * Método que obtém o utilizador registado com o ID fornecido.
     *
     * @param id Identificador único do utilizador a ser consultado.
     * @return Objeto Utilizador com o ID especificado, ou null se não for encontrado.
     */
    Utilizador getUtilizador(String id);

    /**
     * Método que obtém o aluno registado com o número de identificação fornecido.
     *
     * @param num Número único do aluno a ser consultado.
     * @return Objeto Aluno com o número especificado, ou null se não for encontrado.
     */
    Aluno getAluno(String num);

    /**
     * Método que verifica se um aluno com o ID fornecido está registado no sistema.
     *
     * @param id Identificador único do aluno.
     * @return true se o aluno com o ID especificado existir; caso contrário, false.
     */
    boolean existeAluno(String id);

    /**
     * Método que verifica se um utilizador apresenta a senha dada
     *
     * @param u Utilizador ao qual se quer validar a senha.
     * @param senha Senha que se quer validar
     * @return true se o utilizador tiver uma senha igual à dada
     */
    boolean validaUserSenha(Utilizador u, String senha);

    /**
     * Método que adiciona um novo aluno ao sistema.
     *
     * @param a Objeto Aluno a ser adicionado ao sistema.
     */
    void adicionaAluno(Aluno a);

    /**
     * Método que exibe a lista de todos os alunos registados no sistema.
     * Este método pode ser utilizado para fins de visualização ou verificação de dados.
     */
    void listaAlunos();

    /**
     * Método que importa alunos a partir de um ficheiro específico.
     * O ficheiro deve estar em um formato suportado e conter as informações necessárias.
     *
     * @param ficheiro Caminho para o ficheiro que contém os dados dos alunos.
     */
    void importAlunos(String ficheiro);

    /**
     * Método que modifica a senha de um utilizador.
     *
     * @param u Utilizador ao qual queremos mudar a senha
     * @param senha Nova senha
     */
    boolean alterarSenha(Utilizador u, String senha);
}
