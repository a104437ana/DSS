package business;


import business.SSHorarios.SSHorarios;
import business.SSHorarios.ISSHorarios;
import business.SSUtilizadores.ISSUtilizadores;
import business.SSUtilizadores.SSUtilizadores;

import java.util.HashMap;
import java.util.Map;

public class GestHorariosFacade implements IGestHorariosLN{
    private ISSUtilizadores utilizadores;
    private ISSHorarios horarios;

    public GestHorariosFacade() {
        this.utilizadores = new SSUtilizadores();
        this.horarios = new SSHorarios();
    }

    public boolean existeAluno(String codAluno) {
        return this.horarios.existeAluno(codAluno);
    }

    public boolean existeUC(String codUC) {
        return this.horarios.existeUC(codUC);
    }

    public void alocarAlunoAoTurno(String codAluno, String codUC, String codTurno) {
        this.horarios.alocarAlunoAoTurno(codAluno,codUC,codTurno);
    }

    public void removerAlunoDoTurno(String codAluno, String codUC, String codTurno) {
        this.horarios.removerAlunoDoTurno(codAluno,codUC,codTurno);
    }
    /**
     * Importa utilizadores, alunos e suas inscrições de dois arquivos CSV.
     *
     * @param ficheiro Caminho para o arquivo CSV contendo os dados dos alunos.
     */
    public void importarAlunos(String ficheiro) {
        try {

            // Remover inscrições e alunos antigos
            this.horarios.removerTodasInscricoes();
            this.horarios.removerAlunos();

            // Importar alunos
            System.out.println("Importando alunos e inscrições...");
            Map<String, String> utilizadoresParaImportar = new HashMap<>(this.horarios.importarAlunos(ficheiro));
            System.out.println("Alunos e inscrições importadas com sucesso.");

            // Importar utilizadores
            System.out.println("Importando utilizadores...");
            this.utilizadores.atualizarUtilizadores(utilizadoresParaImportar);
            System.out.println("Utilizadores importados com sucesso.");


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
    public String iniciarSessao(String id, String senha) {
        return this.utilizadores.iniciarSessao(id, senha);
    }

    /**
     * Método que preenche as tabelas das salas, ucs e turnos com dados pré-definidos.
     */
    public void inicializacaoSalasUCsTurnos() {
        this.horarios.inicializacaoSalasUCsTurnos();
    }
}
