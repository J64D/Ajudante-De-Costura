# Cursos e aulas do YouTube

O aplicativo agora tem acesso a Cursos e módulos na tela inicial e em Aprender.
Os alunos navegam por curso, módulo e aula e assistem ao vídeo no player incorporado.
O catálogo é sincronizado pelo Cloud Firestore. Os vídeos continuam hospedados no YouTube.
Sem configuração, o aplicativo abre normalmente, mas não pode publicar nem carregar cursos.

## Ativar a publicação

1. Crie ou selecione seu projeto no [Firebase Console](https://console.firebase.google.com/).
2. Cadastre o aplicativo Android com o pacote `com.scarletstudio.ajudantedecostura`.
3. Copie `firebase.properties.example` para `firebase.properties` na raiz do projeto.
   Preencha `apiKey`, `projectId` e `applicationId` com os valores da configuração Android:
   `current_key`, `project_id` e `mobilesdk_app_id` do arquivo `google-services.json`.
   `applicationId` aqui é o ID Firebase (`1:...:android:...`), não o nome do pacote.
   Não coloque senhas, chave de conta de serviço nem chave privada nesse arquivo.
4. Ative Authentication > E-mail/senha e crie a conta que administrará os cursos.
5. Crie o banco Cloud Firestore. Publique as regras de `firestore.rules` na aba Regras.
   Em um banco já usado por outro aplicativo, integre estes blocos às regras existentes;
   não substitua regras alheias nem mantenha uma regra global que libere toda escrita/leitura.
6. Pelo Console, crie `courseAdmins/UID_DA_CONTA` com o campo booleano `enabled: true`.
   O UID é o identificador da conta criada no Authentication. Usuários do app não podem se promover a administradores.
7. Gere e instale uma nova versão do app. Alterações futuras nas aulas não exigem outro APK.

## Publicar uma aula

1. Envie o vídeo ao seu canal do YouTube como público ou não listado, permitindo incorporação.
2. No app, abra Cursos e módulos > Administrar e entre com a conta autorizada.
3. Toque em Nova aula / curso / módulo, preencha os nomes, a ordem, o título e o link.
   Curso e módulo são criados junto da primeira aula. Nas próximas aulas use os botões
   de seleção para reutilizar os mesmos nomes. Nomes distintos criam grupos distintos.
4. Use Conferir vídeo no player para verificar a reprodução antes de publicar.
5. Salve como rascunho ou publique para todos. Só aulas publicadas aparecem no catálogo público.
6. Toque numa aula para editar, trocar o vídeo ou retirá-la do catálogo salvando como rascunho.

Os rascunhos exigem autenticação de administrador no servidor, não apenas um botão oculto.
Um vídeo não listado pode ser acessado por quem tiver seu link; este fluxo não é um controle de cursos pagos.
O player mantém os controles e a marca do YouTube, não inicia automaticamente e pausa ao sair do aplicativo.
A reprodução depende de internet e das permissões do vídeo no YouTube.

## Verificação antes de disponibilizar

- Em uma instalação sem login, verificar que só aulas publicadas aparecem.
- Entrar como administrador, salvar rascunho, conferir vídeo e publicar.
- Em outro celular, verificar a aula no curso e módulo corretos.
- Editar e retirar uma aula; verificar a atualização no segundo celular.
- Tentar escrever com uma conta sem `courseAdmins/{uid}`: a operação deve ser negada.
- Reproduzir em aparelho Android, girar a tela, colocar em segundo plano e voltar.
- Conferir vídeo privado, removido e sem incorporação: o player deve mostrar a indisponibilidade.

Referências: [Configuração Firebase](https://firebase.google.com/docs/android/setup),
[regras Firestore](https://firebase.google.com/docs/firestore/security/rules-conditions) e
[identificação do player incorporado](https://developers.google.com/youtube/terms/required-minimum-functionality#embedded-player-api-client-identity).
