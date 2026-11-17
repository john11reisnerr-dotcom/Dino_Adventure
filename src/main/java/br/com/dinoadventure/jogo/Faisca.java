package br.com.dinoadventure.jogo;

import java.awt.Color;

final class Faisca {
    double x, y;
    double vx, vy;
    int tamanho;
    int vida;
    int vidaMaxima;
    Color cor;
    Faisca(
            double x,
            double y,
            double vx,
            double vy,
            int tamanho,
            int vida,
            Color cor) {
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
