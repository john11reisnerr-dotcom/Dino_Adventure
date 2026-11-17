package br.com.dinoadventure.jogo;

import java.awt.*;
import static br.com.dinoadventure.grafico.DesenhosCenario.desenharArvorePequena;
import static br.com.dinoadventure.jogo.ConfiguracaoJogo.COMPRIMENTO_VALE_OBSTACULOS;

/** Desenha entidades do vale sem atualizar movimento, colisões ou progresso. */
final class RenderizadorVale {
    private RenderizadorVale() { }

    static void desenharPlantasEspinhoMarrom(Graphics2D g2, java.util.List<PlantaEspinhoMarrom> plantasEspinhoMarrom, java.util.List<BolhaExplosiva> bolhasExplosivas, double cameraValeX, int larguraTela) {
        for (PlantaEspinhoMarrom planta : plantasEspinhoMarrom) {
            if (planta.x + planta.largura < cameraValeX - 90
                    || planta.x > cameraValeX + larguraTela + 90) continue;
            int x = planta.x;
            int y = planta.y;
            int w = planta.largura;
            int h = planta.altura;
            int ond = (int) Math.round(Math.sin(planta.animacao) * 4);

            g2.setColor(new Color(60, 38, 21, 85));
            g2.fillOval(x + 8, y + h - 4, w - 16, 8);
            g2.setColor(new Color(98, 66, 34));
            g2.fillRoundRect(x + w / 2 - 7, y + 18, 14, h - 18, 9, 9);
            g2.setColor(new Color(125, 86, 48));
            g2.fillOval(x + 6, y + 6, w - 12, 23);
            g2.fillOval(x + 4, y + 18, w - 8, 18);
            g2.setColor(new Color(72, 46, 22));
            g2.fillOval(x + 15, y + 13, 4, 4);
            g2.fillOval(x + w - 20, y + 13, 4, 4);
            for (int i = 0; i < 7; i++) {
                int sx = x + 5 + i * 7;
                int sy = y + 7 + (i % 2) * 3;
                g2.setColor(new Color(150, 120, 70));
                g2.drawLine(sx, sy, sx - 4, sy - 6 - (i % 2));
                g2.drawLine(sx + 4, sy + 20, sx + 9, sy + 27 + (i % 3));
            }
            g2.drawLine(x + 11, y + h - 18, x + 2, y + h - 23);
            g2.drawLine(x + w - 11, y + h - 18, x + w + 7, y + h - 24);
        }

        for (BolhaExplosiva bolha : bolhasExplosivas) {
            if (bolha.planta.x < cameraValeX - 110 || bolha.planta.x > cameraValeX + larguraTela + 110) continue;
            int bx = bolha.getX();
            int by = bolha.getY();
            if (!bolha.explodindo()) {
                int r = 5 + (bolha.tempo % 8) / 3;
                g2.setColor(new Color(220, 235, 255, 70));
                g2.fillOval(bx - r - 2, by - r - 2, (r + 2) * 2, (r + 2) * 2);
                g2.setColor(new Color(204, 230, 255, 200));
                g2.fillOval(bx - r, by - r, r * 2, r * 2);
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillOval(bx - 1, by - 1, 2, 2);
            } else {
                int er = bolha.getRaioExplosao();
                g2.setColor(new Color(255, 205, 115, 70));
                g2.fillOval(bx - er - 6, by - er - 6, (er + 6) * 2, (er + 6) * 2);
                g2.setColor(new Color(255, 170, 85, 120));
                g2.fillOval(bx - er, by - er, er * 2, er * 2);
                g2.setColor(new Color(255, 235, 175, 210));
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(bx - er + 2, by - er + 2, Math.max(2, er * 2 - 4), Math.max(2, er * 2 - 4));
            }
        }
    }

