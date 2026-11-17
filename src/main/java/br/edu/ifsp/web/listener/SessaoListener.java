package br.edu.ifsp.web.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class SessaoListener implements HttpSessionListener {
    private static final AtomicInteger sessoesAtivas = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int total = sessoesAtivas.incrementAndGet();
        System.out.println("[SessaoListener] Sessão criada (" + se.getSession().getId() + "). Sessões ativas: " + total);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int total = sessoesAtivas.decrementAndGet();
        System.out.println("[SessaoListener] Sessão destruída (" + se.getSession().getId() + "). Sessões ativas: " + total);
    }

    public static int getSessoesAtivas() {
        return sessoesAtivas.get();
    }
}