package br.com.dinoadventure.jogo;

import br.com.dinoadventure.persistencia.RepositorioProgresso;
import br.com.dinoadventure.recursos.GerenciadorRecursos;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharGrama;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharPedrinhasTerra;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharMinhocas;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharMinhoca;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharPaisagemSelecao;
import static br.com.dinoadventure.jogo.ConfiguracaoJogo.*;
import static br.com.dinoadventure.grafico.DesenhosCenario.centralizarTexto;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static br.com.dinoadventure.grafico.DesenhosCenario.desenharEstrela;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharArvorePequena;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharTextoCentralComContorno;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharCadeado;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharIlhaMapa;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharArvoreMapa;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharNuvem;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharMontanha;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharArvore;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharArvoreFrutifera;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharFruto;

/** Coordena as telas, a atualização da partida, as colisões e a renderização. */
public class PainelJogo extends JPanel {
    private static final long serialVersionUID = 1L;
    private final GerenciadorRecursos recursos;
    private final Timer temporizador;
    private BufferedImage dinoAntigo, portal, menu;
    private BufferedImage imagemConfiguracoes, imagemComoJogar;
    private BufferedImage imagemMapaFases;
    private BufferedImage imagemMapaAventura;
    private boolean mapaAventuraHover = false;
    private int contadorAnimacaoMapaAventura = 0;
    private double progressoAnimacaoMapaAventura = 0.0;
    private final BufferedImage[] imagensFases = new BufferedImage[10];
    private int mundoMapaSelecionado = 1;
    private int telaInformativa = 0;
    private TelaJogo tela = TelaJogo.MENU;
    private final RepositorioProgresso progresso;
    private int maiorFaseDesbloqueada = 1;
    private int faseSelecionadaNoMapa = 1;
    private double dinoMapaX = -1;
    private double dinoMapaY = -1;
    private double inicioDinoMapaX;
    private double inicioDinoMapaY;
    private double destinoDinoMapaX;
    private double destinoDinoMapaY;
    private int progressoDinoMapa = 0;
    private boolean animandoDinoNoMapa = false;
    private double animacaoBotaoIniciar = 0;
    private int opcaoMenuEmDestaque = -1;
    private int abaMundoEmDestaque = -1;
    private BufferedImage[] paradoFrames = new BufferedImage[8];
    private BufferedImage[] direitaFrames = new BufferedImage[8];
    private BufferedImage[] esquerdaFrames = new BufferedImage[8];
    private BufferedImage[] pularFrames = new BufferedImage[8];
    private BufferedImage[] abaixarFrames = new BufferedImage[8];
    private BufferedImage[] danoFrames = new BufferedImage[8];
    private BufferedImage[] animacaoAtual;
    private int quadroAnimacao = 0;
    private int contadorAnimacao = 0;
    private final int velocidadeAnimacao = 7;
    private boolean recebendoDano = false;
    private int contadorDano = 0;
    private int dinoX = 100;
    private double dinoY;
    private double velocidadeY = 0;
    private boolean pulando = false;
    private boolean abaixado = false;
    private boolean direita = false;
    private boolean esquerda = false;
    private final int larguraDino = 78;
    private final int alturaNormal = 78;
    private final int alturaAbaixado = 48;
    private final int velocidade = 5;
    private final double gravidade = 0.7;
    private final double forcaPulo = -14;
    private boolean inicializado = false;
    private double movimentoNuvens = 0;
    private double movimentoMontanhas = 0;
    private double movimentoArvores = 0;
    private double movimentoChao = 0;
    private int pontuacao = 0;
    private int fase = 1;
    // Subetapas internas da fase 1-1 (Vale Verde): só libera a fase 1-2 após a quinta.
    private int etapaValeVerde = 1;
    private double dinoMundoX = 75;
    private double cameraValeX = 0;
    private int checkpointVale = 0;
    private boolean espacoPressionado = false;
    private int quantidadePulos = 0;
    private final ArrayList<FrutaEspecial> frutasLiberadas = new ArrayList<>();
    private ArrayList<InimigoVale> inimigosVale = new ArrayList<>();
    private int contadorInimigosVale = 0;

    // Mecânicas do Avanço 2 — Vale dos Obstáculos
    private final ArrayList<BlocoSuspenso> blocosVale = new ArrayList<>();
    private final ArrayList<InimigoVoador> inimigosVoadores = new ArrayList<>();
    private final ArrayList<PlantaBolha> plantasBolha = new ArrayList<>();
    private final ArrayList<PlantaEspinhoMarrom> plantasEspinhoMarrom = new ArrayList<>();
    private final ArrayList<BolhaExplosiva> bolhasExplosivas = new ArrayList<>();
    private final ArrayList<TroncoCacto> troncosCacto = new ArrayList<>();
    private final ArrayList<EstrelaPoder> estrelasPoder = new ArrayList<>();
    private boolean poderFrutaAtivo = false;
    private int tempoPoderFruta = 0;
    private double escalaCrescimentoFruta = 1.0;
    private boolean objetosAvanco2Inicializados = false;
    private final int pontosParaProximaFase = 2000;
    private boolean portalAtivo = false;
    private double animacaoPortal = 0;
    private ArrayList<PedraRolante> pedras = new ArrayList<>();
    private Random random = new Random();
    private int contadorPedras = 0;
    private boolean gameOver = false;
    private int contadorReinicioVale = 0;
    private ArrayList<Faisca> faiscas = new ArrayList<>();
    private ArrayList<EstrelaPontuacao> estrelasPontuacao = new ArrayList<>();
    private int mostrarBonusPulo = 0;
    private int bonusPuloX = 0;
    private int bonusPuloY = 0;
    private final int alturaTerreno = 150;
    private final int alturaGrama = 98;

    public PainelJogo() {
        this(new GerenciadorRecursos(), new RepositorioProgresso());
    }

    PainelJogo(GerenciadorRecursos recursos, RepositorioProgresso progresso) {
        this.recursos = java.util.Objects.requireNonNull(recursos);
        this.progresso = java.util.Objects.requireNonNull(progresso);
        setFocusable(true);
        maiorFaseDesbloqueada = progresso.carregarMaiorFaseDesbloqueada();
        etapaValeVerde = progresso.carregarEtapaValeVerde();
        checkpointVale = progresso.carregarCheckpointVale();
        carregarImagens();
        inicializarBlocosVale();
        configurarTeclado();
        configurarMenu();
        animacaoAtual = paradoFrames;
        temporizador = new Timer(16, e -> atualizarJogo());
    }

    TelaJogo telaAtual() {
        return tela;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        temporizador.start();
    }

    @Override
    public void removeNotify() {
        temporizador.stop();
        super.removeNotify();
    }

    private void carregarImagens() {
        dinoAntigo = carregarImagem("dino.png");
        menu = carregarImagem("menu.png");
        imagemConfiguracoes = carregarImagem("configuracoes.png");
        imagemComoJogar = carregarImagem("como-jogar.png");
        imagemMapaFases = carregarImagem("mapa-fases.png");
        // Prefere uma arte externa atualizada antes da cópia empacotada no JAR.
        imagemMapaAventura = carregarMapaAventuraAtualizado();
        if (imagemMapaAventura == null) {
            imagemMapaAventura = carregarImagem("mapa_das_fases_aventura_dos_dinossauros.png");
        }
        if (imagemMapaAventura == null) {
            imagemMapaAventura = carregarImagem("mapa_dino_adventure_1055x1491.png");
        }
        if (imagemMapaAventura == null) {
            imagemMapaAventura = carregarImagem("mapa-aventura.png");
        }
        carregarImagensDasFases();
        portal = carregarImagem("portal.png");
        carregarAnimacao(paradoFrames, "parado");
        carregarAnimacao(direitaFrames, "andar_direita");
        carregarAnimacao(esquerdaFrames, "andar_esquerda");
        carregarAnimacao(pularFrames, "pular");
        carregarAnimacao(abaixarFrames, "abaixar");
        carregarAnimacao(danoFrames, "dano");
        if (dinoAntigo == null) {
            dinoAntigo = primeiroQuadro(paradoFrames);
            if (dinoAntigo == null) dinoAntigo = primeiroQuadro(direitaFrames);
            if (dinoAntigo == null) dinoAntigo = primeiroQuadro(esquerdaFrames);
            if (dinoAntigo == null) dinoAntigo = primeiroQuadro(pularFrames);
            if (dinoAntigo == null) dinoAntigo = primeiroQuadro(abaixarFrames);
            if (dinoAntigo != null) {
                System.out.println("Imagem reserva do Dino carregada a partir dos sprites.");
            } else {
                System.out.println("ATENÇÃO: nenhuma imagem do Dino foi encontrada em assets.");
            }
        }
    }

    private BufferedImage carregarMapaAventuraAtualizado() {
        String[] nomes = {
                "mapa_das_fases_aventura_dos_dinossauros.png",
                "mapa_dino_adventure_1055x1491.png"
        };
        // Uma imagem externa atualizada tem prioridade sobre a cópia empacotada.
        for (String nome : nomes) {
            BufferedImage imagem = recursos.carregarImagemExterna(nome);
            if (imagem != null) return imagem;
        }
        return null;
    }

    private void carregarImagensDasFases() {
        if (imagemMapaFases == null) {
            System.out.println("ATENÇÃO: coloque a imagem do mapa como assets/mapa-fases.png");
            return;
        }

        int largura = imagemMapaFases.getWidth();
        int altura = imagemMapaFases.getHeight();

        // A imagem enviada é um mosaico com 5 colunas e 2 linhas.
        // Os pequenos espaços entre os cartões são retirados no recorte.
        for (int mundo = 0; mundo < 2; mundo++) {
            for (int coluna = 0; coluna < 5; coluna++) {
                int xInicio = (int) Math.round(coluna * largura / 5.0) + 3;
                int xFim = (int) Math.round((coluna + 1) * largura / 5.0) - 3;

                int yInicio = mundo == 0 ? 0 : (int) (altura * 0.47);
                int yFim = mundo == 0 ? (int) (altura * 0.465) : altura;

                xInicio = Math.max(0, Math.min(largura - 1, xInicio));
                xFim = Math.max(xInicio + 1, Math.min(largura, xFim));
                yInicio = Math.max(0, Math.min(altura - 1, yInicio));
                yFim = Math.max(yInicio + 1, Math.min(altura, yFim));

                int indice = mundo * 5 + coluna;
                imagensFases[indice] = imagemMapaFases.getSubimage(
                        xInicio, yInicio, xFim - xInicio, yFim - yInicio
                );
            }
        }
        System.out.println("Mapa de fases dividido em 10 imagens.");
    }

