package br.edu.iff.jogoforca.dominio.rodada.sorteio;

import br.edu.iff.bancodepalavras.dominio.palavra.Palavra;
import br.edu.iff.bancodepalavras.dominio.palavra.PalavraRepository;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.rodada.Rodada;
import br.edu.iff.jogoforca.dominio.rodada.RodadaFactoryImpl;
import br.edu.iff.jogoforca.dominio.rodada.RodadaRepository;
import java.util.Arrays;
import java.util.Random;

public class RodadaSorteioFactory extends RodadaFactoryImpl {

    private static RodadaSorteioFactory soleInstance;

    private RodadaSorteioFactory(RodadaRepository repository,
                                   TemaRepository temaRepository,
                                   PalavraRepository palavraRepository) {
        super(repository, temaRepository, palavraRepository);
    }

    public static void createSoleInstance(RodadaRepository repository,
                                           TemaRepository temaRepository,
                                           PalavraRepository palavraRepository) {
        if (soleInstance == null) {
            soleInstance = new RodadaSorteioFactory(repository, temaRepository, palavraRepository);
        }
    }

    public static RodadaSorteioFactory getSoleInstance() {
        if (soleInstance == null) {
            throw new RuntimeException("Chame createSoleInstance antes de getSoleInstance.");
        }
        return soleInstance;
    }

    @Override
    public Rodada getRodada(Jogador jogador) {
        Random random = new Random();

        Tema[] temas = this.getTemaRepository().getTodos();
        if (temas.length == 0) {
            throw new RuntimeException("Nenhum tema cadastrado.");
        }
        Tema temaSorteado = temas[random.nextInt(temas.length)];

        
        int qtdPalavras = random.nextInt(Rodada.getMaxPalavras()) + 1;

        
        Palavra[] palavrasDoTema = this.getPalavraRepository().getPorTema(temaSorteado);
        if (palavrasDoTema.length < qtdPalavras) {
            throw new RuntimeException("Palavras insuficientes para o tema: "
                    + temaSorteado.getNome());
        }

        
        Palavra[] palavrasSorteadas = new Palavra[qtdPalavras];
        for (int i = 0; i < qtdPalavras; i++) {
            Palavra sorteada;
            do {
                sorteada = palavrasDoTema[random.nextInt(palavrasDoTema.length)];
            } while (Arrays.asList(palavrasSorteadas).contains(sorteada));
            palavrasSorteadas[i] = sorteada;
        }

        return Rodada.criar(this.getRodadaRepository().getProximoId(),
                palavrasSorteadas, jogador);
    }
}