package br.edu.iff.bancodepalavras.dominio.letra;

public abstract class LetraFactoryImpl implements LetraFactory {
    private Letra[] pool = new Letra[26];
    private Letra encoberta;

    protected abstract Letra criarLetra(char codigo);

    @Override
    public final Letra getLetra(char codigo) {
        int indice = Character.toLowerCase(codigo) - 'a';
        if (pool[indice] == null) {
            pool[indice] = criarLetra(codigo);
        }
        return pool[indice];
    }

    @Override
    public final Letra getLetraEncoberta() {
        if (encoberta == null) {
            encoberta = criarLetra('_');
        }
        return encoberta;
    }
}