    private BufferedImage primeiroQuadro(BufferedImage[] quadros) {
        if (quadros == null) return null;
        for (BufferedImage quadro : quadros) {
            if (quadro != null) return quadro;
        }
        return null;
    }

    private BufferedImage carregarImagem(String nome) {
        return recursos.carregarImagem(nome);
    }

    private void carregarAnimacao(BufferedImage[] quadros, String prefixo) {
        recursos.carregarAnimacao(quadros, prefixo);
    }

    private void atualizarAnimacaoDino() {
        BufferedImage[] desejada;
        if (recebendoDano && primeiroQuadro(danoFrames) != null) {
            desejada = danoFrames;
        } else if (pulando && primeiroQuadro(pularFrames) != null) {
            desejada = pularFrames;
        } else if (abaixado && primeiroQuadro(abaixarFrames) != null) {
            desejada = abaixarFrames;
        } else if (direita && primeiroQuadro(direitaFrames) != null) {
            desejada = direitaFrames;
        } else if (esquerda && primeiroQuadro(esquerdaFrames) != null) {
            desejada = esquerdaFrames;
        } else {
            desejada = paradoFrames;
        }
        if (primeiroQuadro(desejada) == null) {
            if (primeiroQuadro(paradoFrames) != null) {
                desejada = paradoFrames;
            } else if (dinoAntigo != null) {
                animacaoAtual = null;
                return;
            } else {
                return;
            }
        }
        if (animacaoAtual != desejada) {
            animacaoAtual = desejada;
            quadroAnimacao = 0;
            contadorAnimacao = 0;
            ajustarQuadroValido();
        }
        if (animacaoAtual == null) return;
        contadorAnimacao++;
        if (contadorAnimacao < velocidadeAnimacao) return;
        contadorAnimacao = 0;
        if (animacaoAtual == danoFrames && quadroAnimacao >= 7) {
            return;
        }
        for (int tentativa = 0; tentativa < animacaoAtual.length; tentativa++) {
            int proximo = (quadroAnimacao + 1) % animacaoAtual.length;
            quadroAnimacao = proximo;
            if (animacaoAtual[quadroAnimacao] != null) break;
        }
    }

    private void ajustarQuadroValido() {
        if (animacaoAtual == null || animacaoAtual.length == 0) return;
        if (quadroAnimacao >= 0 && quadroAnimacao < animacaoAtual.length
                && animacaoAtual[quadroAnimacao] != null) return;
        for (int i = 0; i < animacaoAtual.length; i++) {
            int indice = (quadroAnimacao + i) % animacaoAtual.length;
            if (animacaoAtual[indice] != null) {
                quadroAnimacao = indice;
                return;
            }
        }
    }

