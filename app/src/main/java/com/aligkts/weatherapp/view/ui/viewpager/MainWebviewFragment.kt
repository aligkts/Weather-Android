package com.aligkts.weatherapp.view.ui.viewpager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aligkts.weatherapp.R
import kotlinx.android.synthetic.main.fragment_main_webview.*

class MainWebviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainWebview.loadUrl("file:///android_asset/html/main_html.html")
        mainWebview.settings.loadWithOverviewMode = true
        mainWebview.settings.useWideViewPort = true
    }

}
