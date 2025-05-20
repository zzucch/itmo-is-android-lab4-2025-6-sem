package com.zcchr.lab4.di

import com.zcchr.lab4.data.repository.DocumentRepository
import com.zcchr.lab4.data.repository.RemoteDataSource
import com.zcchr.lab4.parser.MarkdownParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource = RemoteDataSource()

    @Provides
    @Singleton
    fun provideDocumentRepository(remoteDataSource: RemoteDataSource): DocumentRepository =
        DocumentRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideMarkdownParser(): MarkdownParser = MarkdownParser()
}
