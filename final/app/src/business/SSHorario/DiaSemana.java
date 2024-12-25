package business.SSHorario;

public enum DiaSemana {
    SEGUNDA("Segunda-Feira"),
    TERCA("Terça-Feira"),
    QUARTA("Quarta-Feira"),
    QUINTA("Quinta-Feira"),
    SEXTA("Sexta-Feira");

    private final String descricao;

    DiaSemana(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}

