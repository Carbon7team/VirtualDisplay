package com.carbon7.virtualdisplay.ui.ups_selector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.ListItemUpsBinding
import com.carbon7.virtualdisplay.model.SavedUps

class UpsSelectorAdapter(private val ups_list:List<SavedUps>) :
    RecyclerView.Adapter<UpsSelectorAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val binding: ListItemUpsBinding = ListItemUpsBinding.bind(view)
        val upsName = binding.upsName
        val upsSocket = binding.upsInfo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_ups, parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val savedUps = ups_list[position]

        holder.upsName.text= savedUps.name
        holder.upsSocket.text= "IP: " + savedUps.address + " - port: " + savedUps.port
    }

    override fun getItemCount() = ups_list.size
}