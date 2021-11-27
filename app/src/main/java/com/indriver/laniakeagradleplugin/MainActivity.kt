package com.indriver.laniakeagradleplugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dvinc.feature_1.Feature1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Feature1
    }
}