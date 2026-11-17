package br.com.dinoadventure.jogo;

import br.com.dinoadventure.persistencia.RepositorioProgresso;
import br.com.dinoadventure.recursos.GerenciadorRecursos;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.prefs.Preferences;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/** Verificações de integração sem janela e sem alterar o progresso real. */
@Timeout(30)
final class JogoIntegracaoTest {

    @Test
    void verificarProgresso() throws Exception {
        Preferences no = novoNoTeste();
        try {
            RepositorioProgresso repo = new RepositorioProgresso(no);
            exigir(repo.carregarMaiorFaseDesbloqueada() == 1, "Fase inicial deve ser 1");
            repo.salvarPartida(6, 2, 3, 4200, "1010");
            RepositorioProgresso reaberto = new RepositorioProgresso(no);
            exigir(reaberto.carregarMaiorFaseDesbloqueada() == 6, "Desbloqueio deve persistir");
            exigir(reaberto.carregarEtapaValeVerde() == 2, "Avanço deve persistir");
            exigir(reaberto.carregarCheckpointVale() == 3, "Checkpoint deve persistir");
            exigir(reaberto.carregarPontuacaoVale() == 4200, "Pontuação deve persistir");
            exigir(reaberto.carregarBlocosVale().equals("1010"), "Blocos devem persistir");
            reaberto.limparTentativaVale();
            exigir(repo.carregarCheckpointVale() == 0, "Derrota deve zerar checkpoint");
            exigir(repo.carregarPontuacaoVale() == 0, "Derrota deve zerar pontuação salva");
            exigir(repo.carregarBlocosVale().isEmpty(), "Derrota deve restaurar os blocos");
            exigir(repo.carregarMaiorFaseDesbloqueada() == 6, "Derrota deve preservar desbloqueios");
            exigir(repo.carregarEtapaValeVerde() == 2, "Derrota deve preservar avanço");
            repo.salvarPartida(6, 2, 1, 10, "111");
            repo.salvarPartida(6, 3, 0, 0, null);
            exigir(repo.carregarBlocosVale().isEmpty(), "Novo avanço deve limpar blocos usados");
            no.putInt("maiorFaseDesbloqueada", 100);
            no.putInt("valeVerdeEtapaSalva", -1);
            no.putInt("valeVerdeCheckpoint", -1);
            exigir(repo.carregarMaiorFaseDesbloqueada() == 10, "Limite de fases deve ser respeitado");
            exigir(repo.carregarEtapaValeVerde() == 1, "Avanço inválido deve usar limite mínimo");
            exigir(repo.carregarCheckpointVale() == 0, "Checkpoint não pode ser negativo");
        } finally {
            no.removeNode();
            Preferences.userRoot().flush();
        }
    }

    @Test
    void verificarRecursos() throws Exception {
        Path raiz = Files.createTempDirectory("dino-recursos-");
        try {
            GerenciadorRecursos recursos = new GerenciadorRecursos(raiz);
            Path assets = raiz.resolve("src/main/resources/assets");
            criarImagem(assets.resolve("imagem-teste.png"), 16, 12, Color.BLUE);
            BufferedImage imagem = recursos.carregarImagem("imagem-teste.png");
            exigir(imagem != null && imagem.getWidth() == 16, "Imagem externa deve carregar");

            for (int i = 1; i <= 8; i++) {
                criarImagem(assets.resolve("DinoAdventure_Sprites/teste/teste_0" + i + ".png"),
                        10 + i, 10, Color.GREEN);
            }
            BufferedImage[] quadros = new BufferedImage[8];
            recursos.carregarAnimacao(quadros, "teste");
            exigir(quadros[0] != null && quadros[7] != null, "Oito sprites devem carregar");
            exigir(quadros[7].getWidth() == 18, "Ordem dos sprites deve ser preservada");

            criarImagem(assets.resolve("teste_spritesheet.png"), 80, 20, Color.RED);
            recursos.carregarAnimacao(quadros, "teste");
            exigir(quadros[0].getWidth() == 10 && quadros[7].getHeight() == 20,
                    "Spritesheet deve substituir e dividir os oito quadros");
            exigir(quadros[0].getRGB(0, 0) == Color.RED.getRGB(), "Spritesheet deve ter prioridade");
        } finally {
            apagarPasta(raiz);
        }
    }

