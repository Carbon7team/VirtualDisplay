package com.carbon7.virtualdisplay.ui.status

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.ListItemStatusBinding
import com.carbon7.virtualdisplay.model.Status
import kotlinx.coroutines.withContext

class StatusAdapter(private val status:List<Status>) :
    RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val binding: ListItemStatusBinding = ListItemStatusBinding.bind(view)

        val lblStatus = binding.lblStatus
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_status,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ctx=holder.lblStatus.context
        val s = status[position]

        holder.lblStatus.text= s.code+ ": "+ ctx.getString(s.name)
        holder.lblStatus.setTextColor( if(s.isActive) Color.CYAN else Color.GRAY)
    }

    override fun getItemCount() = status.size


    /*override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ListItemStatusBinding
        val status = getItem(position)
        var convertView = convertView

        if(convertView==null) {
            Log.d("MyApp","ConvertView == NULL")
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = ListItemStatusBinding.inflate(inflater,parent,false)
            convertView=binding.root
            //    LayoutInflater.from(context).inflate(R.layout.list_item_status, parent, false)
        }else
            binding= ListItemStatusBinding.bind(convertView)

        if (status != null) {
            binding.lblStatus.text = status.code+ ": "+ context.getString(status.name)
            binding.lblStatus.setTextColor( if(status.isActive) Color.CYAN else Color.GRAY)
        }

        return convertView
    }*/
}