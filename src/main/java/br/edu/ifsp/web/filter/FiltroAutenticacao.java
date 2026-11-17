package br.edu.ifsp.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class FiltroAutenticacao implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // getSession(false) garante que nenhuma sessão seja criada para quem não está autenticado
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuarioLogado") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write("{\"erro\": \"Usuário não autenticado.\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}