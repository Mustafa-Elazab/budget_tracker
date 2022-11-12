package com.example.budgettracker.view.fragment.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.databinding.CategoryItemLayoutBinding
import com.example.budgettracker.model.CategoryModel

class CategoryAdapter : ListAdapter<CategoryModel, CategoryAdapter.CategoryViewHolder>(CategoryComaptator()) {

    class  CategoryViewHolder(val binding:CategoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : CategoryModel){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        var currentItem = getItem(position)
        if(currentItem !=null){
            holder.bind(currentItem)
        }
    }


    class CategoryComaptator : DiffUtil.ItemCallback<CategoryModel>(){
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem
        }

    }
}