package business.SSHorarios;

import java.util.Map;
import java.util.Set;

public interface ISSHorarios {

    boolean existeAluno(String codAluno);
    boolean existeUC(String codUC);
    boolean alunoInscritoNaUC(String codAluno,String codUC);
    boolean existeTurno(String codTurno, String codUC);
    boolean turnoTemEspaco(String codTurno, String codUC);
    boolean alunoTemConflito(String codAluno, String codTurno, String codUC);

    /**
     * Verifica se um aluno está associado a um turno específico de uma uc.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC   Código da UC.
     * @return True se o aluno está associado ao turno, se não False.
     */
     boolean alunoTemTurno(String codAluno, String codUC, String codTurno);

    /**
     * Adiciona um aluno a um turno específico de uma UC.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC    Código da UC.
     */
    void alocarAlunoAoTurno(String codAluno, String codUC, String codTurno);

    /**
     * Remove um aluno de um turno específico de uma UC.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC     Código da UC.
     */
    void removerAlunoDoTurno(String codAluno, String codUC, String codTurno);

    /**
     * Aloca todos os alunos a turnos a que estão inscritos no semestre dado.
     * 
     * @param semestre  Semestre para o qual fazer a alocação
     * @return Map de codAluno para map de codUC para set do tipo de turno a que não está alocado
     */
    Map<String, Map<String, Set<String>>> gerarHorarios(int semestre);

    /**
     * Importa alunos e suas inscrições a partir de um arquivo CSV combinado.
     *
     * @param ficheiro Caminho para o arquivo CSV combinado contendo alunos e suas inscrições.
     * @return Map onde a chave é o código do aluno e o valor é a senha do aluno.
     */
    Map<String, String> importarAlunos(String ficheiro);


    /**
     * Método que remove todos os alunos.
     */
    void removerAlunos();

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
