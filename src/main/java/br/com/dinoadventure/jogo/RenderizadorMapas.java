package br.com.dinoadventure.jogo;

import java.awt.*;
import java.awt.image.BufferedImage;
import static br.com.dinoadventure.grafico.DesenhosCenario.*;

/** Desenho das telas de seleção e aventura, independente de eventos Swing. */
final class RenderizadorMapas {
    private final DadosMapa dados;
    private final int larguraTela;
    private final int alturaTela;

    RenderizadorMapas(DadosMapa dados, int larguraTela, int alturaTela) {
        this.dados = dados;
        this.larguraTela = larguraTela;
        this.alturaTela = alturaTela;
    }

    void desenharMapaMundos(Graphics2D g2) {
        int w = larguraTela;
        int h = alturaTela;
        Graphics2D mapa = (Graphics2D) g2.create();
        mapa.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        mapa.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        desenharPaisagemSelecao(mapa, w, h);

        // Placa de cabeçalho para destacar a escrita sobre o céu e a paisagem.
        mapa.setColor(new Color(255, 246, 211, 218));
        mapa.fillRoundRect(w / 2 - 365, 5, 730, 57, 22, 22);
        mapa.setColor(new Color(115, 75, 34, 220));
        mapa.setStroke(new BasicStroke(2.5f));
        mapa.drawRoundRect(w / 2 - 365, 5, 730, 57, 22, 22);

        mapa.setFont(new Font("Serif", Font.BOLD, 31));
        desenharTextoCentralComContorno(mapa, "ESCOLHA SUA PRÓXIMA AVENTURA",
                0, 34, w, new Color(20, 27, 18), new Color(255, 255, 235), 4);

        mapa.setFont(new Font("Arial", Font.BOLD, 14));
        desenharTextoCentralComContorno(mapa,
                "Sua jornada começa aqui! Explore, supere desafios e descubra novos mundos.",
                0, 54, w, new Color(24, 35, 25), new Color(255, 255, 245), 3);

        // Abas mais largas, centralizadas e com espaço real entre os mundos.
        int larguraAba = Math.min(340, (w - 50) / 2);
        int espacamentoAbas = 18;
        int yAba = 72;
        int alturaAba = 60;
        int xMundo1 = (w - (larguraAba * 2 + espacamentoAbas)) / 2;
        int xMundo2 = xMundo1 + larguraAba + espacamentoAbas;
        boolean mundo2Liberado = dados.maiorFaseDesbloqueada() >= 6;

        desenharAbaMundo(mapa, xMundo1, yAba, larguraAba, alturaAba,
                "MUNDO 1 — VALE DOS DINOSSAUROS", dados.mundoMapaSelecionado() == 1,
                dados.abaMundoEmDestaque() == 1, true);
        desenharAbaMundo(mapa, xMundo2, yAba, larguraAba, alturaAba,
                "MUNDO 2 — TERRAS SELVAGENS", dados.mundoMapaSelecionado() == 2,
                dados.abaMundoEmDestaque() == 2, mundo2Liberado);

        int margem = 22;
        int espacamento = 12;
        int larguraCartao = (w - margem * 2 - espacamento * 4) / 5;
        int yCartao = 145;
        int alturaCartao = Math.min(300, h - 187);

        String[] nomes = {
                "Vale Verde",
                "Floresta Frutífera",
                "Caverna dos Cristais",
                "Mar dos Piratas",
                "Capitão Pirata",
                "Pântano Misterioso",
                "Deserto dos Fósseis",
                "Montanha Gelada",
                "Ruínas Antigas",
                "Castelo do Tiranossauro"
        };

        int primeiroIndice = (dados.mundoMapaSelecionado() - 1) * 5;
        for (int coluna = 0; coluna < 5; coluna++) {
            int indice = primeiroIndice + coluna;
            int numeroFase = indice + 1;
            boolean liberada = numeroFase <= dados.maiorFaseDesbloqueada();
            boolean selecionada = numeroFase == dados.faseSelecionadaNoMapa();
            boolean emHover = liberada && numeroFase == dados.opcaoMenuEmDestaque();

            int xCartao = margem + coluna * (larguraCartao + espacamento);
            int yAtual = yCartao - (emHover ? 10 : 0);
            int larguraAtual = larguraCartao;
            int alturaAtual = alturaCartao + (emHover ? 6 : 0);
            int borda = 4;

            mapa.setColor(new Color(0, 0, 0, emHover ? 140 : 95));
            mapa.fillRoundRect(xCartao + 4, yAtual + 8,
                    larguraAtual, alturaAtual, 20, 20);

            if (emHover) {
                double pulso = (Math.sin(System.currentTimeMillis() / 170.0) + 1.0) / 2.0;
                int brilhoAlpha = 90 + (int) (pulso * 90);
                mapa.setColor(new Color(255, 229, 124, brilhoAlpha));
                mapa.fillRoundRect(xCartao - 4, yAtual - 4,
                        larguraAtual + 8, alturaAtual + 8, 24, 24);
            }

            mapa.setColor(new Color(224, 174, 83));
            mapa.fillRoundRect(xCartao, yAtual,
                    larguraAtual, alturaAtual, 18, 18);

            BufferedImage imagemFase = dados.imagensFases()[indice];
            if (imagemFase != null) {
                mapa.drawImage(imagemFase,
                        xCartao + borda, yAtual + borda,
                        xCartao + larguraAtual - borda,
                        yAtual + alturaAtual - borda,
                        0, 0, imagemFase.getWidth(), imagemFase.getHeight(), null);
            } else {
                mapa.setColor(new Color(62, 99, 65));
                mapa.fillRoundRect(xCartao + borda, yAtual + borda,
                        larguraAtual - borda * 2, alturaAtual - borda * 2, 12, 12);
                mapa.setColor(Color.WHITE);
                mapa.setFont(new Font("Arial", Font.BOLD, 16));
                centralizarTexto(mapa, "FASE " + (indice < 5 ? 1 : 2),
                        xCartao, yAtual + 65, larguraAtual);
                mapa.setFont(new Font("Arial", Font.BOLD, 13));
                centralizarTexto(mapa, nomes[indice],
                        xCartao, yAtual + 105, larguraAtual);
            }

            mapa.setColor(new Color(255, 255, 255, 35));
            mapa.fillRoundRect(xCartao + borda + 6, yAtual + borda + 6,
                    larguraAtual - 24, 26, 16, 16);

            if (!liberada) {
                mapa.setColor(new Color(0, 0, 0, 150));
                mapa.fillRoundRect(xCartao + borda, yAtual + borda,
                        larguraAtual - borda * 2, alturaAtual - borda * 2, 12, 12);
                desenharCadeado(mapa, xCartao + larguraAtual / 2,
                        yAtual + alturaAtual / 2, Math.max(20, larguraAtual / 5));

                mapa.setColor(new Color(53, 58, 65, 230));
                mapa.fillRoundRect(xCartao + 9, yAtual + alturaAtual - 32,
                        larguraAtual - 18, 24, 10, 10);
                mapa.setFont(new Font("Arial", Font.BOLD, 11));
                mapa.setColor(new Color(220, 225, 228));
                centralizarTexto(mapa, "AGUARDANDO DESBLOQUEIO",
                        xCartao + 9, yAtual + alturaAtual - 16, larguraAtual - 18);
            } else {
                Color faixa = emHover ? new Color(255, 164, 43, 235) : new Color(22, 126, 62, 225);
                mapa.setColor(faixa);
                mapa.fillRoundRect(xCartao + 9, yAtual + alturaAtual - 32,
                        larguraAtual - 18, 24, 10, 10);
                mapa.setFont(new Font("Arial", Font.BOLD, 11));
                mapa.setColor(Color.WHITE);
                centralizarTexto(mapa, emHover ? "CLIQUE PARA AVENTURA" : "TRILHA LIBERADA",
                        xCartao + 9, yAtual + alturaAtual - 16, larguraAtual - 18);
            }

            if (selecionada || emHover) {
                mapa.setColor(emHover ? new Color(255, 246, 178) : new Color(255, 238, 125));
                mapa.setStroke(new BasicStroke(emHover ? 5f : 4f));
                mapa.drawRoundRect(xCartao - 2, yAtual - 2,
                        larguraAtual + 4, alturaAtual + 4, 20, 20);
            } else {
                mapa.setColor(new Color(73, 48, 27));
                mapa.setStroke(new BasicStroke(2f));
                mapa.drawRoundRect(xCartao, yAtual,
                        larguraAtual, alturaAtual, 18, 18);
            }
        }

        mapa.setFont(new Font("Arial", Font.BOLD, 12));
        mapa.setColor(new Color(255, 242, 207));
        mapa.drawString("Dica: passe o mouse nas fases liberadas para destacar sua próxima aventura.",
                22, h - 17);
        desenharBotaoMapaAventura(mapa, w, h);
        desenharBotaoVoltarFolha(mapa, w, h);

        mapa.dispose();
    }

