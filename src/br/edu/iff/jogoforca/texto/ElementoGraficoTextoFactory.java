package br.edu.iff.jogoforca.texto;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.letra.texto.LetraTextoFactory;
import br.edu.iff.jogoforca.ElementoGraficoFactory;
import br.edu.iff.jogoforca.dominio.boneco.Boneco;
import br.edu.iff.jogoforca.dominio.boneco.texto.BonecoTextoFactory;

public class ElementoGraficoTextoFactory implements ElementoGraficoFactory {
    private static ElementoGraficoTextoFactory soleInstance;
    private LetraTextoFactory letraFactory;
    private BonecoTextoFactory bonecoFactory;

    private ElementoGraficoTextoFactory() {
        this.letraFactory = LetraTextoFactory.getSoleInstance();
        this.bonecoFactory = BonecoTextoFactory.getSoleInstance();
    }

    public static ElementoGraficoTextoFactory getSoleInstance() {
        if (soleInstance == null) soleInstance = new ElementoGraficoTextoFactory();
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