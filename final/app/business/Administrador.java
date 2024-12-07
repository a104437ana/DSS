package projetodss.business;

/**
 * Classe que representa um administrador, herdando atributos e métodos de Utilizador.
 */
public class Administrador extends Utilizador {

    /**
     * Construtor para criar um administrador.
     *
     * @param id    Identificador único do administrador
     * @param senha Senha do administrador
     */
    public Administrador(String id, String senha) {
        super(id, "Administrador", senha);
    }

    /**
     * Retorna o tipo de utilizador como "Administrador".
     *
     * @return String representando o tipo de utilizador.
     */
    @Override
    public String getTipoUtilizador() {
        return "Administrador";
    }

    /**
     * @return Representação textual do administrador.
     */
    @Override
    public String toString() {
        return "Administrador{id='" + this.getId() + "', nome='" + this.getNome() + "'}";
    }


}
