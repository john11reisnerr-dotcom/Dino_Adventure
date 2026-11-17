package br.com.dinoadventure.jogo;

final class TroncoCacto {
    final int x, y, largura, altura, cactoLargura, cactoAltura;
    double animacao;

    TroncoCacto(int x, int y, int largura, int altura,
                int cactoLargura, int cactoAltura, double animacaoInicial) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.cactoLargura = cactoLargura;
        this.cactoAltura = cactoAltura;
        this.animacao = animacaoInicial;
    }

    // Oscilação leve: cerca de 32 pixels para cada lado e 8 na vertical.
    int getXAtual() { return x + (int) Math.round(Math.sin(animacao) * 32); }
    int getYAtual() { return y + (int) Math.round(Math.cos(animacao * 0.85) * 8); }
    int getCactoX() { return getXAtual() + largura / 2 - cactoLargura / 2; }
    int getCactoTopoY() { return getYAtual() - cactoAltura; }
}
