package br.com.dinoadventure.jogo;

final class InimigoVale {
    double x;
    int y;
    int tamanho;
    double velocidade;
    double animacao = 0;
    InimigoVale(double x, int y, int tamanho, double velocidade) {
        this.x = x;
        this.y = y;
        this.tamanho = tamanho;
        this.velocidade = velocidade;
    }
}
