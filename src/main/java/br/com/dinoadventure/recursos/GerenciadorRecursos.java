package br.com.dinoadventure.recursos;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

/** Carrega imagens e animações tanto no IntelliJ quanto dentro do JAR. */
public final class GerenciadorRecursos {
    private static final int QUANTIDADE_QUADROS = 8;
    private final List<Path> pastasExternas;

    public GerenciadorRecursos() {
        this(Path.of(System.getProperty("user.dir", ".")));
    }

    /** Permite apontar a busca externa para uma pasta de projeto específica. */
    public GerenciadorRecursos(Path pastaProjeto) {
        Set<Path> bases = new LinkedHashSet<>();
        bases.add(pastaProjeto.toAbsolutePath().normalize());
        try {
            URL local = GerenciadorRecursos.class.getProtectionDomain().getCodeSource().getLocation();
            if (local != null && "file".equalsIgnoreCase(local.getProtocol())) {
                Path origem = Path.of(local.toURI()).toAbsolutePath().normalize();
                if (Files.isRegularFile(origem)) origem = origem.getParent();
                if (origem != null) bases.add(origem);
                // Permite encontrar assets/ ao lado de target/ e out/.
                if (origem != null && origem.getParent() != null) bases.add(origem.getParent());
            }
        } catch (URISyntaxException | SecurityException e) {
            System.out.println("Aviso ao localizar recursos externos: " + e.getMessage());
        }

        Set<Path> pastas = new LinkedHashSet<>();
        for (Path base : bases) {
            pastas.add(base.resolve("src/main/resources/assets"));
            pastas.add(base.resolve("assets"));
            pastas.add(base.resolve("src/assets"));
            pastas.add(base.resolve("resources/assets"));
            pastas.add(base.resolve("DinoAdventure_Sprites"));
        }
        pastasExternas = new ArrayList<>(pastas);
    }

    public BufferedImage carregarImagem(String nome) {
        BufferedImage imagem = tentarCarregarImagem(nome);
        if (imagem == null) {
            System.out.println("Imagem não encontrada: " + nome
                    + ". Coloque o arquivo em src/main/resources/assets/.");
        }
        return imagem;
    }

    /** Usado pelo mapa para priorizar uma arte externa substituída recentemente. */
    public BufferedImage carregarImagemExterna(String nome) {
        Path arquivo = localizarArquivoExterno(nome);
        if (arquivo == null) return null;
        try {
            return ImageIO.read(arquivo.toFile());
        } catch (IOException e) {
            System.out.println("Erro ao ler imagem " + arquivo + ": " + e.getMessage());
            return null;
        }
    }

    public void carregarAnimacao(BufferedImage[] quadros, String prefixo) {
        for (int i = 0; i < quadros.length; i++) {
            String numero2 = String.format(java.util.Locale.ROOT, "%02d", i + 1);
            String numero1 = String.valueOf(i + 1);
            String[] nomes = {
                    prefixo + "_" + numero2 + ".png", prefixo + "_" + numero1 + ".png",
                    prefixo + numero2 + ".png", prefixo + numero1 + ".png"
            };
            for (String nome : nomes) {
                BufferedImage quadro = tentarCarregarImagem("DinoAdventure_Sprites/" + prefixo + "/" + nome);
                if (quadro == null) quadro = tentarCarregarImagem(nome);
                if (quadro != null) {
                    quadros[i] = quadro;
                    break;
                }
            }
        }

        BufferedImage tira = tentarCarregarImagem(prefixo + "_spritesheet.png");
        if (tira == null) {
            tira = tentarCarregarImagem("DinoAdventure_Sprites/" + prefixo + "_spritesheet.png");
        }
        if (tira != null && tira.getWidth() >= QUANTIDADE_QUADROS
                && tira.getWidth() % QUANTIDADE_QUADROS == 0 && tira.getHeight() > 0) {
            int larguraQuadro = tira.getWidth() / QUANTIDADE_QUADROS;
            for (int i = 0; i < Math.min(quadros.length, QUANTIDADE_QUADROS); i++) {
                quadros[i] = tira.getSubimage(i * larguraQuadro, 0, larguraQuadro, tira.getHeight());
            }
        }

        for (BufferedImage quadro : quadros) {
            if (quadro != null) return;
        }
        System.out.println("Nenhuma imagem encontrada para a animação: " + prefixo);
    }

    private BufferedImage tentarCarregarImagem(String nome) {
        String normalizado = normalizarNome(nome);
        String arquivo = Path.of(normalizado).getFileName().toString();
        Set<String> recursos = new LinkedHashSet<>(List.of(
                "assets/" + normalizado, normalizado, "assets/" + arquivo, arquivo));
        ClassLoader classLoader = GerenciadorRecursos.class.getClassLoader();
        for (String recurso : recursos) {
            // Streams também funcionam para URLs jar:, sem converter o recurso em File.
            try (InputStream entrada = classLoader.getResourceAsStream(recurso)) {
                if (entrada == null) continue;
                BufferedImage imagem = ImageIO.read(entrada);
                if (imagem != null) return imagem;
            } catch (IOException e) {
                System.out.println("Erro ao ler recurso " + recurso + ": " + e.getMessage());
            }
        }
        return carregarImagemExterna(normalizado);
    }

    private Path localizarArquivoExterno(String nome) {
        String normalizado = normalizarNome(nome);
        String arquivo = Path.of(normalizado).getFileName().toString();
        for (Path pasta : pastasExternas) {
            Path completo = pasta.resolve(normalizado);
            if (Files.isRegularFile(completo)) return completo;
            Path simples = pasta.resolve(arquivo);
            if (Files.isRegularFile(simples)) return simples;
        }
        // Compatibilidade com as subpastas de sprites do projeto anterior.
        // A busca fica restrita às pastas de recursos, sem percorrer o computador.
        for (Path pasta : pastasExternas) {
            if (!Files.isDirectory(pasta)) continue;
            try (Stream<Path> caminhos = Files.walk(pasta, 6)) {
                Path encontrado = caminhos.filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().equalsIgnoreCase(arquivo))
                        .sorted().findFirst().orElse(null);
                if (encontrado != null) return encontrado;
            } catch (IOException | SecurityException e) {
                System.out.println("Aviso ao pesquisar recursos em " + pasta + ": " + e.getMessage());
            }
        }
        return null;
    }

    private static String normalizarNome(String nome) {
        String normalizado = nome.replace('\\', '/');
        return normalizado.startsWith("assets/") ? normalizado.substring(7) : normalizado;
    }
}
