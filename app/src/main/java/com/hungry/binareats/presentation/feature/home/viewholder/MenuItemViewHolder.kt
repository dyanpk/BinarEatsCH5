package com.hungry.binareats.presentation.feature.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hungry.binareats.core.ViewHolderBinder
import com.hungry.binareats.databinding.ItemGridMenuBinding
import com.hungry.binareats.databinding.ItemLinearMenuBinding
import com.hungry.binareats.model.Menu
import com.hungry.binareats.utils.toCurrencyFormat

class LinearFoodItemViewHolder(
    private val binding: ItemLinearMenuBinding,
    private val onClickListener: (Menu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {

    override fun bind(item: Menu){
        binding.ivItemMenu.load(item.imgUrlMenu){
            crossfade(true)
        }
        binding.tvNameOfMenu.text = item.nameOfMenu
        binding.tvPriceOfMenu.text = item.priceOfMenu?.toCurrencyFormat()

        binding.root.setOnClickListener{
            onClickListener.invoke(item)
        }
    }
}

class GridFoodItemViewHolder(
    private val binding: ItemGridMenuBinding,
    private val onClickListener: (Menu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {

    override fun bind(item: Menu){
        binding.ivItemMenu.load(item.imgUrlMenu){
            crossfade(true)
        }
        binding.tvNameOfMenu.text = item.nameOfMenu
        binding.tvPriceOfMenu.text = item.priceOfMenu?.toCurrencyFormat()

        binding.root.setOnClickListener{
            onClickListener.invoke(item)
        }
    }
}