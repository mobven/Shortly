package com.mobven.shortly.di

import android.content.ClipboardManager
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobven.shortly.adapter.ShortLinkAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

   @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager{
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Provides
    fun provideLinearLayoutManager(@ApplicationContext context: Context): LinearLayoutManager{
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        linearLayoutManager.stackFromEnd = true
        return linearLayoutManager
    }

    @Provides
    fun provideAdapter(): ShortLinkAdapter {
        return ShortLinkAdapter()
    }
}