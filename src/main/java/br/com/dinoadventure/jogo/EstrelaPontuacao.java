package br.com.dinoadventure.jogo;

import java.awt.Color;

final class EstrelaPontuacao {
    double x, y, vx, vy;
    int tamanho, vida, vidaMaxima;
    Color cor;
    EstrelaPontuacao(double x, double y, double vx, double vy,
                     int tamanho, int vida, Color cor) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.tamanho = tamanho;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.cor = cor;
    }
}