    static void desenharTroncosCacto(Graphics2D g2, java.util.List<TroncoCacto> troncosCacto, double cameraValeX, int larguraTela) {
        for (TroncoCacto tronco : troncosCacto) {
            int x = tronco.getXAtual();
            int y = tronco.getYAtual();
            if (x + tronco.largura < cameraValeX - 120
                    || x > cameraValeX + larguraTela + 120) continue;

            int w = tronco.largura;
            int h = tronco.altura;
            // Tronco verde-claro com detalhes de madeira; ele se move horizontalmente
            // e sobe/desce suavemente junto com o cacto.
            g2.setColor(new Color(35, 80, 40, 55));
            g2.fillOval(x + 10, y + h - 1, w - 20, 7);
            g2.setColor(new Color(56, 132, 62));
            g2.fillRoundRect(x, y + 2, w, h, 16, 16);
            g2.setColor(new Color(135, 215, 113));
            g2.fillRoundRect(x + 1, y, w - 2, h - 3, 16, 16);
            g2.setColor(new Color(180, 238, 142));
            g2.fillRoundRect(x + 8, y + 3, w - 16, 6, 8, 8);
            g2.setColor(new Color(79, 157, 76));
            g2.setStroke(new BasicStroke(2f));
            for (int i = 0; i < 4; i++) {
                int anelX = x + 14 + i * 28;
                g2.drawArc(anelX, y + 6, 18, h - 13, 100, 160);
            }
            g2.setColor(new Color(226, 248, 184));
            g2.drawArc(x + w - 20, y + 5, 13, h - 12, 95, 185);

            // O cacto pequeno nasce sobre o topo do tronco, sem atravessá-lo.
            int cx = tronco.getCactoX();
            int topo = tronco.getCactoTopoY();
            int cw = tronco.cactoLargura;
            int ch = tronco.cactoAltura;
            g2.setColor(new Color(167, 111, 46));
            g2.fillRoundRect(cx, topo, cw, ch, 12, 12);
            g2.setColor(new Color(199, 145, 57)); // amarelo queimado
            g2.fillRoundRect(cx + 3, topo + 2, cw - 6, ch - 2, 11, 11);
            g2.setColor(new Color(177, 122, 46));
            g2.fillRoundRect(cx - 9, topo + 13, 11, 19, 7, 7);
            g2.fillRoundRect(cx + cw - 2, topo + 10, 11, 20, 7, 7);
            g2.setColor(new Color(232, 180, 82));
            g2.drawLine(cx + 7, topo + 5, cx + 7, y - 5);
            g2.drawLine(cx + cw - 8, topo + 5, cx + cw - 8, y - 5);

            // Espinhos grandes, mas proporcionais ao cacto reduzido.
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 228, 143));
            for (int i = 0; i < 4; i++) {
                int sy = topo + 7 + i * 8;
                g2.drawLine(cx + 1, sy, cx - 9, sy - 6);
                g2.drawLine(cx + cw - 1, sy + 1, cx + cw + 9, sy - 5);
            }
            g2.drawLine(cx + cw / 2, topo + 2, cx + cw / 2, topo - 9);
            g2.drawLine(cx - 6, topo + 16, cx - 15, topo + 9);
            g2.drawLine(cx + cw + 6, topo + 16, cx + cw + 15, topo + 8);
        }
    }

    static void desenharPlantasBolha(Graphics2D g2, java.util.List<PlantaBolha> plantasBolha, double cameraValeX, int larguraTela) {
        for (PlantaBolha planta : plantasBolha) {
            if (planta.x + planta.largura < cameraValeX - 70
                    || planta.x > cameraValeX + larguraTela + 70) continue;

            int x = planta.x;
            int y = planta.getYAtual();
            int w = planta.largura;
            int h = planta.altura;
            int balanco = (int) Math.round(Math.sin(planta.animacao * 1.35) * 6);

            // A sombra fica no solo, mesmo quando a planta vermelha salta.
            int salto = planta.y - y;
            if (planta.vermelha) {
                g2.setColor(new Color(27, 67, 31, Math.max(45, 115 - salto / 2)));
                g2.fillOval(x + 7, planta.y + h - 5, w - 14, 9);
            }

            // A base fica parada. Na planta vermelha, o corpo inteiro sobe e desce.
            g2.setColor(planta.vermelha ? new Color(112, 31, 46) : new Color(36, 115, 58));
            g2.fillRoundRect(x + w / 2 - 6, y + 13, 12, h - 13, 9, 9);
            g2.setColor(planta.vermelha ? new Color(197, 51, 67) : new Color(63, 170, 84));
            g2.fillRoundRect(x + w / 2 - 3, y + 14, 5, h - 16, 5, 5);

            // Folhas flexionam de forma suave, sem mover o caule das verdes.
            g2.setColor(planta.vermelha ? new Color(210, 45, 65) : new Color(53, 161, 78));
            g2.fillOval(x - 6 + balanco, y + h - 23, 25, 16);
            g2.fillOval(x + w - 19 - balanco, y + h - 25, 25, 17);
            g2.setColor(planta.vermelha ? new Color(250, 98, 104) : new Color(104, 208, 105));
            g2.fillOval(x + 1 - balanco, y + h - 39, 22, 15);
            g2.fillOval(x + w - 23 + balanco, y + h - 40, 22, 15);
            g2.setColor(planta.vermelha ? new Color(255, 145, 147) : new Color(169, 239, 137));
            g2.drawArc(x - 3 + balanco, y + h - 21, 19, 11, 20, 105);
            g2.drawArc(x + w - 18 - balanco, y + h - 23, 18, 12, 55, 95);

            // A cabeca/corola acompanha levemente a oscilacao das folhas.
            int topoX = x + balanco / 3;
            g2.setColor(planta.vermelha ? new Color(146, 24, 49) : new Color(27, 104, 52));
            g2.fillOval(topoX + 3, y + 3, w - 6, 22);
            g2.setColor(planta.vermelha ? new Color(246, 68, 82) : new Color(108, 220, 128));
            g2.fillOval(topoX + 6, y + 4, w - 12, 19);
            g2.setColor(new Color(250, 252, 243));
            g2.fillOval(topoX + 10, y + 9, 7, 7);
            g2.fillOval(topoX + w - 17, y + 9, 7, 7);
            g2.setColor(new Color(24, 41, 34));
            g2.fillOval(topoX + 13, y + 11, 3, 3);
            g2.fillOval(topoX + w - 15, y + 11, 3, 3);

            // Bolhinhas pequenas decorativas sobem acompanhando a planta.
            for (int i = 0; i < 3; i++) {
                double faseBolha = planta.animacao + i * 1.25 + planta.defasagem;
                int bolhaX = topoX + w / 2 + (int) (Math.sin(faseBolha * 1.15) * (5 + i * 2));
                int bolhaY = y - 5 - (int) ((faseBolha * 20) % 34) - i * 7;
                int raio = 4 + (i % 2);
                g2.setColor(new Color(175, 245, 255, 85));
                g2.fillOval(bolhaX - raio - 1, bolhaY - raio - 1, raio * 2 + 2, raio * 2 + 2);
                g2.setColor(new Color(215, 255, 255, 175));
                g2.fillOval(bolhaX - raio, bolhaY - raio, raio * 2, raio * 2);
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillOval(bolhaX - 1, bolhaY - 1, 2, 2);
            }
        }
    }

    static void desenharBlocosSuspensos(Graphics2D g2, java.util.List<BlocoSuspenso> blocosVale, double cameraValeX, int larguraTela) {
        for (BlocoSuspenso bloco : blocosVale) {
            // Só desenha as plataformas visíveis nesta região do mapa.
            if (bloco.x < cameraValeX - 50
                    || bloco.x > cameraValeX + larguraTela + 50) continue;
            int x=bloco.x, y=bloco.y, t=bloco.tamanho;
            g2.setColor(bloco.atingido ? new Color(135, 105, 65) : new Color(238, 165, 45));
            g2.fillRoundRect(x, y, t, t, 7, 7);
            g2.setColor(new Color(255, 220, 115));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(x+2, y+2, t-4, t-4, 6, 6);
            g2.setColor(bloco.atingido ? new Color(95, 75, 50) : new Color(120, 75, 25));
            g2.setFont(new Font("Arial", Font.BOLD, 23));
            g2.drawString(bloco.atingido ? "" : "?", x+12, y+27);
            if (!bloco.atingido && bloco.contemFruta) {
                g2.setColor(new Color(255, 240, 190));
                g2.fillOval(x+t-10, y+4, 5, 5);
            }
        }
    }

    static void desenharInimigosVoadores(Graphics2D g2, java.util.List<InimigoVoador> inimigosVoadores) {
        for (InimigoVoador inimigo : inimigosVoadores) {
            int x = (int) inimigo.x, y = (int) inimigo.y;
            // Pequeno pterodáctilo cartunesco, voando baixo o suficiente para exigir abaixar.
            g2.setColor(new Color(104, 75, 155));
            g2.fillOval(x + 13, y + 10, 30, 18);
            g2.fillOval(x + 34, y + 5, 18, 17);
            g2.setColor(new Color(130, 100, 185));
            int asa = (int) (Math.sin(inimigo.faseAnimacao * 2) * 9);
            Polygon asaEsq = new Polygon(
                    new int[]{x + 25, x + 2, x + 18},
                    new int[]{y + 15, y - asa, y + 25}, 3);
            Polygon asaDir = new Polygon(
                    new int[]{x + 30, x + 55, x + 39},
                    new int[]{y + 15, y - asa, y + 25}, 3);
            g2.fillPolygon(asaEsq);
            g2.fillPolygon(asaDir);
            g2.setColor(new Color(255, 235, 130));
            g2.fillOval(x + 45, y + 10, 4, 4);
            g2.setColor(new Color(45, 35, 60));
            g2.fillOval(x + 47, y + 11, 2, 2);
        }
    }

    static void desenharInimigosVale(Graphics2D g2, java.util.List<InimigoVale> inimigosVale) {
        for (InimigoVale inimigo : inimigosVale) {
            int x = (int) inimigo.x;
            int y = inimigo.y;
            int t = inimigo.tamanho;
            // Pequeno raptor cartunesco: corpo, cabeça, patas e olhos.
            g2.setColor(new Color(40, 105, 53));
            g2.fillOval(x + 3, y + t / 3, t - 7, t / 2);
            g2.fillOval(x + t / 2, y + 2, t / 2, t / 2);
            g2.setColor(new Color(65, 145, 69));
            g2.fillOval(x + t / 2 + 5, y + 7, t / 3, t / 4);
            g2.setColor(new Color(245, 205, 75));
            g2.fillOval(x + t - 12, y + 12, 6, 6);
            g2.setColor(new Color(25, 35, 20));
            g2.fillOval(x + t - 10, y + 14, 3, 3);
            g2.setColor(new Color(35, 85, 43));
            int passo = (int) (Math.sin(inimigo.animacao) * 4);
            g2.fillRoundRect(x + 10, y + t - 10, 9, 14 + passo, 5, 5);
            g2.fillRoundRect(x + t - 17, y + t - 10, 9, 14 - passo, 5, 5);
            g2.setColor(new Color(255, 235, 170));
            g2.drawLine(x + t - 2, y + t / 2, x + t + 5, y + t / 2 + 2);
        }
    }

    static void desenharCenarioAvanco2(Graphics2D g2, double cameraValeX, int larguraTela, int alturaTela, int alturaTerreno) {
        int chao = alturaTela - alturaTerreno;
        int inicio = Math.max(0, (int) (cameraValeX / 250) - 1);
        int fim = Math.min(COMPRIMENTO_VALE_OBSTACULOS / 250,
                (int) ((cameraValeX + larguraTela) / 250) + 2);
        for (int i = inicio; i <= fim; i++) {
            int x = 115 + 250 * i;
            desenharArvorePequena(g2, x, chao, 0.58 + (i % 3) * 0.06);
            if (i % 2 == 0) {
                g2.setColor(new Color(48, 125, 48));
                g2.fillOval(x + 55, chao - 16, 58, 23);
            }
            if (i % 3 == 2) {
                g2.setColor(new Color(111, 70, 36));
                g2.fillRoundRect(x + 115, chao - 18, 68, 18, 12, 12);
            }
        }
    }
}
