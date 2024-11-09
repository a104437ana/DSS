package projetodss.business;

/**
 * Classe abstrata que representa um utilizador genérico no sistema.
 * Pode ser um aluno ou um administrador (diretor de curso).
 */
public abstract class Utilizador implements Comparable<Utilizador> {
    private final String id;
    private String nome;
    private String senha;

    /**
     * Construtor para Utilizador.
     *
     * @param id    Identificador único do utilizador
     * @param nome  Nome do utilizador
     * @param senha Senha do utilizador
     */
    public Utilizador(String id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    /**
     * @return O identificador único do utilizador.
     */
    public String getId() {
        return id;
    }

    /**
     * @return O nome do utilizador.
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return A senha do utilizador.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define um novo nome para o utilizador.
     *
     * @param nome O novo nome do utilizador.
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * Método abstrato para obter o tipo de utilizador (Aluno ou Administrador).
     *
     * @return Tipo do utilizador como String.
     */
    public abstract String getTipoUtilizador();

    /**
     * Representação textual do utilizador, a ser implementada pelas subclasses.
     *
     * @return String que representa o utilizador.
     */
    @Override
    public abstract String toString();

    /**
     * Compara este utilizador com outro utilizador baseado no id.
     *
     * @param o Utilizador a ser comparado.
     * @return Valor positivo se este utilizador tiver id maior, negativo se menor, zero se igual.
     */
    @Override
    public int compareTo(Utilizador o) {
        return this.id.compareTo(o.getId());
    }

    /**
     * Verifica a igualdade entre este utilizador e outro objeto.
     *
     * @param o Objeto a ser comparado.
     * @return true se os atributos 'id' e 'nome' forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Verifica se é o mesmo objeto
        if (o == null || this.getClass() != o.getClass()) return false;  // Verifica se o objeto é nulo ou de outra classe
        Utilizador utilizador = (Utilizador) o;  // Faz o casting para Utilizador
        return this.id.equals(utilizador.id) &&
                this.nome.equals(utilizador.nome);
    }

    /**
     * Calcula o código de hash baseado no id do utilizador.
     *
     * @return Código de hash do utilizador.
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}