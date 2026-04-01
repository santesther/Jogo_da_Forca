package br.edu.iff.jogoforca.dominio.jogador.emmemoria;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.repository.RepositoryException;
import java.util.HashMap;
import java.util.Map;

public class MemoriaJogadorRepository implements JogadorRepository {

    private static MemoriaJogadorRepository soleInstance;
    private Map<Long, Jogador> pool;
    private long idCounter;

    private MemoriaJogadorRepository() {
        this.pool = new HashMap<>();
        this.idCounter = 0;
    }

    public static MemoriaJogadorRepository getSoleInstance() {
        if (soleInstance == null) {
            soleInstance = new MemoriaJogadorRepository();
        }
        return soleInstance;
    }

    @Override
    public long getProximoId() {
        return ++this.idCounter;
    }

    @Override
    public void inserir(Jogador jogador) throws RepositoryException {
        if (pool.containsKey(jogador.getId())) {
            throw new RepositoryException("Jogador já existe.");
        }
        pool.put(jogador.getId(), jogador);
    }

    @Override
    public void atualizar(Jogador jogador) throws RepositoryException {
        if (pool.replace(jogador.getId(), jogador) == null) {
            throw new RepositoryException("Jogador não encontrado para atualizar.");
        }
    }

    @Override
    public void remover(Jogador jogador) throws RepositoryException {
        if (pool.remove(jogador.getId()) == null) {
            throw new RepositoryException("Jogador não encontrado para remover.");
        }
    }

    @Override
    public Jogador getPorId(long id) {
        Jogador jogador = pool.get(id);
        if (jogador == null) {
            throw new RuntimeException("Jogador com id " + id + " não encontrado.");
        }
        return jogador;
    }

    @Override
    public Jogador getPorNome(String nome) {
        return pool.values().stream()
                .filter(jogador -> jogador.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }
}
