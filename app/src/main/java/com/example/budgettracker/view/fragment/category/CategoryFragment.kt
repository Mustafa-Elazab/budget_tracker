package com.example.budgettracker.view.fragment.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.budgettracker.databinding.FragmentCategoryBinding
import com.example.budgettracker.model.CategoryData
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.example.budgettracker.view.fragment.home.HomeViewModel


class CategoryFragment : BaseFragment<FragmentCategoryBinding, HomeViewModel>() {


    override val viewModel: HomeViewModel
            by activityViewModels()

    private var adapter: CategoryAdapter = CategoryAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCategoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getCategoryData()
        initView()


    }

    private fun getCategoryData() {
        adapter.submitList(CategoryData.data)
        Log.i("TAG", "getCategoryData: ${CategoryData.data}")
    }

    private fun initView() {

        binding.apply {
            binding.rvCategory.apply {
                adapter = adapter
            }
        }
    }
}