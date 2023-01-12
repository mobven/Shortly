package com.mobven.shortly.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> AppCompatActivity.collectState(stateFlow: StateFlow<T>, action: (T) -> Unit) {
    lifecycleScope.launch {
        stateFlow
            .flowWithLifecycle(lifecycle, RESUMED)
            .collect { action(it) }
    }
}

fun <T> AppCompatActivity.collectEvent(sharedFlow: SharedFlow<T>, action: (T) -> Unit) {
    lifecycleScope.launch {
        sharedFlow
            .flowWithLifecycle(lifecycle, RESUMED)
            .collect { action(it) }
    }
}

fun <T> Fragment.collectState(stateFlow: StateFlow<T>, action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        stateFlow
            .flowWithLifecycle(lifecycle, RESUMED)
            .collect { action(it) }
    }
}

fun <T> Fragment.collectEvent(sharedFlow: SharedFlow<T>, action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        sharedFlow
            .flowWithLifecycle(lifecycle, RESUMED)
            .collect { action(it) }
    }
}