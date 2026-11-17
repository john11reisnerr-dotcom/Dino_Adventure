package br.com.dinoadventure.jogo;

import java.awt.image.BufferedImage;

/** Vista dos dados do mapa; imagens pertencem ao painel e são apenas lidas. */
record DadosMapa(
        int maiorFaseDesbloqueada,
        int faseSelecionadaNoMapa,
        int mundoMapaSelecionado,
        int abaMundoEmDestaque,
        int opcaoMenuEmDestaque,
        BufferedImage[] imagensFases,
        BufferedImage imagemMapaAventura,
        BufferedImage dinoAntigo,
        double progressoAnimacaoMapaAventura,
        int contadorAnimacaoMapaAventura,
        boolean mapaAventuraHover,
        boolean voltarHover) {
}
