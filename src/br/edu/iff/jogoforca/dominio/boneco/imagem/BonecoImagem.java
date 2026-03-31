package br.edu.iff.jogoforca.dominio.boneco.imagem;

import br.edu.iff.jogoforca.dominio.boneco.Boneco;
import java.util.List;

public class BonecoImagem implements Boneco {
    
    private static BonecoImagem soleInstance;

    public static BonecoImagem getSoleInstance() {
        if (soleInstance == null) {
            soleInstance = new BonecoImagem();
        }
        return soleInstance;
    }

    public BonecoImagem() {
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void exibir(Object contexto, int partes) {
        // Para imagem, o contexto pode ser uma lista onde adicionar URLs de imagens
        if (contexto instanceof List) {
            List<String> urls = (List<String>) contexto;
            String urlImagem = "/images/boneco/boneco_" + partes + ".png";
            urls.add(urlImagem);
        }
        System.out.println("[IMG:Boneco" + partes + "]");
    }
}