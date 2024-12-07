package projetodss.business;

/**
 * Classe que representa uma sala de aulas.
 */
public class Sala {
    private String cod;          // Código da sala
    private int capacidade;      // Capacidade da sala
    private String localizacao;  // Localização da sala

    /**
     * Construtor para Sala.
     *
     * @param cod         Código da sala
     * @param capacidade  Capacidade da sala
     * @param localizacao Localização da sala
     */
    public Sala(String cod, int capacidade, String localizacao) {
        this.cod = cod;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
    }

    // Getters e Setters
    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getCapacidade() {
        return this.capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getLocalizacao() {
        return this.localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    /**
     * Representação em String da Sala.
     *
     * @return Representação textual da sala
     */
    @Override
    public String toString() {
        return "Sala{" + "cod='" + this.cod + '\'' + ", capacidade=" + this.capacidade +
                ", localizacao='" + this.localizacao + '\'' + '}';
    }

    /**
     * Método para verificar igualdade entre duas salas.
     *
     * @param o Objeto a ser comparado
     * @return True se as salas tiverem os mesmos atributos
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sala)) return false;
        Sala sala = (Sala) o;
        return this.capacidade == sala.capacidade &&
                this.cod.equals(sala.cod) &&
                this.localizacao.equals(sala.localizacao);
    }

    /**
     * Método para obter o hashcode da sala.
     *
     * @return Hashcode baseado no codigo da sala
     */
    @Override
    public int hashCode() {
        return this.cod.hashCode();
    }
}

