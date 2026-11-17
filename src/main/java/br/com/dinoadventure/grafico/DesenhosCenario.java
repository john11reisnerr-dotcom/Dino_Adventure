package br.com.dinoadventure.grafico;

import java.awt.*;
import java.awt.geom.Path2D;

/** Desenhos reutilizáveis que não dependem do estado da partida. */
public final class DesenhosCenario {
    private DesenhosCenario() {
    }

    public static void desenharEstrela(Graphics2D g2, int centroX, int centroY, int raio) {
        Polygon estrela = new Polygon();
        for (int i = 0; i < 10; i++) {
            double angulo = -Math.PI / 2 + i * Math.PI / 5;
            int r = (i % 2 == 0) ? raio : Math.max(2, raio / 2);
            estrela.addPoint(
                    centroX + (int) (Math.cos(angulo) * r),
                    centroY + (int) (Math.sin(angulo) * r)
            );
        }
        g2.fillPolygon(estrela);
    }

    public static void desenharArvorePequena(Graphics2D g2, int x, int chao, double escala) {
        int troncoW = (int) (13 * escala), troncoH = (int) (42 * escala);
        g2.setColor(new Color(111, 70, 39));
        g2.fillRoundRect(x, chao - troncoH, troncoW, troncoH, 7, 7);
        g2.setColor(new Color(49, 133, 54));
        g2.fillOval(x - (int)(17*escala), chao - (int)(70*escala),
                (int)(49*escala), (int)(43*escala));
        g2.setColor(new Color(76, 165, 65));
        g2.fillOval(x - (int)(7*escala), chao - (int)(82*escala),
                (int)(34*escala), (int)(35*escala));
        g2.setColor(new Color(255, 240, 170, 170));
        g2.fillOval(x + 4, chao - troncoH - 22, 5, 5);
    }

    public static void desenharTextoCentralComContorno(Graphics2D g2, String texto,
                                                 int x, int baseline, int largura,
                                                 Color corTexto, Color corContorno,
                                                 int espessura) {
        FontMetrics fm = g2.getFontMetrics();
        int textoX = x + (largura - fm.stringWidth(texto)) / 2;
        g2.setColor(corContorno);
        for (int dx = -espessura; dx <= espessura; dx++) {
            for (int dy = -espessura; dy <= espessura; dy++) {
                if (dx * dx + dy * dy <= espessura * espessura + 1) {
                    g2.drawString(texto, textoX + dx, baseline + dy);
                }
            }
        }
        g2.setColor(corTexto);
        g2.drawString(texto, textoX, baseline);
    }

    public static void desenharCadeado(Graphics2D g2, int centroX, int centroY, int tamanho) {
        int corpoLargura = tamanho;
        int corpoAltura = (int) (tamanho * 0.72);
        int x = centroX - corpoLargura / 2;
        int y = centroY - corpoAltura / 3;

        g2.setStroke(new BasicStroke(Math.max(3f, tamanho * 0.13f)));
        g2.setColor(new Color(235, 220, 180));
        g2.drawArc(centroX - tamanho / 3, y - tamanho / 2,
                (int) (tamanho * 0.66), tamanho, 0, 180);
        g2.setColor(new Color(38, 43, 48));
        g2.fillRoundRect(x, y, corpoLargura, corpoAltura, 7, 7);
        g2.setColor(new Color(255, 215, 100));
        g2.drawRoundRect(x, y, corpoLargura, corpoAltura, 7, 7);
        g2.fillOval(centroX - 2, y + corpoAltura / 3, 4, 4);
        g2.fillRect(centroX - 1, y + corpoAltura / 2, 2, corpoAltura / 4);
    }

