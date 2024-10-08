package com.example.limitlife.hilt

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.limitlife.repository.AuthInspector
import com.example.limitlife.repository.FakeUserDataRepository
import com.example.limitlife.repository.NetworkUserDataRepository
import com.example.limitlife.repository.OfflineUserTokenRepository
import com.example.limitlife.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import javax.inject.Singleton


private const val USER_PREFERENCE_NAME = "token_preferences"
private val Application.datastore : DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object AppModuleForToken{

    @Provides
    @Singleton // other option is binding .
    fun provideDataStore(application: Application )  : DataStore<Preferences> {
        return application.datastore
    }
    @Provides // tell how we will create the token provider instance
    @Singleton
    fun provideUserTokenRepository (dataStore: DataStore<Preferences>) : OfflineUserTokenRepository {
        return OfflineUserTokenRepository(dataStore = dataStore )
    }

}


@Module // indicate the class as a module where we will provide the dependency
@InstallIn(SingletonComponent::class) // this makes the app dependency available through  the app ensuring a single instance is used.
abstract class AppModule {
    @Singleton
    @Binds
   abstract fun getUserDataRepository(
        networkUserRepository : NetworkUserDataRepository
    ) : UserDataRepository

    @Singleton
    @Binds
    abstract fun getFakeUserDataRepository(
        fakeUserDataRepository : FakeUserDataRepository
    ) : UserDataRepository
}

