package br.com.dinoadventure.jogo;

final class BolhaExplosiva {
    final PlantaEspinhoMarrom planta;
    final int deslocamentoX;
    final int atrasoInicial;
    int tempo;
    double y;
    BolhaExplosiva(PlantaEspinhoMarrom planta, int deslocamentoX, int atrasoInicial) {
        this.planta = planta;
        this.deslocamentoX = deslocamentoX;
        this.atrasoInicial = atrasoInicial;
        this.tempo = atrasoInicial;
        this.y = planta.y + 16;
    }
    boolean explodindo() { return tempo >= 58 && tempo <= 82; }
    int getX() { return planta.x + planta.largura / 2 + deslocamentoX; }
    int getY() { return (int) Math.round(y); }
    int getRaioExplosao() { return 12 + Math.max(0, tempo - 58) * 2; }
    void reiniciar() { tempo = 0; y = planta.y + 16; }
}