    public static void desenharIlhaMapa(Graphics2D g2, int x, int y, int largura, int altura) {
        g2.setColor(new Color(35, 105, 139, 100));
        g2.fillOval(x - largura / 2, y - altura / 2 + 8, largura, altura);
        g2.setColor(new Color(215, 185, 119));
        g2.fillOval(x - largura / 2, y - altura / 2, largura, altura);
        g2.setColor(new Color(88, 145, 70));
        g2.fillOval(x - largura / 2 + 9, y - altura / 2 - 5,
                largura - 18, altura - 12);
        g2.setColor(new Color(48, 111, 54));
        g2.fillOval(x - 9, y - altura / 2 - 18, 20, 24);
    }

    public static void desenharArvoreMapa(Graphics2D g2, int x, int y, double escala) {
        int tronco = (int)(9 * escala);
        g2.setColor(new Color(92, 59, 34));
        g2.fillRoundRect(x - tronco / 2, y - (int)(35 * escala),
                tronco, (int)(39 * escala), 5, 5);
        g2.setColor(new Color(39, 91, 46));
        g2.fillOval(x - (int)(25 * escala), y - (int)(65 * escala),
                (int)(50 * escala), (int)(42 * escala));
        g2.setColor(new Color(57, 126, 55));
        g2.fillOval(x - (int)(19 * escala), y - (int)(69 * escala),
                (int)(38 * escala), (int)(34 * escala));
        g2.setColor(new Color(119, 164, 75, 130));
        g2.fillOval(x - (int)(10 * escala), y - (int)(61 * escala),
                (int)(15 * escala), (int)(11 * escala));
    }

    public static void desenharNuvem(
            Graphics2D g2,
            int x,
            int y) {
        g2.setColor(Color.WHITE);
        g2.fillOval(x, y, 65, 35);
        g2.fillOval(x + 25, y - 15, 65, 50);
        g2.fillOval(x + 60, y, 65, 35);
        g2.fillRect(x + 20, y + 15, 85, 20);
    }

    public static void desenharMontanha(
            Graphics2D g2,
            int x,
            int base,
            int picoX,
            int picoY) {
        Polygon montanha = new Polygon(
                new int[]{x, x + picoX, x + 360},
                new int[]{base, picoY, base},
                3
        );
        g2.setColor(new Color(95, 145, 125));
        g2.fillPolygon(montanha);
        Polygon neve = new Polygon(
                new int[]{
                        x + picoX - 35,
                        x + picoX,
                        x + picoX + 35,
                        x + picoX + 12
                },
                new int[]{
                        picoY + 42,
                        picoY,
                        picoY + 42,
                        picoY + 32
                },
                4
        );
        g2.setColor(new Color(235, 245, 240));
        g2.fillPolygon(neve);
    }

    public static void desenharArvore(
            Graphics2D g2,
            int x,
            int y,
            double escala) {
        int troncoLargura = (int) (22 * escala);
        int troncoAltura = (int) (65 * escala);
        g2.setColor(new Color(105, 65, 35));
        g2.fillRect(
                x,
                y - troncoAltura,
                troncoLargura,
                troncoAltura
        );
        g2.setColor(new Color(35, 110, 45));
        g2.fillOval(
                x - (int) (35 * escala),
                y - (int) (125 * escala),
                (int) (90 * escala),
                (int) (85 * escala)
        );
        g2.setColor(new Color(55, 145, 55));
        g2.fillOval(
                x - (int) (25 * escala),
                y - (int) (115 * escala),
                (int) (65 * escala),
                (int) (65 * escala)
        );
    }

