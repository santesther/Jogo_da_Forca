package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.factory.EntityFactory;

public class PalavraFactoryImpl extends EntityFactory implements PalavraFactory {

    private static PalavraFactoryImpl soleInstance;

    private PalavraFactoryImpl(PalavraRepository repository) {
        super(repository);
    }

    public static void createSoleInstance(PalavraRepository repository) {
        if (soleInstance == null) {
            soleInstance = new PalavraFactoryImpl(repository);
        }
    }

    public static PalavraFactoryImpl getSoleInstance() {
        if (soleInstance == null) {
            throw new RuntimeException("Chame createSoleInstance antes de getSoleInstance.");
        }
        return soleInstance;
    }

    private PalavraRepository getPalavraRepository() {
        return (PalavraRepository) this.getRepository();
    }

    @Override
    public Palavra getPalavra(String palavra, Tema tema) {
        Palavra existente = this.getPalavraRepository().getPalavra(palavra);
        if (existente != null) {
            return existente;
        }
        return Palavra.criar(this.getProximoId(), palavra, tema);
    }
}