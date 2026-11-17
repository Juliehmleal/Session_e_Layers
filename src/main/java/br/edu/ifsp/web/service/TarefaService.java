package br.edu.ifsp.web.service;

import br.edu.ifsp.web.model.Tarefa;
import br.edu.ifsp.web.model.Usuario;
import java.util.List;

public interface TarefaService {
    List<Tarefa> listar(Usuario usuario);
    Tarefa buscar(int id, Usuario usuario);
    Tarefa criar(String titulo, Usuario usuario);
    Tarefa atualizar(int id, String novoTitulo, Usuario usuario);
    void concluir(int id, Usuario usuario);
    void excluir(int id, Usuario usuario);
}