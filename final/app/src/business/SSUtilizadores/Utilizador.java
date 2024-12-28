package business.SSUtilizadores;

/**
 * Classe que representa um utilizador genérico no sistema.
 */
public class Utilizador implements Comparable<Utilizador> {
    private final String codUtilizador; // Identificador único
    private String senha;    // Senha do utilizador

    /**
     * Construtor para Utilizador.
     *
     * @param codUtilizador    Identificador único do utilizador
     * @param senha Senha do utilizador
     */
    public Utilizador(String codUtilizador, String senha) {
        this.codUtilizador = codUtilizador;
        this.senha = senha;
    }

    /**
     * @return O identificador único do utilizador.
     */
    public String getCodUtilizador() {
        return codUtilizador;
    }

    /**
     * @return A senha do utilizador.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define uma nova senha para o utilizador.
     *
     * @param senha A nova senha do utilizador.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Compara este utilizador com outro utilizador baseado no id.
     *
     * @param o Utilizador a ser comparado.
     * @return Valor positivo se este utilizador tiver id maior, negativo se menor, zero se igual.
     */
    @Override
    public int compareTo(Utilizador o) {
        return this.codUtilizador.compareTo(o.getCodUtilizador());
    }

    /**
     * Verifica a igualdade entre este utilizador e outro objeto.
     *
     * @param o Objeto a ser comparado.
     * @return true se os atributos 'id' forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Verifica se é o mesmo objeto
        if (o == null || this.getClass() != o.getClass()) return false; // Verifica se é nulo ou de outra classe
        Utilizador utilizador = (Utilizador) o; // Casting para Utilizador
        return this.codUtilizador.equals(utilizador.codUtilizador);
    }

    /**
     * Calcula o código de hash baseado no id do utilizador.
     *
     * @return Código de hash do utilizador.
     */
    @Override
    public int hashCode() {
        return this.codUtilizador.hashCode();
    }
}
