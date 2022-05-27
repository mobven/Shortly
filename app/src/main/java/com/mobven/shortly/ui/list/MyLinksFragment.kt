package com.mobven.shortly.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobven.shortly.R

class MyLinksFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mylist, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyLinksFragment()
    }
}