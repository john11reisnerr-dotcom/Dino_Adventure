package br.com.dinoadventure.app;

import br.com.dinoadventure.jogo.PainelJogo;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Ponto de entrada do Dino Adventure. */
public final class Main {
    private Main() {
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Dino Adventure");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setResizable(false);
            PainelJogo painel = new PainelJogo();
            painel.setPreferredSize(new Dimension(1000, 563));
            janela.add(painel);
            janela.pack();
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
            painel.requestFocusInWindow();
        });
    }
}
