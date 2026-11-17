package br.com.dinoadventure.jogo;

/** Parâmetros compartilhados da simulação e do percurso. */
final class ConfiguracaoJogo {
    private ConfiguracaoJogo() { }

    static final int META_VALE_VERDE = 6500; // Avanços mais longos (~1m40s se não houver bônus).
    static final int COMPRIMENTO_VALE_OBSTACULOS = 24000;
    static final int INTERVALO_CHECKPOINT = 3000;
    static final int DURACAO_PODER_FRUTA = 10 * 60; // 10 segundos a 60 FPS
    static final int ESPERA_REINICIO_VALE = 75; // ~1,2 segundo antes do reinicio automatico.
}
