package business.SSHorarios;

import java.time.LocalTime;

/**
 * Classe que representa um turno teórico de uma unidade curricular.
 */
public class TurnoT extends Turno{
    /**
     * Construtor para TurnoT.
     *
     * @param idTurno         Código do turno
     * @param codUC
     * @param diaSemana   Dia da semana em que ocorre o turno
     * @param horaInicial Hora inicial do turno
     * @param horaFinal   Hora final do turno
     * @param lotacao     Lotação máxima do turno
     * @param sala        Sala onde decoree o turno
     */
    public TurnoT(String idTurno, String codUC, DiaSemana diaSemana, LocalTime horaInicial, LocalTime horaFinal, int lotacao, String sala) {
        super(idTurno, codUC, diaSemana, horaInicial, horaFinal, lotacao, sala);
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
        StringBuilder res = new StringBuilder();
        res.append("    ").append(this.getHoraInicial()).append("-").append(this.getHoraFinal())
                .append(" -> Turno ").append(this.getIdTurno()).append(" da UC '").append(this.getCodUC()).append("';");
        return res.toString();
    }
}
