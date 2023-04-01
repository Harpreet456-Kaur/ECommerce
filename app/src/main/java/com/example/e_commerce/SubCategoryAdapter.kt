package com.example.e_commerce

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.LayoutItemsBinding

class SubCategoryAdapter(val subCategoryList: ArrayList<SubCategoryModel>, val subcategoryInterface: SubCategoryInterface): RecyclerView.Adapter<SubCategoryAdapter.viewHolder>() {
    inner class viewHolder(val binding: LayoutItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = LayoutItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.tvName.text = subCategoryList[position].name
        holder.itemView.setOnClickListener {
            subcategoryInterface.edit(position)
        }
    }
}