package br.edu.ifsp.web.command;

import br.edu.ifsp.web.service.TarefaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExcluirTarefaCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, TarefaService service) throws Exception {
        service.excluir(extrairId(req), getUsuario(req));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}