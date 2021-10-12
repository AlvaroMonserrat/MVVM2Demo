package com.rrat.mvvm2demo.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.application.FavDishApplication
import com.rrat.mvvm2demo.databinding.FragmentFavoriteDishesBinding
import com.rrat.mvvm2demo.model.entities.FavDish
import com.rrat.mvvm2demo.view.activites.MainActivity
import com.rrat.mvvm2demo.view.adapters.FavDishAdapter
import com.rrat.mvvm2demo.viewmodel.DashboardViewModel
import com.rrat.mvvm2demo.viewmodel.FavDishViewModel
import com.rrat.mvvm2demo.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private lateinit var binding : FragmentFavoriteDishesBinding

    private lateinit var dashboardViewModel: DashboardViewModel

    private val mFavDishViewModel: FavDishViewModel by  viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)

//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            binding.tvNoFavoriteListOfDishes.text = it
//        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavoriteList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favDishAdapter = FavDishAdapter(this@FavoriteDishesFragment)

        binding.rvFavoriteList.adapter = favDishAdapter

        mFavDishViewModel.favoriteDishes.observe(viewLifecycleOwner)
        {
            dishes ->
            dishes.let {
                if(it.isNotEmpty())
                {
                    binding.rvFavoriteList.visibility = View.VISIBLE
                    binding.tvNoFavoriteListOfDishes.visibility = View.GONE
                    favDishAdapter.dishesList(it)
                }else
                {
                    binding.rvFavoriteList.visibility = View.INVISIBLE
                    binding.tvNoFavoriteListOfDishes.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dishDetails(favDish: FavDish)
    {
        findNavController()
            .navigate(
                FavoriteDishesFragmentDirections
                    .actionNavigationFavoriteDishesToNavigationDishDetails(
                        favDish
                    )
            )
        if(requireActivity() is MainActivity)
        {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity)
        {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

}