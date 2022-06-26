package com.rrat.mvvm2demo.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.application.FavDishApplication
import com.rrat.mvvm2demo.databinding.FragmentRandomDishBinding
import com.rrat.mvvm2demo.model.entities.FavDish
import com.rrat.mvvm2demo.model.entities.RandomDish
import com.rrat.mvvm2demo.utils.Constants
import com.rrat.mvvm2demo.viewmodel.FavDishViewModel
//import com.rrat.mvvm2demo.viewmodel.FavDishViewModelFactory
import com.rrat.mvvm2demo.viewmodel.RandomDishViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RandomDishFragment : Fragment() {

    private var binding: FragmentRandomDishBinding? = null

            private lateinit var  mRandomDishViewModel: RandomDishViewModel

    private var mProgressDialog: Dialog? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRandomDishBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    private fun showCustomProgressDialog()
    {
        mProgressDialog = Dialog(requireActivity())

        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideCustomProgressDialog()
    {
        mProgressDialog?.let{
            it.dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)

        mRandomDishViewModel.getRandomRecipeFromAPI()

        randomViewModelObserver()

        binding!!.srlRandomDish.setOnRefreshListener {
                mRandomDishViewModel.getRandomRecipeFromAPI()
        }
    }

    private fun randomViewModelObserver(){
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner,
            {randomDishResponde -> randomDishResponde?.let {
                Log.i("Randon Dish Response", "${randomDishResponde.recipes[0]}")
                if(binding!!.srlRandomDish.isRefreshing)
                {
                    binding!!.srlRandomDish.isRefreshing = false
                }
                setRandomDishResponseInUI(randomDishResponde.recipes[0])
            }}
            )

        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner,
            {
                dataError -> dataError?.let {
                Log.i("Randon Dish API Error", "$dataError")

                if(binding!!.srlRandomDish.isRefreshing)
                {
                    binding!!.srlRandomDish.isRefreshing = false
                }

            }
            })

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner,
            { loadRandomDish ->

                loadRandomDish?.let {
                    Log.i("Randon Dish Loading", "$loadRandomDish")
                    if(loadRandomDish && !binding!!.srlRandomDish.isRefreshing)
                    {
                        showCustomProgressDialog()
                    }
                    else
                    {
                        hideCustomProgressDialog()
                    }
                }

            }
        )
    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe)
    {
        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(binding!!.ivDishImage)

        binding!!.tvTitle.text = recipe.title

        var dishType = "other"

        if(recipe.dishTypes.isNotEmpty())
        {
            dishType = recipe.dishTypes[0]

        }
        binding!!.tvType.text = dishType
        binding!!.tvCategory.text = "Other"

        var ingredient = ""
        for(value in recipe.extendedIngredients)
        {
            if(ingredient.isEmpty())
            {
                ingredient = value.original
            }else
            {
                ingredient = ingredient + ", \n" + value.original
            }
        }

        binding!!.tvIngredients.text = ingredient

        binding!!.tvCookingDirection.text = Html.fromHtml(
            recipe.instructions,
            Html.FROM_HTML_MODE_COMPACT)

        binding!!.tvCookingTime.text = resources.getString(R.string.lbl_estimate_cooking_time,
            recipe.readyInMinutes.toString()
        )

        binding!!.ivFavoriteDish.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_unselected
            )
        )

        var addedToFavorites = false

        binding!!.ivFavoriteDish.setOnClickListener {

            if(addedToFavorites)
            {
                Toast.makeText(requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites),
                    Toast.LENGTH_SHORT)
                    .show()
            }else
            {
                val randomDishDetails = FavDish(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredient,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )

//                val mFavDishViewModel: FavDishViewModel by viewModels{
//                    FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
//                }

                val mFavDishViewModel: FavDishViewModel by viewModels()

                mFavDishViewModel.insert(randomDishDetails)

                addedToFavorites = true

                binding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_selected)
                )

                Toast.makeText(requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}