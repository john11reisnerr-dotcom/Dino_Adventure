package br.com.dinoadventure.jogo;

import java.util.ArrayList;
import java.util.List;
import static br.com.dinoadventure.jogo.ConfiguracaoJogo.COMPRIMENTO_VALE_OBSTACULOS;

/** Gera um percurso determinístico: a câmera não deve recriar os blocos. */
final class GeradorBlocosVale {
    private GeradorBlocosVale() { }

    static List<BlocoSuspenso> criar() {
        List<BlocoSuspenso> blocos = new ArrayList<>();
        // Grupos de plataformas por todo o percurso: novos desafios surgem à frente.
        int[] alturas = {230, 210, 244, 196, 230, 207, 240, 216};
        for (int grupo = 0; 245 + grupo * 495 < COMPRIMENTO_VALE_OBSTACULOS - 750;
             grupo++) {
            int inicio = 245 + grupo * 495;
            int y = alturas[grupo % alturas.length];
            for (int coluna = 0; coluna < 3; coluna++) {
                boolean temFruta = coluna == 2 && (grupo % 2 == 0);
                blocos.add(new BlocoSuspenso(inicio + coluna * 52,
                        y, 38, temFruta));
            }
            if (grupo > 0 && grupo % 3 != 0) {
                // Bloco mais alto para exigir o salto de dois toques.
                blocos.add(new BlocoSuspenso(inicio + 185, y - 68, 38,
                        grupo == 3));
            }
        }
        return blocos;
    }
}
