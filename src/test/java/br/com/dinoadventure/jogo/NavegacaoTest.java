package br.com.dinoadventure.jogo;

import br.com.dinoadventure.persistencia.RepositorioProgresso;
import br.com.dinoadventure.recursos.GerenciadorRecursos;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.UUID;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

@Timeout(30)
class NavegacaoTest {
    @Test
    void enterAbreSelecaoEIniciaPartida() throws Exception {
        comPainel(painel -> {
            assertEquals(TelaJogo.MENU, painel.telaAtual());
            acionar(painel, "iniciar");
            assertEquals(TelaJogo.SELECAO, painel.telaAtual());
            acionar(painel, "iniciar");
            assertEquals(TelaJogo.PARTIDA, painel.telaAtual());
        });
    }

    @Test
    void escapeVoltaDaSelecaoAoMenu() throws Exception {
        comPainel(painel -> {
            acionar(painel, "iniciar");
            acionar(painel, "voltar");
            assertEquals(TelaJogo.MENU, painel.telaAtual());
        });
    }

    @Test
    void faseBloqueadaNaoIniciaPeloMouse() throws Exception {
        comPainel(painel -> {
            acionar(painel, "iniciar");
            clicar(painel, 300, 220);
            assertEquals(TelaJogo.SELECAO, painel.telaAtual());
        });
    }

    @Test
    void mapaAventuraVoltaParaSelecao() throws Exception {
        comPainel(painel -> {
            acionar(painel, "iniciar");
            clicar(painel, 500, 500);
            assertEquals(TelaJogo.AVENTURA, painel.telaAtual());
            acionar(painel, "voltar");
            assertEquals(TelaJogo.SELECAO, painel.telaAtual());
        });
    }

    @Test
    void reinicioNaoAbrePartidaNoMenu() throws Exception {
        comPainel(painel -> {
            acionar(painel, "reiniciar");
            assertEquals(TelaJogo.MENU, painel.telaAtual());
        });
    }

    private static void comPainel(java.util.function.Consumer<PainelJogo> verificacao) throws Exception {
        Preferences preferencias = Preferences.userRoot().node(
                "/br/com/dinoadventure/testes/" + UUID.randomUUID());
        try {
            SwingUtilities.invokeAndWait(() -> {
                PainelJogo painel = new PainelJogo(new GerenciadorRecursos(Path.of(".")),
                        new RepositorioProgresso(preferencias));
                painel.setSize(1000, 563);
                verificacao.accept(painel);
            });
        } finally {
            preferencias.removeNode();
            Preferences.userRoot().flush();
        }
    }

    private static void acionar(PainelJogo painel, String acao) {
        painel.getActionMap().get(acao).actionPerformed(new ActionEvent(painel, 0, acao));
    }

    private static void clicar(PainelJogo painel, int x, int y) {
        painel.dispatchEvent(new MouseEvent(painel, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, x, y, 1, false));
    }
}
