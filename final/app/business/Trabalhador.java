package projetodss.business;

/**
 * Classe que representa um aluno trabalhador.
 */
public class Trabalhador extends Aluno {

    public Trabalhador(String numero, String nome, String senha) {
        super(numero, nome, senha);
    }

    /**
     * Retorna o estatuto do aluno.
     *
     * @return String representando o estatuto de utilizador.
     */
    @Override
    public String getEstatuto() {
        return "Trabalhador";
    }

    @Override
    public String toString() {
        return super.toString() + ", estatuto=Trabalhador}";
    }
}