    @Test
    void verificarJogoNaThreadSwing() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                verificarJogo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void verificarJogo() throws Exception {
        Preferences no = novoNoTeste();
        Path raiz = Files.createTempDirectory("dino-jogo-");
        try {
            RepositorioProgresso repo = new RepositorioProgresso(no);
            PainelJogo painel = new PainelJogo(new GerenciadorRecursos(raiz), repo);
            painel.setSize(1000, 563);
            renderizar(painel);
            acionar(painel, "iniciar");
            exigir(painel.telaAtual() == TelaJogo.SELECAO, "Enter deve abrir seleção de fases");
            renderizar(painel);
            acionar(painel, "iniciar");
            exigir(painel.telaAtual() == TelaJogo.PARTIDA, "Enter deve iniciar fase desbloqueada");

            acionar(painel, "pular");
            exigir((int) ler(painel, "quantidadePulos") == 1, "Primeiro toque deve saltar");
            acionar(painel, "pular");
            exigir((int) ler(painel, "quantidadePulos") == 1, "Segurar espaço não deve duplicar salto");
            acionar(painel, "soltarPulo");
            acionar(painel, "pular");
            exigir((int) ler(painel, "quantidadePulos") == 2, "Segundo toque deve dar impulso extra");
            exigir((double) ler(painel, "velocidadeY") == -17.5, "Impulso extra deve ser preservado");

            alterar(painel, "quantidadePulos", 0);
            alterar(painel, "pulando", false);
            for (int etapa = 1; etapa <= 5; etapa++) {
                alterar(painel, "etapaValeVerde", etapa);
                chamar(painel, "inicializarBlocosVale");
                renderizar(painel);
            }
            alterar(painel, "etapaValeVerde", 2);
            chamar(painel, "inicializarBlocosVale");
            @SuppressWarnings("unchecked")
            List<BlocoSuspenso> blocos = (List<BlocoSuspenso>) ler(painel, "blocosVale");
            exigir(!blocos.isEmpty(), "Vale dos Obstáculos deve conter blocos");
            blocos.get(0).atingido = true;
            alterar(painel, "checkpointVale", 2);
            alterar(painel, "pontuacao", 150);
            chamar(painel, "salvarProgressoAutomatico");
            exigir(repo.carregarBlocosVale().charAt(0) == '1', "Bloco usado deve ser salvo");
            chamar(painel, "limparTentativaValeAposMorte");
            exigir(repo.carregarBlocosVale().isEmpty(), "Derrota deve limpar os blocos salvos");
            exigir(repo.carregarPontuacaoVale() == 0, "Derrota deve zerar pontuação da tentativa");
            exigir((int) ler(painel, "pontuacao") == 150, "Tela de derrota deve manter pontuação visível");

            for (int fase = 1; fase <= 10; fase++) {
                alterar(painel, "fase", fase);
                renderizar(painel);
            }
            alterar(painel, "tela", TelaJogo.AVENTURA);
            renderizar(painel);
            alterar(painel, "tela", TelaJogo.SELECAO);
            alterar(painel, "mundoMapaSelecionado", 2);
            renderizar(painel);
        } finally {
            no.removeNode();
            Preferences.userRoot().flush();
            apagarPasta(raiz);
        }
    }

