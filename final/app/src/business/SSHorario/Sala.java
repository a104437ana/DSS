package business.SSHorario;

/**
 * Classe que representa uma sala de aulas.
 */
public class Sala {
    private String localizacao;  // Localização da sala
    private int capacidade;      // Capacidade da sala


    /**
     * Construtor para Sala.
     *
     * @param capacidade  Capacidade da sala
     * @param localizacao Localização da sala
     */
    public Sala(int capacidade, String localizacao) {
        this.capacidade = capacidade;
        this.localizacao = localizacao;
    }

    // Getters e Setters

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
        return "Sala{" + "localizacao='" + this.localizacao + '\'' + ", capacidade=" + this.capacidade +'}';
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
                this.localizacao.equals(sala.localizacao);
    }

    /**
     * Método para obter o hashcode da sala.
     *
     * @return Hashcode baseado no codigo da sala
     */
    @Override
    public int hashCode() {
        return this.localizacao.hashCode();
    }
}

