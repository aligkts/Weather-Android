package com.aligkts.weatherapp.view.ui.viewpager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aligkts.weatherapp.R
import kotlinx.android.synthetic.main.fragment_add_location_webview.*

class AddLocationWebviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_location_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addLocationWebview.loadUrl("file:///android_asset/html/add_loc_html.html")
        addLocationWebview.settings.loadWithOverviewMode = true
        addLocationWebview.settings.useWideViewPort = true
    }


}
