package com.example.apppealolder.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.apppealolder.R
import com.example.apppealolder.databinding.AddAppealActivityBinding

class AddAppealActivity : AppCompatActivity() {
    private val binding: AddAppealActivityBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_appeal_activity)


    }
}