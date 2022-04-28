package com.carbon7.virtualdisplay.ui.ups_selector

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.UpsSelectorBinding
import com.carbon7.virtualdisplay.model.Ups

class UpsSelectorAdapter(private val ups_list:List<Ups>) :
    RecyclerView.Adapter<UpsSelectorAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val binding: UpsSelectorBinding = UpsSelectorBinding.bind(view)
        val upsList = binding.upsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ups_selector,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ctx=holder.upsList.context
        val s = ups_list[position]
        holder.upsList.setTextColor(Color.CYAN)
    }

    override fun getItemCount() = ups_list.size
}