    @Test
    void verificarRecursoDentroDoJar() throws Exception {
        Path raiz = Files.createTempDirectory("dino-jar-");
        Path jar = raiz.resolve("recursos-teste.jar");
        String classe = "br/com/dinoadventure/recursos/GerenciadorRecursos.class";
        try {
            try (JarOutputStream saida = new JarOutputStream(Files.newOutputStream(jar));
                 InputStream bytecode = GerenciadorRecursos.class.getClassLoader().getResourceAsStream(classe)) {
                if (bytecode == null) throw new IOException("Classe de recursos não encontrada");
                saida.putNextEntry(new JarEntry(classe));
                bytecode.transferTo(saida);
                saida.closeEntry();
                saida.putNextEntry(new JarEntry("assets/apenas-no-jar.png"));
                saida.write(imagemPng(12, 9, Color.MAGENTA));
                saida.closeEntry();
                saida.putNextEntry(new JarEntry("assets/DinoAdventure_Sprites/pacote_spritesheet.png"));
                saida.write(imagemPng(80, 20, Color.CYAN));
                saida.closeEntry();
            }
            try (URLClassLoader loader = new URLClassLoader(new URL[]{jar.toUri().toURL()},
                    ClassLoader.getPlatformClassLoader())) {
                Class<?> tipo = loader.loadClass("br.com.dinoadventure.recursos.GerenciadorRecursos");
                Object recursos = tipo.getConstructor(Path.class).newInstance(raiz.resolve("sem-assets"));
                BufferedImage imagem = (BufferedImage) tipo.getMethod("carregarImagem", String.class)
                        .invoke(recursos, "apenas-no-jar.png");
                exigir(imagem != null && imagem.getRGB(0, 0) == Color.MAGENTA.getRGB(),
                        "Imagem deve ser lida de dentro do JAR sem arquivo externo");
                BufferedImage[] quadros = new BufferedImage[8];
                tipo.getMethod("carregarAnimacao", BufferedImage[].class, String.class)
                        .invoke(recursos, quadros, "pacote");
                exigir(quadros[0] != null && quadros[7] != null && quadros[7].getWidth() == 10,
                        "Spritesheet deve ser carregada e dividida de dentro do JAR");
            }
        } finally {
            apagarPasta(raiz);
        }
    }

    private static byte[] imagemPng(int largura, int altura, Color cor) throws IOException {
        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagem.createGraphics();
        g.setColor(cor);
        g.fillRect(0, 0, largura, altura);
        g.dispose();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(imagem, "png", bytes);
        return bytes.toByteArray();
    }

    private static Preferences novoNoTeste() {
        return Preferences.userRoot().node("/br/com/dinoadventure/verificacao/" + UUID.randomUUID());
    }

    private static void criarImagem(Path arquivo, int largura, int altura, Color cor) throws IOException {
        Files.createDirectories(arquivo.getParent());
        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagem.createGraphics();
        g.setColor(cor);
        g.fillRect(0, 0, largura, altura);
        g.dispose();
        ImageIO.write(imagem, "png", arquivo.toFile());
    }

    private static void renderizar(PainelJogo painel) {
        BufferedImage imagem = new BufferedImage(1000, 563, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagem.createGraphics();
        painel.paint(g);
        g.dispose();
    }

    private static void acionar(PainelJogo painel, String acao) {
        painel.getActionMap().get(acao).actionPerformed(new ActionEvent(painel, 0, acao));
    }

    private static Object ler(PainelJogo painel, String nome) throws Exception {
        Field campo = PainelJogo.class.getDeclaredField(nome);
        campo.setAccessible(true);
        return campo.get(painel);
    }

    private static void alterar(PainelJogo painel, String nome, Object valor) throws Exception {
        Field campo = PainelJogo.class.getDeclaredField(nome);
        campo.setAccessible(true);
        campo.set(painel, valor);
    }

    private static void chamar(PainelJogo painel, String nome) throws Exception {
        Method metodo = PainelJogo.class.getDeclaredMethod(nome);
        metodo.setAccessible(true);
        metodo.invoke(painel);
    }

    private static void apagarPasta(Path pasta) throws IOException {
        try (Stream<Path> arquivos = Files.walk(pasta)) {
            for (Path arquivo : arquivos.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(arquivo);
            }
        }
    }

    private static void exigir(boolean condicao, String mensagem) {
        org.junit.jupiter.api.Assertions.assertTrue(condicao, mensagem);
    }
}
