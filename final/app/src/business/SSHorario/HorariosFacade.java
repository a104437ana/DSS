package business.SSHorario;

import business.SSUtilizadores.Utilizador;
import data.AlunoDAO;
import data.SalaDAO;
import data.UCDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HorariosFacade implements IHorariosFacade {

    private AlunoDAO alunosDAO;
    private UCDAO ucsDAO;
    private SalaDAO salasDAO;

    public HorariosFacade() {
        this.salasDAO = SalaDAO.getInstance();
        this.ucsDAO = UCDAO.getInstance();
        this.alunosDAO = AlunoDAO.getInstance();
    }

    /**
     * Importa alunos a partir de um arquivo CSV.
     *
     * @param ficheiroAlunos Caminho para o arquivo CSV contendo os alunos.
     */
    public void importarAlunos(String ficheiroAlunos) {
        List<Aluno> alunos = new ArrayList<>();

        try (BufferedReader brAlunos= new BufferedReader(new FileReader(ficheiroAlunos))) {

            // Importar os alunos
            String linha;
            brAlunos.readLine(); // Ignorar a linha de cabeçalho

            while ((linha = brAlunos.readLine()) != null) {
                String[] atributos = linha.split(",");
                if (atributos.length < 5) continue;

                String numero = atributos[0];
                String nome = atributos[1];
                double media = Double.parseDouble(atributos[2]);
                String estatuto = atributos[3];

                Aluno aluno = estatuto.equalsIgnoreCase("Nenhum")
                        ? new Aluno(numero, nome, media)
                        : new AlunoEstatuto(numero, nome, media);
                alunos.add(aluno);
            }

            alunosDAO.putAll(alunos);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Importa inscrições de alunos em UCs a partir de um arquivo CSV.
     *
     * @param ficheiroUCs Caminho para o arquivo CSV contendo as inscrições.
     */
    public void importarInscricoesAlunos(String ficheiroUCs) {
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiroUCs))) {
            String linha;

            br.readLine(); // Ignorar a linha de cabeçalho

            while ((linha = br.readLine()) != null) {
                String[] atributos = linha.split(",");

                if (atributos.length < 3) continue; // Ignorar linhas inválidas

                String codUC = atributos[0]; // Primeiro atributo é o código da UC
                List<Inscricao> inscricoes = new ArrayList<>();

                for (int i = 1; i < atributos.length; i += 2) {
                    String codAluno = atributos[i];
                    int nInscricao;

                    if (i + 1 >= atributos.length) {
                        System.err.println("Linha incompleta: " + Arrays.toString(atributos));
                        continue;
                    }

                    try {
                        nInscricao = Integer.parseInt(atributos[i + 1]); // Converter n_inscricao para int
                    } catch (NumberFormatException e) {
                        System.err.println("Número de inscrição inválido para aluno: " + codAluno);
                        continue;
                    }

                    // Criar a inscrição
                    Inscricao inscricao = new Inscricao(codAluno, nInscricao);

                    // Adicionar à lista de inscrições
                    inscricoes.add(inscricao);
                }

                // Adicionar todas as inscrições para esta UC ao DAO
                UC uc = this.ucsDAO.get(codUC);
                if (uc != null) {
                    uc.adicionarInscricoes(inscricoes);
                } else {
                    System.err.println("UC não encontrada para código: " + codUC);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo de inscrições: " + e.getMessage(), e);
        }
    }

    /**
     * Método que devolve o horário de um Aluno.
     *
     * @param codAluno Código do aluno.
     * @return String string com horário do aluno dado.
     */
    public String getHorarioAluno(String codAluno) {
        Aluno a = this.alunosDAO.get(codAluno);
        return a.getStringHorario();
    }

    /**
     * Método que preenche as tabelas das salas, ucs e turnos com dados pré-definidos.
     */
    public void inicializacaoSalasUCsTurnos() {
        this.salasDAO.inserirSalasIniciais();
        this.ucsDAO.inserirUcsETurnosIniciais();
    }
}
