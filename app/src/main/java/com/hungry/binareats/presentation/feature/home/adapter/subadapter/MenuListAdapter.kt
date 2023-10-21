package com.hungry.binareats.presentation.feature.home.adapter.subadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hungry.binareats.core.ViewHolderBinder
import com.hungry.binareats.databinding.ItemGridMenuBinding
import com.hungry.binareats.databinding.ItemLinearMenuBinding
import com.hungry.binareats.model.Menu
import com.hungry.binareats.presentation.feature.home.viewholder.GridFoodItemViewHolder
import com.hungry.binareats.presentation.feature.home.viewholder.LinearFoodItemViewHolder

class MenuListAdapter(
    var layoutMode: AdapterLayoutMode,
    private val itemClick: (Menu) -> Unit
) : RecyclerView.Adapter<ViewHolder>(){

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(
                oldItem: Menu,
                newItem: Menu
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Menu,
                newItem: Menu
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    fun submitData(data: List<Menu>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType){
            AdapterLayoutMode.GRID.ordinal -> {
                GridFoodItemViewHolder(
                    binding = ItemGridMenuBinding.inflate(
                        LayoutInflater.from(parent.context),parent,false
                    ), itemClick
                )
            }
            else ->{
                LinearFoodItemViewHolder(
                    binding = ItemLinearMenuBinding.inflate(
                        LayoutInflater.from(parent.context),parent,false
                    ), itemClick
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return layoutMode.ordinal
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Menu>).bind(dataDiffer.currentList[position])
    }


    override fun getItemCount(): Int = dataDiffer.currentList.size


}