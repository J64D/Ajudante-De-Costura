# Desenvolvimento no disco D:

- Projeto: D:/GitHub/Ajudante-De-Costura
- Android SDK: D:/Android/Sdk (local.properties)
- Java da compilação: D:/Android/Jdk17 (gradle.properties e .gradle/config.properties)
- Gradle: D:/Android/Gradle; o antigo C:/Users/gamer/.gradle aponta para esse diretório por junction.
- Cache/índices do Android Studio: D:/Android/Studio/system
- Logs do Android Studio: D:/Android/Studio/log
- Temporários da compilação: D:/Android/Temp
- Dados Android novos: D:/Android/User
- Emuladores novos: D:/Android/AVD

Para abrir o projeto com todos esses caminhos e temporários, execute D:/Android/Abrir-Ajudante-de-Costura.cmd. As variáveis GRADLE_USER_HOME, ANDROID_HOME, ANDROID_USER_HOME e ANDROID_AVD_HOME foram configuradas no perfil Windows. A chave debug foi copiada para preservar a assinatura dos APKs de teste.

O SDK e Java originais do Unity permanecem em C: porque são usados pelo Unity. O Studio está instalado em D:. Suas preferências pequenas continuam no perfil Windows; caches e arquivos volumosos de desenvolvimento foram direcionados para D:.

Verificação: assembleDebug concluiu com BUILD SUCCESSFUL usando D:/Android/Jdk17, D:/Android/Sdk e D:/Android/Gradle. Projeto confirmado aberto no Studio.


Atualização: preferências e plugins também direcionados para D:/Android/Studio/config e D:/Android/Studio/plugins, através de STUDIO_PROPERTIES e do atalho. A pasta antiga de cache no perfil local foi movida para D:/Android/Studio/previous-system; o antigo caminho aponta por junction para D:/Android/Studio/system. A sessão atual ainda usa preferências do perfil e precisa fechar para aplicar a mudança das preferências. Nenhum processo do Studio foi encerrado à força.

