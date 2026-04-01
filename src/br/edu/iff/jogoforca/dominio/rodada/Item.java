package br.edu.iff.jogoforca.dominio.rodada;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.palavra.Palavra;
import br.edu.iff.dominio.ObjetoDominioImpl;
import java.util.ArrayList;

public class Item extends ObjetoDominioImpl {

    private boolean[] posicoesDescobertas;
    private String palavraArriscada = null;
    private Palavra palavra;

    static Item criar(int id, Palavra palavra) {
        return new Item(id, palavra);
    }

    public static Item reconstituir(int id, Palavra palavra,
                                     int[] posicoesDescobertas,
                                     String palavraArriscada) {
        return new Item(id, palavra, posicoesDescobertas, palavraArriscada);
    }

    private Item(int id, Palavra palavra) {
        super(id);
        this.palavra = palavra;
        this.posicoesDescobertas = new boolean[palavra.getTamanho()];
    }

    private Item(int id, Palavra palavra,
                  int[] posicoesDescobertas,
                  String palavraArriscada) {
        super(id);
        this.palavra = palavra;
        this.posicoesDescobertas = new boolean[palavra.getTamanho()];
        for (int posicao : posicoesDescobertas) {
            this.posicoesDescobertas[posicao] = true;
        }
        this.palavraArriscada = palavraArriscada;
    }

    public Palavra getPalavra() {
        return this.palavra;
    }

    public String getPalavraArriscada() {
        return this.palavraArriscada;
    }

    boolean tentar(char codigo) {
        int[] posicoes = this.palavra.tentar(codigo);
        for (int posicao : posicoes) {
            this.posicoesDescobertas[posicao] = true;
        }
        return posicoes.length > 0;
    }

    void arriscar(String palavra) {
        this.palavraArriscada = palavra;
    }

    public boolean arriscou() {
        return this.palavraArriscada != null;
    }

    public boolean acertou() {
        return this.palavra.comparar(this.palavraArriscada);
    }

    public boolean descobriu() {
        return this.acertou() || this.qtdeLetrasEncobertas() == 0;
    }

    public int qtdeLetrasEncobertas() {
        int count = 0;
        for (boolean pos : this.posicoesDescobertas) {
            if (!pos) count++;
        }
        return count;
    }

    public int calcularPontosLetrasEncobertas(int pontosPorLetraEncoberta) {
        return this.qtdeLetrasEncobertas() * pontosPorLetraEncoberta;
    }

    public Letra[] getLetrasDescobertas() {
        ArrayList<Letra> result = new ArrayList<>();
        for (int i = 0; i < this.palavra.getTamanho(); i++) {
            if (this.posicoesDescobertas[i]) {
                result.add(this.palavra.getLetra(i));
            }
        }
        return result.toArray(new Letra[0]);
    }

    public Letra[] getLetrasEncobertas() {
        ArrayList<Letra> result = new ArrayList<>();
        for (int i = 0; i < this.palavra.getTamanho(); i++) {
            if (!this.posicoesDescobertas[i]) {
                result.add(this.palavra.getLetra(i));
            }
        }
        return result.toArray(new Letra[0]);
    }

    public void exibir(Object contexto) {
        this.palavra.exibir(contexto, this.posicoesDescobertas);
    }
}