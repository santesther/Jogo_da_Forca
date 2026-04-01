package br.edu.iff.jogoforca.dominio.jogador;

import br.edu.iff.factory.EntityFactory;

public class JogadorFactoryImpl extends EntityFactory implements JogadorFactory {

    private static JogadorFactoryImpl soleInstance;

    private JogadorFactoryImpl(JogadorRepository repository) {
        super(repository);
    }

    public static void createSoleInstance(JogadorRepository repository) {
        if (soleInstance == null) {
            soleInstance = new JogadorFactoryImpl(repository);
        }
    }

    public static JogadorFactoryImpl getSoleInstance() {
        if (soleInstance == null) {
            throw new RuntimeException("Chame createSoleInstance antes de getSoleInstance.");
        }
        return soleInstance;
    }

    private JogadorRepository getJogadorRepository() {
        return (JogadorRepository) this.getRepository();
    }

    @Override
    public Jogador getJogador(String nome) {
        Jogador existente = this.getJogadorRepository().getPorNome(nome);
        if (existente != null) {
            return existente;
        }
        return Jogador.criar(this.getProximoId(), nome);
    }
}
