package br.edu.iff.jogoforca.dominio.rodada.emmemoria;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.rodada.Rodada;
import br.edu.iff.jogoforca.dominio.rodada.RodadaRepository;
import br.edu.iff.repository.RepositoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoriaRodadaRepository implements RodadaRepository {

    private static MemoriaRodadaRepository soleInstance;
    private Map<Long, Rodada> pool;
    private long idCounter;

    private MemoriaRodadaRepository() {
        this.pool = new HashMap<>();
        this.idCounter = 0;
    }

    public static MemoriaRodadaRepository getSoleInstance() {
        if (soleInstance == null) {
            soleInstance = new MemoriaRodadaRepository();
        }
        return soleInstance;
    }

    @Override
    public long getProximoId() {
        return ++this.idCounter;
    }

    @Override
    public void inserir(Rodada rodada) throws RepositoryException {
        if (pool.containsKey(rodada.getId())) {
            throw new RepositoryException("Rodada já existe.");
        }
        pool.put(rodada.getId(), rodada);
    }

    @Override
    public void atualizar(Rodada rodada) throws RepositoryException {
        if (pool.replace(rodada.getId(), rodada) == null) {
            throw new RepositoryException("Rodada não encontrada para atualizar.");
        }
    }

    @Override
    public void remover(Rodada rodada) throws RepositoryException {
        if (pool.remove(rodada.getId()) == null) {
            throw new RepositoryException("Rodada não encontrada para remover.");
        }
    }

    @Override
    public Rodada getPorId(long id) {
        return pool.get(id);
    }

    @Override
    public Rodada[] getPorJogador(Jogador jogador) {
        List<Rodada> result = new ArrayList<>();
        for (Rodada r : pool.values()) {
            if (r.getJogador().getId() == jogador.getId()) {
                result.add(r);
            }
        }
        return result.toArray(new Rodada[0]);
    }
}