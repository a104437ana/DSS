package business;


import business.SSHorarios.SSHorarios;
import business.SSHorarios.ISSHorarios;
import business.SSUtilizadores.ISSUtilizadores;
import business.SSUtilizadores.SSUtilizadores;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;

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

    public boolean alunoInscritoNaUC(String codAluno, String codUC) {
        return this.horarios.alunoInscritoNaUC(codAluno,codUC);
    }

    public boolean existeTurno(String codTurno, String codUC) {
        return this.horarios.existeTurno(codTurno,codUC);
    }

    public boolean turnoTemEspaco(String codTurno, String codUC) {
        return this.horarios.turnoTemEspaco(codTurno,codUC);
    }

    public boolean alunoTemConflito(String codAluno, String codTurno, String codUC) {
        return this.horarios.alunoTemConflito(codAluno,codTurno,codUC);
    }

    /**
     * Verifica se um aluno está associado a um turno específico de uma uc.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC   Código da UC.
     * @return True se o aluno está associado ao turno, se não False.
     */
    public boolean alunoTemTurno(String codAluno, String codUC, String codTurno) {
        return this.horarios.alunoTemTurno(codAluno,codUC,codTurno);
    }

    /**
     * Adiciona um aluno a um turno específico de uma UC.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC    Código da UC.
     */
    public void alocarAlunoAoTurno(String codAluno, String codUC, String codTurno) {
        this.horarios.alocarAlunoAoTurno(codAluno,codUC,codTurno);
        System.out.println("Aluno '" + codAluno + "' alocado com sucesso no turno '" + codTurno + "' da UC '" + codUC + "'.");
    }

    /**
     * Remove um aluno de um turno específico de uma UC.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC     Código da UC.
     */
    public void removerAlunoDoTurno(String codAluno, String codUC, String codTurno) {
        this.horarios.removerAlunoDoTurno(codAluno,codUC,codTurno);
    }

    public void gerarHorarios(int semestre) {
        this.horarios.gerarHorarios(semestre);
    }

    /**
     * Importa utilizadores, alunos e suas inscrições de dois arquivos CSV.
     *
     * @param ficheiro Caminho para o arquivo CSV contendo os dados dos alunos.
     */
    public void importarAlunos(String ficheiro) {
        try {

            File arquivo = new File(ficheiro);
            // Verifica se o arquivo existe e pode ser lido
            if (!arquivo.exists()) {
                throw new IOException("Ficheiro não existe.");
            }
            if (!arquivo.canRead()) {
                throw new IOException("Erro ao ler o ficheiro.");
            }

            // Remover inscrições e alunos antigos
            this.horarios.removerAlunos();

            // Importar alunos
            System.out.println("Importando alunos e inscrições...");
            Map<String, String> utilizadoresParaImportar = new HashMap<>(this.horarios.importarAlunos(ficheiro));
            System.out.println("Alunos e inscrições importadas com sucesso.");

            // Importar utilizadores
            System.out.println("Importando utilizadores...");
            this.utilizadores.atualizarUtilizadores(utilizadoresParaImportar);
            System.out.println("Utilizadores importados com sucesso.");


        } catch (Exception e) {
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
