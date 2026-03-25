package br.edu.iff.bancodepalavras.dominio.tema.emmemoria;

import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.repository.RepositoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoriaTemaRepository implements TemaRepository {

    private static MemoriaTemaRepository soleInstance;
    private Map<Long, Tema> pool;
    private long idCounter;

    private MemoriaTemaRepository() {
        this.pool = new HashMap<>();
        this.idCounter = 0;
    }

    public static MemoriaTemaRepository getSoleInstance() {
        if (soleInstance == null) {
            soleInstance = new MemoriaTemaRepository();
        }
        return soleInstance;
    }

    @Override
    public long getProximoId() {
        return ++this.idCounter;
    }

    @Override
    public void inserir(Tema tema) throws RepositoryException {
        if (pool.containsKey(tema.getId())) {
            throw new RepositoryException("Tema já existe.");
        }
        pool.put(tema.getId(), tema);
    }

    @Override
    public void atualizar(Tema tema) throws RepositoryException {
        if (pool.replace(tema.getId(), tema) == null) {
            throw new RepositoryException("Tema não encontrado para atualizar.");
        }
    }

    @Override
    public void remover(Tema tema) throws RepositoryException {
        if (pool.remove(tema.getId()) == null) {
            throw new RepositoryException("Tema não encontrado para remover.");
        }
    }

    @Override
    public Tema getPorId(long id) {
        return pool.get(id);
    }

    @Override
    public Tema[] getPorNome(String nome) {
        List<Tema> result = new ArrayList<>();
        for (Tema tema : pool.values()) {
            if (tema.getNome().equals(nome)) {
                result.add(tema);
            }
        }
        return result.toArray(new Tema[0]);
    }

    @Override
    public Tema[] getTodos() {
        return pool.values().toArray(new Tema[0]);
    }
}