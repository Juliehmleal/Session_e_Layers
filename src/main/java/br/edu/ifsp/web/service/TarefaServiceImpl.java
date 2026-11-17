package br.edu.ifsp.web.service;

import br.edu.ifsp.web.dao.ConnectionFactory;
import br.edu.ifsp.web.dao.TarefaDAO;
import br.edu.ifsp.web.model.Tarefa;
import br.edu.ifsp.web.model.Usuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TarefaServiceImpl implements TarefaService {
    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final TarefaDAO tarefaDAO = new TarefaDAO();

    @Override
    public List<Tarefa> listar(Usuario usuario) {
        try (Connection conn = connectionFactory.getConnection()) {
            return tarefaDAO.listarPorUsuario(usuario.getId(), conn);
        } catch (SQLException e) {
            throw new ErroInternoException("Erro ao listar tarefas.", e);
        }
    }

    @Override
    public Tarefa buscar(int id, Usuario usuario) {
        try (Connection conn = connectionFactory.getConnection()) {
            Tarefa tarefa = tarefaDAO.buscarPorIdEUsuario(id, usuario.getId(), conn);
            if (tarefa == null) {
                throw new RecursoNaoEncontradoException("Tarefa não encontrada.");
            }
            return tarefa;
        } catch (SQLException e) {
            throw new ErroInternoException("Erro ao buscar tarefa.", e);
        }
    }

    @Override
    public Tarefa criar(String titulo, Usuario usuario) {
        Tarefa nova = new Tarefa(0, titulo, false, usuario);
        try (Connection conn = connectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Tarefa criada = tarefaDAO.inserir(nova, conn);
                conn.commit();
                return criada;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new ErroInternoException("Erro ao criar tarefa.", e);
        }
    }

    @Override
    public Tarefa atualizar(int id, String novoTitulo, Usuario usuario) {
        try (Connection conn = connectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Tarefa tarefa = tarefaDAO.buscarPorIdEUsuario(id, usuario.getId(), conn);
                if (tarefa == null) {
                    throw new RecursoNaoEncontradoException("Tarefa não encontrada.");
                }
                tarefa.setTitulo(novoTitulo);
                tarefaDAO.atualizarTitulo(tarefa, conn);
                conn.commit();
                return tarefa;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new ErroInternoException("Erro ao atualizar tarefa.", e);
        }
    }

    @Override
    public void concluir(int id, Usuario usuario) {
        try (Connection conn = connectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Tarefa tarefa = tarefaDAO.buscarPorIdEUsuario(id, usuario.getId(), conn);
                if (tarefa == null) {
                    throw new RecursoNaoEncontradoException("Tarefa não encontrada.");
                }
                tarefa.concluir();
                tarefaDAO.marcarComoConcluida(tarefa.getId(), usuario.getId(), conn);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new ErroInternoException("Erro ao concluir tarefa.", e);
        }
    }

    @Override
    public void excluir(int id, Usuario usuario) {
        try (Connection conn = connectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean excluiu = tarefaDAO.excluir(id, usuario.getId(), conn);
                if (!excluiu) {
                    throw new RecursoNaoEncontradoException("Tarefa não encontrada.");
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new ErroInternoException("Erro ao excluir tarefa.", e);
        }
    }
}