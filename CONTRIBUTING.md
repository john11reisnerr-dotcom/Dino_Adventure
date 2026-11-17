# Contribuindo

Use JDK 17 ou superior e Maven 3.9 ou superior. Abra o `pom.xml` como projeto Maven.

1. Crie uma branch com objetivo específico.
2. Faça alterações pequenas, mantendo os nomes do domínio em português.
3. Execute `mvn clean verify`.
4. Para mudanças de jogabilidade, valide manualmente com as imagens originais.
5. Abra um pull request explicando comportamento, motivo e validação.

Respeite `.editorconfig` e `.gitattributes`. Não inclua compilados, configurações pessoais da IDE ou arquivos com credenciais. O código de produção usa somente o JDK; JUnit é uma dependência exclusiva de testes.

Evite colocar novos desenhos em `PainelJogo`. Use os renderizadores e passe somente os dados necessários. Alterações no formato de salvamento devem considerar o progresso já existente.
