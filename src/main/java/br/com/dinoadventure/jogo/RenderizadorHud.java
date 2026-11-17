package br.com.dinoadventure.jogo;

import java.awt.*;
import static br.com.dinoadventure.grafico.DesenhosCenario.*;
import static br.com.dinoadventure.jogo.ConfiguracaoJogo.*;

/** Apresenta pontuação, controles e mensagens, sem alterar a partida. */
final class RenderizadorHud {
    private final ResumoPartida dados;
    private final int larguraTela;
    private final int alturaTela;

    RenderizadorHud(ResumoPartida dados, int larguraTela, int alturaTela) {
        this.dados = dados;
        this.larguraTela = larguraTela;
        this.alturaTela = alturaTela;
    }

    void desenhar(Graphics2D g2, boolean gameOver) {
        desenharPainelPontuacao(g2);
        desenharControles(g2);
        if (gameOver) desenharGameOver(g2);
        if (dados.fase() == 1) desenharNomeEtapaValeVerde(g2);
        if (dados.fase() == 2) desenharNovaFase(g2);
    }

    private void desenharPainelPontuacao(Graphics2D g2) {
        int x = 20;
        int y = 20;
        int largura = 205;
        int altura = 82;
        Graphics2D painel = (Graphics2D) g2.create();
        painel.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        painel.setColor(new Color(0, 0, 0, 90));
        painel.fillRoundRect(
                x + 3, y + 4,
                largura, altura,
                20, 20
        );
        painel.setColor(new Color(20, 38, 30, 225));
        painel.fillRoundRect(
                x, y,
                largura, altura,
                20, 20
        );
        GradientPaint borda = new GradientPaint(
                x, y,
                new Color(90, 240, 100),
                x + largura, y + altura,
                new Color(255, 205, 65)
        );
        painel.setPaint(borda);
        painel.setStroke(new BasicStroke(3.5f));
        painel.drawRoundRect(
                x, y,
                largura, altura,
                20, 20
        );
        painel.setColor(new Color(170, 255, 140, 35));
        painel.setStroke(new BasicStroke(1f));
        painel.drawRoundRect(
                x + 4, y + 4,
                largura - 8, altura - 8,
                16, 16
        );
        painel.setColor(new Color(150, 200, 130, 110));
        painel.drawLine(
                x + 15,
                y + 39,
                x + largura - 15,
                y + 39
        );
        painel.setFont(new Font("Arial", Font.BOLD, 17));
        painel.setColor(new Color(180, 245, 140));
        painel.drawString("FASE " + dados.fase(), x + 16, y + 27);
        painel.setColor(new Color(255, 220, 65));
        desenharEstrela(painel, x + 25, y + 59, 10);
        painel.setFont(new Font("Arial", Font.BOLD, 21));
        painel.setColor(new Color(255, 225, 100));
        painel.drawString(String.valueOf(dados.pontuacao()), x + 43, y + 66);
        painel.setFont(new Font("Arial", Font.BOLD, 10));
        painel.setColor(new Color(220, 240, 220));
        String mensagem;
        if (dados.fase() == 1 && !dados.portalAtivo()) {
            mensagem = dados.noValeObstaculos()
                    ? "TRILHA: " + Math.min(100, (int)(dados.dinoMundoX() * 100
                    / (COMPRIMENTO_VALE_OBSTACULOS - 200))) + "%"
                    : "AVANÇO " + dados.etapaValeVerde() + "/5 • META: " + META_VALE_VERDE;
        } else if (dados.portalAtivo()) {
            mensagem = "PORTAL LIBERADO!";
        } else {
            mensagem = "NOVA FASE!";
        }
        int textoX = x + largura - 12 -
                painel.getFontMetrics().stringWidth(mensagem);
        painel.drawString(mensagem, textoX, y + 26);
        painel.dispose();
    }

    private void desenharControles(Graphics2D g2) {
        int x = larguraTela - 150;
        int y = alturaTela - 105;
        g2.setColor(new Color(0, 0, 0, 75));
        g2.fillRoundRect(
                x + 3, y + 3,
                138, 91,
                16, 16
        );
        g2.setColor(new Color(25, 35, 35, 175));
        g2.fillRoundRect(
                x, y,
                138, 91,
                16, 16
        );
        g2.setColor(new Color(150, 190, 110, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x, y, 138, 91, 16, 16);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.setColor(new Color(220, 240, 190));
        g2.drawString("CONTROLES", x + 12, y + 15);
        desenharBotaoControle(g2, x + 12, y + 25, "←");
        desenharBotaoControle(g2, x + 49, y + 25, "→");
        desenharBotaoControle(g2, x + 86, y + 25, "↓");
        g2.setColor(new Color(55, 155, 55));
        g2.fillRoundRect(
                x + 12, y + 65,
                114, 19,
                8, 8
        );
        g2.setColor(new Color(150, 230, 100));
        g2.drawRoundRect(
                x + 12, y + 65,
                114, 19,
                8, 8
        );
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        centralizarTexto(
                g2,
                "ESPAÇO 2x: ALTO",
                x + 12,
                y + 79,
                114
        );
    }

    private void desenharBotaoControle(
            Graphics2D g2,
            int x,
            int y,
            String simbolo) {
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(x + 2, y + 3, 30, 30, 8, 8);
        g2.setColor(new Color(75, 105, 75));
        g2.fillRoundRect(x, y, 30, 30, 8, 8);
        g2.setColor(new Color(160, 205, 130));
        g2.setStroke(new BasicStroke(1.3f));
        g2.drawRoundRect(x, y, 30, 30, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        int textoX = x + (30 - fm.stringWidth(simbolo)) / 2;
        int textoY = y + (30 - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(simbolo, textoX, textoY);
    }

    private void desenharGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, larguraTela, alturaTela);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 42));
        centralizarTexto(
                g2,
                "FIM DE JOGO",
                0,
                240,
                larguraTela
        );
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        centralizarTexto(
                g2,
                "Pontuação: " + dados.pontuacao(),
                0,
                290,
                larguraTela
        );
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        centralizarTexto(
                g2,
                "Pressione D para jogar novamente",
                0,
                350,
                larguraTela
        );
    }

    private void desenharNomeEtapaValeVerde(Graphics2D g2) {
        String[] nomes = {"O INÍCIO DA AVENTURA", "VALE DOS OBSTÁCULOS",
                "TRAVESSIA DO RIO", "TERRITÓRIO SELVAGEM", "PORTAL DA FLORESTA"};
        int largura = 330, x = (larguraTela - largura) / 2, y = 16;
        g2.setColor(new Color(18, 48, 28, 185));
        g2.fillRoundRect(x, y, largura, 34, 16, 16);
        g2.setColor(new Color(205, 230, 135));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, largura, 34, 16, 16);
        g2.setFont(new Font("Arial", Font.BOLD, 15));
        g2.setColor(Color.WHITE);
        centralizarTexto(g2, "VALE VERDE • " + nomes[Math.max(0, Math.min(4, dados.etapaValeVerde() - 1))],
                x, y + 22, largura);
    }

    private void desenharNovaFase(Graphics2D g2) {
        g2.setColor(new Color(20, 40, 30, 180));
        g2.fillRoundRect(
                285, 115,
                230, 65,
                15, 15
        );
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 25));
        g2.drawString("FASE 2!", 350, 155);
    }
}