    public static void desenharArvoreFrutifera(
            Graphics2D g2,
            int x,
            int y,
            double escala) {
        Graphics2D arvore = (Graphics2D) g2.create();
        arvore.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        int baseArvore = y + (int) (14 * escala);
        int troncoLargura = (int) (25 * escala);
        int troncoAltura = (int) (78 * escala);
        int topoTronco = baseArvore - troncoAltura;
        arvore.setColor(new Color(75, 45, 25));
        arvore.fillRoundRect(x, topoTronco, troncoLargura, troncoAltura, 9, 9);
        arvore.setColor(new Color(133, 82, 43));
        arvore.fillRoundRect(
                x + (int) (4 * escala), topoTronco,
                (int) (17 * escala), troncoAltura, 7, 7
        );
        arvore.setColor(new Color(91, 57, 31));
        arvore.drawLine(
                x + (int) (12 * escala), topoTronco + (int) (15 * escala),
                x + (int) (12 * escala), baseArvore - (int) (8 * escala)
        );
        arvore.setStroke(new BasicStroke(
                (float) (7 * escala), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
        ));
        arvore.setColor(new Color(105, 65, 34));
        arvore.drawLine(
                x + troncoLargura / 2, baseArvore - (int) (48 * escala),
                x - (int) (25 * escala), baseArvore - (int) (105 * escala)
        );
        arvore.drawLine(
                x + troncoLargura / 2, baseArvore - (int) (52 * escala),
                x + (int) (55 * escala), baseArvore - (int) (112 * escala)
        );
        arvore.setColor(new Color(28, 91, 39));
        arvore.fillOval(
                x - (int) (52 * escala), baseArvore - (int) (163 * escala),
                (int) (125 * escala), (int) (105 * escala)
        );
        arvore.setColor(new Color(42, 133, 52));
        arvore.fillOval(
                x - (int) (43 * escala), baseArvore - (int) (155 * escala),
                (int) (108 * escala), (int) (88 * escala)
        );
        arvore.setColor(new Color(68, 158, 62));
        arvore.fillOval(
                x - (int) (25 * escala), baseArvore - (int) (149 * escala),
                (int) (68 * escala), (int) (62 * escala)
        );
        desenharFruto(arvore, x - (int) (27 * escala), baseArvore - (int) (125 * escala), escala,
                new Color(210, 45, 38));
        desenharFruto(arvore, x + (int) (31 * escala), baseArvore - (int) (139 * escala), escala,
                new Color(255, 190, 45));
        desenharFruto(arvore, x + (int) (57 * escala), baseArvore - (int) (105 * escala), escala,
                new Color(225, 91, 35));
        desenharFruto(arvore, x - (int) (43 * escala), baseArvore - (int) (91 * escala), escala,
                new Color(235, 65, 48));
        desenharFruto(arvore, x + (int) (10 * escala), baseArvore - (int) (91 * escala), escala,
                new Color(255, 190, 45));
        arvore.dispose();
    }

    public static void desenharFruto(
            Graphics2D g2,
            int x,
            int y,
            double escala,
            Color cor) {
        int tamanho = Math.max(5, (int) (13 * escala));
        g2.setStroke(new BasicStroke((float) (2 * escala)));
        g2.setColor(new Color(91, 62, 30));
        g2.drawLine(x + tamanho / 2, y + 2, x + tamanho / 2 + 1, y - 3);
        g2.setColor(cor.darker());
        g2.fillOval(x, y, tamanho, tamanho);
        g2.setColor(cor);
        g2.fillOval(x + 1, y, tamanho - 2, tamanho - 2);
        g2.setColor(new Color(255, 255, 255, 175));
        g2.fillOval(x + 2, y + 2, Math.max(2, tamanho / 4), Math.max(2, tamanho / 4));
    }

