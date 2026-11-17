package br.edu.ifsp.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FiltroTempo implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long inicio = System.currentTimeMillis();
        HttpServletRequest req = (HttpServletRequest) request;

        try {
            chain.doFilter(request, response);
        } finally {
            long duracao = System.currentTimeMillis() - inicio;
            System.out.println("[FiltroTempo] " + req.getMethod() + " " + req.getRequestURI() + " - " + duracao + " ms");
        }
    }
}