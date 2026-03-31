package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.letra.LetraFactory;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.dominio.ObjetoDominioImpl;

public class Palavra extends ObjetoDominioImpl {
    private Tema tema;
    private Letra[] letras;
    private Letra encoberta;
    private static LetraFactory letraFactory;

    public static void setLetraFactory(LetraFactory factory) {
        letraFactory = factory;
    }

    public static LetraFactory getLetraFactory() {
        return letraFactory;
    }

    private Palavra(long id, String palavra, Tema tema) {
        super(id);
        this.tema = tema;
        this.encoberta = letraFactory.getLetraEncoberta();
        this.letras = new Letra[palavra.length()];
        for (int i = 0; i < palavra.length(); i++) {
            this.letras[i] = letraFactory.getLetra(palavra.charAt(i));
        }
    }

    public static Palavra criar(long id, String palavra, Tema tema) {
        if (letraFactory == null) throw new IllegalStateException("LetraFactory não definida.");
        return new Palavra(id, palavra, tema);
    }

    public static Palavra reconstituir(long id, String palavra, Tema tema) {
        if (letraFactory == null) throw new IllegalStateException("LetraFactory não definida.");
        return new Palavra(id, palavra, tema);
    }

    public int[] tentar(char codigo) {
        int[] temp = new int[this.letras.length];
        int count = 0;
        for (int i = 0; i < this.letras.length; i++) {
            if (this.letras[i].getCodigo() == codigo) {
                temp[count++] = i;
            }
        }
        int[] resultado = new int[count];
        System.arraycopy(temp, 0, resultado, 0, count);
        return resultado; // nunca null, pode ser vazio
    }

    public boolean comparar(String palavra) {
        if (palavra == null || palavra.length() != this.getTamanho()) return false;
        for (int i = 0; i < this.getTamanho(); i++) {
            if (this.letras[i].getCodigo() != palavra.charAt(i)) return false;
        }
        return true;
    }

    public Letra getLetra(int posicao) { return this.letras[posicao]; }
    public Letra[] getLetras() { return this.letras.clone(); }
    public Tema getTema() { return this.tema; }
    public int getTamanho() { return this.letras.length; }

    public void exibir(Object contexto, boolean[] posicoes) {
        for (int i = 0; i < this.getTamanho(); i++) {
            if (posicoes[i]) this.letras[i].exibir(contexto);
            else this.encoberta.exibir(contexto);
        }
    }

    public void exibir(Object contexto) {
        for (Letra l : letras) l.exibir(contexto);
    }
}