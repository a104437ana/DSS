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
                "Importar alunos",
                "Alocar aluno ao turno de uma UC",
                "Remover aluno do turno de uma UC ",
                "Gerar horários automáticamente"
                //"Operações de Listagem",
//                "Definição das Preferências e Limites",
//                "Criação e Gestão de Horários de Alunos",
//                "Verificação de Conflitos e Problemas",
                //"Minha conta"
        });

        menu.setHandler(1, this::menuImport);
       // menu.setHandler(2, this::menuListagem);
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

        menu.setHandler(1, ()->this.model.consultarHorario(user));
       // menu.setHandler(1, ()->this.menuConta(user));

        menu.run();
    }

    /**
     * Estado - Menu Definir preferências e limites
     */
    private void menuDefinirPrefLim() {
        Menu menu = new Menu(new String[]{
                "Definir Preferências de UC",
                "Definir Limite de UC",
                "Definir Preferências de Aluno com Estatuto"
        });

//        menu.setHandler(1, this::definirPrefUC);
//        menu.setHandler(2, this::definirLimiteUC);
//        menu.setHandler(3, this::definirPrefAlunoEstatuto);

        menu.run();
    }

    /**
     * Estado - Menu Operações de consulta e listagem
     */
    private void menuListagem() {
        Menu menu = new Menu(new String[]{
                "Listar Informações de Aluno"//,
//                "Listar Informações de UC",
//                "Listar Informações de Turno",
//                "Listar Informações de Sala"
        });

        menu.setHandler(1, this::infoAluno);
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
                "Importar Alunos e Inscrições"
        });

        menu.setHandler(1, ()->this.model.inicializacaoSalasUCsTurnos());
        menu.setHandler(2, ()->this.model.importarAlunos("src/csv/alunos.csv"));

        menu.run();
    }

    /**
     * Estado - Menu Gestão de horários
     */
    private void menuGestaoHorario() {
        Menu menu = new Menu(new String[]{
                "Gerar Horário",
                "Alocar Aluno a Turno",
                "Remover Aluno de Turno"
        });

//        menu.setHandler(1, this::gerarHorario);
//        menu.setHandler(2, this::alocarAlunoTurno);
//        menu.setHandler(3, this::removerAlunoTurno);

        menu.run();
    }

    /**
     * Estado - Menu Verificação
     */
    private void menuVerificar() {
        Menu menu = new Menu(new String[]{
                "Verificar alunos com conflitos",
                "Verificar preferências não respeitadas",
                "Verificar alunos sem turno",
                "Verificar turnos que ultrapassam limite"
        });

//        menu.setHandler(1, this::verificarConflitos);
//        menu.setHandler(2, this::verificarPreferencias);
//        menu.setHandler(3, this::verificarSemTurno);
//        menu.setHandler(4, this::verificarTurnosLimite);

        menu.run();
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
     * Estado - Menu de listagem de detalhes de um dado aluno
     */
    private void infoAluno() {
        System.out.println("Insira o número do aluno: ");
        String num = scin.nextLine();


    }

    /**
     * Estado - Menu de redefinir senha
     */
    private void redefinirSenha() {
        System.out.println("Insira a sua senha antiga: ");
        String senha = scin.nextLine();


    }
}

