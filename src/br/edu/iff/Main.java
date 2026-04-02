package br.edu.iff;

import br.edu.iff.bancodepalavras.dominio.palavra.PalavraAppService;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.bancodepalavras.dominio.tema.TemaFactory;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.jogador.JogadorFactory;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.jogoforca.dominio.rodada.Rodada;
import br.edu.iff.jogoforca.dominio.rodada.RodadaAppService;
import br.edu.iff.repository.RepositoryException;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Aplicacao app = Aplicacao.getSoleInstance();

    public static void main(String[] args) {

        app.configurar();

        TemaFactory temaFactory = app.getTemaFactory();
        TemaRepository temaRepository = app.getRepositoryFactory().getTemaRepository();

        Tema animais = temaFactory.getTema("Animais");
        Tema frutas  = temaFactory.getTema("Frutas");
        Tema paises  = temaFactory.getTema("Paises");

        try {
            temaRepository.inserir(animais);
            temaRepository.inserir(frutas);
            temaRepository.inserir(paises);
        } catch (RepositoryException e) {
            System.err.println("Erro ao inserir tema: " + e.getMessage());
        }

        PalavraAppService palavraService = PalavraAppService.getSoleInstance();

        palavraService.novaPalavra("gato",     animais.getId());
        palavraService.novaPalavra("cachorro", animais.getId());
        palavraService.novaPalavra("elefante", animais.getId());
        palavraService.novaPalavra("leao",     animais.getId());

        palavraService.novaPalavra("manga",    frutas.getId());
        palavraService.novaPalavra("banana",   frutas.getId());
        palavraService.novaPalavra("abacaxi",  frutas.getId());
        palavraService.novaPalavra("laranja",  frutas.getId());

        palavraService.novaPalavra("brasil",   paises.getId());
        palavraService.novaPalavra("canada",   paises.getId());
        palavraService.novaPalavra("franca",   paises.getId());
        palavraService.novaPalavra("japao",    paises.getId());

        JogadorFactory jogadorFactory = app.getJogadorFactory();
        JogadorRepository jogadorRepository = app.getRepositoryFactory().getJogadorRepository();

        System.out.print("Digite seu nome: ");
        String nomeJogador = scanner.nextLine();

        Jogador jogador = jogadorFactory.getJogador(nomeJogador);

        try {
            jogadorRepository.inserir(jogador);
        } catch (RepositoryException e) {
            System.err.println("Erro ao inserir jogador: " + e.getMessage());
        }

        RodadaAppService rodadaService = RodadaAppService.getSoleInstance();
        String jogarNovamente;

        do {
            jogarRodada(jogador, rodadaService);

            System.out.print("\nDeseja jogar novamente? (sim/nao): ");
            jogarNovamente = scanner.nextLine().trim().toLowerCase();

        } while (jogarNovamente.equals("sim"));

        System.out.println("\nObrigado por jogar, " + jogador.getNome() + "!");
        System.out.println("Pontuacao final: " + jogador.getPontuacao());
    }

    private static void jogarRodada(Jogador jogador, RodadaAppService rodadaService) {

        Rodada rodada = rodadaService.novaRodada(jogador.getId());

        System.out.println("\n=== Nova Rodada ===");
        System.out.println("Tema: " + rodada.getTema().getNome());

        while (!rodada.encerrou()) {

            System.out.println("\n--- Palavras ---");
            rodada.exibirItens(null);

            System.out.println("\n--- Boneco ---");
            rodada.exibirBoneco(null);

            System.out.print("\nLetras erradas: ");
            rodada.exibirLetrasErradas(null);

            System.out.println("\nTentativas restantes: "
                    + rodada.getQtdeTentativasRestantes());

            System.out.println("\n(1) Tentar letra");
            System.out.println("(2) Arriscar palavras");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine().trim();

            switch (escolha) {
                case "1":
                    System.out.print("Digite a letra: ");
                    String entrada = scanner.nextLine().trim();
                    if (!entrada.isEmpty()) {
                        rodada.tentar(entrada.charAt(0));
                    } else {
                        System.out.println("Entrada invalida.");
                    }
                    break;

                case "2":
                    String[] chutes = new String[rodada.getNumPalavras()];
                    for (int i = 0; i < chutes.length; i++) {
                        System.out.print("Palavra " + (i + 1) + ": ");
                        chutes[i] = scanner.nextLine().trim();
                    }
                    rodada.arriscar(chutes);
                    break;

                default:
                    System.out.println("Opcao invalida, tente novamente.");
            }
        }

        System.out.println("\n=== Resultado ===");
        if (rodada.descobriu()) {
            System.out.println("Voce ganhou!");
            System.out.println("Pontos desta rodada: " + rodada.calcularPontos());
        } else {
            System.out.println("Voce perdeu!");
            System.out.print("As palavras eram: ");
            rodada.exibirPalavras(null);
        }

        System.out.println("Pontuacao acumulada: " + jogador.getPontuacao());

        boolean salvo = rodadaService.salvarRodada(rodada);
        if (!salvo) {
            System.err.println("Erro ao salvar a rodada.");
        }
    }
}