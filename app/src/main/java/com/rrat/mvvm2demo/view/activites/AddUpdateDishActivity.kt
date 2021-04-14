package com.rrat.mvvm2demo.view.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.databinding.ActivityAddUpdateDishBinding


class AddUpdateDishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }


}