    void desenharMapaAventuraCompleto(Graphics2D g2) {
        int w = larguraTela, h = alturaTela;
        desenharPaisagemSelecao(g2, w, h);
        g2.setColor(new Color(8, 27, 42, 155));
        g2.fillRect(0, 0, w, h);

        if (dados.imagemMapaAventura() != null) {
            // A arte ocupa a área interna praticamente inteira, como indicado
            // na referência: margens pequenas, título no topo e VOLTAR no rodapé.
            // Mantém a imagem completa (sem recorte) dentro dessa área.
            int margemX = 12;
            int topoMapa = 43;
            int rodapeMapa = 57;
            int mx = margemX;
            int my = topoMapa;
            int mw = Math.max(1, w - margemX * 2);
            int mh = Math.max(1, h - topoMapa - rodapeMapa);

            g2.setColor(new Color(0, 0, 0, 175));
            g2.fillRoundRect(mx - 4, my - 4, mw + 8, mh + 8, 16, 16);
            g2.drawImage(dados.imagemMapaAventura(), mx, my, mw, mh, null);
            g2.setColor(new Color(12, 32, 48));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(mx - 3, my - 3, mw + 6, mh + 6, 15, 15);

            // Marcador do Dino acompanha as dez fases no mapa ampliado.
            double[][] pontos = {
                    {0.20, 0.86}, // 1-1 Vale Verde
                    {0.29, 0.65}, // 1-2 Floresta Frutífera
                    {0.53, 0.73}, // 1-3 Caverna dos Cristais
                    {0.70, 0.88}, // 1-4 Mar dos Piratas
                    {0.86, 0.59}, // 1-5 Capitão Pirata
                    {0.70, 0.40}, // 2-1 Pântano Misterioso
                    {0.28, 0.37}, // 2-2 Deserto dos Fósseis
                    {0.19, 0.24}, // 2-3 Montanha Gelada
                    {0.52, 0.27}, // 2-4 Ruínas Antigas
                    {0.86, 0.20}  // 2-5 Castelo do Tiranossauro
            };
            int faseMarcador = Math.max(1, Math.min(10, dados.maiorFaseDesbloqueada()));
            double progressoMaximo = faseMarcador - 1.0;
            double progresso = Math.min(progressoMaximo, dados.progressoAnimacaoMapaAventura());
            int trecho = Math.min(faseMarcador - 1, (int) progresso);
            double fracao = progressoMaximo <= 0 ? 0 : progresso - trecho;
            int proximo = Math.min(faseMarcador - 1, trecho + 1);
            double mapaX = pontos[trecho][0]
                    + (pontos[proximo][0] - pontos[trecho][0]) * fracao;
            double mapaY = pontos[trecho][1]
                    + (pontos[proximo][1] - pontos[trecho][1]) * fracao;
            int px = mx + (int) (mapaX * mw);
            int py = my + (int) (mapaY * mh)
                    + (int) (Math.sin(dados.contadorAnimacaoMapaAventura() / 5.0) * 3);
            if (dados.dinoAntigo() != null) {
                int tamanho = Math.max(24, Math.min(48, w / 22));
                g2.setColor(new Color(255, 224, 78, 215));
                g2.fillOval(px - tamanho / 2 - 4, py - tamanho / 2 - 4,
                        tamanho + 8, tamanho + 8);
                g2.drawImage(dados.dinoAntigo(), px - tamanho / 2, py - tamanho / 2,
                        tamanho, tamanho, null);
            } else {
                g2.setFont(new Font("Arial", Font.BOLD, 28));
                g2.drawString("🦖", px - 14, py + 10);
            }
        } else {
            g2.setColor(new Color(255, 246, 211));
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            String aviso = "Imagem do mapa não encontrada";
            g2.drawString(aviso, (w - g2.getFontMetrics().stringWidth(aviso)) / 2, h / 2);
            g2.setFont(new Font("Arial", Font.PLAIN, 13));
            g2.drawString("Coloque o mapa atualizado na pasta assets.", 24, h / 2 + 28);
        }

        g2.setColor(new Color(255, 246, 211));
        g2.setFont(new Font("Serif", Font.BOLD, 21));
        String titulo = "MAPA DA AVENTURA";
        g2.drawString(titulo, (w - g2.getFontMetrics().stringWidth(titulo)) / 2, 27);
        desenharBotaoVoltarFolha(g2, w, h);
    }

