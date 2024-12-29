package business.SSHorarios;

import data.TurnoDAO;

import java.util.*;

/**
 * Classe que representa um aluno no sistema.
 */
public class Aluno {
    private String codAluno;    // Identificador único do aluno
    private String nome;        // Nome do aluno
    private double media;       // Média do aluno
    private TurnoDAO turnos;    // DAO dos turnos

    /**
     * Construtor para criar um aluno.
     *
     * @param codAluno Identificador único do aluno (número)
     * @param nome   Nome do aluno
     * @param media  Média do aluno
     */
    public Aluno(String codAluno, String nome, double media) {
        this.codAluno = codAluno;
        this.nome = nome;
        this.media = media;
        this.turnos = TurnoDAO.getInstance();
    }

    /**
     * Retorna o número do aluno.
     *
     * @return Número do aluno
     */
    public String getCodAluno() {
        return codAluno;
    }

    /**
     * Define um novo número para o aluno.
     *
     * @param codAluno Novo número do aluno
     */
    public void setCodAluno(String codAluno) {
        this.codAluno = codAluno;
    }

    /**
     * Retorna o nome do aluno.
     *
     * @return Nome do aluno
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define um novo nome para o aluno.
     *
     * @param nome Novo nome do aluno
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna a média do aluno.
     *
     * @return Média do aluno
     */
    public double getMedia() {
        return media;
    }

    /**
     * Define uma nova média para o aluno.
     *
     * @param media Nova média do aluno
     */
    public void setMedia(double media) {
        this.media = media;
    }

    /**
     * Retorna o estatuto do aluno.
     *
     * @return Estatuto do aluno (padrão: "Nenhum")
     */
    public String getEstatuto() {
        return "Nenhum";
    }

    /**
     * Retorna os turnos do aluno agrupados por UC.
     *
     * @return Map onde a chave é o código da UC e o valor é uma lista de Turnos associados.
     */
    public Map<String, List<Turno>> getTurnos() {
        return this.turnos.getByAluno(this.codAluno);
    }

    /**
     * Retorna o horário do aluno agrupado por Dia da Semana.
     *
     * @return Map onde a chave é o Dia da Semana e o valor é uma lista de Turnos ordenados pela hora inicial.
     */
    public Map<DiaSemana, List<Turno>> getHorario() {
        Map<String, List<Turno>> turnosPorUC = this.turnos.getByAluno(this.codAluno);
        Map<DiaSemana, List<Turno>> horario = new HashMap<>();

        // Iterar por todos os turnos agrupados por UC
        for (List<Turno> turnos : turnosPorUC.values()) {
            for (Turno turno : turnos) {
                // Adicionar o turno ao Map baseado no DiaSemana
                horario.computeIfAbsent(turno.getDiaSemana(), k -> new ArrayList<>()).add(turno);
            }
        }

        // Ordenar os turnos de cada DiaSemana pela hora de início
        for (List<Turno> turnosDoDia : horario.values()) {
            Collections.sort(turnosDoDia);
        }

        return horario;
    }

    /**
     * Retorna o horário do aluno em formato string.
     *
     * @return String com o horario do aluno.
     */
    public String getStringHorario() {
        StringBuilder res = new StringBuilder();
        res.append("Horário do aluno ").append(this.codAluno).append("\n");

        Map<DiaSemana, List<Turno>> horario = new HashMap<>(this.getHorario());

        List<DiaSemana> dias = new ArrayList<>();
        dias.add(DiaSemana.SEGUNDA);
        dias.add(DiaSemana.TERCA);
        dias.add(DiaSemana.QUARTA);
        dias.add(DiaSemana.QUINTA);
        dias.add(DiaSemana.SEXTA);

        for (DiaSemana dia : dias) {
            res.append(dia).append(":\n");
            List<Turno> turnosDia = horario.get(dia);

            if (turnosDia == null) continue;
            for (Turno t : turnosDia) {
                res.append("    ").append(t.getHoraInicial()).append("-").append(t.getHoraFinal())
                    .append(" -> Turno").append(t.getIdTurno()).append(" da UC ").append(t.getCodUC()).append(";\n");
            }
        }
        return res.toString();
    }

    /**
     * Representação textual do aluno.
     *
     * @return String que representa o aluno.
     */
    @Override
    public String toString() {
        return "Aluno{" +
                "numero='" + codAluno + '\'' +
                ", nome='" + nome + '\'' +
                ", media=" + media +
                ", estatuto=" + getEstatuto() +
                '}';
    }

    /**
     * Verifica igualdade entre dois alunos com base no número.
     *
     * @param o Objeto a ser comparado
     * @return True se os números forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno)) return false;
        Aluno aluno = (Aluno) o;
        return codAluno.equals(aluno.codAluno);
    }

    /**
     * Gera o hashcode com base no número do aluno.
     *
     * @return Código de hash do aluno
     */
    @Override
    public int hashCode() {
        return codAluno.hashCode();
    }
}
