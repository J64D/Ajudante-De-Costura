package com.scarletstudio.ajudantedecostura.data.learning

import org.junit.Assert.*
import org.junit.Test

class CourseCatalogTest {
    @Test fun hidesDraftsAndSortsModulesAndLessons() {
        val base = PublishedLesson(courseTitle = "Costura", moduleTitle = "Básico", published = true)
        val courses = CourseRepository.courses(listOf(
            base.copy(id = "draft", published = false),
            base.copy(id = "second", lessonOrder = 2),
            base.copy(id = "advanced", moduleTitle = "Acabamento", moduleOrder = 2),
            base.copy(id = "first", lessonOrder = 1)
        ))
        assertEquals(1, courses.size)
        assertEquals(listOf("Básico", "Acabamento"), courses.single().modules.map { it.title })
        assertEquals(listOf("first", "second"), courses.single().modules.first().lessons.map { it.id })
    }
    @Test fun draftsDoNotCreatePublicCourses() {
        assertTrue(CourseRepository.courses(listOf(PublishedLesson(courseTitle = "Segredo"))).isEmpty())
    }
}
