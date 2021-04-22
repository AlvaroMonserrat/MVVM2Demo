package com.rrat.mvvm2demo.view.activites

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.databinding.ActivityAddUpdateDishBinding
import com.rrat.mvvm2demo.databinding.DialogCustomImageSelectionBinding


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
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
                DialogCustomImageSelectionBinding.inflate(layoutInflater)

        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Toast.makeText(this, "Gallery clicked", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()

    }


}