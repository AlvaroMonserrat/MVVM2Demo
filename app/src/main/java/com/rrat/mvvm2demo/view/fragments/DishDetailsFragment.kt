    package com.rrat.mvvm2demo.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette;
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.application.FavDishApplication
import com.rrat.mvvm2demo.databinding.FragmentDishDetailsBinding
import com.rrat.mvvm2demo.viewmodel.FavDishViewModel
import com.rrat.mvvm2demo.viewmodel.FavDishViewModelFactory
import java.io.IOException
import java.util.*


    class DishDetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentDishDetailsBinding

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDishDetailsBinding.inflate(inflater, container, false)



        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()



        args.let { it ->
            try {
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                    .listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "ERROR")
                            return  false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Palette.from(resource!!.toBitmap()).generate {
                                palette ->
                                val inColor = palette?.vibrantSwatch?.rgb ?:0
                                mBinding.rlDishDetailMain.setBackgroundColor(inColor)
                            }
                            return false
                        }

                    })
                    .into(mBinding.ivDishImage)
            }catch (e: IOException)
            {
                e.printStackTrace()
            }

            mBinding.tvTitle.text = it.dishDetails.title
            mBinding.tvType.text = it.dishDetails.type.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }

            mBinding.tvCategory.text = it.dishDetails.category
            mBinding.tvIngredients.text = it.dishDetails.ingredients
            mBinding.tvCookingDirection.text = it.dishDetails.directionToCook
            mBinding.tvCookingTime.text = resources.getString(R.string.lbl_estimate_cooking_time, it.dishDetails.cookingTime)

            if(args.dishDetails.favoriteDish)
            {
                mBinding.ivFavoriteDish
                    .setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_selected)
                    )
            }else
            {
                mBinding.ivFavoriteDish
                    .setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_unselected)
                    )

            }

        }

        mBinding.ivFavoriteDish.setOnClickListener {
            args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish

            mFavDishViewModel.update(args.dishDetails)


            if(args.dishDetails.favoriteDish)
            {
                mBinding.ivFavoriteDish
                    .setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_selected)
                    )
                Toast.makeText(requireActivity(), resources.getString(R.string.msg_added_to_favorites),
                Toast.LENGTH_SHORT).show()
            }else
            {
                mBinding.ivFavoriteDish
                    .setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_unselected)
                    )
                Toast.makeText(requireActivity(), resources.getString(R.string.msg_removed_from_favorite),
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

}