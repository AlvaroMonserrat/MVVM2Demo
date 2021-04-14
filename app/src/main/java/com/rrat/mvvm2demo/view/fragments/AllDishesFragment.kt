package com.rrat.mvvm2demo.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.view.activites.AddUpdateDishActivity
import com.rrat.mvvm2demo.viewmodel.AllDishesViewModel

class AllDishesFragment : Fragment() {

    private lateinit var allDishesViewModel: AllDishesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        allDishesViewModel =
                ViewModelProvider(this).get(AllDishesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_all_dishes, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        allDishesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
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
        }
        return super.onOptionsItemSelected(item)

    }

}