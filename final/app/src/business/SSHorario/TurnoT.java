package business.SSHorario;

import java.time.LocalTime;

/**
 * Classe que representa um turno teórico de uma unidade curricular.
 */
public class TurnoT extends Turno{
    /**
     * Construtor para TurnoT.
     *
     * @param cod         Código do turno
     * @param codUC
     * @param diaSemana   Dia da semana em que ocorre o turno
     * @param horaInicial Hora inicial do turno
     * @param horaFinal   Hora final do turno
     * @param lotacao     Lotação máxima do turno
     * @param sala        Sala onde decoree o turno
     */
    public TurnoT(String cod, String codUC, DiaSemana diaSemana, LocalTime horaInicial, LocalTime horaFinal, int lotacao, String sala) {
        super(cod, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
    }

    @Override
    public String getTipoTurno() {
        return "T";
    }

    /**
     * Método para representar o turno teórico como uma String.
     *
     * @return Representação em String do turno
     */
    @Override
    public String toString() {
        return "TurnoT{" + "cod='" + this.getCod() + '\'' + ", diaSemana='" + this.getDiaSemana() + '\'' +
                ", horaInicial=" + this.getHoraInicial() + ", horaFinal=" + this.getHoraFinal() +
                ", lotacao=" + this.getLotacao() + ", sala=" + this.getSala() + '}';
    }
}
