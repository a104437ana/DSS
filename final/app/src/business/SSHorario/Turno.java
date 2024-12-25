package business.SSHorario;

import data.AlunoDAO;
import data.SalaDAO;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um turno de uma unidade curricular.
 */
public abstract class Turno implements Comparable<Turno> {
    private String cod;                 // Código do turno
    private String codUC;               // Código da UC do turno
    private DiaSemana diaSemana;        // Dia da semana em que ocorre o turno
    private LocalTime horaInicial;      // Hora inicial do turno
    private LocalTime horaFinal;        // Hora final do turno
    private int lotacao;                // Lotação máxima do turno
    private String sala;                // Código da sala onde se localiza o turno
    private SalaDAO salasDAO;           // DAO das Salas
    private AlunoDAO alunoDAO;          // DAO dos Alunos

    /**
     * Construtor para Turno.
     *
     * @param cod         Código do turno
     * @param diaSemana   Dia da semana em que ocorre o turno
     * @param horaInicial Hora inicial do turno
     * @param horaFinal   Hora final do turno
     * @param lotacao     Lotação máxima do turno
     * @param sala        Sala onde decoree o turno
     */
    public Turno(String cod, String codUC, DiaSemana diaSemana, LocalTime horaInicial, LocalTime horaFinal, int lotacao, String sala) {
        this.cod = cod;
        this.codUC = codUC;
        this.diaSemana = diaSemana;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.lotacao = lotacao;
        this.sala = sala;
        this.salasDAO = SalaDAO.getInstance();
        this.alunoDAO = AlunoDAO.getInstance();
    }

    // Getters e Setters
    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCodUC() {
        return this.codUC;
    }

    public void setCodUC(String codUC) {
        this.codUC = codUC;
    }

    public DiaSemana getDiaSemana() {
        return this.diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicial() {
        return this.horaInicial;
    }

    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    public LocalTime getHoraFinal() {
        return this.horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public int getLotacao() {
        return this.lotacao;
    }

    public void setLotacao(int lotacao) {
        this.lotacao = lotacao;
    }

    public String getSala() {
        return this.sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    // Metodo dos DAOs
    public List<Aluno> getAlunosTurno() {
        return new ArrayList<>(this.alunoDAO.getByTurno(this.cod));
    }

    public int qtdAlunos() {
        return this.alunoDAO.sizeByTurno(this.cod);
    }

    /**
     * Método abstrato para obter o tipo de turno (Teórico, Teórico-Prático, Prático-Laboratorial).
     *
     * @return Tipo do turno como String.
     */
    public abstract String getTipoTurno();

    /**
     * Método para representar o turno como uma String.
     *
     * @return Representação em String do turno
     */
    @Override
    public abstract String toString();


    /**
     * Método para comparar dois turnos pela hora inicial.
     *
     * @param outroTurno Objeto a ser comparado
     * @return 0 se os turnos tiverem a mesma hora inicial;
     *         >0 se a hora inicial deste turno for superior ao turno comparado
     *         <0 se a hora inicial deste turno for inferior ao turno comparado
     */
    @Override
    public int compareTo(Turno outroTurno) {
        return this.horaInicial.compareTo(outroTurno.horaInicial);
    }

    /**
     * Método para verificar igualdade entre dois turnos.
     *
     * @param o Objeto a ser comparado
     * @return True se os turnos tiverem os mesmos atributos
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno)) return false;
        Turno turno = (Turno) o;
        return this.cod.equals(turno.cod) &&
                this.codUC.equals(turno.codUC);
    }

    /**
     * Método que retorna o hashcode para o turno.
     *
     * @return Hashcode baseado no codigo do turno
     */
    @Override
    public int hashCode() {
        return this.cod.hashCode();
    }
}

