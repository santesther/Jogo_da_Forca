package br.edu.iff.jogoforca.dominio.jogador;

import br.edu.iff.dominio.ObjetoDominioImpl;

public class Jogador extends ObjetoDominioImpl {
    private String nome;
    private int pontuacao;

    private Jogador(long id, String nome) {
        super(id);
        this.nome = nome;
        this.pontuacao = 0;
    }

    private Jogador(long id, String nome, int pontuacao) {
        super(id);
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    public static Jogador criar(long id, String nome) {
        return new Jogador(id, nome);
    }

    public static Jogador reconstituir(long id, String nome, int pontuacao) {
        return new Jogador(id, nome, pontuacao);
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontuacao() {
        return this.pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    @Override
    public String toString() {
        return "Jogador{" +
                "id=" + this.getId() +
                ", nome='" + nome + '\'' +
                ", pontuacao=" + pontuacao +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Jogador)) return false;
        Jogador outro = (Jogador) o;
        return this.getId() == outro.getId() && this.nome.equals(outro.nome);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.getId()) + this.nome.hashCode();
    }
}
