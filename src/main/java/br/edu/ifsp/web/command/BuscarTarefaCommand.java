package br.edu.ifsp.web.command;

import br.edu.ifsp.web.service.TarefaService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BuscarTarefaCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, TarefaService service) throws Exception {
        var tarefa = service.buscar(extrairId(req), getUsuario(req));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(new Gson().toJson(tarefa));
    }
}