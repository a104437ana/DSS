package business.SSHorarios;

import data.AlunoDAO;
import data.SalaDAO;
import data.UCDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class SSHorarios implements ISSHorarios {

    private AlunoDAO alunosDAO;
    private UCDAO ucsDAO;
    private SalaDAO salasDAO;


    public SSHorarios() {
        this.salasDAO = SalaDAO.getInstance();
        this.ucsDAO = UCDAO.getInstance();
        this.alunosDAO = AlunoDAO.getInstance();
    }

    public boolean existeAluno(String codAluno) {
        return this.alunosDAO.existeAluno(codAluno);
    }

    public boolean existeUC(String codUC) {
        return this.ucsDAO.existeUC(codUC);
    }

    public boolean alunoInscritoNaUC(String codAluno,String codUC) {
        UC uc = this.ucsDAO.get(codUC);
        return uc.alunoInscrito(codAluno);
    }

    public boolean existeTurno(String codTurno, String codUC) {
        UC uc = this.ucsDAO.get(codUC);
        return uc.existeTurno(codTurno);
    }

    /**
     * Verifica se um aluno está associado a um turno específico de uma uc.
     *
     * @param codTurno  Código do turno.
     * @param codUC   Código da UC.
     * @return True se o turno tem espaço (número de alunos do turno < lotação do turno), se não False.
     */
    public boolean turnoTemEspaco(String codTurno, String codUC) {
        UC uc = this.ucsDAO.get(codUC);
        Turno t = uc.getTurno(codTurno);
        int l = t.getLotacao();
        int n = 0; // numero de alunos no turno

        List<String> listaAlunos = uc.getAlunos();

        // Percorrer a lista de alunos e seus turnos
        for (String codAluno : listaAlunos) {
            Aluno a = this.alunosDAO.get(codAluno);
            if (a == null) continue; // Verifica se existe

            Map<String, List<Turno>> turnosUCAluno = a.getTurnos();
            List<Turno> listaTurnosAluno = turnosUCAluno.get(codUC); // A lista como valor do mapa
            if (listaTurnosAluno == null) continue; // Cerifica se tem turnos

            if (listaTurnosAluno.contains(t)) n++; // Se tiver esse turno adiciona ao número de alunos no turno 'n'
        }

        return n < l;
    }

    public boolean alunoTemConflito(String codAluno, String codTurno, String codUC) {
        UC uc = this.ucsDAO.get(codUC);
        Turno t = uc.getTurno(codTurno);

        DiaSemana dia = t.getDiaSemana();
        LocalTime i = t.getHoraInicial();
        LocalTime f = t.getHoraFinal();

        Aluno a = this.alunosDAO.get(codAluno);
        Map<String,List<Turno>> map = a.getTurnos();
        List<Turno> l = map.get(String.valueOf(dia));

        if (l != null) {
            for (Turno turno : l) {
                LocalTime ii = turno.getHoraInicial();
                LocalTime ff = turno.getHoraFinal();
                if (i.isBefore(ff) && f.isAfter(ii)) return true;
            }
        }
        return false;
    }

    /**
     * Verifica se um aluno está associado a um turno específico de uma uc.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC   Código da UC.
     * @return True se o aluno está associado ao turno, se não False.
     */
    public boolean alunoTemTurno(String codAluno, String codUC, String codTurno) {
        Aluno aluno = this.alunosDAO.get(codAluno);
        return aluno.existeTurno(codUC, codTurno);
    }

    /**
     * Adiciona um aluno a um turno específico de uma UC.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC    Código da UC.
     */
    public void alocarAlunoAoTurno(String codAluno, String codUC, String codTurno) {
        Aluno aluno = this.alunosDAO.get(codAluno);
        aluno.putTurno(codUC, codTurno);
    }

    /**
     * Remove um aluno de um turno específico de uma UC.
     *
     * @param codAluno Código do aluno.
     * @param codTurno  Código do turno.
     * @param codUC     Código da UC.
     */
    public void removerAlunoDoTurno(String codAluno, String codUC, String codTurno) {
        Aluno aluno = this.alunosDAO.get(codAluno);
        aluno.removeTurno(codUC, codTurno);
    }

    public void gerarHorarios(int semestre) {
        return;
    }

    /**
     * Importa alunos e suas inscrições a partir de um arquivo CSV combinado.
     *
     * @param ficheiro Caminho para o arquivo CSV combinado contendo alunos e suas inscrições.
     * @return Map onde a chave é o código do aluno e o valor é a senha do aluno.
     */
    public Map<String, String> importarAlunos(String ficheiro) {
        Map<String, String> alunoSenhaMap = new HashMap<>();
        List<Aluno> alunos = new ArrayList<>();
        Map<String, List<Inscricao>> inscricoesPorUC = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            br.readLine(); // Ignorar a linha de cabeçalho

            while ((linha = br.readLine()) != null) {
                String[] atributos = linha.split(",");

                if (atributos.length < 5) continue; // Verificar estrutura mínima

                // Processar dados do aluno
                String codAluno = atributos[0];
                String nome = atributos[1];
                double media = Double.parseDouble(atributos[2]);
                String estatuto = atributos[3];
                String senha = atributos[4];

                Aluno aluno;
                // Criar aluno e adicioná-lo à lista
                if (estatuto.equals("Nenhum")) {
                    aluno = new Aluno(codAluno, nome, media);
                }
                else {
                    aluno = new AlunoEstatutoEspecial(codAluno, nome, media);
                }

                boolean isAluno = false;

                // Processar inscrições (após os primeiros 5 campos)
                for (int i = 5; i < atributos.length; i += 2) {
                    if (i + 1 >= atributos.length) {
                        System.err.println("Linha incompleta: " + Arrays.toString(atributos));
                        continue;
                    }

                    String codUC = atributos[i];

                    // Verifica que existe a UC
                    UC uc = this.ucsDAO.get(codUC);
                    if (uc == null) {
                        System.err.println("UC não encontrada para código: " + codUC);
                        continue;
                    }

                    int nInscricao;
                    try {
                        nInscricao = Integer.parseInt(atributos[i + 1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Número de inscrição inválido para aluno: " + codAluno);
                        continue;
                    }

                    isAluno = true;
                    // Adicionar a inscrição ao mapa
                    inscricoesPorUC
                            .computeIfAbsent(codUC, k -> new ArrayList<>())
                            .add(new Inscricao(codAluno, nInscricao));
                }

                if (isAluno) {
                    // Adiciona aluno à lista de alunos
                    alunos.add(aluno);

                    // Adicionar o aluno ao mapa de senhas
                    alunoSenhaMap.put(codAluno, senha);
                }
            }

            // Inserir alunos no banco de dados
            this.alunosDAO.putAll(alunos);

            // Adicionar inscrições por UC em lote
            for (Map.Entry<String, List<Inscricao>> entry : inscricoesPorUC.entrySet()) {
                String codUC = entry.getKey();
                List<Inscricao> inscricoes = entry.getValue();

                UC uc = this.ucsDAO.get(codUC);
                uc.adicionarInscricoes(inscricoes);

            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o ficheiro: " + e.getMessage(), e);
        }

        return alunoSenhaMap;
    }

    /**
     * Método que remove inscrições da UC.
     */
    public void removerTodasInscricoes() {
        List<UC> ucs = new ArrayList<>(this.ucsDAO.getAll());
        for (UC uc : ucs) {
            uc.removerInscricoes();
        }
    }

    /**
     * Método que remove todos os alunos.
     */
    public void removerAlunos() {
        List<UC> ucs = new ArrayList<>(this.ucsDAO.getAll());
        for (UC uc : ucs) {
            uc.removerInscricoes();
        }
        this.alunosDAO.deleteAllAlunos();
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
