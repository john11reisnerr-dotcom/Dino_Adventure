package br.com.dinoadventure.jogo;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static br.com.dinoadventure.jogo.ConfiguracaoJogo.COMPRIMENTO_VALE_OBSTACULOS;

class GeradorBlocosValeTest {
    @Test
    void gerarMesmoPercursoEmCadaTentativa() {
        List<BlocoSuspenso> primeira = GeradorBlocosVale.criar();
        List<BlocoSuspenso> segunda = GeradorBlocosVale.criar();
        assertFalse(primeira.isEmpty());
        assertEquals(primeira.size(), segunda.size());
        for (int i = 0; i < primeira.size(); i++) {
            BlocoSuspenso a = primeira.get(i);
            BlocoSuspenso b = segunda.get(i);
            assertAll(() -> assertEquals(a.x, b.x), () -> assertEquals(a.y, b.y),
                    () -> assertEquals(a.tamanho, b.tamanho),
                    () -> assertEquals(a.contemFruta, b.contemFruta));
        }
    }

    @Test
    void novaTentativaNaoCompartilhaEstadoDosBlocos() {
        List<BlocoSuspenso> anterior = GeradorBlocosVale.criar();
        anterior.get(0).atingido = true;
        List<BlocoSuspenso> nova = GeradorBlocosVale.criar();
        assertNotSame(anterior.get(0), nova.get(0));
        assertTrue(nova.stream().noneMatch(bloco -> bloco.atingido));
    }

    @Test
    void percursoTemFrutasEBlocosDentroDosLimites() {
        List<BlocoSuspenso> blocos = GeradorBlocosVale.criar();
        assertTrue(blocos.stream().anyMatch(bloco -> bloco.contemFruta));
        assertTrue(blocos.stream().anyMatch(bloco -> !bloco.contemFruta));
        assertTrue(blocos.stream().allMatch(bloco -> bloco.x >= 0 && bloco.y >= 0
                && bloco.tamanho > 0 && bloco.x + bloco.tamanho < COMPRIMENTO_VALE_OBSTACULOS));
    }
}
