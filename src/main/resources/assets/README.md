# Imagens e sprites do Dino Adventure

Copie para esta pasta o conteúdo da pasta `assets` do seu projeto original. Preserve os nomes, a extensão e as subpastas. Os arquivos de imagem não acompanharam o código enviado para a reorganização.

## Imagens usadas pelas telas

| Arquivo | Uso |
| --- | --- |
| `menu.png` | Menu inicial |
| `configuracoes.png` | Tela de configurações |
| `como-jogar.png` | Tela de instruções |
| `mapa-fases.png` | Mosaico com dez cartões, em cinco colunas e duas linhas |
| `mapa_das_fases_aventura_dos_dinossauros.png` | Arte do mapa da aventura |
| `portal.png` | Portal de conclusão |
| `dino.png` | Imagem reserva do personagem |

Para o mapa da aventura, o código também aceita os nomes `mapa_dino_adventure_1055x1491.png` e `mapa-aventura.png` como alternativas.

## Animações

Cada animação usa até oito quadros. Os prefixos esperados são:

| Prefixo | Animação |
| --- | --- |
| `parado` | Parado |
| `andar_direita` | Movimento para a direita |
| `andar_esquerda` | Movimento para a esquerda |
| `pular` | Salto |
| `abaixar` | Abaixado |
| `dano` | Dano |

O formato recomendado para quadros separados é `DinoAdventure_Sprites/parado/parado_01.png` até `parado_08.png`, e o equivalente para cada prefixo.

Também são aceitos nomes como `parado_1.png`, `parado01.png` e `parado1.png`. Para manter os recursos funcionando dentro do JAR, use a pasta específica de cada prefixo ou coloque os quadros diretamente nesta pasta `assets`.

Como alternativa, use `parado_spritesheet.png`, com **oito quadros horizontais de mesma largura**, diretamente nesta pasta ou em `DinoAdventure_Sprites/`. A mesma regra vale para os outros prefixos. Uma spritesheet válida tem prioridade sobre os quadros individuais.

## Depois de copiar

Recompile o projeto para que as imagens entrem no JAR. Não é necessário adicionar as imagens manualmente ao `pom.xml`.
