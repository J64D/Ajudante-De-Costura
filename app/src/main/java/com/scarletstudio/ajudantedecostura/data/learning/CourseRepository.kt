package com.scarletstudio.ajudantedecostura.data.learning

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.scarletstudio.ajudantedecostura.BuildConfig

data class PublishedLesson(
    val id: String = "",
    val courseTitle: String = "",
    val moduleTitle: String = "",
    val title: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val moduleOrder: Long = 1,
    val lessonOrder: Long = 1,
    val published: Boolean = false
)

class CourseRepository private constructor(app: FirebaseApp) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance(app)
    private val database = FirebaseFirestore.getInstance(app)
    private val lessons = database.collection("courseLessons")

    fun watch(admin: Boolean, result: (List<PublishedLesson>?, Exception?) -> Unit): ListenerRegistration {
        val query = if (admin) lessons else lessons.whereEqualTo("published", true)
        return query.addSnapshotListener(com.google.firebase.firestore.MetadataChanges.INCLUDE) { snapshot, error ->
            result(snapshot?.takeUnless { it.metadata.hasPendingWrites() }?.documents?.map { doc ->
                PublishedLesson(
                    id = doc.id,
                    courseTitle = doc.getString("courseTitle").orEmpty(),
                    moduleTitle = doc.getString("moduleTitle").orEmpty(),
                    title = doc.getString("title").orEmpty(),
                    description = doc.getString("description").orEmpty(),
                    videoUrl = doc.getString("videoUrl").orEmpty(),
                    moduleOrder = doc.getLong("moduleOrder") ?: 1,
                    lessonOrder = doc.getLong("lessonOrder") ?: 1,
                    published = doc.getBoolean("published") == true
                )
            }, error)
        }
    }

    fun checkAdmin(result: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return result(false)
        database.collection("courseAdmins").document(uid).get(com.google.firebase.firestore.Source.SERVER)
            .addOnSuccessListener { result(it.getBoolean("enabled") == true) }
            .addOnFailureListener { result(false) }
    }

    fun save(lesson: PublishedLesson, result: (Exception?) -> Unit) {
        val ref = if (lesson.id.isBlank()) lessons.document() else lessons.document(lesson.id)
        ref.set(mapOf(
            "courseTitle" to lesson.courseTitle.trim(), "moduleTitle" to lesson.moduleTitle.trim(),
            "title" to lesson.title.trim(), "description" to lesson.description.trim(),
            "videoUrl" to "https://www.youtube.com/watch?v=${YouTubeVideo.id(lesson.videoUrl)}",
            "moduleOrder" to lesson.moduleOrder, "lessonOrder" to lesson.lessonOrder,
            "published" to lesson.published
        )).addOnSuccessListener { result(null) }.addOnFailureListener { result(it) }
    }

    companion object {
        fun create(context: Context): CourseRepository? {
            if (listOf(BuildConfig.COURSES_API_KEY, BuildConfig.COURSES_APP_ID, BuildConfig.COURSES_PROJECT_ID).any { it.isBlank() }) return null
            val app = FirebaseApp.getApps(context).find { it.name == "courses" }
                ?: FirebaseApp.initializeApp(context, FirebaseOptions.Builder()
                    .setApiKey(BuildConfig.COURSES_API_KEY).setApplicationId(BuildConfig.COURSES_APP_ID)
                    .setProjectId(BuildConfig.COURSES_PROJECT_ID).build(), "courses")
            return CourseRepository(app)
        }

        fun courses(lessons: List<PublishedLesson>): List<Course> = lessons.filter { it.published }
            .groupBy { it.courseTitle }.toSortedMap().map { (title, courseLessons) ->
                Course(title, title, "", courseLessons.groupBy { it.moduleTitle }.entries
                    .sortedWith(compareBy({ it.value.minOf { lesson -> lesson.moduleOrder } }, { it.key }))
                    .map { (module, entries) -> CourseModule(module, module,
                        entries.sortedWith(compareBy({ it.lessonOrder }, { it.title }, { it.id }))
                            .map { VideoLesson(it.id, it.title, it.description, it.videoUrl) }) })
            }
    }
}
