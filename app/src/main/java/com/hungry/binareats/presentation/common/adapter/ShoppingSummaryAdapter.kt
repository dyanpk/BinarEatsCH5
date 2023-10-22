package com.hungry.binareats.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hungry.binareats.core.ViewHolderBinder
import com.hungry.binareats.databinding.ItemCostBinding
import com.hungry.binareats.model.Cart
import com.hungry.binareats.utils.toCurrencyFormat

class ShoppingSummaryAdapter () : RecyclerView.Adapter<ViewHolder>(){

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Cart>() {
            override fun areItemsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ShoppingSummaryViewHolder(
            ItemCostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }

    fun submitData(data: List<Cart>) {
        dataDiffer.submitList(data)
    }


}

class ShoppingSummaryViewHolder(
    private val binding: ItemCostBinding
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart>{
    override fun bind(item: Cart) {
        setShoppingSummary(item)
    }

    private fun setShoppingSummary(item: Cart) {
        binding.tvItemTitle.text = item.nameOfMenu
        binding.tvItemPrice.text = (item.itemQuantity * item.priceOfMenu).toCurrencyFormat()
    }

}