    private void desenharBotaoMapaAventura(Graphics2D g2, int w, int h) {
        int largura = 176, altura = 42;
        int x = (w - largura) / 2, y = h - 82;
        boolean hover = dados.mapaAventuraHover();
        g2.setColor(new Color(0, 0, 0, 115));
        g2.fillRoundRect(x + 3, y + 5, largura, altura, 18, 18);
        g2.setPaint(new GradientPaint(x, y,
                hover ? new Color(255, 232, 139) : new Color(255, 211, 91),
                x, y + altura, hover ? new Color(205, 121, 34) : new Color(153, 82, 24)));
        g2.fillRoundRect(x, y, largura, altura, 18, 18);
        g2.setColor(new Color(255, 247, 202));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(x, y, largura, altura, 18, 18);
        g2.setColor(new Color(112, 62, 25));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(x + 5, y + 5, largura - 10, altura - 10, 13, 13);
        g2.setFont(new Font("Serif", Font.BOLD, 16));
        String texto = "✦ MAPA DA AVENTURA ✦";
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(new Color(73, 39, 18));
        g2.drawString(texto, x + (largura - fm.stringWidth(texto)) / 2, y + 26);
    }

    private void desenharBotaoVoltarFolha(Graphics2D g2, int w, int h) {
        int x = w - 154;
        int y = h - 48;
        boolean hover = dados.voltarHover();

        Graphics2D folha = (Graphics2D) g2.create();
        folha.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        java.awt.geom.Path2D forma = new java.awt.geom.Path2D.Double();
        forma.moveTo(x + 8, y + 19);
        forma.curveTo(x + 27, y - 1, x + 93, y - 5, x + 128, y + 14);
        forma.curveTo(x + 112, y + 42, x + 42, y + 43, x + 8, y + 19);
        forma.closePath();

        folha.setColor(new Color(0, 0, 0, 95));
        folha.translate(2, 4);
        folha.fill(forma);
        folha.translate(-2, -4);

        folha.setPaint(new GradientPaint(x, y, hover ? new Color(155, 235, 112) : new Color(112, 195, 83),
                x + 20, y + 40, hover ? new Color(28, 125, 54) : new Color(20, 100, 47)));
        folha.fill(forma);
        folha.setColor(new Color(235, 255, 190));
        folha.setStroke(new BasicStroke(2.8f));
        folha.draw(forma);

        // Nervura central e pequenos veios para reforçar o formato de folha.
        folha.setColor(new Color(225, 255, 190, 220));
        folha.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        folha.draw(new java.awt.geom.Line2D.Double(x + 18, y + 20, x + 116, y + 18));
        folha.draw(new java.awt.geom.Line2D.Double(x + 47, y + 19, x + 36, y + 9));
        folha.draw(new java.awt.geom.Line2D.Double(x + 68, y + 19, x + 61, y + 8));
        folha.draw(new java.awt.geom.Line2D.Double(x + 87, y + 19, x + 96, y + 9));

        folha.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = folha.getFontMetrics();
        folha.setColor(new Color(255, 255, 235));
        String texto = "VOLTAR";
        folha.drawString(texto, x + (132 - fm.stringWidth(texto)) / 2, y + 25);
        folha.dispose();
    }

