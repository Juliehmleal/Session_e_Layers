package br.edu.ifsp.web.service;

public class ErroInternoException extends RuntimeException {
    public ErroInternoException(String message, Throwable cause) {
        super(message, cause);
    }
}