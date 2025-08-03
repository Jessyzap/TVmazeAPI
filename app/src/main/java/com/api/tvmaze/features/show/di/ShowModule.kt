package com.api.tvmaze.features.show.di

import com.api.tvmaze.features.show.data.datasource.remote.IShowRemoteDataSource
import com.api.tvmaze.features.show.data.datasource.remote.ShowRemoteDataSourceImpl
import com.api.tvmaze.features.show.data.datasource.service.EpisodeAPI
import com.api.tvmaze.features.show.data.datasource.service.SearchAPI
import com.api.tvmaze.features.show.data.datasource.service.SeasonAPI
import com.api.tvmaze.features.show.data.datasource.service.ShowAPI
import com.api.tvmaze.features.show.data.repository.ShowRepositoryImp
import com.api.tvmaze.features.show.domain.IShowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShowModule {

    @Provides
    @Singleton
    fun provideShowApiService(retrofit: Retrofit): ShowAPI {
        return retrofit.create(ShowAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideSeasonApiService(retrofit: Retrofit): SeasonAPI {
        return retrofit.create(SeasonAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideEpisodeApiService(retrofit: Retrofit): EpisodeAPI {
        return retrofit.create(EpisodeAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchAPIApiService(retrofit: Retrofit): SearchAPI {
        return retrofit.create(SearchAPI::class.java)
    }

    @Provides
    @Singleton
    fun bindRemoteDataSource(
        showAPI: ShowAPI,
        seasonAPI: SeasonAPI,
        episodeAPI: EpisodeAPI,
        searchAPI: SearchAPI
    ): IShowRemoteDataSource = ShowRemoteDataSourceImpl(
        showAPI = showAPI,
        seasonAPI = seasonAPI,
        episodeAPI = episodeAPI,
        searchAPI = searchAPI
    )

    @Provides
    @Singleton
    fun bindRepository(
        remoteDataSource: IShowRemoteDataSource
    ): IShowRepository = ShowRepositoryImp(
        showRemoteDataSource = remoteDataSource
    )

}