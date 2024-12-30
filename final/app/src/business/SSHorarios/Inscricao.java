package business.SSHorarios;


import data.AlunoDAO;

/**
 * Classe que representa uma inscrição de um aluno em uma UC.
 */
public class Inscricao {
    private String codAluno;  // Identificador do aluno
    private int nInscricao;  // Número da inscrição
    private AlunoDAO aluno;

    /**
     * Construtor para criar uma inscrição.
     *
     * @param codAluno     Identificador do aluno.
     * @param nInscricao  Número da inscrição.
     */
    public Inscricao(String codAluno, int nInscricao) {
        this.codAluno = codAluno;
        this.nInscricao = nInscricao;
        this.aluno = AlunoDAO.getInstance();
    }

    public String getCodAluno() {
        return codAluno;
    }

    public void setCodAluno(String codAluno) {
        this.codAluno = codAluno;
    }

    public int getNInscricao() {
        return nInscricao;
    }

    public void setNInscricao(int nInscricao) {
        this.nInscricao = nInscricao;
    }

    // Método de DAO
    public Aluno getAluno(){
        return this.aluno.get(this.codAluno);
    }

    @Override
    public String toString() {
        return "Inscricao{" +
                "Código de aluno ='" + codAluno + '\'' +
                ", Número da inscrição =" + nInscricao +
                '}';
    }
}

