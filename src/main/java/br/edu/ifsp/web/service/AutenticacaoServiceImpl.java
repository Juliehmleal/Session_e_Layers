package br.edu.ifsp.web.service;

import br.edu.ifsp.web.dao.ConnectionFactory;
import br.edu.ifsp.web.dao.UsuarioDAO;
import br.edu.ifsp.web.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.SQLException;

public class AutenticacaoServiceImpl implements AutenticacaoService {
    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public Usuario autenticar(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new AutenticacaoException("E-mail ou senha inválidos.");
        }

        try (Connection conn = connectionFactory.getConnection()) {
            Usuario usuario = usuarioDAO.buscarPorEmail(email.trim(), conn);

            if (usuario == null || !BCrypt.checkpw(senha, usuario.getSenhaHash())) {
                throw new AutenticacaoException("E-mail ou senha inválidos.");
            }

            usuario.setSenhaHash(null);
            return usuario;
        } catch (SQLException e) {
            throw new ErroInternoException("Erro ao autenticar usuário no banco.", e);
        }
    }
}