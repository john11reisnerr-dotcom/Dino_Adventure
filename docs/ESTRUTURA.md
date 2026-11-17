# Arquitetura e decisões

## Fluxo de execução

`Main` cria o `PainelJogo` na thread de eventos do Swing. O painel recebe o gerenciador de recursos e o repositório de progresso por construtor; o construtor público oferece as implementações padrão.

O painel coordena entrada e atualização da partida. Quando uma renderização é solicitada, delega desenhos aos renderizadores especializados. Eles recebem dados e dimensões, sem carregar arquivos, gravar progresso ou modificar as regras do jogo.

## Divisão de responsabilidades

| Componente | Responsabilidade | Limite |
| --- | --- | --- |
| `Main` | Compor a janela e iniciar a aplicação | Não implementa regras do jogo |
| `PainelJogo` | Coordenar eventos, atualizações, colisões e progressão | Delega os desenhos especializados |
| `TelaJogo` | Representar menu, seleção, aventura ou partida | Uma única tela ativa |
| `ConfiguracaoJogo` | Parâmetros compartilhados da simulação | Não contém estado mutável |
| `GeradorBlocosVale` | Construir o percurso dos blocos | Cada chamada cria entidades novas |
| `RenderizadorVale` | Desenhar elementos do vale | Não atualiza entidades |
| `RenderizadorMapas` | Desenhar seleção e mapa ilustrado | Usa `DadosMapa`, sem acessar o painel |
| `RenderizadorHud` | Desenhar pontuação, controles e mensagens | Usa `ResumoPartida` |
| `DesenhosCenario` | Desenhar elementos reutilizáveis | Opera apenas com os parâmetros recebidos |
| `GerenciadorRecursos` | Ler imagens, quadros e spritesheets | Suporta classpath/JAR e diretórios externos |
| `RepositorioProgresso` | Ler e salvar progresso | Pode receber preferências isoladas nos testes |

Os renderizadores e as entidades do jogo permanecem no pacote de domínio `jogo`, com visibilidade restrita ao pacote. Isso evita expor entidades internas somente para organizar pastas por tipo técnico. `DadosMapa` e `ResumoPartida` são records de leitura; os recursos de imagem continuam pertencendo ao painel e não são modificados pelos renderizadores.

## Navegação e temporizador

O `enum TelaJogo` substitui combinações de booleanos. As ações de movimento e salto só são aceitas durante a partida.

O temporizador é iniciado em `addNotify` e interrompido em `removeNotify`. Construir um painel para testes não inicia um temporizador permanente nem exige encerrar a JVM com `System.exit`.

## Recursos

Imagens são lidas por stream, inclusive dentro do JAR. O carregamento mantém a compatibilidade com pastas externas da versão anterior. A prioridade especial do mapa externo é preservada.

Os recursos originais ainda serão adicionados em `src/main/resources/assets/`. A captura em `docs/imagens/jogo.png` é documentação, não uma substituição dos sprites.

## Progresso e compatibilidade

O repositório mantém o nó `/<unnamed>` e as chaves anteriores para ler os salvamentos existentes. Os testes recebem nós temporários, removidos ao final.

A sequência de blocos é determinística. O formato legado armazena estados pela posição na lista; alterações futuras no percurso devem introduzir identificadores e migração, evitando reinterpretar um salvamento antigo na ordem errada.

## Testabilidade

JUnit 5 e Surefire integram as verificações ao ciclo Maven. `GeradorBlocosValeTest` verifica determinismo, limites e independência das tentativas. `NavegacaoTest` verifica transições públicas de teclado e mouse. `JogoIntegracaoTest` cobre persistência, recursos, salto e renderização.

Algumas verificações de integração usam reflexão para alcançar regras ainda internas ao painel. Esse acoplamento permanece uma limitação: ao extrair física e progressão, migre essas verificações para as interfaces das novas classes.

## Limites atuais

Esta é uma refatoração incremental. O painel ainda concentra física, colisões, atualização das entidades e alguns desenhos. O jogo usa atualizações do temporizador como unidade da simulação; atrasos podem afetar a duração real. `Preferences.flush()` ainda é síncrono. Esses itens estão documentados no roadmap e não são apresentados como problemas já resolvidos.

Os testes offscreen verificam comportamentos e ausência de exceções em caminhos exercitados. Não atestam aparência final, fluidez nem equilíbrio das fases. A validação manual deve ser feita após inserir os assets.
