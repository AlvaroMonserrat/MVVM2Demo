package com.rrat.mvvm2demo.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rrat.mvvm2demo.databinding.FragmentRandomDishBinding
import com.rrat.mvvm2demo.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private var binding: FragmentRandomDishBinding? = null

    private lateinit var  mRandomDishViewModel: RandomDishViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRandomDishBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)
        mRandomDishViewModel.getRandomRecipeFromAPI()

        randomViewModelObserver()
    }

    private fun randomViewModelObserver(){
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner,
            {randomDishResponde -> randomDishResponde?.let {
                Log.i("Randon Dish Response", "${randomDishResponde.recipes[0]}")
            }}
            )

        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner,
            {
                dataError -> dataError?.let {
                Log.i("Randon Dish API Error", "$dataError")

            }
            })

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner,
            { loadRandomDish ->
                Log.i("Randon Dish Loading", "$loadRandomDish")

            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}