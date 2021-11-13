package com.rrat.mvvm2demo.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.rrat.mvvm2demo.databinding.ItemCustomListBinding
import com.rrat.mvvm2demo.view.activites.AddUpdateDishActivity
import com.rrat.mvvm2demo.view.fragments.AllDishesFragment

class CustomListItemAdapter(
    private val activity: Activity,
    private val fragment: Fragment?,
    private val listItems: List<String>,
    private val selection: String) : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding = ItemCustomListBinding.inflate(
                LayoutInflater.from(activity), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]

        holder.tvText.text = item

        holder.tvText.setOnClickListener{
            if(activity is AddUpdateDishActivity){
                activity.selectedListItem(item, selection)
            }
            if(fragment is AllDishesFragment)
            {
                fragment.filterSelection(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }


    class ViewHolder(view: ItemCustomListBinding): RecyclerView.ViewHolder(view.root){
        val tvText = view.tvText
    }

}