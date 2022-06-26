package com.rrat.mvvm2demo.view.activites

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.application.FavDishApplication
import com.rrat.mvvm2demo.databinding.ActivityAddUpdateDishBinding
import com.rrat.mvvm2demo.databinding.DialogCustomImageSelectionBinding
import com.rrat.mvvm2demo.databinding.DialogCustomListBinding
import com.rrat.mvvm2demo.model.entities.FavDish
import com.rrat.mvvm2demo.utils.Constants
import com.rrat.mvvm2demo.view.adapters.CustomListItemAdapter
import com.rrat.mvvm2demo.viewmodel.FavDishViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.util.*


class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDishBinding
    private var mImagePath : String = ""

    private lateinit var mCustomListDialog: Dialog

    private var mFavDishDetails: FavDish? = null

//    private val mFavDishViewModel : FavDishViewModel by viewModels{
//        FavDishViewModelFactory((application as FavDishApplication).repository)
//    }
    private val mFavDishViewModel : FavDishViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(binding.root)



        if(intent.hasExtra(Constants.EXTRA_DISH_DETAILS))
        {
            mFavDishDetails = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }

        setupActionBar()

        mFavDishDetails?.let {
            if(it.id != 0)
            {
                mImagePath = it.image
                Glide.with(this@AddUpdateDishActivity)
                    .load(mImagePath)
                    .centerCrop()
                    .into(binding.ivDishImage)

                binding.etTitle.setText(it.title)
                binding.etType.setText(it.type)
                binding.etCategory.setText(it.category)
                binding.etIngredients.setText(it.ingredients)
                binding.etCookingTime.setText(it.cookingTime)
                binding.etDirectionToCook.setText(it.directionToCook)

                binding.btnAddDish.text = resources.getString(R.string.lbl_update_dish)
            }
        }

        binding.ivAddDishImage.setOnClickListener(this)

        binding.etType.setOnClickListener(this)
        binding.etCategory.setOnClickListener(this)
        binding.etCookingTime.setOnClickListener(this)
        binding.btnAddDish.setOnClickListener(this)


    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddDishActivity)

        if(mFavDishDetails != null && mFavDishDetails!!.id != 0)
        {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_dish)
            }
        }
        else
        {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_dish)
            }
        }

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
                R.id.et_type ->{
                    customItemsListDialog(
                            resources.getString(R.string.title_selected_dish_type),
                            Constants.dishTypes(),
                            Constants.DISH_TYPE)
                }
                R.id.et_category ->{
                    customItemsListDialog(
                            resources.getString(R.string.title_selected_dish_category),
                            Constants.dishCategories(),
                            Constants.DISH_CATEGORY)
                }
                R.id.et_cooking_time->{
                    customItemsListDialog(
                            resources.getString(R.string.title_selected_dish_cooking_time),
                            Constants.dishCookTime(),
                            Constants.DISH_COOKING_TIME)
                }
                R.id.btn_add_dish->{
                    val title = binding.etTitle.text.toString().trim{ it <= ' ' }
                    val type = binding.etType.text.toString().trim{ it <= ' '}
                    val category = binding.etCategory.text.toString().trim{ it <= ' '}
                    val ingredients = binding.etIngredients.text.toString().trim{ it <= ' '}
                    val cookingTimeInMinutes = binding.etCookingTime.text.toString().trim{ it <= ' '}
                    val cookingDirection = binding.etDirectionToCook.text.toString().trim{ it <= ' '}


                    when {
                        TextUtils.isEmpty(mImagePath)->{
                            Toast.makeText(
                                    this@AddUpdateDishActivity,
                                    resources.getString(R.string.err_msg_select_dish_image),
                                    Toast.LENGTH_SHORT
                                    ).show()
                        }
                        TextUtils.isEmpty(title)->{
                            Toast.makeText(
                                    this@AddUpdateDishActivity,
                                    resources.getString(R.string.err_msg_enter_dish_title),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(type)->{
                            Toast.makeText(
                                    this@AddUpdateDishActivity,
                                    resources.getString(R.string.err_msg_select_dish_type),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(category)->{
                            Toast.makeText(
                                    this@AddUpdateDishActivity,
                                    resources.getString(R.string.err_msg_select_dish_category),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(ingredients)->{
                            Toast.makeText(
                                    this@AddUpdateDishActivity,
                                    resources.getString(R.string.err_msg_select_dish_ingredients),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingTimeInMinutes)->{
                            Toast.makeText(
                                    this@AddUpdateDishActivity,
                                    resources.getString(R.string.err_msg_select_dish_cooking_time),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingDirection)->{
                            Toast.makeText(
                                    this@AddUpdateDishActivity,
                                    resources.getString(R.string.err_msg_select_dish_cooking_instructions),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {

                            var dishID = 0
                            var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                            var favoriteDish = false

                            mFavDishDetails?.let {
                                if(it.id != 0)
                                {
                                    dishID = it.id
                                    imageSource = it.imageSource
                                    favoriteDish = it.favoriteDish
                                }
                            }

                            val favDishDetails: FavDish = FavDish(
                                mImagePath,
                                imageSource,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTimeInMinutes,
                                cookingDirection,
                                favoriteDish,
                                dishID
                            )

                            if(dishID == 0){
                                mFavDishViewModel.insert(favDishDetails)
                                Toast.makeText(this@AddUpdateDishActivity,
                                    "You successfully added your favorite dish details",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.i("Insertion", "Insert Database successfully")

                            }else
                            {
                                mFavDishViewModel.update(favDishDetails)
                                Toast.makeText(this@AddUpdateDishActivity,
                                    "You successfully updated your favorite dish details",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.i("Updating", "Update Database successfully")
                            }

                            finish()


                        }
                    }

                }
            }
        }
    }

    private fun customImageSelectionDialog(){
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
                DialogCustomImageSelectionBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)

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

            mCustomListDialog.dismiss()
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

            mCustomListDialog.dismiss()
        }

        mCustomListDialog.show()

    }

    fun selectedListItem(item: String, selection: String){
        when(selection){
            Constants.DISH_TYPE ->{
                mCustomListDialog.dismiss()
                binding.etType.setText(item)
            }
            Constants.DISH_CATEGORY ->{
                mCustomListDialog.dismiss()
                binding.etCategory.setText(item)
            }
            Constants.DISH_COOKING_TIME ->{
                mCustomListDialog.dismiss()
                binding.etCookingTime.setText(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA){
                data?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap

                    Glide.with(this)
                            .load(thumbnail)
                            .centerCrop()
                            .into(binding.ivDishImage)

                    mImagePath = saveImageToInternalStorage(thumbnail)

                    Log.i("ImagePath", mImagePath)

                    binding.ivAddDishImage.setImageResource(R.drawable.ic_baseline_edit_24)
                }
            }
            if(requestCode == GALLERY){
                data?.let {
                    val selectedPhotoUri = data.data

                    Glide.with(this)
                            .load(selectedPhotoUri)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable>{
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                    Log.e("TAG", "Error loading image", e)
                                    return false
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    resource?.let {
                                        val bitmap: Bitmap = resource.toBitmap()
                                        mImagePath = saveImageToInternalStorage(bitmap)
                                    }
                                    return false
                                }

                            })
                            .into(binding.ivDishImage)

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

    private fun saveImageToInternalStorage(bitmap: Bitmap) : String {
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return file.absolutePath
    }

    private fun customItemsListDialog(title: String, list: List<String>, selection: String){
        mCustomListDialog = Dialog(this)
        val binding : DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)

        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = CustomListItemAdapter(this, null, list, selection)
        binding.rvList.adapter = adapter

        mCustomListDialog.show()

    }

    companion object{
        private const val CAMERA = 1
        private const val GALLERY = 2

        private const val IMAGE_DIRECTORY = "FavDishImages"
    }


}