    public static void desenharGrama(
            Graphics2D g2,
            int largura,
            int yChao, int alturaGrama, double movimentoChao) {
        g2.setColor(new Color(43, 139, 43));
        g2.fillRect(0, yChao, largura, alturaGrama);
        g2.setColor(new Color(67, 165, 55));
        g2.fillRect(0, yChao, largura, 13);
        g2.setColor(new Color(35, 120, 35));
        for (int i = 0; i < largura + 40; i += 35) {
            int x = Math.floorMod(
                    i - (int) movimentoChao,
                    largura + 40
            ) - 20;
            g2.fillOval(x, yChao + 25, 18, 5);
        }
        g2.setColor(new Color(75, 180, 55));
        for (int i = 0; i < largura + 30; i += 13) {
            int x = Math.floorMod(
                    i - (int) movimentoChao,
                    largura + 30
            ) - 15;
            Path2D ponta = new Path2D.Double();
            ponta.moveTo(x, yChao + 9);
            ponta.lineTo(x + 5, yChao - 3);
            ponta.lineTo(x + 9, yChao + 9);
            ponta.closePath();
            g2.fill(ponta);
        }
        g2.setColor(new Color(48, 130, 40));
        for (int i = 0; i < largura + 30; i += 28) {
            int x = Math.floorMod(
                    i - (int) movimentoChao,
                    largura + 30
            ) - 15;
            g2.fillOval(
                    x,
                    yChao + alturaGrama - 7,
                    22,
                    12
            );
        }
    }

    public static void desenharPedrinhasTerra(
            Graphics2D g2,
            int largura,
            int yChao, int alturaGrama, double movimentoChao) {
        Color[] cores = {
                new Color(120, 112, 100),
                new Color(155, 145, 130),
                new Color(95, 91, 85),
                new Color(180, 160, 135)
        };
        for (int i = 0; i < 32; i++) {
            int posicaoBase = i * 47 + 15;
            int x = Math.floorMod(
                    posicaoBase - (int) movimentoChao,
                    largura + 70
            ) - 35;
            int y = yChao + alturaGrama + 10 + (i % 4) * 8;
            int tamanho = 4 + (i % 4) * 2;
            g2.setColor(cores[i % cores.length]);
            g2.fillOval(x, y, tamanho + 3, tamanho);
            g2.setColor(new Color(210, 200, 180, 150));
            g2.fillOval(
                    x + 2,
                    y + 1,
                    Math.max(2, tamanho / 2),
                    2
            );
        }
        for (int i = 0; i < 9; i++) {
            int x = Math.floorMod(
                    i * 137 - (int) movimentoChao,
                    largura + 50
            ) - 25;
            int y = yChao + alturaGrama + 27 + (i % 3) * 10;
            g2.setColor(new Color(105, 96, 84));
            g2.fillOval(x, y, 13, 8);
            g2.setColor(new Color(165, 151, 128));
            g2.fillOval(x + 2, y + 1, 6, 3);
        }
    }

    public static void desenharMinhocas(
            Graphics2D g2,
            int largura,
            int yChao, int alturaGrama, double movimentoChao) {
        for (int i = 0; i < 6; i++) {
            int x = Math.floorMod(
                    i * 185 + 80 - (int) movimentoChao,
                    largura + 90
            ) - 45;
            int y = yChao + alturaGrama + 19 + (i % 3) * 15;
            desenharMinhoca(g2, x, y, i);
        }
    }