    private void desenharAbaMundo(Graphics2D g2, int x, int y, int largura,
                                  int altura, String texto, boolean selecionada,
                                  boolean emHover, boolean liberada) {
        Graphics2D aba = (Graphics2D) g2.create();
        aba.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sombra e halo de destaque ao passar o mouse.
        aba.setColor(new Color(0, 0, 0, emHover ? 155 : 115));
        aba.fillRoundRect(x + 4, y + 6, largura, altura, 19, 19);
        if (emHover) {
            aba.setColor(new Color(255, 230, 130, 105));
            aba.fillRoundRect(x - 5, y - 5, largura + 10, altura + 10, 23, 23);
        }

        // Identidade visual distinta para cada mundo, mantendo dimensões e posição.
        Color topo;
        Color base;
        if (!liberada) {
            topo = new Color(239, 231, 210);
            base = new Color(157, 159, 146);
        } else if (texto.startsWith("MUNDO 1")) {
            topo = new Color(255, 239, 137);
            base = new Color(190, 153, 48);
        } else {
            topo = new Color(177, 231, 143);
            base = new Color(49, 132, 73);
        }
        if (selecionada || emHover) {
            topo = topo.brighter();
            base = base.brighter();
        }

        aba.setPaint(new GradientPaint(x, y, topo, x, y + altura, base));
        aba.fillRoundRect(x, y, largura, altura, 18, 18);

        // Filete interno e borda externa para dar acabamento de botão de aventura.
        aba.setColor(new Color(255, 255, 232, 220));
        aba.setStroke(new BasicStroke(2.2f));
        aba.drawRoundRect(x + 3, y + 3, largura - 6, altura - 6, 15, 15);
        aba.setColor(!liberada ? new Color(89, 78, 61) : new Color(93, 65, 28));
        aba.setStroke(new BasicStroke(emHover ? 3.5f : 2.2f));
        aba.drawRoundRect(x, y, largura, altura, 18, 18);

        // Pequenos detalhes laterais lembrando placas de madeira/aventura.
        aba.setColor(new Color(255, 248, 211, 190));
        aba.fillOval(x + 10, y + altura / 2 - 3, 6, 6);
        aba.fillOval(x + largura - 16, y + altura / 2 - 3, 6, 6);

        String[] partes = texto.split("\\s*—\\s*", 2);
        String linhaSuperior = partes.length > 0 ? partes[0] : texto;
        String linhaInferior = partes.length > 1 ? partes[1] : "";

        aba.setFont(new Font("Arial", Font.BOLD, 13));
        desenharTextoCentralComContorno(aba, linhaSuperior, x, y + 24, largura,
                new Color(18, 25, 17), new Color(255, 255, 238), 2);
        aba.setFont(new Font("Arial", Font.BOLD, 12));
        desenharTextoCentralComContorno(aba, linhaInferior, x, y + 43, largura,
                new Color(18, 25, 17), new Color(255, 255, 238), 2);

        if (!liberada) {
            int badgeLargura = 94;
            int badgeAltura = 16;
            int badgeX = x + largura - badgeLargura - 9;
            int badgeY = y + altura - 19;
            aba.setColor(new Color(90, 12, 15, 90));
            aba.fillRoundRect(badgeX + 1, badgeY + 2, badgeLargura, badgeAltura, 9, 9);
            aba.setPaint(new GradientPaint(badgeX, badgeY,
                    new Color(255, 104, 91), badgeX, badgeY + badgeAltura,
                    new Color(151, 20, 25)));
            aba.fillRoundRect(badgeX, badgeY, badgeLargura, badgeAltura, 9, 9);
            aba.setColor(new Color(255, 211, 184));
            aba.setStroke(new BasicStroke(1.2f));
            aba.drawRoundRect(badgeX, badgeY, badgeLargura, badgeAltura, 9, 9);
            aba.setFont(new Font("Arial", Font.BOLD, 8));
            aba.setColor(Color.WHITE);
            FontMetrics fm = aba.getFontMetrics();
            String bloqueado = "BLOQUEADO";
            aba.drawString(bloqueado, badgeX + (badgeLargura - fm.stringWidth(bloqueado)) / 2,
                    badgeY + 11);
        }
        aba.dispose();
    }
}
