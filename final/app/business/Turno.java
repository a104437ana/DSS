package projetodss.business;

import java.time.LocalTime;
import java.util.Collection;


/**
 * Classe que representa um turno de uma unidade curricular.
 */
public abstract class Turno {
    private String cod;                 // Código do turno
    private String diaSemana;           // Dia da semana em que ocorre o turno
    private LocalTime horaInicial;      // Hora inicial do turno
    private LocalTime horaFinal;        // Hora final do turno
    private int lotacao;                // Lotação máxima do turno
    private String sala;                // Código da sala onde se localiza o turno


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
    public Turno(String cod, String diaSemana, LocalTime horaInicial, LocalTime horaFinal, int lotacao, String sala) {
        this.cod = cod;
        this.diaSemana = diaSemana;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.lotacao = lotacao;
        this.sala = sala;
    }

    // Getters e Setters
    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDiaSemana() {
        return this.diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
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
        return this.lotacao == turno.lotacao &&
                this.cod.equals(turno.cod) &&
                this.diaSemana.equals(turno.diaSemana) &&
                this.horaInicial.equals(turno.horaInicial) &&
                this.horaFinal.equals(turno.horaFinal) &&
                this.sala.equals(turno.getSala()) &&
                this.getTipoTurno().equals(turno.getTipoTurno());
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

