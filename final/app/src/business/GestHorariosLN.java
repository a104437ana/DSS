package business;


import business.SSHorario.HorariosFacade;
import business.SSHorario.IHorariosFacade;
import business.SSUtilizadores.IUtilizadoresFacade;
import business.SSUtilizadores.UtilizadoresFacade;

public class GestHorariosLN implements IGestHorariosLN{
    private IUtilizadoresFacade utilizadores;
    private IHorariosFacade horarios;

    public GestHorariosLN() {
        this.utilizadores = new UtilizadoresFacade();
        this.horarios = new HorariosFacade();
    }

    /**
     * Importa utilizadores, alunos e suas inscrições de dois arquivos CSV.
     *
     * @param ficheiroAlunos Caminho para o arquivo CSV contendo os dados dos alunos.
     * @param ficheiroUCs Caminho para o arquivo CSV contendo as inscrições dos alunos nas UCs.
     */
    public void importAlunos(String ficheiroAlunos, String ficheiroUCs) {
        try {
            // Importar utilizadores
            System.out.println("Importando utilizadores...");
            this.utilizadores.importarUtilizadores(ficheiroAlunos);
            System.out.println("Utilizadores importados com sucesso.");

            // Importar alunos
            System.out.println("Importando alunos...");
            this.horarios.importarAlunos(ficheiroAlunos);
            System.out.println("Alunos importados com sucesso.");

            // Importar inscrições
            System.out.println("Importando inscrições...");
            this.horarios.importarInscricoesAlunos(ficheiroUCs);
            System.out.println("Inscrições importadas com sucesso.");

        } catch (RuntimeException e) {
            System.err.println("Erro durante a importação: " + e.getMessage());
        }
    }

    /**
     * Consulta horário de um determinado aluno.
     *
     * @param codAluno Código de aluno cujo horário se quer consultar.
     * @return String Horário do aluno.
     */
    public String consultarHorario(String codAluno) {
        return this.horarios.getHorarioAluno(codAluno);
    }

    /**
     * Método de ínicio de sessão.
     *
     * @param id Id de utilizador a procurar
     * @param senha Senha do utilizador a verificar
     *
     * @return String com id do utilizador autenticado ou null se credenciais estão inválidas
     */
    public String login(String id, String senha) {
        return this.utilizadores.login(id, senha);
    }

    /**
     * Método que preenche as tabelas das salas, ucs e turnos com dados pré-definidos.
     */
    public void inicializacaoSalasUCsTurnos() {
        this.horarios.inicializacaoSalasUCsTurnos();
    }
}
