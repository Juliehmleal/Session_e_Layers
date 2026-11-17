package br.edu.ifsp.web.dao;

import br.edu.ifsp.web.model.Tarefa;
import br.edu.ifsp.web.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    public List<Tarefa> listarPorUsuario(int usuarioId, Connection conn) throws SQLException {
        String sql = "SELECT id, titulo, concluida FROM tarefas WHERE usuario_id = ?";
        List<Tarefa> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs, usuarioId));
                }
            }
        }
        return lista;
    }

    public Tarefa buscarPorIdEUsuario(int id, int usuarioId, Connection conn) throws SQLException {
        String sql = "SELECT id, titulo, concluida FROM tarefas WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs, usuarioId);
                }
                return null;
            }
        }
    }

    public Tarefa inserir(Tarefa tarefa, Connection conn) throws SQLException {
        String sql = "INSERT INTO tarefas (titulo, concluida, usuario_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tarefa.getTitulo());
            stmt.setBoolean(2, tarefa.isConcluida());
            stmt.setInt(3, tarefa.getUsuario().getId());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    tarefa.setId(keys.getInt(1));
                }
            }
            return tarefa;
        }
    }

    public void atualizarTitulo(Tarefa tarefa, Connection conn) throws SQLException {
        String sql = "UPDATE tarefas SET titulo = ? WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tarefa.getTitulo());
            stmt.setInt(2, tarefa.getId());
            stmt.setInt(3, tarefa.getUsuario().getId());
            stmt.executeUpdate();
        }
    }

    public void marcarComoConcluida(int id, int usuarioId, Connection conn) throws SQLException {
        String sql = "UPDATE tarefas SET concluida = true WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
        }
    }

    public boolean excluir(int id, int usuarioId, Connection conn) throws SQLException {
        String sql = "DELETE FROM tarefas WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, usuarioId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Tarefa mapear(ResultSet rs, int usuarioId) throws SQLException {
        Usuario u = new Usuario();
        u.setId(usuarioId);
        return new Tarefa(rs.getInt("id"), rs.getString("titulo"), rs.getBoolean("concluida"), u);
    }
}