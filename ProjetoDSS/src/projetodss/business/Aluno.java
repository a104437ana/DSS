package projetodss.business;

/**
 * Classe que representa um aluno, herdando atributos e métodos de Utilizador.
 */
public class Aluno extends Utilizador {

    /**
     * Construtor para criar um aluno.
     *
     * @param numero Identificador único do aluno (número)
     * @param nome   Nome do aluno
     * @param senha  Senha do aluno
     */
    public Aluno(String numero, String nome, String senha) {
        super(numero, nome, senha);
    }

    /**
     * Retorna o tipo de utilizador como "Aluno".
     *
     * @return String representando o tipo de utilizador.
     */
    @Override
    public String getTipoUtilizador() {
        return "Aluno";
    }

    /**
     * Retorna o estatuto do aluno.
     *
     * @return String representando o estatuto de utilizador.
     */
    public String getEstatuto() {
        return "Nenhum";
    }

    /**
     * @return Representação textual do aluno.
     */
    @Override
    public String toString() {
        return "Aluno{id='" + this.getId() + "', nome='" + this.getNome() + "'}";
    }
}
