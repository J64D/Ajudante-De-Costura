# Publicação e anúncios

## Configuração preparada

- `compileSdk` e `targetSdk` estão em 36; a versão foi elevada para `67` para permitir uma nova atualização na Play Console.
- Android Gradle Plugin `9.0.1`, Gradle `9.1.0`, Kotlin `2.2.10` e Room `2.8.5`.
- O release usa R8, minificação e remoção de recursos. No AGP 9, a remoção otimizada de recursos é aplicada automaticamente com `isShrinkResources = true`.
- As regras do OpenCV preservam os nomes JNI necessários e deixam o restante do app disponível para otimização/ofuscação.
- Google Mobile Ads SDK `25.5.0`; os banners usam o tamanho adaptativo grande recomendado no SDK atual.
- O alvo de empacotamento em páginas de 16 KB e o empacotamento moderno das bibliotecas nativas continuam habilitados.

## Anúncios e receita

O app tem banners em telas de conteúdo e um intersticial depois que a pessoa toca em “Entrar no ateliê”. O relatório enviado mostra 33 solicitações, 22 impressões e eCPM de US$ 0,79; esse volume ainda é pequeno para avaliar uma tendência de receita. Atualizar o SDK e o formato do banner melhora a compatibilidade e dá acesso aos formatos atuais, mas não garante aumento de ganhos. Impressões, país dos usuários, eCPM e tempo com o anúncio visível também influenciam.

O card de ARPU no AdMob ainda pede vínculo com Firebase. Isso precisa ser concluído no console do AdMob/Firebase com o projeto correto; o app já usa Firebase para as aulas, mas essa configuração de console não pode ser feita pelo Gradle.

## Verificação desta revisão

- `:app:assembleDebug` e `:app:bundleRelease` concluíram com sucesso no disco D.
- A saída de release está em `app/build/outputs/bundle/release/app-release.aab` e foi gerada sem assinatura de upload; assine com a chave de upload antes de enviar à Play Console.
- O release novo não contém a referência de bytecode `LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES` encontrada no aviso da versão anterior.
- O build foi encerrado sem executar os passos demorados `lintVital*`; nenhuma suíte de testes foi executada.
- Só o processamento da Play Console e testes em Android 15/16 confirmam todos os avisos e a aparência real de ponta a ponta.

## Limitação de compatibilidade

`android.disallowKotlinSourceSets=false` é uma opção experimental temporária necessária porque a versão atual do KSP registra os arquivos gerados pela API antiga de source sets. O build passou com essa configuração; quando o KSP migrar para `android.sourceSets`, a opção pode ser removida.