    private void configurarTeclado() {
        InputMap mapa = getInputMap(
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        ActionMap acoes = getActionMap();
        mapa.put(KeyStroke.getKeyStroke("pressed RIGHT"), "direita");
        mapa.put(KeyStroke.getKeyStroke("released RIGHT"), "pararDireita");
        mapa.put(KeyStroke.getKeyStroke("pressed LEFT"), "esquerda");
        mapa.put(KeyStroke.getKeyStroke("released LEFT"), "pararEsquerda");
        mapa.put(KeyStroke.getKeyStroke("pressed SPACE"), "pular");
        mapa.put(KeyStroke.getKeyStroke("released SPACE"), "soltarPulo");
        mapa.put(KeyStroke.getKeyStroke("pressed DOWN"), "abaixar");
        mapa.put(KeyStroke.getKeyStroke("released DOWN"), "levantar");
        mapa.put(KeyStroke.getKeyStroke("pressed D"), "reiniciar");
        mapa.put(KeyStroke.getKeyStroke("pressed ENTER"), "iniciar");
        mapa.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "voltar");
        acoes.put("direita", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (tela == TelaJogo.PARTIDA && !gameOver && !recebendoDano) {
                    direita = true;
                    esquerda = false;
                }
            }
        });
        acoes.put("pararDireita", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                direita = false;
            }
        });
        acoes.put("esquerda", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (tela == TelaJogo.PARTIDA && !gameOver && !recebendoDano) {
                    esquerda = true;
                    direita = false;
                }
            }
        });
        acoes.put("pararEsquerda", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                esquerda = false;
            }
        });
        acoes.put("pular", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Repetição automática do teclado não conta como segundo toque.
                if (espacoPressionado) return;
                espacoPressionado = true;
                if (tela != TelaJogo.PARTIDA || gameOver
                        || recebendoDano || abaixado) return;
                if (!pulando && quantidadePulos == 0) {
                    velocidadeY = forcaPulo;
                    pulando = true;
                    quantidadePulos = 1;
                } else if (pulando && quantidadePulos == 1) {
                    // Um segundo toque no ar dá impulso extra para subir aos blocos.
                    velocidadeY = -17.5;
                    quantidadePulos = 2;
                }
            }
        });
        acoes.put("soltarPulo", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                espacoPressionado = false;
            }
        });
        acoes.put("abaixar", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (tela == TelaJogo.PARTIDA && !gameOver && !pulando)
                    abaixado = true;
            }
        });
        acoes.put("levantar", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                abaixado = false;
            }
        });
        acoes.put("iniciar", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (tela == TelaJogo.MENU && telaInformativa == 0)
                    iniciarJogo();
                else if (tela == TelaJogo.SELECAO)
                    iniciarFaseSelecionada();
            }
        });
        acoes.put("voltar", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (tela == TelaJogo.AVENTURA) {
                    tela = TelaJogo.SELECAO;
                    mapaAventuraHover = false;
                    repaint();
                } else if (tela == TelaJogo.SELECAO) {
                    tela = TelaJogo.MENU;
                    repaint();
                } else if (tela == TelaJogo.MENU && telaInformativa != 0) {
                    telaInformativa = 0;
                    repaint();
                }
            }
        });
        acoes.put("reiniciar", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (gameOver)
                    reiniciarJogo();
            }
        });
    }

    private void configurarMenu() {
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (tela == TelaJogo.AVENTURA) {
                    boolean hover = clicouBotaoVoltarMapaAventura(e.getX(), e.getY());
                    if (hover != mapaAventuraHover) {
                        mapaAventuraHover = hover;
                        repaint();
                    }
                    return;
                }
                if (tela == TelaJogo.SELECAO) {
                    int faseHover = identificarFaseMapa(e.getX(), e.getY());
                    int abaHover = identificarAbaMundo(e.getX(), e.getY());
                    boolean mudou = false;
                    if (faseHover != opcaoMenuEmDestaque) {
                        opcaoMenuEmDestaque = faseHover;
                        mudou = true;
                    }
                    if (abaHover != abaMundoEmDestaque) {
                        abaMundoEmDestaque = abaHover;
                        mudou = true;
                    }
                    if (mudou) repaint();
                    return;
                }
                if (tela != TelaJogo.MENU) return;
                int novaOpcao = identificarOpcaoMenu(
                        e.getX() / (double) getWidth(),
                        e.getY() / (double) getHeight()
                );
                if (novaOpcao != opcaoMenuEmDestaque) {
                    opcaoMenuEmDestaque = novaOpcao;
                    repaint();
                }
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (opcaoMenuEmDestaque != -1 || abaMundoEmDestaque != -1) {
                    opcaoMenuEmDestaque = -1;
                    abaMundoEmDestaque = -1;
                    repaint();
                }
            }
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (tela == TelaJogo.AVENTURA) {
                    if (clicouBotaoVoltarMapaAventura(e.getX(), e.getY())) {
                        tela = TelaJogo.SELECAO;
                        mapaAventuraHover = false;
                        repaint();
                    }
                    return;
                }
                if (tela == TelaJogo.SELECAO) {
                    if (animandoDinoNoMapa) return;
                    if (clicouBotaoMapaAventura(e.getX(), e.getY())) {
                        tela = TelaJogo.AVENTURA;
                        contadorAnimacaoMapaAventura = 0;
                        progressoAnimacaoMapaAventura = 0.0;
                        repaint();
                        return;
                    }

                    if (clicouBotaoVoltarFolha(e.getX(), e.getY())) {
                        tela = TelaJogo.MENU;
                        repaint();
                        return;
                    }

                    int mundoClicado = identificarAbaMundo(e.getX(), e.getY());
                    if (mundoClicado == 1) {
                        mundoMapaSelecionado = 1;
                        faseSelecionadaNoMapa = Math.min(maiorFaseDesbloqueada, 5);
                        repaint();
                        return;
                    } else if (mundoClicado == 2) {
                        // Alterna para o Mundo 2 na mesma tela, sem abrir outra janela.
                        mundoMapaSelecionado = 2;
                        faseSelecionadaNoMapa = Math.max(6,
                                Math.min(maiorFaseDesbloqueada, 10));
                        repaint();
                        return;
                    }

                    int faseClicada = identificarFaseMapa(e.getX(), e.getY());
                    if (faseClicada >= 1 && faseClicada <= maiorFaseDesbloqueada) {
                        faseSelecionadaNoMapa = faseClicada;
                        iniciarFaseSelecionada();
                    }
                    return;
                }
                if (tela != TelaJogo.MENU) return;
                double x = e.getX() / (double) getWidth();
                double y = e.getY() / (double) getHeight();
                if (telaInformativa != 0) {
                    if (x >= 0.32 && x <= 0.70 && y >= 0.79) {
                        telaInformativa = 0;
                        repaint();
                    }
                    return;
                }
                if (x >= 0.45 && x <= 0.83 && y >= 0.45 && y <= 0.62) {
                    iniciarJogo();
                } else if (x >= 0.51 && x <= 0.79
                        && y >= 0.625 && y <= 0.74) {
                    telaInformativa = 1;
                    repaint();
                } else if (x >= 0.51 && x <= 0.79
                        && y >= 0.745 && y <= 0.85) {
                    telaInformativa = 2;
                    repaint();
                } else if (x >= 0.51 && x <= 0.79
                        && y >= 0.855 && y <= 0.98) {
                    System.exit(0);
                }
            }
        });
    }

    private int identificarOpcaoMenu(double x, double y) {
        if (telaInformativa != 0) {
            return (x >= 0.32 && x <= 0.70 && y >= 0.79) ? 4 : -1;
        }
        if (x >= 0.45 && x <= 0.83 && y >= 0.45 && y <= 0.62) return 0;
        if (x >= 0.51 && x <= 0.79 && y >= 0.625 && y <= 0.74) return 1;
        if (x >= 0.51 && x <= 0.79 && y >= 0.745 && y <= 0.85) return 2;
        if (x >= 0.51 && x <= 0.79 && y >= 0.855 && y <= 0.98) return 3;
        return -1;
    }

    private void iniciarJogo() {

        tela = TelaJogo.SELECAO;
        faseSelecionadaNoMapa = maiorFaseDesbloqueada;
        mundoMapaSelecionado = faseSelecionadaNoMapa <= 5 ? 1 : 2;
        posicionarDinoNoMapa(faseSelecionadaNoMapa);
        animandoDinoNoMapa = false;
        requestFocusInWindow();
        repaint();
    }

    private void posicionarDinoNoMapa(int numeroFase) {
        int[][] centros = obterCentrosFasesMapa();
        int indice = Math.max(0, Math.min(centros.length - 1, numeroFase - 1));
        dinoMapaX = centros[indice][0];
        dinoMapaY = centros[indice][1] - 48;
    }

    private void iniciarCaminhadaNoMapa(int faseOrigem, int faseDestino) {
        int[][] centros = obterCentrosFasesMapa();
        int origem = Math.max(0, Math.min(centros.length - 1, faseOrigem - 1));
        int destino = Math.max(0, Math.min(centros.length - 1, faseDestino - 1));
        inicioDinoMapaX = centros[origem][0];
        inicioDinoMapaY = centros[origem][1] - 48;
        destinoDinoMapaX = centros[destino][0];
        destinoDinoMapaY = centros[destino][1] - 48;
        dinoMapaX = inicioDinoMapaX;
        dinoMapaY = inicioDinoMapaY;
        progressoDinoMapa = 0;
        animandoDinoNoMapa = faseOrigem != faseDestino;
    }

    private void atualizarAnimacaoDinoMapa() {
        if (!animandoDinoNoMapa) return;
        progressoDinoMapa++;
        double t = Math.min(1.0, progressoDinoMapa / 65.0);
        double suave = t * t * (3.0 - 2.0 * t);
        dinoMapaX = inicioDinoMapaX + (destinoDinoMapaX - inicioDinoMapaX) * suave;
        dinoMapaY = inicioDinoMapaY + (destinoDinoMapaY - inicioDinoMapaY) * suave
                - Math.sin(t * Math.PI) * 12.0;
        if (t >= 1.0) {
            animandoDinoNoMapa = false;
            dinoMapaX = destinoDinoMapaX;
            dinoMapaY = destinoDinoMapaY;
        }
    }

    private int[][] obterCentrosFasesMapa() {
        int w = getWidth(), h = getHeight();
        return new int[][]{
                {(int)(w * 0.17), (int)(h * 0.68)},
                {(int)(w * 0.39), (int)(h * 0.43)},
                {(int)(w * 0.63), (int)(h * 0.65)},
                {(int)(w * 0.84), (int)(h * 0.36)}
        };
    }

    private int identificarAbaMundo(int x, int y) {
        int larguraAba = Math.min(340, (getWidth() - 50) / 2);
        int espacamentoAbas = 18;
        int yInicio = 72;
        int yFim = 132;
        int xMundo1 = (getWidth() - (larguraAba * 2 + espacamentoAbas)) / 2;
        int xMundo2 = xMundo1 + larguraAba + espacamentoAbas;
        if (y < yInicio || y > yFim) return -1;
        if (x >= xMundo1 && x <= xMundo1 + larguraAba) return 1;
        if (x >= xMundo2 && x <= xMundo2 + larguraAba) return 2;
        return -1;
    }

    private int identificarFaseMapa(int x, int y) {
        int margem = 22;
        int espacamento = 12;
        int larguraCartao = (getWidth() - margem * 2 - espacamento * 4) / 5;
        int alturaCartao = Math.min(300, getHeight() - 187);
        int yCartao = 145;

        if (y < yCartao || y > yCartao + alturaCartao) return -1;

        for (int coluna = 0; coluna < 5; coluna++) {
            int xCartao = margem + coluna * (larguraCartao + espacamento);
            if (x >= xCartao && x <= xCartao + larguraCartao) {
                return (mundoMapaSelecionado - 1) * 5 + coluna + 1;
            }
        }
        return -1;
    }

    private void iniciarFaseSelecionada() {
        if (faseSelecionadaNoMapa > maiorFaseDesbloqueada) return;
        fase = faseSelecionadaNoMapa;
        etapaValeVerde = fase == 1
                ? progresso.carregarEtapaValeVerde() : 1;
        checkpointVale = fase == 1 && etapaValeVerde == 2
                ? progresso.carregarCheckpointVale() : 0;
        checkpointVale = Math.min(checkpointVale, maxCheckpointVale());
        dinoMundoX = fase == 1 && etapaValeVerde == 2
                ? Math.min(COMPRIMENTO_VALE_OBSTACULOS - 420,
                75 + checkpointVale * INTERVALO_CHECKPOINT) : 75;
        atualizarCameraVale();
        inicializarBlocosVale();
        inimigosVoadores.clear();
        frutasLiberadas.clear();
        quantidadePulos = 0;
        espacoPressionado = false;
        inimigosVale.clear();
        contadorInimigosVale = 0;
        pontuacao = fase == 1 && etapaValeVerde == 2 && checkpointVale > 0
                ? progresso.carregarPontuacaoVale() : 0;
        portalAtivo = false;
        animacaoPortal = 0;
        pedras.clear();
        faiscas.clear();
        estrelasPontuacao.clear();
        contadorPedras = 0;
        dinoX = fase == 1 && etapaValeVerde == 2
                ? (int) Math.round(dinoMundoX - cameraValeX) : 100;
        dinoY = getHeight() - alturaTerreno - alturaNormal;
        velocidadeY = 0;
        pulando = abaixado = direita = esquerda = false;
        gameOver = recebendoDano = false;
        contadorDano = 0;
        animacaoAtual = paradoFrames;
        quadroAnimacao = contadorAnimacao = 0;
        inicializado = true;
        tela = TelaJogo.PARTIDA;

        requestFocusInWindow();
        repaint();
    }

    private void concluirFaseAtual() {
        if (fase < 10 && maiorFaseDesbloqueada < fase + 1) {
            maiorFaseDesbloqueada = fase + 1;
            progresso.salvarMaiorFaseDesbloqueada(maiorFaseDesbloqueada);
        }

        if (fase == 1) {
            etapaValeVerde = 1;
            checkpointVale = 0;
            pontuacao = 0;
            salvarProgressoAutomatico();
        }
        inimigosVale.clear();
        faseSelecionadaNoMapa = Math.min(10, fase + 1);
        mundoMapaSelecionado = faseSelecionadaNoMapa <= 5 ? 1 : 2;
        posicionarDinoNoMapa(faseSelecionadaNoMapa);
        animandoDinoNoMapa = false;
        tela = TelaJogo.SELECAO;

        portalAtivo = false;
        pedras.clear();
        faiscas.clear();
        estrelasPontuacao.clear();
        direita = esquerda = pulando = abaixado = false;
        repaint();
    }

    // Salvamento ocorre ao concluir um avanço ou alcançar um ponto de controle,
    // evitando gravações em disco a cada quadro do jogo.
    private void salvarProgressoAutomatico() {
        String blocosUsados = jogandoValeObstaculos() ? serializarBlocosVale() : null;
        progresso.salvarPartida(maiorFaseDesbloqueada, etapaValeVerde,
                checkpointVale, pontuacao, blocosUsados);
    }

    // Cada posicao corresponde a um bloco do percurso, gerado sempre na mesma ordem.
    // Enquanto o Dino vive, o estado dos blocos nao e recriado ao mover a camera.
    private String serializarBlocosVale() {
        StringBuilder estado = new StringBuilder(blocosVale.size());
        for (BlocoSuspenso bloco : blocosVale) {
            estado.append(bloco.atingido ? '1' : '0');
        }
        return estado.toString();
    }

    private void restaurarBlocosVale() {
        if (!jogandoValeObstaculos()) return;
        String estado = progresso.carregarBlocosVale();
        if (estado.length() != blocosVale.size()) return;
        for (int i = 0; i < blocosVale.size(); i++) {
            blocosVale.get(i).atingido = estado.charAt(i) == '1';
        }
    }

    // Chamado uma unica vez no GAME OVER, nao em cada quadro do dano.
    // Nao altera a pontuacao mostrada na tela de derrota; zera o save da tentativa.
    private void limparTentativaValeAposMorte() {
        if (!jogandoValeObstaculos()) return;
        checkpointVale = 0;
        progresso.limparTentativaVale();
    }

    private boolean jogandoValeObstaculos() {
        return fase == 1 && etapaValeVerde == 2;
    }

    private void atualizarCameraVale() {
        if (!jogandoValeObstaculos()) {
            cameraValeX = 0;
            return;
        }
        int largura = Math.max(1, getWidth());
        cameraValeX = Math.max(0, Math.min(COMPRIMENTO_VALE_OBSTACULOS - largura,
                dinoMundoX - largura * 0.36));
        dinoX = (int) Math.round(dinoMundoX - cameraValeX);
    }

    private int maxCheckpointVale() {
        return Math.max(0, (COMPRIMENTO_VALE_OBSTACULOS - 900) / INTERVALO_CHECKPOINT);
    }

    private void verificarCheckpointVale() {
        if (!jogandoValeObstaculos()) return;
        int indice = Math.min(maxCheckpointVale(),
                Math.max(0, (int) ((dinoMundoX - 75) / INTERVALO_CHECKPOINT)));
        if (indice > checkpointVale) {
            checkpointVale = indice;
            salvarProgressoAutomatico();
            criarEstrelasPontuacao(dinoX + larguraDino / 2, (int) dinoY - 5);
        }
    }

    private void deslocarDinoNoVale() {
        double anterior = dinoMundoX;
        if (direita) dinoMundoX += velocidade;
        if (esquerda) dinoMundoX -= velocidade;
        dinoMundoX = Math.max(0,
                Math.min(COMPRIMENTO_VALE_OBSTACULOS - larguraDino, dinoMundoX));
        // Colisões laterais: o corpo não pode atravessar um bloco pelo lado.
        for (BlocoSuspenso bloco : blocosVale) {
            boolean alturaCoincide = dinoY + getAlturaDino() - 6 > bloco.y
                    && dinoY + 8 < bloco.y + bloco.tamanho;
            if (!alturaCoincide) continue;
            if (dinoMundoX > anterior
                    && anterior + larguraDino - 12 <= bloco.x + 1
                    && dinoMundoX + larguraDino - 12 > bloco.x) {
                dinoMundoX = bloco.x - larguraDino + 12;
            } else if (dinoMundoX < anterior
                    && anterior + 12 >= bloco.x + bloco.tamanho - 1
                    && dinoMundoX + 12 < bloco.x + bloco.tamanho) {
                dinoMundoX = bloco.x + bloco.tamanho - 12;
            }
        }
        atualizarCameraVale();
        verificarCheckpointVale();
    }

    private void resolverColisoesVerticaisVale(double yAnterior) {
        int altura = getAlturaDino();
        double xEsquerda = dinoMundoX + 12;
        double xDireita = dinoMundoX + larguraDino - 12;
        for (BlocoSuspenso bloco : blocosVale) {
            if (xDireita <= bloco.x || xEsquerda >= bloco.x + bloco.tamanho) continue;
            if (velocidadeY >= 0
                    && yAnterior + altura <= bloco.y + 1.5
                    && dinoY + altura >= bloco.y) {
                // O Dino pousa, fica de pé e pode caminhar sobre os blocos.
                dinoY = bloco.y - altura;
                velocidadeY = 0;
                pulando = false;
                quantidadePulos = 0;
                return;
            }
            if (velocidadeY < 0
                    && yAnterior + 7 >= bloco.y + bloco.tamanho
                    && dinoY <= bloco.y + bloco.tamanho) {
                // Cabeçada: conteúdo só sai ao acertar o bloco por baixo.
                dinoY = bloco.y + bloco.tamanho;
                velocidadeY = 1.5;
                atingirBloco(bloco);
                return;
            }
        }
    }

    private void atingirBloco(BlocoSuspenso bloco) {
        if (bloco.atingido) return;
        bloco.atingido = true;
        pontuacao += 100;
        criarFaiscas((int) (bloco.x - cameraValeX + bloco.tamanho / 2),
                bloco.y + bloco.tamanho / 2);
        if (bloco.contemFruta) {
            // A fruta emerge, fica sobre o bloco e só ativa ao ser coletada.
            frutasLiberadas.add(new FrutaEspecial(bloco.x + bloco.tamanho / 2.0,
                    bloco.y + 6, bloco.y - 24));
        } else {
            criarEstrelasPontuacao((int) (bloco.x - cameraValeX + bloco.tamanho / 2),
                    bloco.y - 5);
        }
        // O bloco fica vazio até o Dino morrer, mesmo se voltar pelo caminho
        // ou fechar e reabrir o jogo enquanto a tentativa continuar ativa.
        if (jogandoValeObstaculos()) salvarProgressoAutomatico();
    }

    private void atualizarFrutasLiberadas() {
        if (!jogandoValeObstaculos()) return;
        Rectangle areaDino = new Rectangle((int) dinoMundoX + 12,
                (int) dinoY + 5, larguraDino - 24, getAlturaDino() - 8);
        Iterator<FrutaEspecial> it = frutasLiberadas.iterator();
        while (it.hasNext()) {
            FrutaEspecial fruta = it.next();
            if (fruta.y > fruta.yDestino) {
                fruta.y = Math.max(fruta.yDestino, fruta.y - 2.0);
            }
            fruta.animacao += 0.10;
            Rectangle areaFruta = new Rectangle((int) fruta.x - 12,
                    (int) fruta.y - 11, 24, 24);
            if (areaDino.intersects(areaFruta)) {
                ativarPoderFruta();
                it.remove();
            }
        }
    }

    private void desenharFrutasLiberadas(Graphics2D g2) {
        for (FrutaEspecial fruta : frutasLiberadas) {
            int x = (int) Math.round(fruta.x);
            int y = (int) Math.round(fruta.y + Math.sin(fruta.animacao) * 2);
            g2.setColor(new Color(255, 230, 100, 80));
            g2.fillOval(x - 16, y - 17, 32, 32);
            g2.setColor(new Color(235, 65, 48));
            g2.fillOval(x - 10, y - 10, 20, 21);
            g2.setColor(new Color(255, 150, 100));
            g2.fillOval(x - 6, y - 7, 7, 7);
            g2.setColor(new Color(48, 150, 65));
            g2.fillOval(x + 1, y - 15, 10, 7);
            g2.setColor(new Color(85, 65, 35));
            g2.drawLine(x, y - 10, x + 2, y - 15);
        }
    }

    private int getAlturaDino() {
        return abaixado && !pulando
                ? alturaAbaixado
                : alturaNormal;
    }

    private void atualizarJogo() {
        if (tela == TelaJogo.AVENTURA) {
            contadorAnimacaoMapaAventura++;
            double limite = Math.max(0, maiorFaseDesbloqueada - 1.0);
            if (progressoAnimacaoMapaAventura < limite) {
                progressoAnimacaoMapaAventura = Math.min(limite,
                        progressoAnimacaoMapaAventura + 0.012);
            }
            repaint();
            return;
        }
        if (tela == TelaJogo.SELECAO) {
            atualizarAnimacaoDinoMapa();
            repaint();
            return;
        }
        if (tela == TelaJogo.MENU) {
            if (telaInformativa == 0) {
                animacaoBotaoIniciar += 0.10;
            }
            repaint();
            return;
        }
        if (gameOver) {
            if (jogandoValeObstaculos()) {
                contadorReinicioVale++;
                if (contadorReinicioVale >= ESPERA_REINICIO_VALE) {
                    reiniciarJogo();
                }
            }
            repaint();
            return;
        }
        atualizarAnimacaoDino();
        atualizarFaiscas();
        atualizarEstrelasPontuacao();
        atualizarPoderFruta();
        atualizarEstrelasPoder();
        if (recebendoDano) {
            contadorDano++;
            if (contadorDano >= 8 * velocidadeAnimacao) {
                recebendoDano = false;
                gameOver = true;
                contadorReinicioVale = 0;
                // Morrer no Vale dos Obstáculos encerra a tentativa inteira.
                // O avanço 2 continua desbloqueado, mas o próximo início será do zero.
                limparTentativaValeAposMorte();
            }
            repaint();
            return;
        }
        int yChao = getHeight() - alturaTerreno;
        if (!inicializado && getHeight() > 0) {
            dinoY = yChao - alturaNormal;
            inicializado = true;
        }
        if (jogandoValeObstaculos()) {
            deslocarDinoNoVale();
        } else {
            if (direita) dinoX += velocidade;
            if (esquerda) dinoX -= velocidade;
            dinoX = Math.max(0, Math.min(dinoX, getWidth() - larguraDino));
        }
        double yAnterior = dinoY;
        dinoY += velocidadeY;
        velocidadeY += gravidade;
        if (jogandoValeObstaculos()) resolverColisoesVerticaisVale(yAnterior);
        if (dinoY + getAlturaDino() >= yChao) {
            dinoY = yChao - getAlturaDino();
            velocidadeY = 0;
            pulando = false;
            quantidadePulos = 0;
        }
        if (jogandoValeObstaculos()) {
            // Câmera e paisagem seguem o Dino, como em um jogo de plataforma lateral.
            movimentoNuvens = (cameraValeX * 0.13) % 900;
            movimentoMontanhas = (cameraValeX * 0.20) % 900;
            movimentoArvores = (cameraValeX * 0.36) % 900;
            movimentoChao = (cameraValeX * 0.60) % 900;
        } else {
            movimentoNuvens = (movimentoNuvens + 0.4) % 900;
            movimentoMontanhas = (movimentoMontanhas + 0.3) % 900;
            movimentoArvores = (movimentoArvores + 1.5) % 900;
            movimentoChao = (movimentoChao + 2.5) % 900;
        }
        pontuacao++;
        // Avanço 2 termina apenas quando o jogador realmente percorre a trilha.
        if (jogandoValeObstaculos()
                ? dinoMundoX >= COMPRIMENTO_VALE_OBSTACULOS - 700
                : pontuacao >= (fase == 1 ? META_VALE_VERDE : pontosParaProximaFase)) {
            portalAtivo = true;
            pedras.clear();
            contadorPedras = 0;
        }
        if (portalAtivo)
            animacaoPortal += 0.08;
        if (!portalAtivo && !(fase == 1 && etapaValeVerde == 2)) {
            contadorPedras++;
            int intervalo = fase == 1 ? 105 : 75;
            if (contadorPedras >= intervalo) {
                contadorPedras = 0;
                int tamanho = 35 + random.nextInt(20);
                pedras.add(new PedraRolante(
                        getWidth() + 20,
                        yChao - tamanho + 5,
                        tamanho,
                        tamanho
                ));
            }
        }
        double velocidadePedra = fase == 1 ? 4.8 + etapaValeVerde * 0.45 : 7.0;
        if (fase == 1 && etapaValeVerde == 2) pedras.clear();
        Iterator<PedraRolante> iterator = pedras.iterator();
        while (iterator.hasNext()) {
            PedraRolante pedra = iterator.next();
            pedra.x -= velocidadePedra;
            pedra.rotacao += 0.12;
            int baseDoChao = getHeight() - alturaTerreno;
            boolean dinoEstaNoAr = pulando
                    || dinoY + getAlturaDino() < baseDoChao - 8;
            boolean pedraFoiSuperada = pedra.x + pedra.largura < dinoX + 22;
            if (!pedra.pontuacaoConcedida && pedraFoiSuperada && dinoEstaNoAr) {
                pedra.pontuacaoConcedida = true;
                pontuacao += 50;
                criarEstrelasPontuacao(dinoX + larguraDino / 2, (int) dinoY + 20);
            }
            if (pedra.x + pedra.largura < 0)
                iterator.remove();
        }
        atualizarInimigosVale(yChao);
        if (jogandoValeObstaculos()) {
            atualizarInimigosVoadores();
            atualizarPlantasBolha();
            atualizarFrutasLiberadas();
        }
        verificarColisoes();
        verificarPortal();
        repaint();
    }

    private void criarEstrelasPontuacao(int centroX, int centroY) {
        Color[] cores = {
                new Color(255, 225, 70),
                new Color(255, 245, 160),
                new Color(255, 175, 45),
                new Color(255, 255, 235)
        };
        for (int i = 0; i < 9; i++) {
            double angulo = (Math.PI * 2 * i / 9) - Math.PI / 2;
            double velocidadeEstrela = 1.2 + random.nextDouble() * 2.0;
            estrelasPontuacao.add(new EstrelaPontuacao(
                    centroX, centroY,
                    Math.cos(angulo) * velocidadeEstrela,
                    Math.sin(angulo) * velocidadeEstrela - 0.5,
                    5 + random.nextInt(5),
                    30 + random.nextInt(12),
                    cores[random.nextInt(cores.length)]
            ));
        }
        mostrarBonusPulo = 42;
        bonusPuloX = centroX - 12;
        bonusPuloY = centroY - 8;
    }

    private void atualizarEstrelasPontuacao() {
        Iterator<EstrelaPontuacao> iterator = estrelasPontuacao.iterator();
        while (iterator.hasNext()) {
            EstrelaPontuacao estrela = iterator.next();
            estrela.x += estrela.vx;
            estrela.y += estrela.vy;
            estrela.vy += 0.035;
            estrela.vx *= 0.985;
            estrela.vida--;
            if (estrela.vida <= 0) iterator.remove();
        }
        if (mostrarBonusPulo > 0) {
            mostrarBonusPulo--;
            bonusPuloY--;
        }
    }

    private void desenharEstrelasPontuacao(Graphics2D g2) {
        if (estrelasPontuacao.isEmpty() && mostrarBonusPulo <= 0) return;
        Graphics2D brilho = (Graphics2D) g2.create();
        brilho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (EstrelaPontuacao estrela : estrelasPontuacao) {
            float fator = Math.max(0f, Math.min(1f, estrela.vida / (float) estrela.vidaMaxima));
            int alpha = (int) (255 * fator);
            brilho.setColor(new Color(
                    estrela.cor.getRed(), estrela.cor.getGreen(),
                    estrela.cor.getBlue(), alpha
            ));
            desenharEstrela(brilho, (int) estrela.x, (int) estrela.y, estrela.tamanho);
        }
        if (mostrarBonusPulo > 0) {
            int alpha = Math.min(255, mostrarBonusPulo * 7);
            brilho.setFont(new Font("Arial", Font.BOLD, 15));
            brilho.setColor(new Color(255, 235, 95, alpha));
            brilho.drawString("+50", bonusPuloX, bonusPuloY);
        }
        brilho.dispose();
    }

    private void verificarColisoes() {
        Rectangle corpoDino = new Rectangle(
                dinoX + 12,
                (int) dinoY + 8,
                Math.max(20, larguraDino - 24),
                Math.max(20, getAlturaDino() - 12)
        );

        // As colisões dos blocos são resolvidas com precisão no movimento vertical.

        // Obstáculos tradicionais continuam ativos nos outros avanços.
        if (!(fase == 1 && etapaValeVerde == 2)) {
            for (PedraRolante pedra : pedras) {
                Rectangle corpoPedra = new Rectangle(pedra.x + 5, pedra.y + 5,
                        pedra.largura - 10, pedra.altura - 10);
                if (corpoDino.intersects(corpoPedra)) {
                    aplicarDanoDino();
                    break;
                }
            }
        }

        if (!recebendoDano && !poderFrutaAtivo) {
            for (InimigoVale inimigo : inimigosVale) {
                Rectangle corpoInimigo = new Rectangle((int) inimigo.x + 5,
                        inimigo.y + 5, inimigo.tamanho - 8, inimigo.tamanho - 8);
                if (corpoDino.intersects(corpoInimigo)) {
                    aplicarDanoDino();
                    break;
                }
            }
        }

        if (!recebendoDano && !poderFrutaAtivo
                && fase == 1 && etapaValeVerde == 2) {
            for (InimigoVoador inimigo : inimigosVoadores) {
                Rectangle areaInimigo = new Rectangle((int) (inimigo.x - cameraValeX) + 4,
                        (int) inimigo.y + 5, inimigo.largura - 8, inimigo.altura - 8);
                if (corpoDino.intersects(areaInimigo)) {
                    aplicarDanoDino();
                    break;
                }
            }
            if (!recebendoDano) {
                for (PlantaBolha planta : plantasBolha) {
                    Rectangle areaPlanta = new Rectangle(
                            (int) (planta.x - cameraValeX) + 5,
                            planta.getYAtual() + 5,
                            Math.max(12, planta.largura - 10),
                            Math.max(18, planta.altura - 6)
                    );
                    if (corpoDino.intersects(areaPlanta)) {
                        aplicarDanoDino();
                        break;
                    }
                }
            }
            if (!recebendoDano) {
                for (PlantaEspinhoMarrom planta : plantasEspinhoMarrom) {
                    Rectangle areaPlanta = new Rectangle(
                            (int) (planta.x - cameraValeX) + 4,
                            planta.y + 4,
                            Math.max(16, planta.largura - 8),
                            Math.max(24, planta.altura - 4)
                    );
                    if (corpoDino.intersects(areaPlanta)) {
                        aplicarDanoDino();
                        break;
                    }
                }
            }
            if (!recebendoDano) {
                for (BolhaExplosiva bolha : bolhasExplosivas) {
                    Rectangle area;
                    if (bolha.explodindo()) {
                        int r = bolha.getRaioExplosao();
                        area = new Rectangle(bolha.getX() - r - (int) cameraValeX, bolha.getY() - r, r * 2, r * 2);
                    } else {
                        int r = 7;
                        area = new Rectangle(bolha.getX() - r - (int) cameraValeX, bolha.getY() - r, r * 2, r * 2);
                    }
                    if (corpoDino.intersects(area)) {
                        aplicarDanoDino();
                        break;
                    }
                }
            }
            if (!recebendoDano) {
                for (TroncoCacto tronco : troncosCacto) {
                    Rectangle areaCacto = new Rectangle(
                            (int) (tronco.getCactoX() - cameraValeX) - 10,
                            tronco.getCactoTopoY(),
                            tronco.cactoLargura + 20,
                            tronco.cactoAltura + 8
                    );
                    if (corpoDino.intersects(areaCacto)) {
                        aplicarDanoDino();
                        break;
                    }
                }
            }
        }
    }

    private void aplicarDanoDino() {
        if (poderFrutaAtivo || recebendoDano) return;
        criarFaiscas(dinoX + larguraDino / 2,
                (int) dinoY + getAlturaDino() / 2);
        recebendoDano = true;
        contadorDano = 0;
        animacaoAtual = danoFrames;
        quadroAnimacao = contadorAnimacao = 0;
        direita = esquerda = false;
    }

    private void inicializarBlocosVale() {
        blocosVale.clear();
        frutasLiberadas.clear();
        blocosVale.addAll(GeradorBlocosVale.criar());
        inicializarPlantasBolha();
        inicializarPlantasEspinhoMarrom();
        inicializarTroncosCacto();
        // Restaura blocos ja esvaziados se a tentativa foi salva e nao houve morte.
        restaurarBlocosVale();
        objetosAvanco2Inicializados = true;
    }

    private void inicializarPlantasBolha() {
        plantasBolha.clear();
        int yChao = (getHeight() > 0 ? getHeight() : 563) - alturaTerreno;
        for (int i = 0, x = 760; x < COMPRIMENTO_VALE_OBSTACULOS - 1050; i++, x += 1280) {
            // As verdes agora sao cerca de 30% maiores. Permanecem fixas no chao.
            plantasBolha.add(new PlantaBolha(x, yChao - 54, 46, 54, i * 0.7, false));
            if (i % 2 == 0) {
                plantasBolha.add(new PlantaBolha(x + 285, yChao - 50, 42, 50,
                        i * 0.7 + 1.4, false));
            }
            // Novo tipo: planta vermelha que salta em sua posicao e volta ao solo.
            if (i % 2 == 1) {
                plantasBolha.add(new PlantaBolha(x + 610, yChao - 56, 48, 56,
                        i * 0.45, true));
            }
        }
    }

    private void inicializarPlantasEspinhoMarrom() {
        plantasEspinhoMarrom.clear();
        bolhasExplosivas.clear();
        int yChao = (getHeight() > 0 ? getHeight() : 563) - alturaTerreno;
        for (int i = 0, x = 1420; x < COMPRIMENTO_VALE_OBSTACULOS - 1350; i++, x += 2120) {
            PlantaEspinhoMarrom planta = new PlantaEspinhoMarrom(x, yChao - 62, 56, 62, i * 1.55);
            plantasEspinhoMarrom.add(planta);
            bolhasExplosivas.add(new BolhaExplosiva(planta, -8, 0));
            bolhasExplosivas.add(new BolhaExplosiva(planta, 9, 24));
        }
    }

    private void inicializarTroncosCacto() {
        troncosCacto.clear();
        int yChao = (getHeight() > 0 ? getHeight() : 563) - alturaTerreno;
        int[] alturas = {168, 210, 186, 228};
        for (int i = 0, x = 980; x < COMPRIMENTO_VALE_OBSTACULOS - 900; i++, x += 1750) {
            int y = yChao - alturas[i % alturas.length];
            // Cacto menor, centrado sobre a tora verde-clara.
            troncosCacto.add(new TroncoCacto(x, y, 118, 26,
                    26 + (i % 2) * 4, 36 + (i % 3) * 3, i * 1.35));
        }
    }

    private void atualizarPlantasBolha() {
        for (PlantaBolha planta : plantasBolha) {
            planta.animacao += 0.08;
        }
        for (PlantaEspinhoMarrom planta : plantasEspinhoMarrom) {
            planta.animacao += 0.06;
        }
        for (BolhaExplosiva bolha : bolhasExplosivas) {
            bolha.tempo++;
            if (bolha.explodindo()) {
                if (bolha.tempo > 82) bolha.reiniciar();
            } else {
                bolha.y = bolha.planta.y + 16 - (bolha.tempo * 1.45);
                if (bolha.tempo >= 58) {
                    bolha.y -= 4;
                }
            }
        }
        // Tronco e cacto oscilam juntos, sem alterar a posição de outros obstáculos.
        for (TroncoCacto tronco : troncosCacto) {
            tronco.animacao += 0.035;
        }
    }

    private void ativarPoderFruta() {
        poderFrutaAtivo = true;
        tempoPoderFruta = DURACAO_PODER_FRUTA;
        escalaCrescimentoFruta = 1.10;
        estrelasPoder.clear();
        for (int i = 0; i < 8; i++) {
            estrelasPoder.add(new EstrelaPoder(
                    dinoX + larguraDino / 2 + random.nextInt(25) - 12,
                    (int) dinoY - 4 - random.nextInt(20),
                    (random.nextDouble() - 0.5) * 0.7,
                    -0.8 - random.nextDouble() * 1.1,
                    24 + random.nextInt(20)
            ));
        }
        criarFaiscas(dinoX + larguraDino / 2, (int) dinoY);
    }

    private void atualizarPoderFruta() {
        if (!poderFrutaAtivo) return;
        tempoPoderFruta--;
        // O crescimento diminui suavemente nos instantes finais do poder.
        if (tempoPoderFruta <= 90) {
            double fator = Math.max(0.0, tempoPoderFruta / 90.0);
            escalaCrescimentoFruta = 1.0 + 0.10 * fator;
        }
        if (tempoPoderFruta <= 0) {
            poderFrutaAtivo = false;
            escalaCrescimentoFruta = 1.0;
            estrelasPoder.clear();
        }
    }

    private void atualizarEstrelasPoder() {
        if (fase == 1 && etapaValeVerde == 2 && poderFrutaAtivo
                && random.nextInt(5) == 0) {
            estrelasPoder.add(new EstrelaPoder(
                    dinoX + larguraDino / 2 + random.nextInt(30) - 15,
                    (int) dinoY - 2,
                    (random.nextDouble() - 0.5) * 0.8,
                    -1.0 - random.nextDouble() * 1.2,
                    30 + random.nextInt(18)
            ));
        }
        Iterator<EstrelaPoder> it = estrelasPoder.iterator();
        while (it.hasNext()) {
            EstrelaPoder estrela = it.next();
            estrela.x += estrela.vx;
            estrela.y += estrela.vy;
            estrela.vida--;
            if (estrela.vida <= 0 || !poderFrutaAtivo) it.remove();
        }
    }

    private void desenharEstrelasPoder(Graphics2D g2) {
        for (EstrelaPoder estrela : estrelasPoder) {
            float fade = Math.min(1f, estrela.vida / 18f);
            if (poderFrutaAtivo && tempoPoderFruta < 180) {
                fade *= Math.max(0.08f, tempoPoderFruta / 180f);
            }
            int alpha = Math.max(0, Math.min(255, (int) (230 * fade)));
            g2.setColor(new Color(255, 225, 70, alpha));
            desenharEstrela(g2, (int) estrela.x, (int) estrela.y, 5);
        }
    }

    private void atualizarInimigosVoadores() {
        if (inimigosVoadores.isEmpty()) {
            for (int x = 640, i = 0; x < COMPRIMENTO_VALE_OBSTACULOS - 850;
                 x += 1050, i++) {
                // Inimigos ficam espalhados pelas regiões sucessivas do vale.
                inimigosVoadores.add(new InimigoVoador(x,
                        314 - (i % 3) * 18, 54, 30, i % 2 == 0 ? 1.1 : -1.1));
            }
        }
        for (InimigoVoador inimigo : inimigosVoadores) {
            inimigo.faseAnimacao += 0.12;
            inimigo.x += inimigo.velocidade;
            if (inimigo.x < inimigo.xInicial - 75
                    || inimigo.x > inimigo.xInicial + 75) {
                inimigo.velocidade *= -1;
            }
            inimigo.y = inimigo.yBase + (int) (Math.sin(inimigo.faseAnimacao) * 9);
        }
    }

    private void atualizarInimigosVale(int yChao) {
        if (fase != 1 || etapaValeVerde < 4 || portalAtivo) return;
        contadorInimigosVale++;
        int intervalo = etapaValeVerde >= 4 ? 145 : 210;
        if (contadorInimigosVale >= intervalo) {
            contadorInimigosVale = 0;
            int tamanho = etapaValeVerde >= 4 ? 48 : 40;
            inimigosVale.add(new InimigoVale(getWidth() + 25, yChao - tamanho, tamanho,
                    etapaValeVerde >= 4 ? 3.4 : 2.6));
        }
        Iterator<InimigoVale> it = inimigosVale.iterator();
        while (it.hasNext()) {
            InimigoVale inimigo = it.next();
            inimigo.x -= inimigo.velocidade + (etapaValeVerde >= 4 ? 1.0 : 0.0);
            inimigo.animacao += 0.16;
            if (inimigo.x + inimigo.tamanho < -10) it.remove();
        }
    }

    private void criarFaiscas(int centroX, int centroY) {
        faiscas.clear();
        for (int i = 0; i < 26; i++) {
            double angulo = (Math.PI * 2 * i / 26)
                    + (random.nextDouble() - 0.5) * 0.35;
            double velocidadeParticula = 2.0 + random.nextDouble() * 4.5;
            double vx = Math.cos(angulo) * velocidadeParticula;
            double vy = Math.sin(angulo) * velocidadeParticula - 1.2;
            Color[] cores = {
                    new Color(255, 245, 150),
                    new Color(255, 205, 55),
                    new Color(255, 135, 25),
                    new Color(255, 255, 235)
            };
            Color cor = cores[random.nextInt(cores.length)];
            int tamanho = 3 + random.nextInt(5);
            int duracao = 22 + random.nextInt(17);
            faiscas.add(new Faisca(
                    centroX,
                    centroY,
                    vx,
                    vy,
                    tamanho,
                    duracao,
                    cor
            ));
        }
    }

    private void atualizarFaiscas() {
        Iterator<Faisca> iterator = faiscas.iterator();
        while (iterator.hasNext()) {
            Faisca faisca = iterator.next();
            faisca.x += faisca.vx;
            faisca.y += faisca.vy;
            faisca.vy += 0.16;
            faisca.vx *= 0.97;
            faisca.vida--;
            if (faisca.vida <= 0) {
                iterator.remove();
            }
        }
    }

    private void desenharFaiscas(Graphics2D g2) {
        if (faiscas.isEmpty()) return;
        Graphics2D brilho = (Graphics2D) g2.create();
        brilho.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        for (Faisca faisca : faiscas) {
            float proporcao = Math.max(
                    0f,
                    Math.min(1f, faisca.vida / (float) faisca.vidaMaxima)
            );
            int alpha = (int) (255 * proporcao);
            int x = (int) faisca.x;
            int y = (int) faisca.y;
            brilho.setStroke(new BasicStroke(
                    Math.max(1f, faisca.tamanho / 2f),
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));
            brilho.setColor(new Color(
                    faisca.cor.getRed(),
                    faisca.cor.getGreen(),
                    faisca.cor.getBlue(),
                    Math.max(0, alpha)
            ));
            brilho.drawLine(
                    x,
                    y,
                    (int) (x - faisca.vx * 2.2),
                    (int) (y - faisca.vy * 2.2)
            );
            brilho.setColor(new Color(
                    255,
                    255,
                    225,
                    Math.max(0, alpha)
            ));
            int tamanhoCentro = Math.max(2, faisca.tamanho / 2);
            brilho.fillOval(
                    x - tamanhoCentro / 2,
                    y - tamanhoCentro / 2,
                    tamanhoCentro,
                    tamanhoCentro
            );
        }
        brilho.dispose();
    }

    private void verificarPortal() {
        if (!portalAtivo) return;
        int yChao = getHeight() - alturaTerreno;
        Rectangle corpoDino = new Rectangle(
                dinoX + 15,
                (int) dinoY + 10,
                larguraDino - 30,
                getAlturaDino() - 15
        );
        Rectangle areaPortal = new Rectangle(
                jogandoValeObstaculos()
                        ? (int) Math.round(COMPRIMENTO_VALE_OBSTACULOS - 210 - cameraValeX)
                        : getWidth() - 210,
                yChao - 240, 160, 240
        );
        if (corpoDino.intersects(areaPortal)) {
            if (fase == 1 && etapaValeVerde < 5) {
                avancarEtapaValeVerde();
            } else {
                concluirFaseAtual();
            }
        }
    }

    private void avancarEtapaValeVerde() {
        etapaValeVerde++;
        checkpointVale = 0;
        pontuacao = 0;
        dinoMundoX = 75;
        cameraValeX = 0;
        // O novo avanço deve começar com os próprios blocos intactos.
        progresso.limparBlocosVale();
        salvarProgressoAutomatico();
        if (fase == 1 && etapaValeVerde == 2) {
            movimentoNuvens = 0;
            movimentoMontanhas = 0;
            movimentoArvores = 0;
            movimentoChao = 0;
        }
        pontuacao = 0;
        portalAtivo = false;
        animacaoPortal = 0;
        pedras.clear();
        inimigosVale.clear();
        inimigosVoadores.clear();
        blocosVale.clear();
        frutasLiberadas.clear();
        objetosAvanco2Inicializados = false;
        poderFrutaAtivo = false;
        tempoPoderFruta = 0;
        escalaCrescimentoFruta = 1.0;
        estrelasPoder.clear();
        inicializarBlocosVale();
        contadorPedras = 0;
        contadorInimigosVale = 0;
        dinoX = 75;
        dinoY = getHeight() - alturaTerreno - alturaNormal;
        velocidadeY = 0;
        quantidadePulos = 0;
        espacoPressionado = false;
        pulando = abaixado = direita = esquerda = false;
        // A próxima etapa continua sendo a fase 1-1; não altera o desbloqueio.
        System.out.println("VALE VERDE — avanço " + etapaValeVerde + " de 5 iniciado.");
        repaint();
    }

    private void reiniciarJogo() {
        // 'D' depois da derrota reinicia TODO o avanço 2, nunca o checkpoint.
        // Os outros avanços conservam sua forma anterior de reiniciar.
        if (jogandoValeObstaculos()) {
            limparTentativaValeAposMorte();
        }
        dinoMundoX = 75;
        cameraValeX = 0;
        quantidadePulos = 0;
        espacoPressionado = false;
        frutasLiberadas.clear();
        poderFrutaAtivo = false;
        tempoPoderFruta = 0;
        escalaCrescimentoFruta = 1.0;
        estrelasPoder.clear();
        inimigosVoadores.clear();

        pontuacao = 0;
        fase = Math.max(1, Math.min(10, fase));
        portalAtivo = false;
        animacaoPortal = 0;
        dinoX = jogandoValeObstaculos() ? 75 : 100;
        dinoY = getHeight() - alturaTerreno - alturaNormal;
        velocidadeY = 0;
        pulando = false;
        abaixado = false;
        direita = false;
        esquerda = false;
        pedras.clear();
        inimigosVale.clear();
        inimigosVoadores.clear();
        // Recria os blocos apenas nesta nova tentativa (todos com seus itens).
        inicializarBlocosVale();
        contadorPedras = 0;
        contadorInimigosVale = 0;
        faiscas.clear();
        gameOver = false;
        recebendoDano = false;
        contadorDano = 0;
        contadorReinicioVale = 0;
        quadroAnimacao = 0;
        contadorAnimacao = 0;
        animacaoAtual = paradoFrames;
        inicializado = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        if (tela == TelaJogo.AVENTURA) {
            renderizadorMapas().desenharMapaAventuraCompleto(g2);
            g2.dispose();
            return;
        }
        if (tela == TelaJogo.SELECAO) {
            renderizadorMapas().desenharMapaMundos(g2);
            g2.dispose();
            return;
        }
        if (tela == TelaJogo.MENU) {
            BufferedImage imagemTela = menu;
            if (telaInformativa == 1)
                imagemTela = imagemConfiguracoes;
            if (telaInformativa == 2)
                imagemTela = imagemComoJogar;
            if (imagemTela != null) {
                g2.drawImage(
                        imagemTela,
                        0, 0,
                        getWidth(),
                        getHeight(),
                        this
                );
            } else {
                g2.setColor(new Color(35, 75, 35));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.drawString("Imagem não encontrada.", 40, 100);
            }
            if (telaInformativa == 0 && imagemTela != null) {
                desenharAnimacaoBotaoIniciar(g2);
            }
            if (imagemTela != null && opcaoMenuEmDestaque >= 0) {
                desenharDestaqueOpcaoMenu(g2, imagemTela);
            }
            g2.dispose();
            return;
        }
        int largura = getWidth();
        int altura = getHeight();
        int yChao = altura - alturaTerreno;
        g2.setColor(new Color(135, 206, 235));
        g2.fillRect(0, 0, largura, altura);
        g2.setColor(new Color(255, 220, 70));
        g2.fillOval(650, 40, 85, 85);
        for (int i = -1; i < 4; i++) {
            int x = i * 300 - (int) movimentoNuvens;
            desenharNuvem(g2, x, 75);
            desenharNuvem(g2, x + 120, 115);
        }
        for (int i = -1; i < 3; i++) {
            int x = i * 450 - (int) movimentoMontanhas;
            desenharMontanha(g2, x, yChao, 180, 210);
        }
        for (int i = -1; i < 4; i++) {
            int x = i * 440 + 105 - (int) movimentoMontanhas;
            desenharArvoreFrutifera(g2, x, yChao, 0.90);
        }
        for (int i = -1; i < 5; i++) {
            int x = i * 220 - (int) movimentoArvores;
            desenharArvore(g2, x, yChao, 1.0);
        }
        g2.setColor(new Color(133, 83, 43));
        g2.fillRect(0, yChao, largura, alturaTerreno);
        g2.setColor(new Color(111, 67, 36));
        for (int i = 0; i < 12; i++) {
            int x = i * 110 - (int) movimentoChao;
            x = Math.floorMod(x, largura + 110) - 55;
            g2.fillOval(
                    x,
                    yChao + alturaGrama + 10 + (i % 3) * 9,
                    35,
                    5
            );
        }
        desenharGrama(g2, largura, yChao, alturaGrama, movimentoChao);
        desenharPedrinhasTerra(g2, largura, yChao, alturaGrama, movimentoChao);
        desenharMinhocas(g2, largura, yChao, alturaGrama, movimentoChao);
        if (fase == 1 && etapaValeVerde == 3) {
            // Rio atravessando o vale; as ilhas de pedra funcionam como plataformas visuais.
            int rioY = yChao - 5;
            g2.setColor(new Color(35, 155, 205, 220));
            g2.fillRect(0, rioY, largura, 38);
            g2.setColor(new Color(155, 235, 245, 210));
            for (int i = 0; i < 8; i++) {
                int px = Math.floorMod(i * 155 - (int) movimentoChao, largura + 120) - 60;
                g2.fillOval(px, rioY + 7 + (i % 2) * 12, 72, 9);
            }
            g2.setColor(new Color(115, 95, 65));
            for (int i = 0; i < 5; i++) {
                int px = 70 + i * 205;
                g2.fillRoundRect(px, yChao - 12 - (i % 2) * 8, 68, 18, 14, 14);
            }
        }
        if (jogandoValeObstaculos()) {
            Graphics2D mundo = (Graphics2D) g2.create();
            mundo.translate(-cameraValeX, 0);
            RenderizadorVale.desenharCenarioAvanco2(mundo, cameraValeX, getWidth(), getHeight(), alturaTerreno);
            RenderizadorVale.desenharBlocosSuspensos(mundo, blocosVale, cameraValeX, getWidth());
            RenderizadorVale.desenharPlantasBolha(mundo, plantasBolha, cameraValeX, getWidth());
            RenderizadorVale.desenharPlantasEspinhoMarrom(mundo, plantasEspinhoMarrom, bolhasExplosivas, cameraValeX, getWidth());
            RenderizadorVale.desenharTroncosCacto(mundo, troncosCacto, cameraValeX, getWidth());
            desenharFrutasLiberadas(mundo);
            RenderizadorVale.desenharInimigosVoadores(mundo, inimigosVoadores);
            mundo.dispose();
            desenharEstrelasPoder(g2);
        }
        desenharPortal(g2);
        for (PedraRolante pedra : pedras)
            desenharPedraRolante(g2, pedra);
        RenderizadorVale.desenharInimigosVale(g2, inimigosVale);
        desenharDinossauro(g2);
        desenharFaiscas(g2);
        desenharEstrelasPontuacao(g2);
        ResumoPartida resumo = new ResumoPartida(fase, etapaValeVerde, pontuacao,
                portalAtivo, dinoMundoX);
        new RenderizadorHud(resumo, getWidth(), getHeight()).desenhar(g2, gameOver);
        g2.dispose();
    }

    private void desenharDestaqueOpcaoMenu(Graphics2D g2, BufferedImage imagemTela) {
        double[] limites;
        if (telaInformativa != 0) {
            if (opcaoMenuEmDestaque != 4) return;
            limites = new double[]{0.32, 0.70, 0.79, 0.96};
        } else {
            switch (opcaoMenuEmDestaque) {
                case 0: limites = new double[]{0.45, 0.83, 0.45, 0.62}; break;
                case 1: limites = new double[]{0.51, 0.79, 0.625, 0.74}; break;
                case 2: limites = new double[]{0.51, 0.79, 0.745, 0.85}; break;
                case 3: limites = new double[]{0.51, 0.79, 0.855, 0.98}; break;
                default: return;
            }
        }
        int x1 = (int) (limites[0] * getWidth());
        int x2 = (int) (limites[1] * getWidth());
        int y1 = (int) (limites[2] * getHeight());
        int y2 = (int) (limites[3] * getHeight());
        int margemX = Math.max(3, (int) ((x2 - x1) * 0.035));
        int margemY = Math.max(2, (int) ((y2 - y1) * 0.06));
        int sx1 = Math.max(0, (int) (x1 * imagemTela.getWidth() / (double) getWidth()));
        int sx2 = Math.min(imagemTela.getWidth(),
                (int) (x2 * imagemTela.getWidth() / (double) getWidth()));
        int sy1 = Math.max(0, (int) (y1 * imagemTela.getHeight() / (double) getHeight()));
        int sy2 = Math.min(imagemTela.getHeight(),
                (int) (y2 * imagemTela.getHeight() / (double) getHeight()));
        Graphics2D hover = (Graphics2D) g2.create();
        hover.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        hover.setColor(new Color(255, 224, 105, 65));
        hover.fillRoundRect(x1 - margemX, y1 - margemY,
                (x2 - x1) + margemX * 2, (y2 - y1) + margemY * 2, 18, 18);
        int dx1 = x1 - margemX;
        int dy1 = y1 - margemY;
        int dx2 = x2 + margemX;
        int dy2 = y2 + margemY;
        hover.drawImage(imagemTela,
                dx1, dy1, dx2, dy2,
                sx1, sy1, sx2, sy2,
                this);
        hover.setColor(new Color(255, 232, 130, 185));
        hover.setStroke(new BasicStroke(1.6f));
        hover.drawRoundRect(dx1, dy1, dx2 - dx1, dy2 - dy1, 18, 18);
        hover.dispose();
    }

    private void desenharPiscadaDinoMenu(Graphics2D g2) {
        Graphics2D olho = (Graphics2D) g2.create();
        olho.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        double sx = getWidth() / 994.0;
        double sy = getHeight() / 565.0;
        int x = (int) (173 * sx);
        int y = (int) (106 * sy);
        int w = (int) (48 * sx);
        int h = (int) (36 * sy);
        olho.setColor(new Color(74, 151, 39));
        olho.fillOval(x, y + (int) (4 * sy), w, h - (int) (5 * sy));
        olho.setColor(new Color(103, 184, 49));
        olho.setStroke(new BasicStroke(Math.max(2.0f, (float) (4 * sx)),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        java.awt.geom.QuadCurve2D dobra = new java.awt.geom.QuadCurve2D.Double(
                x + 4 * sx, y + 20 * sy,
                x + 24 * sx, y + 30 * sy,
                x + 44 * sx, y + 18 * sy
        );
        olho.draw(dobra);
        olho.setColor(new Color(35, 91, 34));
        olho.setStroke(new BasicStroke(Math.max(1.5f, (float) (2.2 * sx)),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        java.awt.geom.QuadCurve2D linhaOlho = new java.awt.geom.QuadCurve2D.Double(
                x + 5 * sx, y + 20 * sy,
                x + 24 * sx, y + 27 * sy,
                x + 43 * sx, y + 19 * sy
        );
        olho.draw(linhaOlho);
        olho.dispose();
    }

    private void desenharAnimacaoBotaoIniciar(Graphics2D g2) {
        Graphics2D botao = (Graphics2D) g2.create();
        double pulso = (Math.sin(animacaoBotaoIniciar) + 1.0) / 2.0;
        float espessura = (float) (2.0 + pulso * 2.0);
        int alpha = (int) (75 + pulso * 125);
        int x = (int) (getWidth() * 0.45);
        int y = (int) (getHeight() * 0.45);
        int largura = (int) (getWidth() * 0.38);
        int altura = (int) (getHeight() * 0.17);
        botao.setColor(new Color(255, 220, 75, alpha / 3));
        botao.setStroke(new BasicStroke(espessura + 5,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        botao.drawRoundRect(x + 2, y + 2, largura - 4, altura - 4, 20, 20);
        botao.setColor(new Color(255, 225, 95, alpha));
        botao.setStroke(new BasicStroke(espessura,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        botao.drawRoundRect(x + 2, y + 2, largura - 4, altura - 4, 18, 18);
        int deslocamento = (int) (Math.sin(animacaoBotaoIniciar * 1.7) * 4);
        int centroX = x + 19;
        int centroY = y + altura / 2 + deslocamento;
        Polygon seta = new Polygon(
                new int[]{centroX - 5, centroX + 5, centroX + 5, centroX + 12,
                        centroX, centroX - 12, centroX - 5},
                new int[]{centroY - 7, centroY - 7, centroY - 3, centroY,
                        centroY + 9, centroY, centroY - 3},
                7
        );
        botao.setColor(new Color(255, 235, 125, alpha));
        botao.fillPolygon(seta);
        botao.dispose();
    }

    private RenderizadorMapas renderizadorMapas() {
        Point mouse = null;
        try {
            mouse = getMousePosition();
        } catch (HeadlessException ignored) {
            // Renderização offscreen dos testes não tem ponteiro de mouse.
        }
        boolean voltarHover = mouse != null && clicouBotaoVoltarFolha(mouse.x, mouse.y);
        DadosMapa dados = new DadosMapa(maiorFaseDesbloqueada, faseSelecionadaNoMapa,
                mundoMapaSelecionado, abaMundoEmDestaque, opcaoMenuEmDestaque,
                imagensFases, imagemMapaAventura, dinoAntigo, progressoAnimacaoMapaAventura,
                contadorAnimacaoMapaAventura, mapaAventuraHover, voltarHover);
        return new RenderizadorMapas(dados, getWidth(), getHeight());
    }

    private boolean clicouBotaoMapaAventura(int x, int y) {
        int largura = 176;
        int altura = 42;
        int bx = (getWidth() - largura) / 2;
        int by = getHeight() - 82;
        return x >= bx && x <= bx + largura && y >= by && y <= by + altura;
    }

    private boolean clicouBotaoVoltarMapaAventura(int x, int y) {
        int bx = getWidth() - 154;
        int by = getHeight() - 48;
        return x >= bx && x <= bx + 132 && y >= by && y <= by + 38;
    }

    private boolean clicouBotaoVoltarFolha(int x, int y) {
        int bx = getWidth() - 154;
        int by = getHeight() - 48;
        return x >= bx && x <= bx + 132 && y >= by && y <= by + 38;
    }

    private void desenharDinossauro(Graphics2D g2) {
        BufferedImage quadro = null;
        if (animacaoAtual != null && quadroAnimacao >= 0
                && quadroAnimacao < animacaoAtual.length) {
            quadro = animacaoAtual[quadroAnimacao];
        }
        if (quadro == null && animacaoAtual != null) {
            for (int i = 0; i < animacaoAtual.length; i++) {
                int indice = (quadroAnimacao + i) % animacaoAtual.length;
                if (animacaoAtual[indice] != null) {
                    quadro = animacaoAtual[indice];
                    break;
                }
            }
        }
        if (quadro == null) quadro = dinoAntigo;
        if (quadro == null) quadro = primeiroQuadro(paradoFrames);
        if (quadro == null) quadro = primeiroQuadro(direitaFrames);
        if (quadro == null) quadro = primeiroQuadro(esquerdaFrames);
        if (quadro == null) return;
        int largura = (int) Math.round(84 * escalaCrescimentoFruta);
        int altura = (int) Math.round(76 * escalaCrescimentoFruta);
        int x = dinoX - 3;
        int y = (int) dinoY + 2;
        if (abaixado && !pulando) {
            altura = (int) Math.round(52 * escalaCrescimentoFruta);
            y += 14;
        }
        boolean corrigirSomenteCabeca =
                (direita || esquerda)
                        && !pulando
                        && !abaixado
                        && !recebendoDano;
        BufferedImage cabecaParada = primeiroQuadro(paradoFrames);
        if (corrigirSomenteCabeca
                && cabecaParada != null
                && quadro != cabecaParada
                && quadro.getWidth() == cabecaParada.getWidth()
                && quadro.getHeight() == cabecaParada.getHeight()) {
            int larguraOriginal = quadro.getWidth();
            int alturaOriginal = quadro.getHeight();
            int[] pixelsAndando = quadro.getRGB(
                    0, 0, larguraOriginal, alturaOriginal,
                    null, 0, larguraOriginal
            );
            int[] pixelsCabeca = cabecaParada.getRGB(
                    0, 0, larguraOriginal, alturaOriginal,
                    null, 0, larguraOriginal
            );
            boolean olhandoEsquerda = esquerda && !direita;
            int limiteX = (int) (larguraOriginal * 0.62);
            int limiteY = (int) (alturaOriginal * 0.59);
            for (int py = 0; py < limiteY; py++) {
                for (int px = 0; px < larguraOriginal; px++) {
                    int origemX = olhandoEsquerda
                            ? larguraOriginal - 1 - px
                            : px;
                    boolean dentroDaCabeca = olhandoEsquerda
                            ? px < limiteX
                            : px >= larguraOriginal - limiteX;
                    if (!dentroDaCabeca) continue;
                    int indiceDestino = py * larguraOriginal + px;
                    int indiceOrigem = py * larguraOriginal + origemX;
                    pixelsAndando[indiceDestino] = pixelsCabeca[indiceOrigem];
                }
            }
            BufferedImage dinoComCabecaCorrigida = new BufferedImage(
                    larguraOriginal,
                    alturaOriginal,
                    BufferedImage.TYPE_INT_ARGB
            );
            dinoComCabecaCorrigida.setRGB(
                    0, 0,
                    larguraOriginal, alturaOriginal,
                    pixelsAndando, 0, larguraOriginal
            );
            g2.drawImage(
                    dinoComCabecaCorrigida,
                    x, y, largura, altura,
                    this
            );
        } else {
            g2.drawImage(quadro, x, y, largura, altura, this);
        }
    }

    private void desenharPortal(Graphics2D g2) {
        if (!portalAtivo || fase != 1)
            return;
        int yChao = getHeight() - alturaTerreno;
        int x = jogandoValeObstaculos()
                ? (int) Math.round(COMPRIMENTO_VALE_OBSTACULOS - 245 - cameraValeX)
                : getWidth() - 245;
        int y = yChao - 270;
        int brilho = (int) (12 + Math.sin(animacaoPortal) * 9);
        g2.setColor(new Color(140, 50, 255, 55));
        g2.fillOval(
                x - brilho,
                y - brilho,
                230 + brilho * 2,
                270 + brilho * 2
        );
        if (portal != null) {
            g2.drawImage(portal, x, y, 230, 270, this);
        } else {
            g2.setColor(new Color(100, 40, 255));
            g2.fillOval(x + 20, y + 10, 190, 260);
        }
        desenharMensagemPortal(g2, x, y);
    }

    private void desenharMensagemPortal(Graphics2D g2, int x, int y) {
        int largura = 340;
        int cx = Math.max(
                10,
                Math.min(x + 115 - largura / 2, getWidth() - largura - 10)
        );
        int cy = y - 95;
        g2.setColor(new Color(20, 15, 55, 230));
        g2.fillRoundRect(cx, cy, largura, 82, 22, 22);
        g2.setColor(new Color(170, 100, 255));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cx, cy, largura, 82, 22, 22);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(255, 220, 70));
        centralizarTexto(g2, "PARABÉNS!", cx, cy + 25, largura);
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(Color.WHITE);
        centralizarTexto(
                g2,
                "Você passou para o próximo desafio!",
                cx,
                cy + 49,
                largura
        );
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(120, 220, 255));
        centralizarTexto(g2, "Preparado(a)?", cx, cy + 70, largura);
    }

    private void desenharPedraRolante(
            Graphics2D g2,
            PedraRolante pedra) {
        Graphics2D pg = (Graphics2D) g2.create();
        int cx = pedra.x + pedra.largura / 2;
        int cy = pedra.y + pedra.altura / 2;
        pg.rotate(pedra.rotacao, cx, cy);
        pg.setColor(new Color(85, 85, 85));
        pg.fillOval(
                pedra.x,
                pedra.y,
                pedra.largura,
                pedra.altura
        );
        pg.setColor(new Color(145, 145, 140));
        pg.fillOval(
                pedra.x + 7,
                pedra.y + 5,
                pedra.largura / 2,
                pedra.altura / 2
        );
        pg.setColor(new Color(65, 65, 65));
        pg.drawOval(
                pedra.x,
                pedra.y,
                pedra.largura,
                pedra.altura
        );
        pg.dispose();
    }

}
