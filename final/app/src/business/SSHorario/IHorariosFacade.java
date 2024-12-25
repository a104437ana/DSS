package business.SSHorario;

import business.SSUtilizadores.Utilizador;

import java.util.Collection;

public interface IHorariosFacade {


    /**
     * Importa alunos a partir de um arquivo CSV.
     *
     * @param ficheiroAlunos Caminho para o arquivo CSV contendo os alunos.
     */
    void importarAlunos(String ficheiroAlunos);

    /**
     * Importa inscrições de alunos em UCs a partir de um arquivo CSV.
     *
     * @param ficheiroUCs Caminho para o arquivo CSV contendo as inscrições.
     */
    void importarInscricoesAlunos(String ficheiroUCs);

    /**
     * Método que devolve o horário de um Aluno.
     *
     * @param codAluno Código do aluno.
     * @return String string com horário do aluno dado.
     */
    String getHorarioAluno(String codAluno);

    /**
     * Método que preenche as tabelas das salas, ucs e turnos com dados pré-definidos.
     */
    void inicializacaoSalasUCsTurnos();

}
