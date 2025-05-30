package ui;

import business.GestHorariosFacade;
import business.IGestHorariosLN;
import business.SSHorarios.DiaSemana;


import java.util.*;


/**
 * Exemplo de interface em modo texto.
 *
 * @author DSS
 * @version 20230915
 */
public class TextUI {
    // O model tem a 'lógica de negócio'.
    private IGestHorariosLN model;
    // Scanner para leitura
    private Scanner scin;

    /**
     * Construtor.
     * <p>
     * Cria os menus e a camada de negócio.
     */
    public TextUI() {
        model = new GestHorariosFacade();
        scin = new Scanner(System.in);
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção seleccionada.
     */
    public void run() {
        System.out.println("Bem vindo ao Sistema de Gestão de Horários!");
        this.initMenu();
        System.out.println("Até breve...");
    }

    // Métodos auxiliares - Estados da UI

    private void initMenu() {
        Menu menu = new Menu(new String[]{
                "Entrar na Plataforma"
        });
        menu.setHandler(1, this::menuLogin);
        menu.run();
    }

    private void menuLogin() {
        System.out.println("Insira código de utilizador: ");
        String cod = scin.nextLine();
        System.out.println("Insira senha de utilizador: ");
        String senha = scin.nextLine();

        boolean iniciarSessao = this.model.iniciarSessao(cod, senha);
        if (iniciarSessao == false) {
            System.out.println("Credenciais inválidas!");
        } else {
            System.out.println("'" + cod + "' logado com sucesso!");
            if (cod.toLowerCase().equals("admin")) this.menuPrincipalAdmin(cod);
            else this.menuPrincipalAluno(cod);
        }
    }

    /**
     * Estado - Menu Principal do Admin
     */
    private void menuPrincipalAdmin(String user) {
        Menu menu = new Menu(new String[]{
                "Importação",
                "Atribuição automática dos alunos aos turnos",
                "Atribuição manual dos alunos aos turnos",
                "Operações de Listagem"
//                "Definição das Preferências e Limites",
//                "Criação e Gestão de Horários de Alunos",
//                "Verificação de Conflitos e Problemas",
                //"Minha conta"
        });

        menu.setHandler(1, this::menuImport);
        menu.setHandler(2, this::menuAuto);
        menu.setHandler(3, this::menuManual);
        menu.setHandler(4, this::menuListagem);
//        menu.setHandler(3, this::menuDefinirPrefLim);
//        menu.setHandler(4, this::menuGestaoHorario);
//        menu.setHandler(5, this::menuVerificar);
        //menu.setHandler(3, ()->this.menuConta(user));

        menu.run();
    }

    /**
     * Estado - Menu Principal do Aluno
     */
    private void menuPrincipalAluno(String user) {

        Menu menu = new Menu(new String[]{
                "Consultar o meu Horário"
        });


        menu.setHandler(1, ()->this.printHorario(user));
       // menu.setHandler(, ()->this.menuConta(user));


        menu.run();
    }

    /**
     * Mostrar horário de aluno
     */
    private void printHorario(String user) {
        Map<String, List<String>> horario = this.model.consultarHorario(user);
        if (horario.isEmpty()) { // Verificar se tem turnos
            System.out.println("O aluno ainda não tem horário!");
            return;
        }
        System.out.print("Horário do aluno ");
        System.out.println(user);

        List<String> dias = new ArrayList<>();
        dias.add("Segunda-Feira");
        dias.add("Terça-Feira");
        dias.add("Quarta-Feira");
        dias.add("Quinta-Feira");
        dias.add("Sexta-Feira");

        for(String dia : dias) {
            System.out.print(dia);
            System.out.println(":");
            List<String> turnos = horario.get(dia);
            if (turnos != null) {
                for (String turno : turnos) {
                    System.out.println(turno);
                }
            }
        }
    }


    /**
     * Estado - Menu Operações de listagem e consulta
     */
    private void menuListagem() {
        Menu menu = new Menu(new String[]{
                    "Consultar Horário de um Aluno"
//                "Listar Informações de Aluno",
//                "Listar Informações de UC",
//                "Listar Informações de Turno",
//                "Listar Informações de Sala"
        });

        menu.setHandler(1, this::consultarHorario);
//        menu.setHandler(1, this::infoAluno);
//        menu.setHandler(2, this::listarUC);
//        menu.setHandler(3, this::listarTurno);
//        menu.setHandler(4, this::listarSala);

        menu.run();
    }



    /**
     * Estado - Menu Importação e Exportação
     */
    private void menuImport() {
        Menu menu = new Menu(new String[]{
                "Preencher Salas, UCs e Turnos",
                "Importar Alunos"
        });

        menu.setHandler(1, ()->{
            this.model.inicializacaoSalasUCsTurnos();
            System.out.println("As salas, UCs e turnos foram inseridos no sistema com sucesso.");
        });
        menu.setHandler(2, this::importarAlunos);

        menu.run();
    }

    /**
     * Estado - Menu de alocação manual de alunos aos seus turnos
     */
    private void menuManual() {
        Menu menu = new Menu(new String[]{
                "Alocar aluno ao turno de uma UC",
                "Remover aluno do turno de uma UC"
        });

        menu.setHandler(1, this::alocarAlunoAoTurno);
        menu.setHandler(2, this::removerAlunoDoTurno);

        menu.run();
    }

    /**
     * Estado - Menu de alocação automática de alunos aos seus turnos
     */
    private void menuAuto() {
        Menu menu = new Menu(new String[]{
                "Gerar horários automáticamente"
        });

        menu.setHandler(1, this::gerarHorarios);

        menu.run();
    }

    /**
     * Auxiliar - Gerar horário automático dos alunos
     */
    private void gerarHorarios() {
        int semestre;
        System.out.println("Insira o semestre atual (1 ou 2): ");
        String s = scin.nextLine();
        try {
            semestre = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Semestre inválido!");
            return;
        }
        if (semestre != 1 && semestre != 2) {
            System.out.println("Semestre inválido!");
            return;
        }
        Map<String, Map<String, Set<String>>> alunosNaoAlocados = this.model.gerarHorarios(semestre);
        StringBuilder sb = new StringBuilder();
        if (alunosNaoAlocados.size() > 0) {
            sb.append("Alunos não alocados:");
            for (Map.Entry<String, Map<String, Set<String>>> entrada : alunosNaoAlocados.entrySet()) {
                String codAluno = entrada.getKey();
                Map<String, Set<String>> ucsNaoAlocados = entrada.getValue();
                sb.append("\nAluno ").append(codAluno).append(":");
                for (Map.Entry<String, Set<String>> entradaUC : ucsNaoAlocados.entrySet()) {
                    String codUC = entradaUC.getKey();
                    Set<String> turnos = entradaUC.getValue();
                    sb.append("\n\tUC ").append(codUC).append(":");
                    for (String tipoTurno : turnos) {
                        sb.append(" ").append(tipoTurno);
                    }
                }
            }
            System.out.println(sb.toString());
        }
        else {
            System.out.println("Todos os alunos alocados com sucesso!");
        }
    }

    /**
     * Auxiliar - Alocação manual de um aluno
     */
    private void alocarAlunoAoTurno() {
        System.out.println("Insira o código de aluno: ");
        String codAluno = scin.nextLine();
        if (!this.model.existeAluno(codAluno)) {
            System.out.println("Aluno não existe!");
            return;
        }
        System.out.println("Insira o código da UC: ");
        String codUC = scin.nextLine();
        if (!this.model.existeUC(codUC)) {
            System.out.println("UC não existe!");
            return;
        }
        if (!this.model.alunoInscritoNaUC(codAluno,codUC)) {
            System.out.println("O aluno não está inscrito nesta UC!");
            return;
        }
        System.out.println("Insira o código do turno: ");
        String codTurno = scin.nextLine();
        if (!this.model.existeTurno(codTurno,codUC)) {
            System.out.println("Turno não existe!");
            return;
        }
        if (this.model.alunoTemTurno(codAluno,codUC,codTurno)) {
            System.out.println("O aluno já está neste turno!");
            return;
        }
        if (!this.model.turnoTemEspaco(codTurno,codUC)) {
            System.out.println("Turno não tem espaço suficiente!");
            System.out.println("Deseja alocar mesmo assim? (Y/n)");
            while(true) {
                String s = scin.nextLine();
                if (s.equals("Y")) break;
                else {
                    if (s.equals("n")) return;
                    else {
                        System.out.println("Resposta inválida! Tente novamente.");
                        System.out.println("Deseja alocar mesmo assim? (Y/n)");
                    }
                }
            }
        }
        if (this.model.alunoTemConflito(codAluno,codTurno,codUC)) {
            System.out.println("Aluno já está noutros turnos á mesma hora e no mesmo dia que o Turno atual!");
            System.out.println("Deseja alocar mesmo assim? (Y/n)");
            while(true) {
                String s = scin.nextLine();
                if (s.equals("Y")) break;
                else {
                    if (s.equals("n")) return;
                    else {
                        System.out.println("Resposta inválida! Tente novamente.");
                        System.out.println("Deseja alocar mesmo assim? (Y/n)");
                    }
                }
            }
        }
        this.model.alocarAlunoAoTurno(codAluno,codUC,codTurno);
        System.out.println("Aluno '" + codAluno + "' alocado com sucesso no turno '" + codTurno + "' da UC '" + codUC + "'.");
    }

    /**
     * Auxiliar - Remoção manual de um aluno
     */
    private void removerAlunoDoTurno() {
        System.out.println("Insira o código de aluno: ");
        String codAluno = scin.nextLine();
        if (!this.model.existeAluno(codAluno)) {
            System.out.println("Aluno não existe!");
            return;
        }
        System.out.println("Insira o código da UC: ");
        String codUC = scin.nextLine();
        if (!this.model.existeUC(codUC)) {
            System.out.println("UC não existe!");
            return;
        }
        if (!this.model.alunoInscritoNaUC(codAluno,codUC)) {
            System.out.println("O aluno não está inscrito nesta UC!");
            return;
        }
        System.out.println("Insira o código do turno: ");
        String codTurno = scin.nextLine();
        if (!this.model.existeTurno(codTurno,codUC)) {
            System.out.println("Turno não existe!");
            return;
        }
        if (!this.model.alunoTemTurno(codAluno,codUC,codTurno)) {
            System.out.println("O aluno não está neste turno");
            return;
        }
        this.model.removerAlunoDoTurno(codAluno,codUC,codTurno);
        System.out.println("Aluno '" + codAluno + "' removido com sucesso do turno '" + codTurno + "' da UC '" + codUC + "'.");
    }

    /**
     * Auxiliar - Importação de alunos dado um ficheiro
     */
    private void importarAlunos() {
        System.out.println("Insira o caminho do ficheiro de alunos a importar: ");
        System.out.println("Exemplo - src/csv/alunos.csv");
        String caminho = scin.nextLine();

        this.model.importarAlunos(caminho);
    }

    /**
     * Auxiliar - Consultar horário de aluno
     */
    private void consultarHorario() {
        System.out.println("Insira o código de aluno a consultar: ");
        String codAluno = scin.nextLine();

        if (!this.model.existeAluno(codAluno)) { // Verificar se existe o aluno primeiro
            System.out.println("O aluno não existe!");
            return;
        }

        Map<String,List<String>> horario = this.model.consultarHorario(codAluno);
        if (horario.isEmpty()) { // Verificar se tem turnos
            System.out.println("O aluno ainda não tem horário!");
            return;
        }
        System.out.print("Horário do aluno ");
        System.out.println(codAluno);

        List<String> dias = new ArrayList<>();
        dias.add("Segunda-Feira");
        dias.add("Terça-Feira");
        dias.add("Quarta-Feira");
        dias.add("Quinta-Feira");
        dias.add("Sexta-Feira");

        for(String dia : dias) {
            System.out.print(dia);
            System.out.println(":");
            List<String> turnos = horario.get(dia);
            if (turnos != null) {
                for (String turno : turnos) {
                    System.out.println(turno);
                }
            }
        }
    }

    /**
     * Estado - Menu das Definições da Conta de Utilizador
     */
    private void menuConta(String user) {
        Menu menu = new Menu(new String[]{
//                "Detalhes da Conta",
                "Redefinir Senha"
        });

//        menu.setHandler(1, this::detalhesConta);
        menu.setHandler(1, ()->this.redefinirSenha());

        menu.run();
    }

    /**
     * Estado - Menu de redefinir senha
     */
    private void redefinirSenha() {
        System.out.println("Insira a sua senha antiga: ");
        String senha = scin.nextLine();


    }
}

