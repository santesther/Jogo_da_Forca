package br.edu.iff.jogoforca.imagem;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.letra.imagem.LetraImagemFactory;
import br.edu.iff.jogoforca.ElementoGraficoFactory;
import br.edu.iff.jogoforca.dominio.boneco.Boneco;
import br.edu.iff.jogoforca.dominio.boneco.imagem.BonecoImagemFactory;

public class ElementoGraficoImagemFactory implements ElementoGraficoFactory {
    private static ElementoGraficoImagemFactory soleInstance;
    private LetraImagemFactory letraFactory;
    private BonecoImagemFactory bonecoFactory;

    private ElementoGraficoImagemFactory() {
        this.letraFactory = LetraImagemFactory.getSoleInstance();
        this.bonecoFactory = BonecoImagemFactory.getSoleInstance();
    }

    public static ElementoGraficoImagemFactory getSoleInstance() {
        if (soleInstance == null) soleInstance = new ElementoGraficoImagemFactory();
        return soleInstance;
    }

    @Override
    public Letra getLetra(char codigo) {
        return this.letraFactory.getLetra(codigo);
    }

    @Override
    public Letra getLetraEncoberta() {
        return this.letraFactory.getLetraEncoberta();
    }

    @Override
    public Boneco getBoneco() {
        return this.bonecoFactory.getBoneco();
    }
}
