package br.edu.ifsp.web.command;

import br.edu.ifsp.web.service.TarefaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ConcluirTarefaCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, TarefaService service) throws Exception {
        service.concluir(extrairId(req), getUsuario(req));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}