package projetodss.business;

/**
 * Classe que representa um aluno trabalhador.
 */
public class Atleta extends Aluno {

    public Atleta(String numero, String nome, String senha) {
        super(numero, nome, senha);
    }

    /**
     * Retorna o estatuto do aluno.
     *
     * @return String representando o estatuto de utilizador.
     */
    @Override
    public String getEstatuto() {
        return "Atleta";
    }

    @Override
    public String toString() {
        return super.toString() + ", estatuto=Atleta}";
    }
}
