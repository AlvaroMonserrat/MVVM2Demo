package com.rrat.mvvm2demo.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.application.FavDishApplication
import com.rrat.mvvm2demo.databinding.DialogCustomListBinding
import com.rrat.mvvm2demo.databinding.FragmentAllDishesBinding
import com.rrat.mvvm2demo.model.entities.FavDish
import com.rrat.mvvm2demo.utils.Constants
import com.rrat.mvvm2demo.view.activites.AddUpdateDishActivity
import com.rrat.mvvm2demo.view.activites.MainActivity
import com.rrat.mvvm2demo.view.adapters.CustomListItemAdapter
import com.rrat.mvvm2demo.view.adapters.FavDishAdapter
import com.rrat.mvvm2demo.viewmodel.AllDishesViewModel
import com.rrat.mvvm2demo.viewmodel.FavDishViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllDishesFragment : Fragment() {

    private lateinit var binding : FragmentAllDishesBinding

    private lateinit var mFavDishAdapter: FavDishAdapter

    private lateinit var mCustomListDialog: Dialog

//    private val mFavDishViewModel: FavDishViewModel by viewModels {
//        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
//    }

    private val mFavDishViewModel : FavDishViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllDishesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        mFavDishAdapter = FavDishAdapter(this@AllDishesFragment)

        binding.rvDishesList.adapter = mFavDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes ->
                dishes.let {
                    if(it.isNotEmpty()){
                        binding.rvDishesList.visibility = View.VISIBLE
                        binding.tvNoDishesAddedYet.visibility = View.GONE
                        mFavDishAdapter.dishesList(it)
                    }else{
                        binding.rvDishesList.visibility = View.INVISIBLE
                        binding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
        }
    }

    fun dishDetails(favDish: FavDish)
    {
        findNavController()
            .navigate(
                AllDishesFragmentDirections
                .actionNavigationAllDishesToNavigationDishDetails(
                    favDish
                )
            )
        if(requireActivity() is MainActivity)
        {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    private fun filterDishesListDialog()
    {
        mCustomListDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)

        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishType = Constants.dishTypes()
        dishType.add(0, Constants.ALL_ITEMS)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())

        val adapter = CustomListItemAdapter(requireActivity(), this@AllDishesFragment, dishType, Constants.FILTER_SELECTION)
        binding.rvList.adapter = adapter

        mCustomListDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity)
        {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    fun deleteDish(dish: FavDish)
    {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.msg_delete_dish_dialog))
        builder.setMessage(resources.getString(R.string.msg_delete_dish_dialog))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.lbl_yes))
        {
            dialogInterface,
            _->
            mFavDishViewModel.delete(dish)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no))
        {
                dialogInterface,
                _->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_add_dish->{
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
            R.id.action_filter_dishes->
            {
                filterDishesListDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

    fun filterSelection(filterItemSelection: String)
    {
        mCustomListDialog.dismiss()
        Log.i("Filter Selection", filterItemSelection)

        if(filterItemSelection == Constants.ALL_ITEMS)
        {
            mFavDishViewModel.allDishesList.observe(viewLifecycleOwner){
                    dishes ->
                dishes.let {
                    if(it.isNotEmpty()){
                        binding.rvDishesList.visibility = View.VISIBLE
                        binding.tvNoDishesAddedYet.visibility = View.GONE
                        mFavDishAdapter.dishesList(it)
                    }else{
                        binding.rvDishesList.visibility = View.INVISIBLE
                        binding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
        else
        {
            mFavDishViewModel.getFilteredList(filterItemSelection).observe(viewLifecycleOwner)
            {
                dishes ->
                dishes.let {
                    if(it.isNotEmpty())
                    {
                        binding.rvDishesList.visibility = View.VISIBLE
                        binding.tvNoDishesAddedYet.visibility = View.GONE

                        mFavDishAdapter.dishesList(it)
                    }else
                    {
                        binding.rvDishesList.visibility = View.GONE
                        binding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }

        }

    }

}