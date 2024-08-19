import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val API_SETTINGS_FILE = "API_SETTINGS_FILE"
private val Context.store : DataStore<Preferences> by preferencesDataStore(name = API_SETTINGS_FILE)
class Preferences(
   private val context: Context
) {

    /**
     *  Set preference value for referenced key in suspend function
     */

    suspend fun set(preference: String, value : Boolean = false) {
        context.store.edit { settings ->
            settings[getPreferenceBoolean(preference)] = value
        }
    }


    suspend fun set(preference: String, value : String) {
        context.store.edit { settings ->
            settings[getPreferenceString(preference)] = value
        }
    }


    suspend fun set(preference: String, value : Int) {
        context.store.edit { settings ->
            settings[getPreferenceInt(preference)] = value
        }
    }


    suspend fun set(preference: String, value : Long) {
        context.store.edit { settings ->
            settings[getPreferenceLong(preference)] = value
        }
    }


    suspend fun set(preference: String, value : Float) {
        context.store.edit { settings ->
            settings[getPreferenceFloat(preference)] = value
        }
    }


    suspend fun set(preference: String, value : Double) {
        context.store.edit { settings ->
            settings[getPreferenceDouble(preference)] = value
        }
    }

    /**
     *  Get preference value for referenced key in suspend function
     */


    suspend fun getBoolean(preference: String,defaultValue : Boolean = false): Boolean {
        return context.store.data.map { preferences ->
            preferences[getPreferenceBoolean(preference)]?:defaultValue
        }.first()
    }


    suspend fun getString(preference: String, defaultValue : String) : String {
        return context.store.data.map { preferences ->
            preferences[getPreferenceString(preference)]?:defaultValue
        }.first()
    }


    suspend fun getInt(preference: String,defaultValue : Int = -1) : Int {
        return context.store.data.map { preferences ->
            preferences[getPreferenceInt(preference)]?:defaultValue
        }.first()
    }


    suspend fun getLong(preference: String,defaultValue: Long = -1) : Long {
        return context.store.data.map { preferences ->
            preferences[getPreferenceLong(preference)]?:defaultValue
        }.first()
    }


    suspend fun getFloat(preference: String, defaultValue: Float = -1f) : Float {
        return context.store.data.map { preferences ->
            preferences[getPreferenceFloat(preference)]?:defaultValue
        }.first()
    }


    suspend fun getDouble(preference: String, defaultValue: Double = -1.0) : Double {
        return context.store.data.map { preferences ->
            preferences[getPreferenceDouble(preference)]?:defaultValue
        }.first()
    }

    /**
     *  Get preference value for referenced key in flow
     *  in case if is null or error return a default value
     */


    fun getStringFlow(preference: String, defaultValue : String) : Flow<String> {
        return context.store
            .data
            .mappingNullableFlow(
                preferenceKey =  getPreferenceString(preference),
                defaultValue = defaultValue
            ).filterNotNull()
    }


    fun getIntFlow(preference: String,defaultValue : Int = -1) : Flow<Int> {
        return context.store
            .data
            .mappingFlow(
                preferenceKey =  getPreferenceInt(preference),
                defaultValue = defaultValue
            )
    }


    fun getLongFlow(preference: String,defaultValue: Long = -1) : Flow<Long> {
        return context.store
            .data
            .mappingFlow(
                preferenceKey =  getPreferenceLong(preference),
                defaultValue = defaultValue
            )
    }


    fun getFloatFlow(preference: String, defaultValue: Float = -1f) : Flow<Float> {
        return context.store
            .data
            .mappingFlow(
                preferenceKey =  getPreferenceFloat(preference),
                defaultValue = defaultValue
            )
    }


    fun getDoubleFlow(preference: String, defaultValue: Double = -1.0) : Flow<Double> {
        return context.store
            .data
            .mappingFlow(
                preferenceKey =  getPreferenceDouble(preference),
                defaultValue = defaultValue
            )
    }


    fun getBooleanFlow(preference: String,defaultValue : Boolean = false): Flow<Boolean> {
        return context.store
            .data
            .mappingFlow(
                preferenceKey =  getPreferenceBoolean(preference),
                defaultValue = defaultValue
            )
    }


    private fun<T> Flow<Preferences>.mappingFlow(preferenceKey: Preferences.Key<T>, defaultValue : T) : Flow<T> {
        return map { preferences ->
            preferences[preferenceKey]?:defaultValue
        }.catch { exception ->
            if (exception is IOException) {
                emit(defaultValue)
            }
            else {
                throw exception
            }
        }
    }


    private fun<T> Flow<Preferences>.mappingNullableFlow(preferenceKey: Preferences.Key<T>, defaultValue : T?) : Flow<T?> {
        return map { preferences ->
            preferences[preferenceKey]?:defaultValue
        }.catch { exception ->
            if (exception is IOException) {
                emit(defaultValue)
            }
            else {
                throw exception
            }
        }
    }


    /**
     * Get preference keys in type of value
     * for private operations
     */

    private fun getPreferenceBoolean(preference: String) : Preferences.Key<Boolean> {
        return booleanPreferencesKey(preference)
    }

    private fun getPreferenceString(preference: String) : Preferences.Key<String> {
        return stringPreferencesKey(preference)
    }

    private fun getPreferenceInt(preference: String) : Preferences.Key<Int> {
        return intPreferencesKey(preference)
    }

    private fun getPreferenceLong(preference: String) : Preferences.Key<Long> {
        return longPreferencesKey(preference)
    }

    private fun getPreferenceFloat(preference: String) : Preferences.Key<Float> {
        return getPreferenceFloat(preference)
    }

    private fun getPreferenceDouble(preference: String) : Preferences.Key<Double> {
        return doublePreferencesKey(preference)
    }
}
