package whiz.tss.sspark.s_spark_android.utility

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile")

class ProfileManager(private val context: Context) {

    val STUDENT_KEY = stringPreferencesKey("Student")
    val INSTRUCTOR_KEY = stringPreferencesKey("Instructor")
    val GUARDIAN_KEY = stringPreferencesKey("Guardian")

    val student: Flow<Student?> = context.dataStore.data
        .map { preferences ->
            preferences[STUDENT_KEY]?.toObject<Student>()
        }

    val instructor: Flow<Instructor?> = context.dataStore.data
        .map { preferences ->
            preferences[INSTRUCTOR_KEY]?.toObject<Instructor>()
        }

    val profile: Flow<Profile?> = context.dataStore.data
        .map { preferences ->
            when (SSparkApp.role) {
                RoleType.JUNIOR,
                RoleType.SENIOR -> preferences[STUDENT_KEY]?.toObject<Student>()?.convertToProfile()
                RoleType.INSTRUCTOR -> preferences[INSTRUCTOR_KEY]?.toObject<Instructor>()?.convertToProfile()
                else -> null
            }
        }

    val term: Flow<Term?> = context.dataStore.data
        .map { preferences ->
            when (SSparkApp.role) {
                RoleType.JUNIOR,
                RoleType.SENIOR -> preferences[STUDENT_KEY]?.toObject<Student>()?.term
                RoleType.INSTRUCTOR -> preferences[INSTRUCTOR_KEY]?.toObject<Instructor>()?.term
                else -> null
            }
        }

    suspend fun saveStudent(student: Student) {
        context.dataStore.edit { settings ->
            settings[STUDENT_KEY] = student.toJson()
        }
    }

    suspend fun clearData() {
        context.dataStore.edit {
            it.clear()
        }
    }
}