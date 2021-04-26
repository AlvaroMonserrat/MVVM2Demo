package com.rrat.mvvm2demo.view.activites

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.databinding.ActivityAddUpdateDishBinding
import com.rrat.mvvm2demo.databinding.DialogCustomImageSelectionBinding
import java.lang.Exception


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
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                ).withListener(object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent, CAMERA)
                            }
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // Show Alert Dialog
                        showRationalDialogPermissions()
                    }

                }).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                ).withListener(object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                startActivityForResult(galleryIntent, GALLERY)
                            }
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // Show Alert Dialog
                        showRationalDialogPermissions()
                    }

                }).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA){
                data?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                    binding.ivDishImage.setImageBitmap(thumbnail)
                    binding.ivAddDishImage.setImageResource(R.drawable.ic_baseline_edit_24)
                }
            }
            if(requestCode == GALLERY){
                data?.let {
                    val selectedPhotoUri = data.data
                    binding.ivDishImage.setImageURI(selectedPhotoUri)
                    binding.ivAddDishImage.setImageResource(R.drawable.ic_baseline_edit_24)
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Cancelled", "User cancel image selection")
        }
    }

    private fun showRationalDialogPermissions(){
        AlertDialog.Builder(this).setMessage("It Looks like you have turned off permissions" +
                "required for this feature. It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS"){
                _,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){
                    dialog,_-> dialog.dismiss()
            }.show()
    }

    companion object{
        private const val CAMERA = 1
        private const val GALLERY = 2
    }


}