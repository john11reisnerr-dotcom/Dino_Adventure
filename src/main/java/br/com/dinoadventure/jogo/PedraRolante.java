package br.com.dinoadventure.jogo;

final class PedraRolante {
    int x, y, largura, altura;
    double rotacao = 0;
    boolean pontuacaoConcedida = false;
    PedraRolante(
            int x,
            int y,
            int largura,
            int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
    }
}
