package br.com.dinoadventure.jogo;

final class BlocoSuspenso {
    int x, y, tamanho;
    boolean contemFruta;
    boolean atingido = false;
    BlocoSuspenso(int x, int y, int tamanho, boolean contemFruta) {
        this.x = x;
        this.y = y;
        this.tamanho = tamanho;
        this.contemFruta = contemFruta;
    }
}
