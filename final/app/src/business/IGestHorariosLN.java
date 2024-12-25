package business;

public interface IGestHorariosLN {
    /**
     * Importa utilizadores, alunos e suas inscrições de dois arquivos CSV.
     *
     * @param ficheiroAlunos Caminho para o arquivo CSV contendo os dados dos alunos.
     * @param ficheiroUCs Caminho para o arquivo CSV contendo as inscrições dos alunos nas UCs.
     */
    void importAlunos(String ficheiroAlunos, String ficheiroUCs);

    /**
     * Consulta horário de um determinado aluno.
     *
     * @param codAluno Código de aluno cujo horário se quer consultar.
     * @return String Horário do aluno.
     */
    String consultarHorario(String codAluno);

    /**
     * Método de ínicio de sessão.
     *
     * @param id Id de utilizador a procurar
     * @param senha Senha do utilizador a verificar
     *
     * @return String com id do utilizador autenticado ou null se credenciais estão inválidas
     */
    String login(String id, String senha);

    /**
     * Método que preenche as tabelas das salas, ucs e turnos com dados pré-definidos.
     */
    void inicializacaoSalasUCsTurnos();
}
