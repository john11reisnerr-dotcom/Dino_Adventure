# Publicar o Dino Adventure no GitHub

Publique o conteúdo da pasta `dino-adventure`, incluindo o código, o `pom.xml`, o README, os arquivos de configuração e as imagens originais que você adicionar.

## Preparar a pasta

1. Extraia o ZIP e abra a pasta `dino-adventure`.
2. Copie o conteúdo da antiga pasta `assets` para `src/main/resources/assets/`.
3. Abra o `pom.xml` no IntelliJ, selecione um JDK 17 ou superior e execute `Main`.
4. Confira as telas com os sprites e os mapas originais.
5. Instale o Maven e execute `mvn clean verify`, ou use `verificar.bat` no Windows / `bash scripts/projeto.sh verify` no Linux e macOS.

## Criar o repositório

Crie um repositório vazio com o nome sugerido **dino-adventure**. Se você usar os comandos abaixo, deixe desmarcadas as opções de gerar README, `.gitignore` e licença, pois os dois primeiros já existem nesta pasta. A licença pode ser escolhida posteriormente pelo autor.

Descrição sugerida para o repositório:

> Jogo de plataforma 2D em Java Swing, com fases, obstáculos, pontuação e salvamento de progresso. Projeto acadêmico de ADS.

## Enviar os arquivos

Abra o terminal dentro da pasta que contém o `pom.xml` e execute:

```bash
git init
git add .
git status
git commit -m "Organiza arquitetura, testes e documentação do Dino Adventure"
git branch -M main
```

Copie a URL do repositório que você criou. No exemplo abaixo, substitua `SEU_USUARIO` pelo nome da sua conta:

```bash
git remote add origin https://github.com/SEU_USUARIO/dino-adventure.git
git push -u origin main
```

Se o Git solicitar autenticação, conclua a autenticação com sua conta. Se ainda não houver nome e e-mail configurados no Git do computador, configure sua identificação antes de fazer o commit.

## Conferir a publicação

Abra a página do repositório e confira o README, a captura do jogo, os fontes e a pasta de imagens. A aba **Actions** exibirá o resultado das verificações configuradas.

Para alterações posteriores, faça um novo commit e envie:

```bash
git add .
git commit -m "Descreve a alteração realizada"
git push
```

Os comandos desta página pressupõem um repositório novo. Se você já tem histórico Git para o jogo, aproveite o repositório existente e copie os arquivos organizados para ele, revisando as alterações antes do commit.
