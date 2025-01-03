package business.SSHorarios;

/**
 * Classe que representa um aluno com estatuto específico.
 */
public class AlunoEstatutoEspecial extends Aluno {

    public AlunoEstatutoEspecial(String numero, String nome, double media) {
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

    /**
     * Retorna se o aluno tem estatuto.
     *
     * @return Se o aluno tem estatuto (padrão: False)
     */
    @Override
    public boolean temEstatuto() {
        return true;
    }
}

