package br.com.dinoadventure.jogo;

/** Dados de leitura necessários para desenhar o HUD. */
record ResumoPartida(int fase, int etapaValeVerde, int pontuacao,
                     boolean portalAtivo, double dinoMundoX) {
    boolean noValeObstaculos() {
        return fase == 1 && etapaValeVerde == 2;
    }
}
