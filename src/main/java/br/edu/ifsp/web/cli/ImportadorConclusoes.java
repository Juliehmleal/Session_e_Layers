package br.edu.ifsp.web.cli;

import br.edu.ifsp.web.model.Usuario;
import br.edu.ifsp.web.service.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImportadorConclusoes {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Uso: ImportadorConclusoes <caminho_arquivo> <email> <senha>");
            System.exit(1);
        }

        String caminhoArquivo = args[0];
        String email = args[1];
        String senha = args[2];

        AutenticacaoService authService = new AutenticacaoServiceImpl();
        TarefaService tarefaService = new TarefaServiceImpl();

        Usuario usuario;
        try {
            usuario = authService.autenticar(email, senha);
        } catch (AutenticacaoException e) {
            System.err.println("Falha na autenticação: " + e.getMessage());
            System.exit(1);
            return;
        }

        try {
            List<String> linhas = Files.readAllLines(Path.of(caminhoArquivo));
            for (String linha : linhas) {
                if (linha.isBlank()) continue;
                try {
                    int id = Integer.parseInt(linha.trim());
                    tarefaService.concluir(id, usuario);
                    System.out.println("ID " + id + ": concluída");
                } catch (RecursoNaoEncontradoException e) {
                    System.out.println("ID " + linha.trim() + ": não encontrada");
                } catch (RegraNegocioException e) {
                    System.out.println("ID " + linha.trim() + ": já estava concluída");
                } catch (Exception e) {
                    System.out.println("ID " + linha.trim() + ": erro (" + e.getMessage() + ")");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            System.exit(1);
        }
    }
}