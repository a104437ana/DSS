package business.SSHorario;

import data.InscritoDAO;
import data.TurnoDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma unidade curricular (UC).
 */
public class UC {
    private String cod;                 // Código da unidade curricular
    private String nome;                // Nome da unidade curricular
    private int semestre;               // Semestre em que a unidade curricular é lecionada
    private boolean opcional;           // Indica se a unidade curricular é opcional
    private Pref preferencia;           // Preferência associada à unidade curricular
    private TurnoDAO turnoDAO;          // DAO dos turnos
    private InscritoDAO inscritoDAO;    // DAO dos inscritos


    /**
     * Construtor para Unidade Curricular (UC) sem turnos.
     *
     * @param cod         Código da UC
     * @param nome        Nome da UC
     * @param semestre    Semestre da UC
     * @param opcional    Indicador de se a UC é opcional
     * @param preferencia Preferência da UC
     */
    public UC(String cod, String nome, int semestre, boolean opcional, Pref preferencia) {
        this.cod = cod;
        this.nome = nome;
        this.semestre = semestre;
        this.opcional = opcional;
        this.preferencia = preferencia;
        this.turnoDAO = TurnoDAO.getInstance();
        this.inscritoDAO = InscritoDAO.getInstance();
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

    public Pref getPreferencia() {
        return this.preferencia;
    }

    public void setPreferencia(Pref preferencia) {
        this.preferencia = preferencia;
    }

    // Métodos dos DAOs Associados
    /**
     * Método que devolve os turnos da UC.
     *
     * @return Lista de turnos da UC.
     */
    public List<Turno> getTurnos() {
        return new ArrayList<>(this.turnoDAO.getByUC(this.cod));
    }

    /**
     * Método que adiciona uma única inscrição à UC.
     *
     * @param inscricao Inscrição a adicionar.
     */
    public void adicionarInscricao(Inscricao inscricao) {
        this.inscritoDAO.put(this.cod, inscricao);
    }

    /**
     * Método que adiciona várias inscrições à UC.
     *
     * @param inscricoes Lista de inscrições a adicionar.
     */
    public void adicionarInscricoes(List<Inscricao> inscricoes) {
        this.inscritoDAO.putAll(this.cod, inscricoes);
    }

    /**
     * Método que devolve as inscrições associadas à UC.
     *
     * @return Lista de inscrições da UC.
     */
    public List<Inscricao> getInscricoes() {
        return this.inscritoDAO.getByUC(this.cod);
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
                ", preferencia='" + this.preferencia + '\'' + '}';
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
        return this.cod.equals(uc.cod);

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
