package br.edu.iff.jogoforca.dominio.boneco.texto;

import br.edu.iff.jogoforca.dominio.boneco.Boneco;

public class BonecoTexto implements Boneco {
    private static BonecoTexto soleInstance;

    private BonecoTexto() {}

    public static BonecoTexto getSoleInstance() {
        if (soleInstance == null) soleInstance = new BonecoTexto();
        return soleInstance;
    }

    @Override
    public void exibir(Object contexto, int partes) {
        System.out.println("  _____");
        System.out.println("  |   |");
        System.out.println("  |   " + (partes >= 1 ? "O" : " "));
        System.out.println("  |  " + (partes >= 7 ? "/" : " ") + (partes >= 6 ? "|" : " ") + (partes >= 8 ? "\\" : " "));
        System.out.println("  |  " + (partes >= 9 ? "/" : " ") + " " + (partes >= 10 ? "\\" : " "));
        System.out.println("  |");
        System.out.println("___|___");
    }
}