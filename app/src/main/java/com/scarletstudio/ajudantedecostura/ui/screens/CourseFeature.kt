package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.scarletstudio.ajudantedecostura.data.learning.*
import java.util.UUID

@Composable
fun CourseFeature(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { runCatching { CourseRepository.create(context) }.getOrNull() }
    var manage by rememberSaveable { mutableStateOf(false) }
    var admin by remember { mutableStateOf(false) }
    var lessons by remember { mutableStateOf(emptyList<PublishedLesson>()) }
    var loading by remember { mutableStateOf(repository != null) }
    var error by remember { mutableStateOf<String?>(null) }
    var retry by remember { mutableIntStateOf(0) }
    DisposableEffect(repository) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            admin = false
            val uid = auth.currentUser?.uid
            if (uid != null) repository?.checkAdmin { allowed ->
                if (repository.auth.currentUser?.uid == uid) admin = allowed
            }
        }
        repository?.auth?.addAuthStateListener(listener)
        onDispose { repository?.auth?.removeAuthStateListener(listener) }
    }
    DisposableEffect(repository, admin, retry) {
        loading = repository != null
        error = null
        lessons = emptyList()
        val listener = repository?.watch(admin) { data, exception ->
            if (exception != null) {
                loading = false
                error = "Não foi possível carregar as aulas. Confira sua conexão e tente novamente."
            } else if (data != null) {
                loading = false
                lessons = data
            }
        }
        onDispose { listener?.remove() }
    }
    if (manage) {
        CourseAdminScreen(repository, admin, lessons, onBack = { manage = false }, onAdmin = { admin = it })
    } else {
        Column(Modifier.fillMaxSize()) {
            if (error != null) TextButton(onClick = { retry++ }) { Text("Tentar carregar novamente") }
            CoursesScreen(onBack, CourseRepository.courses(lessons), onManage = { manage = true }, loading = loading,
                error = error ?: if (repository == null) "O catálogo de cursos ainda não foi conectado. A publicação precisa ser configurada pelo responsável pelo aplicativo." else null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseAdminScreen(
    repository: CourseRepository?, admin: Boolean, lessons: List<PublishedLesson>,
    onBack: () -> Unit, onAdmin: (Boolean) -> Unit
) {
    var editing by remember { mutableStateOf<PublishedLesson?>(null) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    val back: () -> Unit = { if (!busy) { if (editing != null) editing = null else onBack() } }
    BackHandler(onBack = back)
    Scaffold(topBar = {
        TopAppBar(title = { Text(if (editing == null) "Administrar cursos" else "Editar aula") },
            navigationIcon = { IconButton(onClick = back, enabled = !busy) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
            actions = {
                if (admin) TextButton(enabled = !busy, onClick = {
                    repository?.auth?.signOut(); onAdmin(false); editing = null
                }) { Text("Sair") }
            })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).imePadding().padding(horizontal = 16.dp)) {
            message?.let { Text(it, Modifier.padding(vertical = 8.dp)) }
            when {
                repository == null -> {
                    Text("Conecte seu catálogo", style = MaterialTheme.typography.headlineSmall)
                    Text("A publicação ainda não foi ativada neste aplicativo. É necessário configurar o serviço de cursos e a conta do administrador antes de publicar para os usuários.")
                }
                !admin -> Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Acesso de administrador", style = MaterialTheme.typography.headlineSmall)
                    Text("Entre com a conta autorizada para criar cursos, módulos e aulas.")
                    OutlinedTextField(email, { email = it }, label = { Text("E-mail") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(password, { password = it }, label = { Text("Senha") }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    Button(enabled = !busy && email.isNotBlank() && password.isNotBlank(), onClick = {
                        busy = true; message = null
                        repository.auth.signInWithEmailAndPassword(email.trim(), password)
                            .addOnSuccessListener {
                                password = ""
                                repository.checkAdmin { allowed ->
                                    busy = false
                                    if (allowed) onAdmin(true)
                                    else { repository.auth.signOut(); message = "Esta conta não tem permissão para publicar cursos." }
                                }
                            }.addOnFailureListener {
                                busy = false; message = "Não foi possível entrar. Verifique e-mail, senha e conexão."
                            }
                    }) { Text(if (busy) "Entrando…" else "Entrar") }
                }
                editing != null -> key(editing!!.id) {
                    LessonEditor(editing!!, lessons, busy, onSave = { entry ->
                        busy = true; message = null
                        repository.save(entry) { failure ->
                            busy = false
                            if (failure == null) {
                                message = if (entry.published) "Aula publicada para os usuários." else "Rascunho salvo. A aula não aparece para os usuários."
                                editing = null
                            } else message = "Não foi possível salvar. Verifique sua conexão e a permissão da conta."
                        }
                    })
                }
                else -> {
                    Button(onClick = { message = null; editing = PublishedLesson(id = UUID.randomUUID().toString()) }) { Text("Nova aula / curso / módulo") }
                    Text("Crie o curso e o módulo ao cadastrar a primeira aula. Use os mesmos nomes nas próximas aulas.")
                    if (lessons.isEmpty()) Text("Você ainda não cadastrou aulas.", Modifier.padding(vertical = 16.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 12.dp)) {
                        items(lessons.sortedWith(compareBy({ it.courseTitle }, { it.moduleOrder }, { it.lessonOrder })), key = { it.id }) { entry ->
                            Card(onClick = { message = null; editing = entry }, modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(entry.title, style = MaterialTheme.typography.titleMedium)
                                    Text("${entry.courseTitle} • ${entry.moduleTitle}")
                                    Text(if (entry.published) "Publicada • Toque para editar" else "Rascunho • Toque para editar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonEditor(initial: PublishedLesson, lessons: List<PublishedLesson>, busy: Boolean, onSave: (PublishedLesson) -> Unit) {
    var course by rememberSaveable { mutableStateOf(initial.courseTitle) }
    var module by rememberSaveable { mutableStateOf(initial.moduleTitle) }
    var title by rememberSaveable { mutableStateOf(initial.title) }
    var description by rememberSaveable { mutableStateOf(initial.description) }
    var url by rememberSaveable { mutableStateOf(initial.videoUrl) }
    var moduleOrder by rememberSaveable { mutableStateOf(initial.moduleOrder.toString()) }
    var lessonOrder by rememberSaveable { mutableStateOf(initial.lessonOrder.toString()) }
    var preview by remember { mutableStateOf(false) }
    if (preview) {
        YouTubeLessonScreen(VideoLesson(initial.id, title, description, url), onBack = { preview = false })
        return
    }
    val valid = course.isNotBlank() && module.isNotBlank() && title.isNotBlank() && YouTubeVideo.id(url) != null &&
        (moduleOrder.toLongOrNull() ?: 0) in 1..10000 && (lessonOrder.toLongOrNull() ?: 0) in 1..10000
    fun entry(published: Boolean) = initial.copy(courseTitle = course, moduleTitle = module, title = title,
        description = description, videoUrl = url, moduleOrder = moduleOrder.toLong(), lessonOrder = lessonOrder.toLong(), published = published)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(course, { course = it.take(120) }, label = { Text("Nome do curso") }, enabled = !busy, modifier = Modifier.fillMaxWidth())
        lessons.map { it.courseTitle }.distinct().take(10).forEach { name ->
            TextButton(enabled = !busy, onClick = { course = name }) { Text("Usar curso: $name") }
        }
        OutlinedTextField(module, { module = it.take(120) }, label = { Text("Nome do módulo") }, enabled = !busy, modifier = Modifier.fillMaxWidth())
        lessons.filter { it.courseTitle == course }.distinctBy { it.moduleTitle }.take(10).forEach { entry ->
            TextButton(enabled = !busy, onClick = { module = entry.moduleTitle; moduleOrder = entry.moduleOrder.toString() }) { Text("Usar módulo: ${entry.moduleTitle}") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(moduleOrder, { moduleOrder = it.filter(Char::isDigit).take(5) }, label = { Text("Ordem do módulo") }, enabled = !busy,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
            OutlinedTextField(lessonOrder, { lessonOrder = it.filter(Char::isDigit).take(5) }, label = { Text("Ordem da aula") }, enabled = !busy,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
        }
        OutlinedTextField(title, { title = it.take(160) }, label = { Text("Título da aula") }, enabled = !busy, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it.take(3000) }, label = { Text("Descrição (opcional)") }, enabled = !busy, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(url, { url = it.take(2048) }, label = { Text("Link do YouTube") }, enabled = !busy,
            isError = url.isNotBlank() && YouTubeVideo.id(url) == null, modifier = Modifier.fillMaxWidth(),
            supportingText = { Text("Cole o link de um vídeo público ou não listado, com incorporação permitida.") })
        TextButton(enabled = !busy && YouTubeVideo.id(url) != null, onClick = { preview = true }) { Text("Conferir vídeo no player") }
        Button(enabled = valid && !busy, onClick = { onSave(entry(true)) }, modifier = Modifier.fillMaxWidth()) { Text(if (busy) "Salvando…" else "Publicar aula para todos") }
        OutlinedButton(enabled = valid && !busy, onClick = { onSave(entry(false)) }, modifier = Modifier.fillMaxWidth()) {
            Text(if (initial.published) "Retirar do catálogo e salvar rascunho" else "Salvar rascunho")
        }
        Spacer(Modifier.height(24.dp))
    }
}
