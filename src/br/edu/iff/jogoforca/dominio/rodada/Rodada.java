package br.edu.iff.jogoforca.dominio.rodada;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.palavra.Palavra;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.dominio.ObjetoDominioImpl;
import br.edu.iff.jogoforca.dominio.boneco.Boneco;
import br.edu.iff.jogoforca.dominio.boneco.BonecoFactory;
import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import java.util.ArrayList;

public class Rodada extends ObjetoDominioImpl {

    private static int maxPalavras = 3;
    private static int maxErros = 10;
    private static int pontosQuandoDescobreTodasAsPalavras = 100;
    private static int pontosPorLetraEncoberta = 15;
    private static BonecoFactory bonecoFactory;

    private Jogador jogador;
    private Boneco boneco;
    private Item[] itens;
    private ArrayList<Letra> erradas;

    public static int getMaxPalavras() { return maxPalavras; }
    public static void setMaxPalavras(int max) { maxPalavras = max; }
    public static int getMaxErros() { return maxErros; }
    public static void setMaxErros(int max) { maxErros = max; }
    public static int getPontosQuandoDescobreTodasAsPalavras() { return pontosQuandoDescobreTodasAsPalavras; }
    public static void setPontosQuandoDescobreTodasAsPalavras(int pontos) { pontosQuandoDescobreTodasAsPalavras = pontos; }
    public static int getPontosPorLetraEncoberta() { return pontosPorLetraEncoberta; }
    public static void setPontosPorLetraEncoberta(int pontos) { pontosPorLetraEncoberta = pontos; }
    public static BonecoFactory getBonecoFactory() { return bonecoFactory; }
    public static void setBonecoFactory(BonecoFactory factory) { bonecoFactory = factory; }

    public static Rodada criar(long id, Palavra[] palavras, Jogador jogador) {
        if (bonecoFactory == null) {
            throw new IllegalStateException("BonecoFactory não definida.");
        }
        return new Rodada(id, palavras, jogador);
    }

    public static Rodada reconstituir(long id, Item[] itens,
                                       Letra[] erradas, Jogador jogador) {
        if (bonecoFactory == null) {
            throw new IllegalStateException("BonecoFactory não definida.");
        }
        return new Rodada(id, itens, erradas, jogador);
    }

    private Rodada(long id, Palavra[] palavras, Jogador jogador) {
        super(id);
        this.jogador = jogador;
        this.erradas = new ArrayList<>();
        this.itens = new Item[palavras.length];
        for (int i = 0; i < palavras.length; i++) {
            this.itens[i] = Item.criar(i, palavras[i]);
        }
      
        Tema temaTeste = this.itens[0].getPalavra().getTema();
        for (Item item : this.itens) {
            if (item.getPalavra().getTema() != temaTeste) {
                throw new RuntimeException("Todas as palavras devem ser do mesmo tema.");
            }
        }
        this.boneco = bonecoFactory.getBoneco();
    }

    private Rodada(long id, Item[] itens, Letra[] erradas, Jogador jogador) {
        super(id);
        this.jogador = jogador;
        this.itens = itens;
        this.erradas = new ArrayList<>();
        for (Letra l : erradas) {
            this.erradas.add(l);
        }
        
        Tema temaTeste = this.itens[0].getPalavra().getTema();
        for (Item item : this.itens) {
            if (item.getPalavra().getTema() != temaTeste) {
                throw new RuntimeException("Todas as palavras devem ser do mesmo tema.");
            }
        }
        this.boneco = bonecoFactory.getBoneco();
    }

    public Jogador getJogador() { return this.jogador; }

    public int getNumPalavras() { return this.itens.length; }

    public Tema getTema() {
        return this.itens[0].getPalavra().getTema();
    }

    public Palavra[] getPalavras() {
        Palavra[] palavras = new Palavra[this.itens.length];
        for (int i = 0; i < this.itens.length; i++) {
            palavras[i] = this.itens[i].getPalavra();
        }
        return palavras;
    }

    public void tentar(char codigo) {
        if (this.descobriu() || this.getQtdeTentativasRestantes() == 0) {
            throw new RuntimeException("Rodada já encerrou.");
        }
        if (this.arriscou()) {
            throw new RuntimeException("Você já arriscou, não pode mais tentar letras.");
        }
        boolean acertou = false;
        for (Item item : this.itens) {
            if (item.tentar(codigo)) acertou = true;
        }
        if (!acertou) {
            this.erradas.add(Palavra.getLetraFactory().getLetra(codigo));
        }
        if (this.encerrou()) {
            this.jogador.atualizarPontuacao(this.calcularPontos());
        }
    }

    public void arriscar(String[] palavras) {
        if (this.descobriu() || this.getQtdeTentativasRestantes() == 0) {
            throw new RuntimeException("Rodada já encerrou.");
        }
        if (this.arriscou()) {
            throw new RuntimeException("Você já arriscou nesta rodada.");
        }
        for (int i = 0; i < this.itens.length; i++) {
            this.itens[i].arriscar(palavras[i]);
        }

        if (this.encerrou()) {
            int pontos = this.calcularPontos();
            this.jogador.atualizarPontuacao(pontos);
        }
    }

    public boolean encerrou() {
        return this.arriscou() || this.descobriu() || this.getQtdeTentativasRestantes() == 0;
    }

    public boolean arriscou() {
        
        for (Item item : this.itens) {
            if (item.arriscou()) return true;
        }
        return false;
    }

    public boolean descobriu() {
        for (Item item : this.itens) {
            if (!item.descobriu()) return false;
        }
        return true;
    }

    public int calcularPontos() {
        if (!this.descobriu()) return 0;
        int pontos = pontosQuandoDescobreTodasAsPalavras;
        for (Item item : this.itens) {
            pontos += item.calcularPontosLetrasEncobertas(pontosPorLetraEncoberta);
        }
        return pontos;
    }

    public int getQtdeTentativasRestantes() {
        return maxErros - this.getQtdeErros();
    }

    public int getQtdeErros() {
        return this.erradas.size();
    }

    public int getQtdeAcertos() {
        return this.getCertas().length;
    }

    public int getQtdeTentativas() {
        return this.getTentativas().length;
    }

    public Letra[] getErradas() {
        return this.erradas.toArray(new Letra[0]);
    }

    public Letra[] getCertas() {
        ArrayList<Letra> certas = new ArrayList<>();
        for (Item item : this.itens) {
            for (Letra letra : item.getLetrasDescobertas()) {
                if (!certas.contains(letra)) {
                    certas.add(letra);
                }
            }
        }
        return certas.toArray(new Letra[0]);
    }

    public Letra[] getTentativas() {
        Letra[] certas = this.getCertas();
        Letra[] erradas = this.getErradas();
        Letra[] tentativas = new Letra[certas.length + erradas.length];
        System.arraycopy(certas, 0, tentativas, 0, certas.length);
        System.arraycopy(erradas, 0, tentativas, certas.length, erradas.length);
        return tentativas;
    }

    public void exibirItens(Object contexto) {
        for (Item item : this.itens) {
            item.exibir(contexto);
            System.out.println();
        }
    }

    public void exibirPalavras(Object contexto) {
        for (Item item : this.itens) {
            item.getPalavra().exibir(contexto);
            System.out.println();
        }
    }

    public void exibirBoneco(Object contexto) {
        this.boneco.exibir(contexto, this.erradas.size());
    }

    public void exibirLetrasErradas(Object contexto) {
        for (Letra letra : this.erradas) {
            letra.exibir(contexto);
            System.out.print(" ");
        }
    }
}