package com.hungry.binareats.presentation.feature.detailmenu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import coil.load
import com.hungry.binareats.databinding.ActivityDetailMenuBinding
import com.hungry.binareats.model.Menu
import com.hungry.binareats.utils.proceedWhen
import com.hungry.binareats.utils.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailMenuActivity : AppCompatActivity() {

    private val binding: ActivityDetailMenuBinding by lazy {
        ActivityDetailMenuBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailMenuViewModel by viewModel {
        parametersOf(intent.extras ?: bundleOf())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindMenu(viewModel.menu)
        observeData()
        setClickListener()
    }

    private fun bindMenu(menu: Menu?) {
        menu?.let { item ->
            binding.ivItemMenu.load(item.imgUrlMenu) {
                crossfade(true)
            }
            binding.tvNameOfMenu.text = item.nameOfMenu
            binding.tvDescMenu.text = item.descOfMenu
            binding.tvPriceOfMenu.text = (item.priceOfMenu ?: 0.0).toCurrencyFormat()
            binding.tvAddressDetail.text = item.locationOfMenu
        }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this) {
            binding.btnAddToCart.text = it.toCurrencyFormat()
        }
        viewModel.menuCountLiveData.observe(this) {
            binding.tvItemQuantity.text = it.toString()
        }
        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.icMinus.setOnClickListener {
            viewModel.minus()
        }
        binding.icPlus.setOnClickListener {
            viewModel.add()
        }
        binding.btnAddToCart.setOnClickListener {
            viewModel.addToCart()
        }
        binding.clLocation.setOnClickListener {
            navigateToGoogleMap()
        }
    }

    private fun navigateToGoogleMap() {
        val locationUrl = viewModel.menu?.locationUrl
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl))
        startActivity(intent)
    }

    companion object {
        const val EXTRA_MENU = "EXTRA_MENU"
        fun startActivity(context: Context, menu: Menu) {
            val intent = Intent(context, DetailMenuActivity::class.java)
            intent.putExtra(EXTRA_MENU, menu)
            context.startActivity(intent)
        }
    }
}
