package br.edu.ifsp.web.command;

import br.edu.ifsp.web.service.TarefaService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AtualizarTarefaCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, TarefaService service) throws Exception {
        Gson gson = new Gson();
        JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
        String titulo = (body != null && body.has("titulo")) ? body.get("titulo").getAsString() : null;

        var atualizada = service.atualizar(extrairId(req), titulo, getUsuario(req));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(atualizada));
    }
}