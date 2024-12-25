package business.SSHorario;


import data.AlunoDAO;

/**
 * Classe que representa uma inscrição de um aluno em uma UC.
 */
public class Inscricao {
    private String alunoId;  // Identificador do aluno
    private int nInscricao;  // Número da inscrição
    private AlunoDAO alunos;

    /**
     * Construtor para criar uma inscrição.
     *
     * @param alunoId     Identificador do aluno.
     * @param nInscricao  Número da inscrição.
     */
    public Inscricao(String alunoId, int nInscricao) {
        this.alunoId = alunoId;
        this.nInscricao = nInscricao;
        this.alunos = AlunoDAO.getInstance();
    }

    public String getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(String alunoId) {
        this.alunoId = alunoId;
    }

    public int getNInscricao() {
        return nInscricao;
    }

    public void setNInscricao(int nInscricao) {
        this.nInscricao = nInscricao;
    }

    // Método de DAO
    public Aluno getAluno(){
        return this.alunos.get(this.alunoId);
    }

    @Override
    public String toString() {
        return "Inscricao{" +
                "alunoId='" + alunoId + '\'' +
                ", nInscricao=" + nInscricao +
                '}';
    }
}

