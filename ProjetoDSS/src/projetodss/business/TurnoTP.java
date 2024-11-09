package projetodss.business;

import java.time.LocalTime;

/**
 * Classe que representa um turno teórico-prático de uma unidade curricular.
 */
public class TurnoTP extends Turno{
    /**
     * Construtor para TurnoTP.
     *
     * @param cod         Código do turno
     * @param diaSemana   Dia da semana em que ocorre o turno
     * @param horaInicial Hora inicial do turno
     * @param horaFinal   Hora final do turno
     * @param lotacao     Lotação máxima do turno
     * @param sala        Sala onde decoree o turno
     */
    public TurnoTP(String cod, String diaSemana, LocalTime horaInicial, LocalTime horaFinal, int lotacao, String sala) {
        super(cod, diaSemana, horaInicial, horaFinal, lotacao, sala);
    }

    @Override
    public String getTipoTurno() {
        return "TP";
    }

    /**
     * Método para representar o turno teórico-prático como uma String.
     *
     * @return Representação em String do turno
     */
    @Override
    public String toString() {
        return "TurnoTP{" + "cod='" + this.getCod() + '\'' + ", diaSemana='" + this.getDiaSemana() + '\'' +
                ", horaInicial=" + this.getHoraInicial() + ", horaFinal=" + this.getHoraFinal() +
                ", lotacao=" + this.getLotacao() + ", sala=" + this.getSala() + '}';
    }
}
