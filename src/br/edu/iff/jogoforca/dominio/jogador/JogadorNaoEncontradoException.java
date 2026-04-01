package br.edu.iff.jogoforca.dominio.jogador;

public class JogadorNaoEncontradoException extends RuntimeException {
    
    public JogadorNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
