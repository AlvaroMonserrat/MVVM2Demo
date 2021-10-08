package com.rrat.mvvm2demo.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rrat.mvvm2demo.databinding.ItemDishLayoutBinding
import com.rrat.mvvm2demo.model.entities.FavDish
import com.rrat.mvvm2demo.view.fragments.AllDishesFragment

class FavDishAdapter(private val fragment: Fragment): RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavDishAdapter.ViewHolder {
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavDishAdapter.ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)

        holder.tvTile.text = dish.title

        holder.itemView.setOnClickListener{
            if(fragment is AllDishesFragment)
            {
                fragment.dishDetails()
            }
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishesList(list: List<FavDish>){
        dishes = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root){
        val ivDishImage = view.ivDishImage
        val tvTile = view.tvDishTitle
    }

}