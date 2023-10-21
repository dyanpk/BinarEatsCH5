package com.hungry.binareats.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hungry.binareats.R
import com.hungry.binareats.core.ViewHolderBinder
import com.hungry.binareats.databinding.ItemCartMenuBinding
import com.hungry.binareats.databinding.ItemCartMenuOrderBinding
import com.hungry.binareats.model.Cart
import com.hungry.binareats.utils.doneEditing
import com.hungry.binareats.utils.toCurrencyFormat

class CartListAdapter(private val cartListener: CartListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Cart>() {
            override fun areItemsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    fun submitData(data: List<Cart>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (cartListener != null) CartViewHolder(
            ItemCartMenuBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), cartListener
        ) else CartOrderViewHolder(
            ItemCartMenuOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }

}

class CartViewHolder(
    private val binding: ItemCartMenuBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
        setClickListeners(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.ivItemMenu.load(item.imgUrlMenu) {
                crossfade(true)
            }
            tvMenuCount.text = item.itemQuantity.toString()
            tvMenuName.text = item.nameOfMenu
            tvProductPrice.text = (item.itemQuantity * item.priceOfMenu).toCurrencyFormat()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.etNotesItem.setText(item.itemNotes)
        binding.etNotesItem.doneEditing {
            binding.etNotesItem.clearFocus()
            val newItem = item.copy().apply {
                itemNotes = binding.etNotesItem.text.toString().trim()
            }
            cartListener?.onUserDoneEditingNotes(newItem)
        }
    }

    private fun setClickListeners(item: Cart) {
        with(binding) {
            ivMinus.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item) }
            ivPlus.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item) }
            ivRemoveCart.setOnClickListener { cartListener?.onRemoveCartClicked(item) }
        }
    }
}

class CartOrderViewHolder(
    private val binding: ItemCartMenuOrderBinding,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.ivItemMenu.load(item.imgUrlMenu) {
                crossfade(true)
            }
            tvTotalQuantity.text =
                itemView.rootView.context.getString(
                    R.string.total_quantity,
                    item.itemQuantity.toString()
                )
            tvMenuName.text = item.nameOfMenu
            tvMenuPrice.text = (item.itemQuantity * item.priceOfMenu).toCurrencyFormat()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.tvNotes.text = item.itemNotes
    }

}


interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}