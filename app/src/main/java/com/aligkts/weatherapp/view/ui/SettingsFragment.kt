package com.aligkts.weatherapp.view.ui


import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.util.HeatUnit
import com.aligkts.weatherapp.util.UnitType
import com.aligkts.weatherapp.util.toast
import kotlinx.android.synthetic.main.custom_alert_dialog_rate_app.view.*
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }
    private lateinit var currentUnit: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        currentUnit = prefs.getString("unitType", "Metric")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textFontLightx = Typeface.createFromAsset(activity!!.assets, "fonts/MontserratLight.ttf")
        switchHeatUnit.typeface = textFontLightx
        updateSwitchPosition()
        switchHeatUnit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                currentUnit = UnitType.Imperial.toString()
                updateSwitchPosition()
            } else {
                currentUnit = UnitType.Metric.toString()
                updateSwitchPosition()
            }
            prefs.edit().putString("unitType", currentUnit).apply()
        }
    }

    private fun updateSwitchPosition() {
        when (currentUnit) {
            UnitType.Imperial.toString() -> {
                switchHeatUnit.isChecked = true
                switchHeatUnit.text = " \u2109"
            }
            UnitType.Metric.toString() -> {
                switchHeatUnit.isChecked = false
                switchHeatUnit.text = "C"+ 0x00B0.toChar()
            }
        }
    }

}
