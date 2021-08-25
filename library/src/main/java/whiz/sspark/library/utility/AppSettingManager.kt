package whiz.sspark.library.utility

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppSettingManager(private val context: Context) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_setting")
    val THEME_KEY = intPreferencesKey("Theme")

    val exampleCounterFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: 0
        }

    suspend fun incrementCounter() {
        context.dataStore.edit { settings ->
            val currentCounterValue = settings[THEME_KEY] ?: 0
            settings[THEME_KEY] = currentCounterValue + 1
        }
    }

    suspend fun clearCounter() {
        context.dataStore.edit {
            it.remove(THEME_KEY)
        }
    }
}