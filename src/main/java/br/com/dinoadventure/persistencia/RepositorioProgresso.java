package br.com.dinoadventure.persistencia;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/** Centraliza a leitura e a gravação do progresso local do jogador. */
public final class RepositorioProgresso {
    private static final String MAIOR_FASE = "maiorFaseDesbloqueada";
    private static final String ETAPA = "valeVerdeEtapaSalva";
    private static final String CHECKPOINT = "valeVerdeCheckpoint";
    private static final String PONTUACAO = "valeVerdePontuacaoSalva";
    private static final String BLOCOS = "valeVerdeBlocosUsados";

    // PainelJogo era uma classe sem package. Mantém o nó original do Preferences
    // para que mudar as pastas do código não apague o progresso já existente.
    private static final String NO_LEGADO = "/<unnamed>";
    private final Preferences preferencias;

    public RepositorioProgresso() {
        this(Preferences.userRoot().node(NO_LEGADO));
    }

    public RepositorioProgresso(Preferences preferencias) {
        this.preferencias = preferencias;
    }

    public int carregarMaiorFaseDesbloqueada() {
        return Math.max(1, Math.min(10, preferencias.getInt(MAIOR_FASE, 1)));
    }

    public int carregarEtapaValeVerde() {
        return Math.max(1, Math.min(5, preferencias.getInt(ETAPA, 1)));
    }

    public int carregarCheckpointVale() {
        return Math.max(0, preferencias.getInt(CHECKPOINT, 0));
    }

    public int carregarPontuacaoVale() {
        return preferencias.getInt(PONTUACAO, 0);
    }

    public String carregarBlocosVale() {
        return preferencias.get(BLOCOS, "");
    }

    public void salvarMaiorFaseDesbloqueada(int maiorFase) {
        preferencias.putInt(MAIOR_FASE, maiorFase);
        gravar();
    }

    public void salvarPartida(int maiorFase, int etapa, int checkpoint,
                             int pontuacao, String blocosUsados) {
        preferencias.putInt(MAIOR_FASE, maiorFase);
        preferencias.putInt(ETAPA, etapa);
        preferencias.putInt(CHECKPOINT, checkpoint);
        preferencias.putInt(PONTUACAO, pontuacao);
        if (blocosUsados != null) {
            preferencias.put(BLOCOS, blocosUsados);
        } else {
            limparBlocosVale();
        }
        if (gravar()) {
            System.out.println("Progresso salvo automaticamente: avanço "
                    + etapa + ", checkpoint " + checkpoint);
        }
    }

    public void limparTentativaVale() {
        preferencias.putInt(CHECKPOINT, 0);
        preferencias.putInt(PONTUACAO, 0);
        limparBlocosVale();
        if (gravar()) {
            System.out.println("Derrota no Vale dos Obstáculos: tentativa zerada.");
        }
    }

    public void limparBlocosVale() {
        preferencias.remove(BLOCOS);
    }

    private boolean gravar() {
        try {
            preferencias.flush();
            return true;
        } catch (BackingStoreException | SecurityException e) {
            System.out.println("Falha ao salvar progresso: " + e.getMessage());
            return false;
        }
    }
}
