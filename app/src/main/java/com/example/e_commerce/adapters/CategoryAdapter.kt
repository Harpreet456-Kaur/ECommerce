package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.CategoryInterface
import com.example.e_commerce.MainActivity
import com.example.e_commerce.databinding.LayoutItemsBinding
import com.example.e_commerce.models.CategoryModel

class CategoryAdapter(val categoryList: ArrayList<CategoryModel>,var mainActivity: MainActivity, val categoryInterface: CategoryInterface): RecyclerView.Adapter<CategoryAdapter.viewHolder>() {
    inner class viewHolder(val binding: LayoutItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = LayoutItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.tvName.text = categoryList[position].name
        holder.binding.ibEdit.setOnClickListener {
            categoryInterface.edit(position)
        }
        holder.binding.ibSubCat.setOnClickListener {
            categoryInterface.subCat(position)
        }
        Glide.with(mainActivity)
            .load(categoryList[position].image)
            .circleCrop()
            .into(holder.binding.iv)
    }

}