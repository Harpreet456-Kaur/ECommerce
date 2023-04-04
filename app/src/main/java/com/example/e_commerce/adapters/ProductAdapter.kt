package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.Products
import com.example.e_commerce.databinding.LayoutItemsBinding
import com.example.e_commerce.models.ProductModel

class ProductAdapter(val productList: ArrayList<ProductModel>, val categoryInterface: Products): RecyclerView.Adapter<ProductAdapter.viewHolder>() {
    inner class viewHolder (val binding: LayoutItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = LayoutItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.tvName.text = productList[position].name
        holder.binding.ibEdit.setOnClickListener {
            categoryInterface.edit(position)
        }
        holder.binding.ibSubCat.visibility = View.GONE
    }

}
