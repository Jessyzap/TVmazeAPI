package com.api.tvmaze.di

import com.api.tvmaze.data.datasource.local.IShowLocalDataSource
import com.api.tvmaze.data.datasource.local.ShowLocalDataSourceImpl
import com.api.tvmaze.data.datasource.remote.IShowRemoteDataSource
import com.api.tvmaze.data.datasource.remote.ShowRemoteDataSourceImpl
import com.api.tvmaze.data.repository.ShowRepositoryImp
import com.api.tvmaze.domain.IShowRepository
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
    fun bindLocalDataSource(): IShowLocalDataSource = ShowLocalDataSourceImpl()

    @Provides
    @Singleton
    fun bindRepository(
        remoteDataSource: IShowRemoteDataSource,
        localDataSource: IShowLocalDataSource
    ): IShowRepository = ShowRepositoryImp(
        showLocalDataSource = localDataSource,
        showRemoteDataSource = remoteDataSource
    )

}