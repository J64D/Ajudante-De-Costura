package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scarletstudio.ajudantedecostura.data.learning.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(onBack: () -> Unit, courses: List<Course> = emptyList(), onManage: () -> Unit = {}, loading: Boolean = false, error: String? = null) {
    var courseId by rememberSaveable { mutableStateOf<String?>(null) }
    var moduleId by rememberSaveable { mutableStateOf<String?>(null) }
    val course = courses.find { it.id == courseId }
    val module = course?.modules?.find { it.id == moduleId }
    var lessonId by rememberSaveable { mutableStateOf<String?>(null) }
    val lesson = module?.lessons?.find { it.id == lessonId }
    if (lesson != null) {
        YouTubeLessonScreen(lesson, onBack = { lessonId = null })
        return
    }
    val back: () -> Unit = {
        when {
            moduleId != null -> { moduleId = null; lessonId = null }
            courseId != null -> courseId = null
            else -> onBack()
        }
    }
    BackHandler(enabled = courseId != null, onBack = back)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (module != null) "Aulas" else if (course != null) "Módulos" else "Cursos") },
                actions = { TextButton(onClick = onManage) { Text("Administrar") } },
                navigationIcon = {
                    IconButton(onClick = back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (courses.isNotEmpty()) item { InlineAd() }
            item {
                com.scarletstudio.ajudantedecostura.ui.theme.LiquidHero(
                    if (module != null) "SUAS AULAS" else "APRENDA NO SEU RITMO",
                    module?.title ?: course?.title ?: "Um novo ponto de partida.",
                    if (module != null) "Escolha uma aula para assistir." else "Cursos organizados em módulos, passo a passo.")
            }

            if (loading) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
            if (error != null) item { Text(error, color = MaterialTheme.colorScheme.error) }
            when {
                module != null -> {
                    if (module.lessons.isEmpty()) item { Text("Nenhuma aula publicada neste módulo ainda.") }
                    items(module.lessons, key = { it.id }) { lesson ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(lesson.title, fontWeight = FontWeight.Bold)
                                if (lesson.description.isNotBlank()) Text(lesson.description)
                                Button(onClick = { lessonId = lesson.id }) {
                                    Icon(Icons.Default.PlayCircle, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Assistir aula")
                                }
                            }
                        }
                    }
                }
                course != null -> {
                    if (course.modules.isEmpty()) item { Text("Os módulos deste curso serão publicados em breve.") }
                    items(course.modules, key = { it.id }) { entry ->
                        Card(onClick = { moduleId = entry.id }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text(entry.title, fontWeight = FontWeight.Bold)
                                Text("${entry.lessons.size} aula(s)")
                                Text("Ver aulas", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
                else -> {
                    if (courses.isEmpty() && !loading && error == null) item {
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Novos cursos em breve", fontWeight = FontWeight.Bold)
                                Text("Quando as aulas forem publicadas, você encontrará os cursos e seus módulos aqui.")
                            }
                        }
                    }
                    items(courses, key = { it.id }) { entry ->
                        Card(onClick = { courseId = entry.id; moduleId = null }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(entry.title, fontWeight = FontWeight.Bold)
                                Text(entry.description)
                                Text("${entry.modules.size} módulo(s) • ${entry.modules.sumOf { it.lessons.size }} aula(s)")
                                Text("Ver curso", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

