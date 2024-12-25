package data;

import business.SSHorario.Aluno;
import business.SSHorario.AlunoEstatuto;

import java.security.KeyPair;
import java.sql.*;
import java.util.*;


/**
 * Classe DAO para gerenciar operações no banco de dados relacionadas à entidade Aluno.
 */
public class AlunoDAO {
    // Singleton
    private static AlunoDAO singleton = null;

    // Construtor privado
    private AlunoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Criar a tabela 'alunos' se não existir
            String sql = "CREATE TABLE IF NOT EXISTS alunos (" +
                    "num VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "media DOUBLE NOT NULL, " +
                    "estatuto VARCHAR(20))";
            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS turnosDoAluno (" +
                    "aluno_num VARCHAR(10) NOT NULL, " +
                    "turno_cod VARCHAR(10) NOT NULL, " +
                    "PRIMARY KEY (aluno_num, turno_cod), " +
                    "FOREIGN KEY (aluno_num) REFERENCES alunos(num), " +
                    "FOREIGN KEY (turno_cod) REFERENCES turnos(cod))";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Implementação do padrão Singleton
     *
     * @return Instância única de AlunoDAO
     */
    public static AlunoDAO getInstance() {
        if (singleton == null) {
            singleton = new AlunoDAO();
        }
        return singleton;
    }

    /**
     * Método que cerifica se um código de aluno existe na base de dados
     *
     * @param key id de um aluno
     * @return true se o aluno existe
     * @throws NullPointerException Em caso de erro - deveriam ser criadas exepções do projecto
     */

    public boolean existeAluno(Object key) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT cod FROM alunos WHERE num=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                r = rs.next();  // A chave existe na tabela
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar código do aluno: " + e.getMessage());
        }
        return r;
    }

    /**
     * Método que verifica se um objeto Aluno existe na base de dados.
     *
     * @param value Objeto Aluno a ser verificado.
     * @return True se o objeto existir na base de dados e seus valores coincidirem.
     * @throws IllegalArgumentException Se o objeto não for uma instância de Aluno.
     */
    public boolean existeValorAluno(Object value) {
        if (!(value instanceof Aluno)) {
            throw new IllegalArgumentException("O objeto fornecido não é uma instância de Aluno");
        }

        Aluno a = (Aluno) value;

        // Verifica se a chave (código) existe e compara o aluno da base de dados com o aluno fornecido
        return this.existeAluno(a.getNumero()) && a.equals(this.get(a.getNumero()));
    }


    /**
     * Método para remover todos os registros da tabela 'alunos'.
     */
    public void deleteAllAlunos() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Executa comando para remover todos os alunos
            stm.executeUpdate("DELETE FROM alunos");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar todos os alunos: " + e.getMessage());
        }
    }

    /**
     * Adiciona ou atualiza um aluno na tabela.
     *
     * @param aluno Objeto Aluno a ser adicionado ou atualizado.
     */
    public void put(Aluno aluno) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "INSERT INTO alunos (num, nome, media, estatuto) VALUES (?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE nome = VALUES(nome), media = VALUES(media), estatuto = VALUES(estatuto)")
        ) {
            pstm.setString(1, aluno.getNumero());
            pstm.setString(2, aluno.getNome());
            pstm.setDouble(3, aluno.getMedia());
            pstm.setString(4, aluno.getEstatuto());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir ou atualizar aluno: " + e.getMessage());
        }
    }

    /**
     * Adiciona ou atualiza uma coleção de alunos na tabela.
     *
     * @param alunos Coleção de objetos Aluno a serem adicionados ou atualizados.
     */
    public void putAll(Collection<Aluno> alunos) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            conn.setAutoCommit(false); // Iniciar transação

            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO alunos (num, nome, media, estatuto) VALUES (?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE nome = VALUES(nome), media = VALUES(media), estatuto = VALUES(estatuto)")
            ) {
                for (Aluno aluno : alunos) {
                    pstm.setString(1, aluno.getNumero());
                    pstm.setString(2, aluno.getNome());
                    pstm.setDouble(3, aluno.getMedia());
                    pstm.setString(4, aluno.getEstatuto());
                    pstm.addBatch();
                }
                pstm.executeBatch();
            }

            conn.commit(); // Confirmar transação
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Reverter em caso de erro
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao inserir ou atualizar coleção de alunos: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Obtém todos os alunos inscritos em um turno específico.
     *
     * @param turnoCod Código do turno.
     * @return Coleção de alunos inscritos no turno.
     */
    public Collection<Aluno> getByTurno(String turnoCod) {
        Collection<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT a.num, a.nome, a.media, a.estatuto " +
                "FROM alunos a " +
                "JOIN turnosDoAluno ta ON a.num = ta.aluno_num " +
                "WHERE ta.turno_cod = ?";

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, turnoCod);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    String numero = rs.getString("num");
                    String nome = rs.getString("nome");
                    double media = rs.getDouble("media");
                    String estatuto = rs.getString("estatuto");
                    Aluno aluno;
                    if (estatuto.equals("Nenhum")) {
                        aluno = new Aluno(numero, nome, media);
                    } else {
                        aluno = new AlunoEstatuto(numero, nome, media);
                    }
                    alunos.add(aluno);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar alunos por turno: " + e.getMessage());
        }

        return alunos;
    }

    /**
     * Obtém o número de alunos inscritos em um turno específico.
     *
     * @param turnoCod Código do turno.
     * @return Número de alunos inscritos no turno.
     */
    public int sizeByTurno(String turnoCod) {
        int size = 0;
        String sql = "SELECT COUNT(*) AS total " +
                "FROM turnosDoAluno " +
                "WHERE turno_cod = ?";

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, turnoCod);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    size = rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar alunos por turno: " + e.getMessage());
        }

        return size;
    }


    /**
     * Obtém um aluno pelo número.
     *
     * @param numero Número do aluno a ser buscado.
     * @return Objeto Aluno correspondente ou null se não encontrado.
     */
    public Aluno get(String numero) {
        Aluno aluno = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM alunos WHERE num = ?")) {
            pstm.setString(1, numero);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    double media = rs.getDouble("media");
                    String estatuto = rs.getString("estatuto");
                    if (estatuto.equals("Nenhum")) {
                        aluno = new Aluno(numero, nome, media);
                    } else {
                        aluno = new AlunoEstatuto(numero, nome, media);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar aluno: " + e.getMessage());
        }
        return aluno;
    }

    /**
     * Obtém todos os alunos da tabela.
     *
     * @return Coleção contendo todos os alunos.
     */
    public Collection<Aluno> getAll() {
        Collection<Aluno> alunos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM alunos")) {
            while (rs.next()) {
                String numero = rs.getString("num");
                String nome = rs.getString("nome");
                double media = rs.getDouble("media");
                String estatuto = rs.getString("estatuto");
                Aluno aluno;
                if (estatuto.equals("Nenhum")) {
                    aluno = new Aluno(numero, nome, media);
                } else {
                    aluno = new AlunoEstatuto(numero, nome, media);
                }
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os alunos: " + e.getMessage());
        }
        return alunos;
    }

    /**
     * Obtém todos os alunos da tabela como um mapa.
     *
     * @return Um mapa contendo todos os alunos, onde a chave é o número do aluno.
     */
    public Map<String, Aluno> getAllAsMap() {
        Map<String, Aluno> alunosMap = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM alunos")) {
            while (rs.next()) {
                String numero = rs.getString("num");
                String nome = rs.getString("nome");
                double media = rs.getDouble("media");
                String estatuto = rs.getString("estatuto");
                Aluno aluno;
                if (estatuto.equals("Nenhum")) {
                    aluno = new Aluno(numero, nome, media);
                } else {
                    aluno = new AlunoEstatuto(numero, nome, media);
                }
                alunosMap.put(numero, aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os alunos como mapa: " + e.getMessage());
        }
        return alunosMap;
    }
}

