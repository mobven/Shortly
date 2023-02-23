package com.mobven.shortly.ui.list

import com.google.android.play.core.review.ReviewInfo

sealed class MyLinksUiEvent {

    object ShowError : MyLinksUiEvent()
    class LaunchReviewFlow(val reviewInfo: ReviewInfo) : MyLinksUiEvent()

}