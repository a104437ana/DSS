package business;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGestHorariosLN {

    boolean existeAluno(String codAluno);
    boolean existeUC(String codUC);
    boolean alunoInscritoNaUC(String codAluno, String codUC);
    boolean existeTurno(String codTurno, String codUC);
    boolean turnoTemEspaco(String codTurno, String codUC);
    boolean alunoTemConflito(String codAluno, String codTurno, String codUC);

    /**
     * Verifica se um aluno está associado a um turno específico de uma uc.
     *
     * @param codAluno  Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC     Código da UC.
     * @return True se o aluno está associado ao turno, se não False.
     */
    boolean alunoTemTurno(String codAluno, String codUC, String codTurno);

    /**
     * Adiciona um aluno a um turno específico de uma UC.
     *
     * @param codAluno  Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC     Código da UC.
     */
    void alocarAlunoAoTurno(String codAluno, String codUC, String codTurno);

    /**
     * Remove um aluno de um turno específico de uma UC.
     *
     * @param codAluno  Código do aluno.
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
     * Importa utilizadores, alunos e suas inscrições de dois arquivos CSV.
     *
     * @param ficheiroAlunos Caminho para o arquivo CSV contendo os dados dos alunos.
     */
    void importarAlunos(String ficheiroAlunos);

    /**
     * Consulta horário de um determinado aluno.
     *
     * @param codAluno Código de aluno cujo horário se quer consultar.
     * @return String Horário do aluno.
     */
    Map<String, List<String>> consultarHorario(String codAluno);

    /**
     * Método de ínicio de sessão.
     *
     * @param id Id de utilizador a procurar
     * @param senha Senha do utilizador a verificar
     *
     * @return String com id do utilizador autenticado ou null se credenciais estão inválidas
     */
    String iniciarSessao(String id, String senha);

    /**
     * Método que preenche as tabelas das salas, ucs e turnos com dados pré-definidos.
     */
    void inicializacaoSalasUCsTurnos();
}
