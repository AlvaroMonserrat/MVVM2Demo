package com.rrat.mvvm2demo.view.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.databinding.ActivityAddUpdateDishBinding


class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupActionBar()

        binding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.iv_add_dish_image ->{
                    Toast.makeText(this,    "You have clicked the image", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }


}