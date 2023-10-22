package com.hungry.binareats.presentation.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hungry.binareats.R
import com.hungry.binareats.data.local.datastore.appDataStore
import com.hungry.binareats.data.local.preferences.UserPreferenceDataSourceImpl
import com.hungry.binareats.data.network.api.datasource.BinarEatsApiDataSource
import com.hungry.binareats.data.network.api.service.BinarEatsApiService
import com.hungry.binareats.data.repository.MenuRepository
import com.hungry.binareats.data.repository.MenuRepositoryImpl
import com.hungry.binareats.databinding.FragmentHomeBinding
import com.hungry.binareats.model.Menu
import com.hungry.binareats.presentation.feature.detailmenu.DetailMenuActivity
import com.hungry.binareats.presentation.feature.home.adapter.subadapter.AdapterLayoutMode
import com.hungry.binareats.presentation.feature.home.adapter.subadapter.CategoryListAdapter
import com.hungry.binareats.presentation.feature.home.adapter.subadapter.MenuListAdapter
import com.hungry.binareats.utils.GenericViewModelFactory
import com.hungry.binareats.utils.PreferenceDataStoreHelperImpl
import com.hungry.binareats.utils.proceedWhen

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val categoryAdapter: CategoryListAdapter by lazy {
        CategoryListAdapter{
            viewModel.getMenus(it.slug)
        }
    }

    private val menuAdapter: MenuListAdapter by lazy {
        MenuListAdapter(
            layoutMode = AdapterLayoutMode.GRID,
            itemClick = {
                navigateToDetailMenu(it)
            }
        )
    }

    private fun navigateToDetailMenu(it: Menu) {
        DetailMenuActivity.startActivity(requireContext(),it)
    }

    private val viewModel : HomeViewModel by viewModels {
        val service = BinarEatsApiService.invoke()
        val dataSource = BinarEatsApiDataSource(service)
        val repo: MenuRepository = MenuRepositoryImpl(dataSource)
        val dataStore = this.requireContext().appDataStore
        val dataStoreHelper = PreferenceDataStoreHelperImpl(dataStore)
        val userPref = UserPreferenceDataSourceImpl(dataStoreHelper)
        GenericViewModelFactory.create(HomeViewModel(repo, userPref))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        getData()
        setupLayout()
        setupLayoutMode()
    }

    private fun observeData() {
        viewModel.categories.observe(viewLifecycleOwner){
            it.proceedWhen(doOnSuccess = {
                binding.inclCategories.layoutState.root.isVisible = false
                binding.inclCategories.layoutState.pbLoading.isVisible = false
                binding.inclCategories.layoutState.tvError.isVisible = false
                binding.inclCategories.rvCategory.apply {
                    isVisible = true
                    adapter = categoryAdapter
                }
                it.payload?.let { data -> categoryAdapter.submitData(data) }
            }, doOnLoading = {
                binding.inclCategories.layoutState.root.isVisible = true
                binding.inclCategories.layoutState.pbLoading.isVisible = true
                binding.inclCategories.layoutState.tvError.isVisible = false
                binding.inclCategories.rvCategory.isVisible = false
            }, doOnError = {
                binding.inclCategories.layoutState.root.isVisible = true
                binding.inclCategories.layoutState.pbLoading.isVisible = false
                binding.inclCategories.layoutState.tvError.isVisible = true
                binding.inclCategories.layoutState.tvError.text = it.exception?.message.orEmpty()
                binding.inclCategories.rvCategory.isVisible = false
            })
        }
        viewModel.menus.observe(viewLifecycleOwner){
            it.proceedWhen(doOnSuccess = {
                binding.inclMenus.layoutState.root.isVisible = false
                binding.inclMenus.layoutState.pbLoading.isVisible = false
                binding.inclMenus.layoutState.tvError.isVisible = false
                binding.inclMenus.rvMenuList.apply {
                    isVisible = true
                    adapter = menuAdapter
                }
                it.payload?.let { data -> menuAdapter.submitData(data) }
            }, doOnLoading = {
                binding.inclMenus.layoutState.root.isVisible = true
                binding.inclMenus.layoutState.pbLoading.isVisible = true
                binding.inclMenus.layoutState.tvError.isVisible = false
                binding.inclMenus.rvMenuList.isVisible = false
            }, doOnError = {
                binding.inclMenus.layoutState.root.isVisible = true
                binding.inclMenus.layoutState.pbLoading.isVisible = false
                binding.inclMenus.layoutState.tvError.isVisible = true
                binding.inclMenus.layoutState.tvError.text = it.exception?.message.orEmpty()
                binding.inclMenus.rvMenuList.isVisible = false
            }, doOnEmpty = {
                binding.inclMenus.layoutState.root.isVisible = true
                binding.inclMenus.layoutState.pbLoading.isVisible = false
                binding.inclMenus.layoutState.tvError.isVisible = true
                binding.inclMenus.layoutState.tvError.text = getString(R.string.menu_not_found)
                binding.inclMenus.rvMenuList.isVisible = false
            })
        }
    }

    private fun getData() {
        viewModel.getCategories()
        viewModel.getMenus()
    }

    private fun setupLayout() {
        val span = if (menuAdapter.layoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.inclMenus.rvMenuList.layoutManager = GridLayoutManager(requireContext(), span)
        binding.inclMenus.rvMenuList.adapter = menuAdapter
    }

    private fun setupLayoutMode() {
        viewModel.userLayoutModeLiveData().observe(viewLifecycleOwner) { isLinearMode ->
            val layoutMode = if (isLinearMode) AdapterLayoutMode.LINEAR else AdapterLayoutMode.GRID
            updateLayout(layoutMode, isLinearMode)
            setLayoutMode(isLinearMode)
        }
    }

    private fun updateLayout(layoutMode: AdapterLayoutMode, isLinearMode: Boolean) {
        val iconLayoutMode = if (layoutMode == AdapterLayoutMode.GRID) R.drawable.ic_list else R.drawable.ic_grid
        val layoutManager = binding.inclMenus.rvMenuList.layoutManager as GridLayoutManager
        layoutManager.spanCount = if (isLinearMode) 1 else 2
        menuAdapter.layoutMode = if (isLinearMode) AdapterLayoutMode.LINEAR else AdapterLayoutMode.GRID
        binding.inclMenus.ivLayoutMode.setImageDrawable(ContextCompat.getDrawable(requireContext(), iconLayoutMode))
    }

    private fun setLayoutMode(usingList: Boolean) {
        binding.inclMenus.ivLayoutMode.setOnClickListener {
            if (usingList) {
                viewModel.setUserListViewMode(false)
            } else {
                viewModel.setUserListViewMode(true)
            }
        }
    }
}
