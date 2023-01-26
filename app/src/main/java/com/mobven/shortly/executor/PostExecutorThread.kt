package com.mobven.shortly.executor

import kotlinx.coroutines.CoroutineDispatcher

interface PostExecutorThread {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}