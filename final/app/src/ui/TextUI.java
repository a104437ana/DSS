package ui;

import business.GestHorariosFacade;
import business.IGestHorariosLN;


import java.util.Scanner;


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

        String user = this.model.iniciarSessao(cod, senha);
        if (user == null) {
            System.out.println("Credenciais inválidas!");
        } else if (user.equals("admin")) {
            this.menuPrincipalAdmin(user);
        } else {
            this.menuPrincipalAluno(user);
        }

    }

    /**
     * Estado - Menu Principal do Admin
     */
    private void menuPrincipalAdmin(String user) {
        Menu menu = new Menu(new String[]{
                "Importação",
                "Atribuição manual dos alunos aos turnos",
                "Atribuição automática dos alunos aos turnos",
                "Operações de Listagem"
//                "Definição das Preferências e Limites",
//                "Criação e Gestão de Horários de Alunos",
//                "Verificação de Conflitos e Problemas",
                //"Minha conta"
        });

        menu.setHandler(1, this::menuImport);
        menu.setHandler(2, this::menuManual);
        menu.setHandler(3, this::menuAuto);
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
        String horario = this.model.consultarHorario(user);
        if (horario == null) { // Verificar se tem turnos
            System.out.println("O aluno ainda não tem horário!");
            return;
        }
        System.out.println(horario);
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

        menu.setHandler(1, ()->this.model.inicializacaoSalasUCsTurnos());
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
        this.model.gerarHorarios(semestre);
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
        if (!this.model.alunoTemTurno(codAluno,codTurno,codUC)) {
            System.out.println("O aluno não está neste turno");
            return;
        }
        this.model.removerAlunoDoTurno(codAluno,codUC,codTurno);
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

        String horario = this.model.consultarHorario(codAluno);
        if (horario == null) { // Verificar se tem turnos
            System.out.println("O aluno ainda não tem horário!");
            return;
        }

        System.out.println(horario);
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

