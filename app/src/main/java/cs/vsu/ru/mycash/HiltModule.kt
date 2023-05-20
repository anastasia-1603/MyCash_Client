package cs.vsu.ru.mycash

import cs.vsu.ru.mycash.api.TokenApiService
import cs.vsu.ru.mycash.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideAuthRepository(tokenApiService: TokenApiService) = AuthRepository(tokenApiService)
}