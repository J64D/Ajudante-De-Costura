package com.scarletstudio.ajudantedecostura.data.learning

/** Public course content. The publishing service supplies only published records. */
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val modules: List<CourseModule> = emptyList()
)

data class CourseModule(
    val id: String,
    val title: String,
    val lessons: List<VideoLesson> = emptyList()
)

data class VideoLesson(
    val id: String,
    val title: String,
    val description: String,
    val videoUrl: String
)
