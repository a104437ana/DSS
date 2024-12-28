package business.SSHorarios;

import java.time.LocalTime;

/**
 * Classe que representa um turno prático-laboratorial de uma unidade curricular.
 */
public class TurnoPL extends Turno{
    /**
     * Construtor para TurnoPL.
     *
     * @param idTurno         Código do turno
     * @param codUC
     * @param diaSemana   Dia da semana em que ocorre o turno
     * @param horaInicial Hora inicial do turno
     * @param horaFinal   Hora final do turno
     * @param lotacao     Lotação máxima do turno
     * @param sala        Sala onde decoree o turno
     */

    public TurnoPL(String idTurno, String codUC, DiaSemana diaSemana, LocalTime horaInicial, LocalTime horaFinal, int lotacao, String sala) {
        super(idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
    }

    @Override
    public String getTipoTurno() {
        return "PL";
    }

    /**
     * Método para representar o turno prático-laboratorial como uma String.
     *
     * @return Representação em String do turno
     */
    @Override
    public String toString() {
        return "TurnoPL{" + "idTurno='" + this.getIdTurno() + '\'' + ", diaSemana='" + this.getDiaSemana().toString() + '\'' +
                ", horaInicial=" + this.getHoraInicial() + ", horaFinal=" + this.getHoraFinal() +
                ", lotacao=" + this.getLotacao() + ", sala=" + this.getSala() + '}';
    }
}
