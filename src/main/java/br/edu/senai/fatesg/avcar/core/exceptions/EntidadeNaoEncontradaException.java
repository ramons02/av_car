package br.edu.senai.fatesg.avcar.core.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(entidade + " com id " + id + " não encontrado(a)");
    }
}
