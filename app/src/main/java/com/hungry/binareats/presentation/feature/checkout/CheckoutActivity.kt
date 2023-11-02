package com.hungry.binareats.presentation.feature.checkout

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hungry.binareats.R
import com.hungry.binareats.databinding.ActivityCheckoutBinding
import com.hungry.binareats.presentation.common.adapter.CartListAdapter
import com.hungry.binareats.presentation.common.adapter.ShoppingSummaryAdapter
import com.hungry.binareats.utils.proceedWhen
import com.hungry.binareats.utils.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : AppCompatActivity() {

    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter()
    }

    private val summaryAdapter: ShoppingSummaryAdapter by lazy {
        ShoppingSummaryAdapter()
    }

    private val viewModel: CheckoutViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        observeData()
        setClickListeners()
    }

    private fun setupList() {
        binding.layoutContent.rvCart.adapter = adapter
        binding.layoutContent.rvShoppingSummary.adapter = summaryAdapter
    }

    private fun observeData() {
        observeCartData()
        observeCheckoutResult()
    }

    private fun observeCartData() {
        viewModel.cartList.observe(this) {
            it.proceedWhen(doOnSuccess = { result ->
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = true
                binding.layoutContent.rvCart.isVisible = true
                binding.cvSectionOrder.isVisible = true
                binding.layoutContent.cvShoppingSummary.isVisible = true
                binding.layoutContent.rvShoppingSummary.isVisible = true
                result.payload?.let { (carts, totalPrice) ->
                    adapter.submitData(carts)
                    summaryAdapter.submitData(carts)
                    binding.tvTotalPrice.text = totalPrice.toCurrencyFormat()
                }
            }, doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                    binding.cvSectionOrder.isVisible = false
                }, doOnError = { err ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                    binding.cvSectionOrder.isVisible = false
                }, doOnEmpty = { data ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                    data.payload?.let { (_, totalPrice) ->
                        binding.tvTotalPrice.text = totalPrice.toCurrencyFormat()
                    }
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                    binding.cvSectionOrder.isVisible = false
                })
        }
    }

    private fun observeCheckoutResult() {
        viewModel.checkoutResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    showDialogCheckoutSuccess()
                },
                doOnError = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    Toast.makeText(this, "Checkout Error", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                }
            )
        }
    }

    private fun setClickListeners() {
        binding.btnCheckout.setOnClickListener {
            viewModel.order()
        }
    }

    private fun showDialogCheckoutSuccess() {
        AlertDialog.Builder(this)
            .setMessage("Checkout Success")
            .setPositiveButton(getString(R.string.text_okay)) { _, _ ->
                viewModel.clearCart()
                finish()
            }.create().show()
    }
}
