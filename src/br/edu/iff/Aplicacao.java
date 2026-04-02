package br.edu.iff;

import br.edu.iff.bancodepalavras.dominio.letra.LetraFactory;
import br.edu.iff.bancodepalavras.dominio.palavra.Palavra;
import br.edu.iff.bancodepalavras.dominio.palavra.PalavraAppService;
import br.edu.iff.bancodepalavras.dominio.palavra.PalavraFactory;
import br.edu.iff.bancodepalavras.dominio.palavra.PalavraFactoryImpl;
import br.edu.iff.bancodepalavras.dominio.tema.TemaFactory;
import br.edu.iff.bancodepalavras.dominio.tema.TemaFactoryImpl;
import br.edu.iff.jogoforca.ElementoGraficoFactory;
import br.edu.iff.jogoforca.RepositoryFactory;
import br.edu.iff.jogoforca.dominio.boneco.BonecoFactory;
import br.edu.iff.jogoforca.dominio.jogador.JogadorFactory;
import br.edu.iff.jogoforca.dominio.jogador.JogadorFactoryImpl;
import br.edu.iff.jogoforca.dominio.rodada.Rodada;
import br.edu.iff.jogoforca.dominio.rodada.RodadaAppService;
import br.edu.iff.jogoforca.dominio.rodada.RodadaFactory;
import br.edu.iff.jogoforca.dominio.rodada.sorteio.RodadaSorteioFactory;
import br.edu.iff.jogoforca.embdr.BDRRepositoryFactory;
import br.edu.iff.jogoforca.emmemoria.MemoriaRepositoryFactory;
import br.edu.iff.jogoforca.imagem.ElementoGraficoImagemFactory;
import br.edu.iff.jogoforca.texto.ElementoGraficoTextoFactory;

public class Aplicacao {

    private static final String[] TIPOS_REPOSITORY_FACTORY = {"memoria", "relacional"};
    private static final String[] TIPOS_ELEMENTO_GRAFICO_FACTORY = {"texto", "imagem"};
    private static final String[] TIPOS_RODADA_FACTORY = {"sorteio"};

    private static Aplicacao soleInstance;

    private String tipoRepositoryFactory = TIPOS_REPOSITORY_FACTORY[0];
    private String tipoElementoGraficoFactory = TIPOS_ELEMENTO_GRAFICO_FACTORY[0];
    private String tipoRodadaFactory = TIPOS_RODADA_FACTORY[0];

    private Aplicacao() {}

    public static Aplicacao getSoleInstance() {
        if (soleInstance == null) {
            soleInstance = new Aplicacao();
        }
        return soleInstance;
    }

    public void configurar() {
        RodadaSorteioFactory.createSoleInstance(
            this.getRepositoryFactory().getRodadaRepository(),
            this.getRepositoryFactory().getTemaRepository(),
            this.getRepositoryFactory().getPalavraRepository()
        );
        TemaFactoryImpl.createSoleInstance(
            this.getRepositoryFactory().getTemaRepository()
        );
        PalavraFactoryImpl.createSoleInstance(
            this.getRepositoryFactory().getPalavraRepository()
        );
        JogadorFactoryImpl.createSoleInstance(
            this.getRepositoryFactory().getJogadorRepository()
        );
        PalavraAppService.createSoleInstance(
            this.getRepositoryFactory().getTemaRepository(),
            this.getRepositoryFactory().getPalavraRepository(),
            this.getPalavraFactory()
        );
        RodadaAppService.createSoleInstance(
            this.getRodadaFactory(),
            this.getRepositoryFactory().getRodadaRepository(),
            this.getRepositoryFactory().getJogadorRepository()
        );

        Palavra.setLetraFactory(this.getLetraFactory());
        Rodada.setBonecoFactory(this.getBonecoFactory());
    }


    public String[] getTiposRepositoryFactory() {
        return TIPOS_REPOSITORY_FACTORY;
    }

    public void setTipoRepositoryFactory(String tipo) {
        this.tipoRepositoryFactory = tipo;
        this.configurar();
    }

    public RepositoryFactory getRepositoryFactory() {
        if (this.tipoRepositoryFactory.equals(TIPOS_REPOSITORY_FACTORY[0])) {
            return MemoriaRepositoryFactory.getSoleInstance();
        } else if (this.tipoRepositoryFactory.equals(TIPOS_REPOSITORY_FACTORY[1])) {
            return BDRRepositoryFactory.getSoleInstance();
        }
        throw new RuntimeException("Tipo de repositório não previsto: "
                + this.tipoRepositoryFactory);
    }


    public String[] getTiposElementoGraficoFactory() {
        return TIPOS_ELEMENTO_GRAFICO_FACTORY;
    }

    public void setTipoElementoGraficoFactory(String tipo) {
        this.tipoElementoGraficoFactory = tipo;
        this.configurar();
    }

    private ElementoGraficoFactory getElementoGraficoFactory() {
        if (this.tipoElementoGraficoFactory.equals(TIPOS_ELEMENTO_GRAFICO_FACTORY[0])) {
            return ElementoGraficoTextoFactory.getSoleInstance();
        } else if (this.tipoElementoGraficoFactory.equals(TIPOS_ELEMENTO_GRAFICO_FACTORY[1])) {
            return ElementoGraficoImagemFactory.getSoleInstance();
        }
        throw new RuntimeException("Tipo de elemento gráfico não previsto: "
                + this.tipoElementoGraficoFactory);
    }

    public LetraFactory getLetraFactory() {
        return this.getElementoGraficoFactory();
    }

    public BonecoFactory getBonecoFactory() {
        return this.getElementoGraficoFactory();
    }


    public String[] getTiposRodadaFactory() {
        return TIPOS_RODADA_FACTORY;
    }

    public void setTipoRodadaFactory(String tipo) {
        this.tipoRodadaFactory = tipo;
        this.configurar();
    }

    public RodadaFactory getRodadaFactory() {
        if (this.tipoRodadaFactory.equals(TIPOS_RODADA_FACTORY[0])) {
            return RodadaSorteioFactory.getSoleInstance();
        }
        throw new RuntimeException("Tipo de rodada factory não previsto: "
                + this.tipoRodadaFactory);
    }


    public TemaFactory getTemaFactory() {
        return TemaFactoryImpl.getSoleInstance();
    }

    public PalavraFactory getPalavraFactory() {
        return PalavraFactoryImpl.getSoleInstance();
    }

    public JogadorFactory getJogadorFactory() {
        return JogadorFactoryImpl.getSoleInstance();
    }
}