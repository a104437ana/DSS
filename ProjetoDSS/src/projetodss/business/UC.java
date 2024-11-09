package projetodss.business;

import java.util.Collection;
import java.util.HashSet;

/**
 * Classe que representa uma unidade curricular (UC).
 */
public class UC {
    private String cod;                 // Código da unidade curricular
    private String nome;                // Nome da unidade curricular
    private int semestre;               // Semestre em que a unidade curricular é lecionada
    private boolean opcional;           // Indica se a unidade curricular é opcional
    private String preferencia;         // Preferência associada à unidade curricular
    private Collection<String> turnos;  // Coleção com os códigos identificadores dos turnos

    /**
     * Construtor para Unidade Curricular (UC) sem turnos.
     *
     * @param cod         Código da UC
     * @param nome        Nome da UC
     * @param semestre    Semestre da UC
     * @param opcional    Indicador de se a UC é opcional
     * @param preferencia Preferência da UC
     */
    public UC(String cod, String nome, int semestre, boolean opcional, String preferencia) {
        this.cod = cod;
        this.nome = nome;
        this.semestre = semestre;
        this.opcional = opcional;
        this.preferencia = preferencia;
        this.turnos = new HashSet<>();
    }

    /**
     * Construtor para Unidade Curricular (UC) com coleção de turnos.
     *
     * @param cod         Código da UC
     * @param nome        Nome da UC
     * @param semestre    Semestre da UC
     * @param opcional    Indicador de se a UC é opcional
     * @param preferencia Preferência da UC
     * @param turnos      Coleção de códigos dos turnos da UC
     */
    public UC(String cod, String nome, int semestre, boolean opcional, String preferencia, Collection<String> turnos) {
        this.cod = cod;
        this.nome = nome;
        this.semestre = semestre;
        this.opcional = opcional;
        this.preferencia = preferencia;
        this.turnos = new HashSet<>(turnos);
    }

    // Getters e Setters
    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSemestre() {
        return this.semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public boolean isOpcional() {
        return this.opcional;
    }

    public void setOpcional(boolean opcional) {
        this.opcional = opcional;
    }

    public String getPreferencia() {
        return this.preferencia;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public Collection<String> getTurnos() {
        return new HashSet<>(this.turnos);
    }

    public void setTurnos(Collection<String> turnos) {
        this.turnos = new HashSet<>(turnos);
    }

    /**
     * Adiciona um turno à coleção de turnos da UC.
     *
     * @param turnoCod Código do turno a ser adicionado
     */
    public void addTurno(String turnoCod) {
        this.turnos.add(turnoCod);
    }

    /**
     * Remove um turno da coleção de turnos da UC.
     *
     * @param turnoCod Código do turno a ser removido
     */
    public void removeTurno(String turnoCod) {
        this.turnos.remove(turnoCod);
    }

    /**
     * Representação em String da UC.
     *
     * @return Representação textual da UC
     */
    @Override
    public String toString() {
        return "UC{" + "cod='" + this.cod + '\'' + ", nome='" + this.nome + '\'' +
                ", semestre=" + this.semestre + ", opcional=" + this.opcional +
                ", preferencia='" + this.preferencia + '\'' + ", turnos=" + this.turnos + '}';
    }

    /**
     * Método para verificar igualdade entre duas UCs.
     *
     * @param o Objeto a ser comparado
     * @return True se as UCs tiverem os mesmos atributos
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UC)) return false;
        UC uc = (UC) o;
        return this.semestre == uc.semestre &&
                this.opcional == uc.opcional &&
                this.cod.equals(uc.cod) &&
                this.nome.equals(uc.nome) &&
                this.preferencia.equals(uc.preferencia) &&
                this.turnos.equals(uc.turnos);
    }

    /**
     * Método para obter o hashcode da UC.
     *
     * @return Hashcode baseado no código da UC
     */
    @Override
    public int hashCode() {
        return this.cod.hashCode();
    }
}
