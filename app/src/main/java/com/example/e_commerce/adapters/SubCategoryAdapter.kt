package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.CategoryInterface
import com.example.e_commerce.databinding.LayoutItemsBinding
import com.example.e_commerce.models.CategoryModel
import com.example.e_commerce.models.SubCategoryModel

class SubCategoryAdapter(val categoryList: ArrayList<SubCategoryModel>, val newInterface: CategoryInterface): RecyclerView.Adapter<SubCategoryAdapter.viewHolder>() {
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
            newInterface.edit(position)
        }
        holder.binding.ibSubCat.visibility = View.GONE
    }
}