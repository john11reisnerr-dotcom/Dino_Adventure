package br.com.dinoadventure.jogo;

final class InimigoVoador {
    double x, xInicial, velocidade, faseAnimacao;
    int y, yBase, largura, altura;
    InimigoVoador(double x, int y, int largura, int altura, double velocidade) {
        this.x = x;
        this.xInicial = x;
        this.y = y;
        this.yBase = y;
        this.largura = largura;
        this.altura = altura;
        this.velocidade = velocidade;
    }
}
