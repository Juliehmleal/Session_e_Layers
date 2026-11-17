package br.edu.ifsp.web.servlet;

import br.edu.ifsp.web.command.*;
import br.edu.ifsp.web.service.*;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/tarefas/*")
public class TaskServlet extends HttpServlet {
    private final TarefaService tarefaService = new TarefaServiceImpl();
    private final Gson gson = new Gson();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            Command cmd = resolverCommand(req);
            cmd.execute(req, resp, tarefaService);
        } catch (DadosInvalidosException | NumberFormatException e) {
            enviarErro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (RecursoNaoEncontradoException e) {
            enviarErro(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (RegraNegocioException e) {
            enviarErro(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (Exception e) {
            enviarErro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno no servidor.");
        }
    }

    private Command resolverCommand(HttpServletRequest req) {
        String method = req.getMethod();
        String path = req.getPathInfo();
        boolean temId = path != null && path.length() > 1;

        if ("GET".equals(method) && !temId) return new ListarTarefasCommand();
        if ("GET".equals(method) && temId)  return new BuscarTarefaCommand();
        if ("POST".equals(method) && !temId) return new CriarTarefaCommand();
        if ("PUT".equals(method) && temId)  return new AtualizarTarefaCommand();
        if ("PATCH".equals(method) && temId) return new ConcluirTarefaCommand();
        if ("DELETE".equals(method) && temId) return new ExcluirTarefaCommand();
        throw new RecursoNaoEncontradoException("Rota não encontrada.");
    }

    private void enviarErro(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write(gson.toJson(Map.of("erro", msg != null ? msg : "Erro na requisição.")));
    }
}