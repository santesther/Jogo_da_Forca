package br.edu.iff.bancodepalavras.dominio.palavra.emmemoria;

import br.edu.iff.bancodepalavras.dominio.palavra.Palavra;
import br.edu.iff.bancodepalavras.dominio.palavra.PalavraRepository;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.repository.RepositoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoriaPalavraRepository implements PalavraRepository {

    private static MemoriaPalavraRepository soleInstance;
    private Map<Long, Palavra> pool;
    private long idCounter;

    private MemoriaPalavraRepository() {
        this.pool = new HashMap<>();
        this.idCounter = 0;
    }

    public static MemoriaPalavraRepository getSoleInstance() {
        if (soleInstance == null) {
            soleInstance = new MemoriaPalavraRepository();
        }
        return soleInstance;
    }

    @Override
    public long getProximoId() {
        return ++this.idCounter;
    }

    @Override
    public void inserir(Palavra palavra) throws RepositoryException {
        if (pool.containsKey(palavra.getId())) {
            throw new RepositoryException("Palavra já existe.");
        }
        pool.put(palavra.getId(), palavra);
    }

    @Override
    public void atualizar(Palavra palavra) throws RepositoryException {
        if (pool.replace(palavra.getId(), palavra) == null) {
            throw new RepositoryException("Palavra não encontrada para atualizar.");
        }
    }

    @Override
    public void remover(Palavra palavra) throws RepositoryException {
        if (pool.remove(palavra.getId()) == null) {
            throw new RepositoryException("Palavra não encontrada para remover.");
        }
    }

    @Override
    public Palavra getPorId(long id) {
        return pool.get(id);
    }

    @Override
    public Palavra getPalavra(String palavra) {
        for (Palavra p : pool.values()) {
            if (p.toString().equalsIgnoreCase(palavra)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Palavra[] getTodas() {
        return pool.values().toArray(new Palavra[0]);
    }

    @Override
    public Palavra[] getPorTema(Tema tema) {
        List<Palavra> result = new ArrayList<>();
        for (Palavra p : pool.values()) {
            if (p.getTema().getId() == tema.getId()) {
                result.add(p);
            }
        }
        return result.toArray(new Palavra[0]);
    }
}