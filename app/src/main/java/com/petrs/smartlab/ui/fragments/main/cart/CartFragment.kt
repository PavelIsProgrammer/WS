package com.petrs.smartlab.ui.fragments.main.cart

import androidx.navigation.fragment.findNavController
import com.petrs.smartlab.R
import com.petrs.smartlab.databinding.FragmentCartBinding
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.models.CatalogItemDomain
import com.petrs.smartlab.ui.activities.main.MainActivity
import com.petrs.smartlab.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(
    FragmentCartBinding::inflate
) {
    override val viewModel: CartViewModel by viewModel()
    private lateinit var cartAdapter: CartAdapter

    override fun initView() {
        (requireActivity() as MainActivity).hideBottomBar()

        cartAdapter = CartAdapter(
            object : CartEventListener {
                override fun onAddClicked(item: CatalogItemDomain) {
                    viewModel.addInCart(item)
                }

                override fun onSubscriptionClicked(item: CatalogItemDomain) {
                    viewModel.addInCart(item)
                }
            }
        )

        viewModel.getCart()
        viewModel.getCartSum()
        binding.apply {
            rvCart.adapter = cartAdapter
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnTrash.setOnClickListener {
                viewModel.clearCart()
            }

            btnGoToOrder.setOnClickListener {}
        }
    }

    override fun observeViewModel() {
        viewModel.apply {
            cart.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Success -> {
                        cartAdapter.submitList(domainResult.data)
                    }
                    is DomainResult.Error -> {}
                    is DomainResult.Loading -> {}
                }
            }
            carSum.observe {
                binding.tvSumPrice.text = getString(R.string.price_rub, it.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).showBottomBar()
    }

}