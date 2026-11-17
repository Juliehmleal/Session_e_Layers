package br.edu.ifsp.web.model;

import br.edu.ifsp.web.service.DadosInvalidosException;
import br.edu.ifsp.web.service.RegraNegocioException;

public class Tarefa {
    private int id;
    private String titulo;
    private boolean concluida;
    private transient Usuario usuario;

    public Tarefa() {}

    public Tarefa(int id, String titulo, boolean concluida, Usuario usuario) {
        this.id = id;
        setTitulo(titulo);
        this.concluida = concluida;
        this.usuario = usuario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new DadosInvalidosException("O título da tarefa é obrigatório.");
        }
        this.titulo = titulo.trim();
    }

    public boolean isConcluida() { return concluida; }

    public void concluir() {
        if (this.concluida) {
            throw new RegraNegocioException("A tarefa já está concluída.");
        }
        this.concluida = true;
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
