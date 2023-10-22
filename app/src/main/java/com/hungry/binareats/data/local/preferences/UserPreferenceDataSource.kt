package com.hungry.binareats.data.local.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.hungry.binareats.utils.PreferenceDataStoreHelper
import kotlinx.coroutines.flow.Flow

interface UserPreferenceDataSource {
    suspend fun getUserLayoutPref(): Boolean
    fun getUserLayoutPrefFlow(): Flow<Boolean>
    suspend fun setUserLayoutPref(isLinearMode: Boolean)
}

class UserPreferenceDataSourceImpl(
    private val preferenceHelper: PreferenceDataStoreHelper
) : UserPreferenceDataSource {


    companion object {
        val PREF_USER_LAYOUT =  booleanPreferencesKey("PREF_USER_LAYOUT")
    }

    override suspend fun getUserLayoutPref(): Boolean {
        return preferenceHelper.getFirstPreference(PREF_USER_LAYOUT, false)
    }

    override fun getUserLayoutPrefFlow(): Flow<Boolean> {
        return preferenceHelper.getPreference(PREF_USER_LAYOUT,false)
    }

    override suspend fun setUserLayoutPref(isLinearMode: Boolean) {
        return preferenceHelper.putPreference(PREF_USER_LAYOUT, isLinearMode)
    }
}
