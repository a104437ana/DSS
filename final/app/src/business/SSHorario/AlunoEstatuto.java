package business.SSHorario;

/**
 * Classe que representa um aluno com estatuto específico.
 */
public class AlunoEstatuto extends Aluno {

    public AlunoEstatuto(String numero, String nome, double media) {
        super(numero, nome, media);
    }

    /**
     * Retorna o estatuto do aluno.
     *
     * @return String representando o estatuto do aluno.
     */
    @Override
    public String getEstatuto() {
        return "Estatuto";
    }
}

