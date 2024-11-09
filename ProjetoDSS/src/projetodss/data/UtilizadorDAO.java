package projetodss.data;

import projetodss.business.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class UtilizadorDAO {

    // Construtor privado para Singleton
    private UtilizadorDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Criar a tabela 'utilizadores' se não existir
            String sql = "CREATE TABLE IF NOT EXISTS utilizadores (" +
                    "id VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "senha VARCHAR(45) NOT NULL)";
            stm.executeUpdate(sql);

            // Criar a tabela 'alunos' se não existir
            sql = "CREATE TABLE IF NOT EXISTS alunos (" +
                    "num VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(45) NOT NULL, " +
                    "estatuto VARCHAR(20), "+
                    "user_id VARCHAR(10) NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES utilizadores(id))";
            stm.executeUpdate(sql);

            // Criar a VIEW 'alunosUser' para unir 'alunos' e 'utilizadores'
            sql = "CREATE OR REPLACE VIEW alunosUsers AS " +
                    "SELECT alunos.num AS cod, alunos.nome AS nome, alunos.estatuto AS estatuto, utilizadores.senha AS senha " +
                    "FROM alunos " +
                    "LEFT JOIN utilizadores ON alunos.user_id = utilizadores.id";
            stm.executeUpdate(sql);

            // Criar Admin
            sql = "INSERT IGNORE INTO utilizadores (id, senha) VALUES ('admin', 'admin')";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private static UtilizadorDAO singleton = null;

    /**
     * Implementação do padrão Singleton
     *
     * @return devolve a instância única desta classe
     */
    public static UtilizadorDAO getInstance() {
        if (UtilizadorDAO.singleton == null) {
            UtilizadorDAO.singleton = new UtilizadorDAO();
        }
        return UtilizadorDAO.singleton;
    }

    /**
     * @return número de alunos na base de dados
     */

    public int sizeAlunos() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM alunos")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    /**
     * Método que verifica se existem alunos
     *
     * @return true se existirem 0 alunos
     */

    public boolean isEmpty() {
        return this.sizeAlunos() == 0;
    }


    /**
     * Método que cerifica se um id de user existe na base de dados
     *
     * @param key id de um user
     * @return true se o user existe
     * @throws NullPointerException Em caso de erro - deveriam ser criadas exepções do projecto
     */

    public boolean existeIdUtilizador(Object key) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM utilizadores WHERE id=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                r = rs.next();  // A chave existe na tabela
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar id de utilizador: " + e.getMessage());
        }
        return r;
    }

    /**
     * Método que cerifica se um código de aluno existe na base de dados
     *
     * @param key id de um aluno
     * @return true se o aluno existe
     * @throws NullPointerException Em caso de erro - deveriam ser criadas exepções do projecto
     */

    public boolean existeCodAluno(Object key) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT cod FROM alunosUsers WHERE cod=?")) {
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
        return this.existeCodAluno(a.getId()) && a.equals(this.getAluno(a.getId()));
    }


    /**
     * Método para obter um aluno dado o seu código.
     *
     * @param cod Código de aluno
     * @return Aluno ou null se não existir
     */
    public Aluno getAluno(String cod) {
        Aluno u = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM alunosUsers WHERE cod = ?")) {
            pstm.setString(1, cod);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String senha = rs.getString("senha");
                    String estatuto = rs.getString("estatuto");

                    if (estatuto.equals("Atleta")) {
                        u = new Atleta(cod, nome, senha);
                    }
                    else if (estatuto.equals("Trabalhador")) {
                        u = new Trabalhador(cod, nome, senha);
                    }
                    else {
                        u = new Aluno(cod, nome, senha);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return u;
    }

    /**
     * Método para obter um user dado o seu ID.
     *
     * @param id ID de Utilizador
     * @return Utilizador ou null se não existir
     */
    public Utilizador get(String id) {
        Utilizador u = null;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM utilizadores WHERE id = ?")) {
            pstm.setString(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String senha = rs.getString("senha");

                    if (id.equals("admin")) {
                        u = new Administrador(id, senha);
                    }
                    else if (existeCodAluno(id)){
                        u = this.getAluno(id);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return u;
    }


    /**
     * Método para adicionar ou atualizar um aluno, criando uma conta se necessário.
     *
     * @param a Aluno
     */
    public void putAluno(Aluno a) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            conn.setAutoCommit(false); // Iniciar a transação

            // Verificar se o utilizador já existe
            if (existeIdUtilizador(a.getId())) {
                // Se o utilizador já existe, atualiza apenas a tabela `alunos`
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO alunos (num, nome, estatuto, user_id) VALUES (?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE nome = VALUES(nome), estatuto = VALUES(estatuto)")) {
                    pstm.setString(1, a.getId());
                    pstm.setString(2, a.getNome());
                    pstm.setString(3, a.getEstatuto());
                    pstm.setString(4, a.getId());
                    pstm.executeUpdate();
                }

            } else {

                // Se o utilizador não existe, insere-o em `utilizadores`
                try (PreparedStatement pstmUtilizador = conn.prepareStatement(
                        "INSERT INTO utilizadores (id, senha) VALUES (?, ?)")) {
                    pstmUtilizador.setString(1, a.getId());
                    pstmUtilizador.setString(2, a.getSenha());
                    pstmUtilizador.executeUpdate();
                }


                // Insere o aluno na tabela `alunos`
                try (PreparedStatement pstmAluno = conn.prepareStatement(
                        "INSERT INTO alunos (num, nome, estatuto, user_id) VALUES (?, ?, ?, ?)")) {
                    pstmAluno.setString(1, a.getId());
                    pstmAluno.setString(2, a.getNome());
                    pstmAluno.setString(3, a.getEstatuto());
                    pstmAluno.setString(4, a.getId());
                    pstmAluno.executeUpdate();
                }
            }

            conn.commit(); // Fazer commit da transação

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Reverter a transação em caso de erro
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao adicionar/atualizar o aluno: " + e.getMessage());
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
     * Método para remover um aluno pelo seu ID.
     *
     * @param id codigo do aluno a remover
     * @return Aluno removido ou null se não existir
     */
    public Aluno removeAluno(String id) {
        Aluno alunoRemovido = this.getAluno(id); // Recupera o aluno antes de remover
        if (alunoRemovido == null) return null; // Retorna null se o aluno não existe

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            conn.setAutoCommit(false);

            // Remove o aluno da tabela 'alunos'
            try (PreparedStatement pstmAluno = conn.prepareStatement("DELETE FROM alunos WHERE num = ?")) {
                pstmAluno.setString(1, id);
                pstmAluno.executeUpdate();
            }

            // Remove o utilizador da tabela 'utilizadores'
            try (PreparedStatement pstmUtilizador = conn.prepareStatement("DELETE FROM utilizadores WHERE id = ?")) {
                pstmUtilizador.setString(1, id);
                pstmUtilizador.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Reverte a transação em caso de erro
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao remover o aluno: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Retorna ao modo de auto-commit
                    conn.close(); // Fecha a conexão
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return alunoRemovido;
    }


    /**
     * Método para listar todos os alunos.
     *
     * @return Collection de todos os alunos
     */
    public Collection<Aluno> valuesAlunos() {
        Collection<Aluno> res = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM alunosUsers")) {
            while (rs.next()) {
                String id = rs.getString("cod");
                String nome = rs.getString("nome");
                String estatuto = rs.getString("estatuto");
                String senha = rs.getString("senha");

                Aluno a = null;
                if (estatuto.equals("Atleta")) {
                    a = new Atleta(id, nome, senha);
                }
                else if (estatuto.equals("Trabalhador")) {
                    a = new Trabalhador(id, nome, senha);
                }
                else{
                    a = new Aluno(id, nome, senha);
                }

                res.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return res;
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
     * Método para remover contas de utilizadores que não têm alunos associados,
     * exceto o utilizador com ID "admin".
     */
    private void removeContasSemAluno() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            String sql = "DELETE FROM utilizadores WHERE id != 'admin' AND id NOT IN (SELECT user_id FROM alunos)";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover utilizadores não associados: " + e.getMessage());
        }
    }

    /**
     * Método para importar alunos a partir de um ficheiro.
     *
     * @param filename caminho e nome do ficheiro a ler
     */
    public void importAlunosFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {


            br.readLine();

            // Deletar alunos primeiro
            deleteAllAlunos();

            // Criar alunos e respetivas contas
            String line;
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");

                // Verificar se a linha tem as colunas esperadas (3 no caso: id, nome, estatuto, senha)
                if (attributes.length < 4) {
                    continue;  // Ignorar esta linha e continuar para a próxima

                }
                String id = attributes[0];
                String nome = attributes[1];
                String estatuto = attributes[2];
                String senha = attributes[3];

                Aluno a = null;

                if (estatuto.equals("Trabalhador")) {
                    a = new Trabalhador(id, nome, senha);
                }
                else if (estatuto.equals("Atleta")) {
                    a = new Atleta(id, nome, senha);
                }
                else {
                    a = new Aluno(id, nome, senha);
                }

                this.putAluno(a);
            }

            // Remove as contas sem aluno
            removeContasSemAluno();

        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo " + filename + " não encontrado.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao ler o ficheiro: " + e.getMessage());
        }
    }

    /**
     * Método para alterar a senha de um utilizador existente.
     *
     * @param userId  O ID do utilizador cujo a senha será alterada
     * @param novaSenha A nova senha a ser definida
     * @return True se a senha foi alterada com sucesso, False caso o utilizador não exista
     * @throws RuntimeException se o utilizador não for encontrado
     */
    public boolean alterarSenha(String userId, String novaSenha) {
        boolean senhaAlterada = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("UPDATE utilizadores SET senha = ? WHERE id = ?")) {

            pstm.setString(1, novaSenha);
            pstm.setString(2, userId);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Utilizador com ID " + userId + " não encontrado.");
            }

            senhaAlterada = true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao alterar a senha do utilizador: " + e.getMessage());
        }
        return senhaAlterada;
    }


}

