# Interface rosa e branca

## Direção visual

Tema rosa e branco aplicado na atividade principal, com texto em rosa escuro,
superfícies claras, cartões arredondados e ícones Material Rounded.
`LiquidHero` exibe uma tesoura vetorial rosa, substituindo a antiga bolha,
com entrada suave de posição, escala e rotação. A animação acontece uma vez ao entrar na composição e
usa o relógio de animação do Compose; não mantém um loop consumindo recursos.
Os atalhos respondem ao toque com uma pequena compressão e retorno por mola.

## Fluxos implementados

- Ao tocar em Entrar no ateliê, exibe o intersticial se já estiver carregado e abre o painel após fechar. Se indisponível ou falhar, entra diretamente. Debug usa a unidade de teste; release mantém a unidade de produção existente.
- Painel com quantidades reais de peças, registros e cores.
- Cadastro, edição, busca, quantidade com atualização atômica e exclusão confirmada.
- Resumo compartilhável pelo seletor do Android, sem apagar registros.
- Backup JSON por seletor de arquivos, importação aditiva com confirmação e validação.
- Cálculo de preço com vírgula decimal e acréscimo configurável e explícito.
- Teste de elasticidade com validação, resultado e instruções.
- Guia de tecidos pesquisável, dicas expansíveis e cursos com player incorporado.
- Banners de teste em builds debug, com liberação do AdView ao sair da composição.
- Publicidade no topo do conteúdo, logo abaixo do cabeçalho, nas telas com anúncios. Sem banner fixo no rodapé; entrada e formulários permanecem livres de anúncios.

## Dependências externas e limites

A publicação real de cursos depende da configuração descrita em `CURSOS.md`.
Sem `firebase.properties`, a tela informa que o catálogo não está conectado;
não apresenta cursos fictícios nem confirma publicações locais como se fossem online.
Biblioteca com ARD-045 reconstruído em segmentos vetoriais, três projetos básicos, importação PLT e exportação SVG/PLT. O original ARD-045 não é distribuído no aplicativo. Dimensões usam a unidade padrão PLT; tamanhos e margens do ARD-045 precisam ser conferidos.
Não foram adicionadas funções de cobrança, entregas ou conclusão de encomendas:
as métricas antigas fixas foram substituídas por informações do banco existente.

## Conferência em aparelho

1. Abrir o app e conferir marca, tesoura, navegação e legibilidade em tela pequena e fonte ampliada.
2. Cadastrar e editar uma peça, buscar, alterar quantidade e reiniciar o app.
3. Exportar backup, importar confirmando a adição e conferir o total.
4. Cancelar exclusão e verificar que os dados permanecem.
5. Digitar valores inválidos e com vírgula nas duas ferramentas.
6. Testar o compartilhamento e voltar ao aplicativo sem perder os registros.
7. Conectar Firebase, validar permissões e publicação em outro aparelho.
8. Assistir vídeo permitido pelo YouTube, sair do app e verificar pausa do player.

Compilação e testes locais não substituem esta conferência visual e de serviços online.

## Verificação local em 26/09/2026

- Testes unitários cobrem cálculos, backup, catálogo e geometria dos moldes.
- Na etapa anterior, `lintDebug` terminou com zero erros e 103 avisos de manutenção; não repetido após a biblioteca de moldes.
- APK de desenvolvimento gerado em `app/build/outputs/apk/debug/app-debug.apk`.
- Sem aparelho/emulador conectado; os fluxos visuais, player e serviços online
  ainda precisam ser conferidos em Android com o catálogo configurado.

## Entrada, régua e moldes

O botão de entrada fica fora da área rolável, no rodapé com espaço para a navegação do Android. Régua horizontal até 200 cm com calibração persistida; a rolagem não mede tecido automaticamente. O ARD-045 é desenhado pelo Canvas a partir de geometria JSON local, com zoom e exportação vetorial. Os projetos básicos incluem capa de almofada, sacola e saquinho, com medidas e instruções.



