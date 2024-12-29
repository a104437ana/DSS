package business.SSHorarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ISSHorarios {

    boolean existeAluno(String codAluno);
    boolean existeUC(String codUC);
    void alocarAlunoAoTurno(String codAluno, String codUC, String codTurno);
    void removerAlunoDoTurno(String codAluno, String codUC, String codTurno);
    void gerarHorarios(int semestre);

    /**
     * Importa alunos e suas inscrições a partir de um arquivo CSV combinado.
     *
     * @param ficheiro Caminho para o arquivo CSV combinado contendo alunos e suas inscrições.
     * @return Map onde a chave é o código do aluno e o valor é a senha do aluno.
     */
    Map<String, String> importarAlunos(String ficheiro);

    /**
     * Método que remove inscrições da UC.
     */
    //void removerTodasInscricoes();

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
