package br.edu.ifsp.web.servlet;

import br.edu.ifsp.web.listener.SessaoListener;
import br.edu.ifsp.web.model.Usuario;
import br.edu.ifsp.web.service.AutenticacaoException;
import br.edu.ifsp.web.service.AutenticacaoService;
import br.edu.ifsp.web.service.AutenticacaoServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
    private final AutenticacaoService authService = new AutenticacaoServiceImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");

        if ("/login".equals(path)) {
            try {
                JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
                String email = (body != null && body.has("email")) ? body.get("email").getAsString() : null;
                String senha = (body != null && body.has("senha")) ? body.get("senha").getAsString() : null;

                Usuario usuario = authService.autenticar(email, senha);

                // Se já havia uma sessão anterior, invalida antes de criar a nova
                HttpSession sessaoAntiga = req.getSession(false);
                if (sessaoAntiga != null) {
                    sessaoAntiga.invalidate();
                }

                HttpSession novaSessao = req.getSession(true);
                req.changeSessionId(); // Garante que o identificador da sessão mude após login bem-sucedido
                novaSessao.setAttribute("usuarioLogado", usuario);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(usuario)); // senhaHash é transient, nunca vai no JSON
            } catch (AutenticacaoException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write(gson.toJson(Map.of("erro", e.getMessage())));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(Map.of("erro", "Erro interno ao autenticar.")));
            }
        } else if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(gson.toJson(Map.of("erro", "Rota não encontrada.")));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        if ("/me".equals(req.getPathInfo())) {
            HttpSession session = req.getSession(false);
            Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

            if (usuario == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write(gson.toJson(Map.of("erro", "Usuário não autenticado.")));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(Map.of(
                    "usuario", usuario,
                    "sessoesAtivas", SessaoListener.getSessoesAtivas()
            )));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(gson.toJson(Map.of("erro", "Rota não encontrada.")));
        }
    }
}