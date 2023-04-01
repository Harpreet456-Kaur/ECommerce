package com.example.e_commerce

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.LayoutItemsBinding

class PrintedAdapter(val PrintedList: ArrayList<PrintedModel>, val printedInterface: PrintedInterface): RecyclerView.Adapter<PrintedAdapter.viewHolder>() {
    inner class viewHolder(val binding: LayoutItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = LayoutItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.tvName.text = PrintedList[position].name
        holder.itemView.setOnClickListener {
            printedInterface.edit(position)
    }
    }

    override fun getItemCount(): Int {
        return PrintedList.size
    }
}