package com.example.e_commerce

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.LayoutItemsBinding

class CategoryAdapter(val categoryList: ArrayList<CategoryModel>, val newInterface: NewInterface): RecyclerView.Adapter<CategoryAdapter.viewHolder>() {
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
        holder.itemView.setOnClickListener {
            newInterface.edit(position)
        }
    }
}