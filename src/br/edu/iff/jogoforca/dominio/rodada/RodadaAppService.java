package br.edu.iff.jogoforca.dominio.rodada;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.jogador.JogadorNaoEncontradoException;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.repository.RepositoryException;

public class RodadaAppService {

    private static RodadaAppService soleInstance;
    private RodadaFactory rodadaFactory;
    private RodadaRepository rodadaRepository;
    private JogadorRepository jogadorRepository;

    private RodadaAppService(RodadaFactory rodadaFactory,
                              RodadaRepository rodadaRepository,
                              JogadorRepository jogadorRepository) {
        this.rodadaFactory = rodadaFactory;
        this.rodadaRepository = rodadaRepository;
        this.jogadorRepository = jogadorRepository;
    }

    public static void createSoleInstance(RodadaFactory rodadaFactory,
                                           RodadaRepository rodadaRepository,
                                           JogadorRepository jogadorRepository) {
        if (soleInstance == null) {
            soleInstance = new RodadaAppService(rodadaFactory,
                    rodadaRepository, jogadorRepository);
        }
    }

    public static RodadaAppService getSoleInstance() {
        if (soleInstance == null) {
            throw new RuntimeException("Chame createSoleInstance antes de getSoleInstance.");
        }
        return soleInstance;
    }

    
    public Rodada novaRodada(long idJogador) {
        Jogador jogador = this.jogadorRepository.getPorId(idJogador);
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado para o id: " + idJogador);
        }
        return this.rodadaFactory.getRodada(jogador);
    }

    
    public Rodada novaRodada(String nomeJogador) throws JogadorNaoEncontradoException {
        Jogador jogador = this.jogadorRepository.getPorNome(nomeJogador);
        if (jogador == null) {
            throw new JogadorNaoEncontradoException(nomeJogador);
        }
        return this.rodadaFactory.getRodada(jogador);
    }

    public boolean salvarRodada(Rodada rodada) {
        try {
            this.rodadaRepository.inserir(rodada);
            return true;
        } catch (RepositoryException e) {
            return false;
        }
    }
}