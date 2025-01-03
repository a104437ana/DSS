package business.SSHorarios;

import data.TurnoDAO;

import java.util.*;
import java.util.stream.Collectors;

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
     * Retorna se o aluno tem estatuto.
     *
     * @return Se o aluno tem estatuto (padrão: False)
     */
    public boolean temEstatuto() {
        return false;
    }

    public static Comparator<Aluno> compararPorEstatuto() {
        return new Comparator<Aluno>() {
            @Override
            public int compare(Aluno a1, Aluno a2) {
                int comparaEstatuto = Boolean.compare(a1.temEstatuto(), a2.temEstatuto());
                if (comparaEstatuto != 0) return comparaEstatuto;
                return a1.getCodAluno().compareTo(a2.getCodAluno());
            }
        };
    }

    public static Comparator<Aluno> compararPorMedia() {
        return new Comparator<Aluno>() {
            @Override
            public int compare(Aluno a1, Aluno a2) {
                int comparaMedia = Double.compare(a1.getMedia(), a2.getMedia());
                if (comparaMedia != 0) return comparaMedia;
                return a1.getCodAluno().compareTo(a2.getCodAluno());
            }
        };
    }

    public static Comparator<Aluno> compararPorCodAluno() {
        return new Comparator<Aluno>() {
            @Override
            public int compare(Aluno a1, Aluno a2) {
                return a1.getCodAluno().compareTo(a2.getCodAluno());
            }
        };
    }

    /*-------------- OUTROS MÉTODOS DAOS ------------*/

    /**
     * Adiciona um aluno a um turno específico de uma UC.
     *
     * @param codTurno  Código do turno.
     * @param codUC     Código da UC.
     */
    public void putTurno(String codUC, String codTurno) {
        this.turnos.putAlunoTurno(this.codAluno,codUC,codTurno);
    }

    /**
     * Remove um aluno de um turno específico de uma UC.
     *
     * @param codTurno  Código do turno.
     * @param codUC     Código da UC.
     */
    public void removeTurno(String codUC, String codTurno) {
        this.turnos.removeAlunoTurno(this.codAluno, codUC, codTurno);
    }

    /**
     * Verifica se um aluno está associado a um turno específico de uma uc.
     *
     * @param codTurno  Código do turno.
     * @param codUC     Código da UC.
     * @return True se o aluno está associado ao turno, se não False.
     */
    public boolean existeTurno(String codUC, String codTurno) {
        return this.turnos.existeAlunoTurno(this.codAluno, codUC, codTurno);
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
    public Map<String, List<String>> getHorario() {
        Map<String, List<Turno>> turnosPorUC = this.turnos.getByAluno(this.codAluno);
        Map<String, List<Turno>> horario = new HashMap<>();

        // Iterar por todos os turnos agrupados por UC
        for (List<Turno> turnos : turnosPorUC.values()) {
            for (Turno turno : turnos) {
                // Adicionar o turno ao Map baseado no DiaSemana
                horario.computeIfAbsent(turno.getDiaSemana().toString(), k -> new ArrayList<>()).add(turno);
            }
        }

        // Ordenar os turnos de cada DiaSemana pela hora de início
        for (List<Turno> turnosDoDia : horario.values()) {
            Collections.sort(turnosDoDia);
        }

        Map<String,List<String>> h = new HashMap<>();

        for (Map.Entry<String, List<Turno>> entry : horario.entrySet()) {
            String key = entry.getKey();
            List<Turno> turnos = entry.getValue();

            // Converter a lista de Turno em uma lista de String
            List<String> stringList = new ArrayList<>();
            for (Turno turno : turnos) {
                stringList.add(turno.toString());
            }

            // Colocar no novo mapa
            h.put(key, stringList);
        }


        return h;
    }

    /**
     * Retorna o horário do aluno em formato string.
     *
     * @return String com o horario do aluno.
     */
    public String getStringHorario( Map<DiaSemana, List<Turno>> horario) {
        StringBuilder res = new StringBuilder();
        if (horario.isEmpty()) return null; // verifica se é vazio

        res.append("Horário do aluno ").append(this.codAluno).append("\n");


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
                    .append(" -> Turno ").append(t.getIdTurno()).append(" da UC '").append(t.getCodUC()).append("';\n");
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
