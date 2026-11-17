package br.com.dinoadventure.jogo;

final class PlantaBolha {
    final int x, y, largura, altura;
    final double defasagem;
    final boolean vermelha;
    double animacao;

    PlantaBolha(int x, int y, int largura, int altura,
                double defasagem, boolean vermelha) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.animacao = defasagem;
        this.defasagem = defasagem;
        this.vermelha = vermelha;
    }

    int getYAtual() {
        if (!vermelha) return y;
        // Sobe ate 95 pixels, desce ao mesmo lugar e faz uma breve pausa.
        double impulso = Math.max(0, Math.sin(animacao * 0.70));
        return y - (int) Math.round(95 * impulso);
    }
}
