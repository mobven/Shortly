package com.mobven.shortly.di

import com.mobven.shortly.executor.PostExecutionThreadImpl
import com.mobven.shortly.executor.PostExecutorThread
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class ExecutorModule {

    @Binds
    abstract fun bindPostExecution(
        postExecutionThreadImpl: PostExecutionThreadImpl
    ): PostExecutorThread
}