    public static void desenharMinhoca(
            Graphics2D g2,
            int x,
            int y,
            int variacao) {
        Graphics2D minhoca = (Graphics2D) g2.create();
        int largura = 25 + variacao % 3 * 4;
        minhoca.setColor(new Color(65, 43, 27, 100));
        minhoca.setStroke(new BasicStroke(
                6,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        minhoca.drawArc(x + 1, y + 3, largura, 10, 0, 180);
        minhoca.setColor(new Color(220, 119, 126));
        minhoca.setStroke(new BasicStroke(
                5,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        minhoca.drawArc(x, y, largura, 10, 0, 180);
        minhoca.setColor(new Color(255, 177, 170));
        minhoca.setStroke(new BasicStroke(
                2,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        minhoca.drawArc(x + 3, y + 1, largura - 7, 7, 0, 180);
        minhoca.setColor(new Color(180, 85, 100));
        for (int j = 1; j < 4; j++) {
            int sx = x + j * (largura / 5);
            minhoca.drawLine(sx, y + 4, sx + 1, y + 7);
        }
        minhoca.setColor(new Color(65, 45, 40));
        minhoca.fillOval(x + largura - 4, y + 4, 2, 2);
        minhoca.dispose();
    }

    public static void desenharPaisagemSelecao(Graphics2D g2, int w, int h) {
        g2.setPaint(new GradientPaint(0, 0, new Color(73, 171, 233),
                0, h, new Color(9, 54, 86)));
        g2.fillRect(0, 0, w, h);

        g2.setColor(new Color(255, 231, 156, 210));
        g2.fillOval(w - 180, 35, 100, 100);
        g2.setColor(new Color(255, 247, 210, 90));
        g2.fillOval(w - 205, 10, 150, 150);

        desenharNuvem(g2, 40, 70);
        desenharNuvem(g2, 270, 40);
        desenharNuvem(g2, 560, 75);
        desenharNuvem(g2, 790, 48);

        Polygon montanhaFundo = new Polygon(
                new int[]{-60, 80, 210, 330, 460, 600, 730, 865, 1030},
                new int[]{270, 145, 240, 155, 250, 150, 245, 160, 280}, 9);
        g2.setColor(new Color(77, 108, 134));
        g2.fillPolygon(montanhaFundo);

        Polygon montanhaMedia = new Polygon(
                new int[]{-40, 130, 255, 390, 520, 655, 815, 1020},
                new int[]{320, 205, 280, 198, 280, 205, 295, 225}, 8);
        g2.setColor(new Color(100, 141, 122));
        g2.fillPolygon(montanhaMedia);

        g2.setColor(new Color(221, 235, 229, 170));
        g2.fillPolygon(new Polygon(new int[]{95, 135, 170, 125}, new int[]{192, 145, 188, 180}, 4));
        g2.fillPolygon(new Polygon(new int[]{600, 640, 675, 630}, new int[]{203, 158, 202, 194}, 4));

        g2.setColor(new Color(44, 107, 67));
        g2.fillOval(-150, 300, 620, 300);
        g2.setColor(new Color(77, 146, 82));
        g2.fillOval(-90, 318, 540, 260);
        g2.setColor(new Color(95, 173, 92));
        g2.fillOval(20, 352, 380, 170);

        g2.setColor(new Color(42, 133, 186));
        g2.fillOval(620, 300, 520, 275);
        g2.setColor(new Color(86, 185, 214));
        g2.fillOval(665, 322, 430, 225);
        g2.setColor(new Color(171, 232, 240, 110));
        g2.fillOval(720, 348, 290, 150);

        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(214, 248, 247, 145));
        for (int i = 0; i < 7; i++) {
            int yy = 340 + i * 24;
            g2.drawArc(700 + (i % 2) * 22, yy, 110, 30, 15, 150);
            g2.drawArc(860 - (i % 3) * 16, yy + 8, 96, 24, 10, 145);
        }

        desenharIlhaMapa(g2, 800, 285, 100, 48);
        desenharIlhaMapa(g2, 930, 390, 86, 42);
        desenharIlhaMapa(g2, 705, 440, 74, 36);

        desenharArvoreMapa(g2, 65, 315, 0.84);
        desenharArvoreMapa(g2, 145, 350, 0.68);
        desenharArvoreMapa(g2, 255, 320, 0.78);
        desenharArvoreMapa(g2, 360, 390, 0.66);
        desenharArvoreMapa(g2, 455, 330, 0.80);
        desenharArvoreMapa(g2, 780, 270, 0.68);
        desenharArvoreMapa(g2, 920, 312, 0.62);

        g2.setColor(new Color(0, 0, 0, 65));
        g2.fillRect(0, 108, w, h - 108);
    }

    public static void centralizarTexto(
            Graphics2D g2,
            String texto,
            int x,
            int y,
            int largura) {
        int tx = x + (
                largura - g2.getFontMetrics().stringWidth(texto)
        ) / 2;
        g2.drawString(texto, tx, y);
    }
}
