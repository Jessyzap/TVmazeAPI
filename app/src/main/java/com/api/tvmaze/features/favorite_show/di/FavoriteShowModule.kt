package com.api.tvmaze.features.favorite_show.di

import com.api.tvmaze.features.favorite_show.data.repository.FavoriteShowRepositoryImpl
import com.api.tvmaze.features.favorite_show.data.datasource.local.IFavoriteShowLocalDataSource
import com.api.tvmaze.features.favorite_show.data.datasource.local.IFavoriteShowLocalDataSourceImpl
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteShowModule {

    @Provides
    @Singleton
    fun bindLocalDataSource(): IFavoriteShowLocalDataSource = IFavoriteShowLocalDataSourceImpl()

    @Provides
    @Singleton
    fun bindRepository(
        localDataSource: IFavoriteShowLocalDataSource
    ): IFavoriteShowRepository = FavoriteShowRepositoryImpl(
        showLocalDataSource = localDataSource,
    )

}