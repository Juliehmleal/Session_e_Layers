package br.edu.ifsp.web.command;

import br.edu.ifsp.web.model.Usuario;
import br.edu.ifsp.web.service.TarefaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
    void execute(HttpServletRequest req, HttpServletResponse resp, TarefaService service) throws Exception;

    default Usuario getUsuario(HttpServletRequest req) {
        return (Usuario) req.getSession(false).getAttribute("usuarioLogado");
    }

    default int extrairId(HttpServletRequest req) {
        return Integer.parseInt(req.getPathInfo().substring(1));
    }
}