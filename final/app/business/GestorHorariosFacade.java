package projetodss.business;

import projetodss.data.UtilizadorDAO;

import java.util.ArrayList;
import java.util.Collection;

public class GestorHorariosFacade implements IGestorHorariosFacade {

    private UtilizadorDAO utilizadores;

    public GestorHorariosFacade() {
        this.utilizadores = UtilizadorDAO.getInstance();
    }


    @Override
    public Collection<Aluno> getAlunos() {
        return new ArrayList<>(this.utilizadores.valuesAlunos());
    }

    @Override
    public boolean existemAlunos() {
        return !this.utilizadores.isEmpty();
    }

    @Override
    public int numAlunos() {
        return this.utilizadores.sizeAlunos();
    }

    @Override
    public Utilizador getUtilizador(String id) {
        return this.utilizadores.get(id);
    }

    @Override
    public Aluno getAluno(String num) {
        return this.utilizadores.getAluno(num);
    }

    @Override
    public boolean existeAluno(String id) {
        return this.utilizadores.existeCodAluno(id);
    }

    @Override
    public boolean validaUserSenha(Utilizador u, String senha) {
        return (u!=null && u.getSenha().equals(senha));
    }

    @Override
    public void adicionaAluno(Aluno a) {
        this.utilizadores.putAluno(a);
    }

    @Override
    public void listaAlunos() {
        System.out.println(this.utilizadores.valuesAlunos().toString());
    }

    @Override
    public void importAlunos(String ficheiro) {
        this.utilizadores.importAlunosFile(ficheiro);
    }

    @Override
    public boolean alterarSenha(Utilizador u, String senha) {
        return this.utilizadores.alterarSenha(u.getId(), senha);
    }
}
