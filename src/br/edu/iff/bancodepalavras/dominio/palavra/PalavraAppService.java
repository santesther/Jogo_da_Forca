package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.repository.RepositoryException;

public class PalavraAppService {
    private static PalavraAppService soleInstance;
    private TemaRepository temaRepository;
    private PalavraRepository palavraRepository;
    private PalavraFactory palavraFactory;

    private PalavraAppService(TemaRepository temaRepository, PalavraRepository palavraRepository, PalavraFactory palavraFactory) {
        this.temaRepository = temaRepository;
        this.palavraRepository = palavraRepository;
        this.palavraFactory = palavraFactory;
    }

    public static void createSoleInstance(TemaRepository temaRepository, PalavraRepository palavraRepository, PalavraFactory palavraFactory) {
        if (soleInstance == null) {
            soleInstance = new PalavraAppService(temaRepository, palavraRepository, palavraFactory);
        }
    }

    public static PalavraAppService getSoleInstance() {
        if (soleInstance == null) throw new RuntimeException("Chame createSoleInstance antes.");
        return soleInstance;
    }

    public boolean novaPalavra(String palavra, long idTema) {
        if (this.palavraRepository.getPalavra(palavra) != null) return true;
        Tema tema = this.temaRepository.getPorId(idTema);
        Palavra novaPalavra = this.palavraFactory.getPalavra(palavra, tema);
        try {
            this.palavraRepository.inserir(novaPalavra);
            return true;
        } catch (RepositoryException e) {
            return false;
        }
    }
}