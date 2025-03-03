package com.api.tvmaze.features.show.di

import com.api.tvmaze.features.show.data.datasource.remote.IShowRemoteDataSource
import com.api.tvmaze.features.show.data.datasource.remote.ShowRemoteDataSourceImpl
import com.api.tvmaze.features.show.data.repository.ShowRepositoryImp
import com.api.tvmaze.features.show.domain.IShowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShowModule {

    @Provides
    @Singleton
    fun bindRemoteDataSource(): IShowRemoteDataSource = ShowRemoteDataSourceImpl()

    @Provides
    @Singleton
    fun bindRepository(
        remoteDataSource: IShowRemoteDataSource
    ): IShowRepository = ShowRepositoryImp(
        showRemoteDataSource = remoteDataSource
    )

}